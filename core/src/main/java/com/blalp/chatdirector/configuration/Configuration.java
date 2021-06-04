package com.blalp.chatdirector.configuration;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.blalp.chatdirector.model.IItem;
import com.blalp.chatdirector.ChatDirector;
import com.blalp.chatdirector.model.IConfiguration;
import com.blalp.chatdirector.model.IDaemon;
import com.blalp.chatdirector.model.IModule;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonDeserialize(using = ConfigurationDeserializer.class)
public class Configuration implements IConfiguration {
    boolean debug;
    // Do not allow the user to specify whether or not they are in testing mode,
    // that should only be done programmatically in the unit tests.
    @JsonIgnore()
    boolean testing = false;
    ServiceLoader<IModule> modules;
    List<IDaemon> daemons = new ArrayList<>();
    Map<String, Chain> chains = new HashMap<String, Chain>();
    // This is for storage of generic keys that modules may need.
    // The first key is the module name
    Map<String, Map<String, String>> moduleData = new HashMap<>();

    public Configuration() {
        modules = ServiceLoader.load(IModule.class, this.getClass().getClassLoader());
        try {
            Class.forName("org.junit.jupiter.api.Test");
            testing = true;
        } catch (ClassNotFoundException e) {
            testing = false;
        }
    }

    // https://stackoverflow.com/questions/58102069/how-to-do-a-partial-deserialization-with-jackson#58102226
    @Override
    public boolean load() {
        boolean result = true;
        if (debug) {
            System.out.println("Modules");
            for (IModule module : modules) {
                System.out.println(module);
            }
            System.out.println("Module Data");
            for (Entry<String,Map<String,String>> module : moduleData.entrySet()) {
                System.out.println(module.getKey()+": ");
                for (Entry<String,String> item : module.getValue().entrySet()) {
                    System.out.println("\t"+item.getKey()+": "+item.getValue());
                }
            }
            System.out.println("Chains");
            for (String pipeKey : chains.keySet()) {
                System.out.println("Chain " + pipeKey);
                if (chains.get(pipeKey) != null) {
                    for (IItem item : chains.get(pipeKey).getItems()) {
                        System.out.println(item);
                    }
                }
            }
        }
        for (IModule module : getModules()) {
            result = result && module.load();
            if (debug) {
                System.out.println(module + " returned " + result);
            }
        }
        if (!isValid()) {
            return false;
        }
        for (IDaemon daemon : getDaemons()) {
            if (!daemon.load()) {
                ChatDirector.getLogger().log(Level.SEVERE, "daemon " + daemon.toString() + " failed to load.");
                return false;
            }
        }
        return result;
    }

    @Override
    public Class<? extends IItem> getItemClass(String itemType, Iterable<IModule> inputModules) {
        for (IModule module : inputModules) {
            if (module.getItemNames().contains(itemType)) {
                return module.getItemClass(itemType);
            }
        }
        return null;
    }

    @Override
    public boolean unload() {
        for (IDaemon daemon : daemons) {
            daemon.unload();
        }
        for (IModule module : getModules()) {
            module.unload();
        }
        return true;
    }

    @Override
    public Class<? extends IItem> getItemClass(String itemType) {
        return getItemClass(itemType, modules);
    }

    public IModule getModule(Class<? extends IModule> class1) {
        for (IModule module : modules) {
            if (module.getClass().equals(class1)) {
                return module;
            }
        }
        return null;
    }

    public boolean hasDaemon(Class<? extends IDaemon> class1) {
        for (IDaemon daemon : daemons) {
            if (daemon.getClass().isAssignableFrom(class1)) {
                return true;
            }
        }
        return false;
    }

    public IDaemon getOrCreateDaemon(Class<? extends IDaemon> class1) {
        for (IDaemon daemon : daemons) {
            if (daemon.getClass().isAssignableFrom(class1)) {
                return daemon;
            }
        }
        try {
            IDaemon daemon = (IDaemon) class1.getConstructors()[0].newInstance();
            daemons.add(daemon);
            return daemon;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isValid() {
        for (Entry<String, Chain> chain : getChains().entrySet()) {
            if (chain.getValue() != null && !chain.getValue().isValid()) {
                ChatDirector.getLogger().log(Level.SEVERE, "chain: " + chain.toString() + " is not valid.");
                return false;
            }
        }
        for (IModule module : getModules()) {
            if (!module.isValid()) {
                ChatDirector.getLogger().log(Level.SEVERE, "module " + module.toString() + " is not valid.");
                return false;
            }
        }
        return true;
    }
}