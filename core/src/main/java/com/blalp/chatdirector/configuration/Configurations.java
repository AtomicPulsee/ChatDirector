package com.blalp.chatdirector.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.blalp.chatdirector.model.IConfiguration;
import com.blalp.chatdirector.model.IModule;

/**
 * This is a holder for multiple configurations, meant to be
 */
public class Configurations implements IConfiguration {

    /**
     * Maintain a list of all configurations
     */
    static List<IConfiguration> configurations = new ArrayList<>();

    public static void addConfiguration(IConfiguration configuration) {
        configurations.add(configuration);
    }

    @Override
    public void load() {
        for (IConfiguration configuration : configurations) {
            configuration.load();
        }
    }

    @Override
    public void unload() {
        for (IConfiguration configuration : configurations) {
            configuration.unload();
        }
    }

    @Override
    public Class<?> getModuleClass(String moduleType) {
        Class<?> output = null;
        for (IConfiguration configuration : configurations) {
            output = configuration.getModuleClass(moduleType);
            if (output != null) {
                break;
            }
        }
        return output;
    }

    @Override
    public Class<?> getItemClass(String itemType) {
        Class<?> output = null;
        for (IConfiguration configuration : configurations) {
            output = configuration.getItemClass(itemType);
            if (output != null) {
                break;
            }
        }
        return output;
    }

    @Override
    public List<IModule> getModules() {
        List<IModule> modules = new ArrayList<>();
        for (IConfiguration configuration : configurations) {
            modules.addAll(configuration.getModules());
        }
        return modules;
    }

    @Override
    public Map<String, Chain> getChains() {
        Map<String, Chain> chains = new HashMap<String, Chain>();
        for (IConfiguration configuration : configurations) {
            chains.putAll(configuration.getChains());
        }
        return chains;
    }


}