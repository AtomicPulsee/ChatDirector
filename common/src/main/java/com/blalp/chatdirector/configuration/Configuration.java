package com.blalp.chatdirector.configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.blalp.chatdirector.ChatDirector;
import com.blalp.chatdirector.modules.common.CommonModule;
import com.blalp.chatdirector.modules.common.StopItem;
import com.blalp.chatdirector.model.format.Formatters;
import com.blalp.chatdirector.model.format.IFormatter;
import com.blalp.chatdirector.model.IItem;
import com.blalp.chatdirector.model.Item;
import com.blalp.chatdirector.model.Loadable;
import com.blalp.chatdirector.model.Pipe;
import com.blalp.chatdirector.modules.IModule;
import com.blalp.chatdirector.modules.console.ConsoleModule;
import com.blalp.chatdirector.modules.context.ContextModule;
import com.blalp.chatdirector.modules.file.FileModule;
import com.blalp.chatdirector.modules.logic.LogicModule;
import com.blalp.chatdirector.modules.luckperms.LuckPermsModule;
import com.blalp.chatdirector.modules.replacement.ReplacementModule;
import com.blalp.chatdirector.modules.sql.SQLModule;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.scanner.ScannerException;

public class Configuration extends Loadable {

    String fileName;
    public static List<IModule> loadedModules = new ArrayList<>();
	public static boolean debug;
    public HashMap<String,Pipe> chains = new HashMap<String,Pipe>();

    public Configuration(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void load() {
//        Constructor constructor = new Constructor(BaseConfiguration.class);
//        TypeDescription typeDescription = new TypeDescription(BaseConfiguration.class);
//        typeDescription.putMapPropertyType("modules", String.class, Module.class);
//        constructor.addTypeDescription(typeDescription);
        Yaml yaml = new Yaml();
        try {
            Map<String,Object> configuration = (Map<String,Object>)yaml.load(new FileReader(fileName));
            if(configuration.containsKey("debug")){
                debug= (boolean) configuration.get("debug");
            }
            loadedModules.add(new CommonModule());
            if (configuration.containsKey("modules")) {
                for (Object key : (Iterable<Object>)configuration.get("modules")) {
                    loadedModules.add(loadModule(key));
                    if(loadedModules.contains(null)){
                        loadedModules.remove(null);
                        try {
                            throw new NullPointerException(key+" is not a valid module for this platform");
                        } catch(NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (configuration.containsKey("chains")) {
                for (LinkedHashMap<String,ArrayList<LinkedHashMap<String,Object>>> outerKey : ((ArrayList<LinkedHashMap<String,ArrayList<LinkedHashMap<String,Object>>>>)configuration.get("chains"))) {
                    for (String key : outerKey.keySet()) {
                        chains.put(key,new Pipe(loadItems(outerKey.get(key))));
                    }
                }
            }
            if(debug){
                System.out.println("Formatters");
                for (IFormatter formatter : ((Formatters)ChatDirector.formatter).formatters) {
                    System.out.println(formatter);
                }
                System.out.println("Modules");
                for (IModule module : loadedModules) {
                    System.out.println(module);
                }
                System.out.println("Pipes");
                for (String pipeKey: chains.keySet()) {
                    System.out.println("Pipe "+pipeKey);
                    IItem item = chains.get(pipeKey).rootItem;
                    while(item!=null&&!(item instanceof StopItem)) {
                        System.out.println(item);
                        if (item instanceof Item){
                            item=((Item)item).next;
                        } else {
                            System.out.println("Not an Item, don't know how to go deeper");
                        }
                    }
                }
            }


        } catch (FileNotFoundException e) {
            System.err.println("CONFIG NOT FOUND!");
            e.printStackTrace();
        } catch (Exception e){
            System.err.println("Invalid YAML");
            e.printStackTrace();
            System.out.println("Reloading every 10 seconds");
            new Thread(new TimedLoad()).start();
        }
    }
    public static IItem loadItems(ArrayList<LinkedHashMap<String,Object>> items) {
        IItem nullItem = new StopItem();
        if(items==null){
            return nullItem;
        }
        IItem output = null;
        IItem lastItem = null;
        IItem item=nullItem;
        LinkedHashMap<String,Object> pipeVal=null;
        for(Object pipeValObj:items) {
            try {
                pipeVal = (LinkedHashMap<String, Object>) pipeValObj;
            } catch (ClassCastException e){
                System.err.println("Make sure to have a `: null` after any items that don't require configuration.");
                e.printStackTrace();
                continue;
            }
            item = loadItem((String)pipeVal.keySet().toArray()[0],pipeVal.values().toArray()[0]);
            if(output==null){
                output=item;
            }
            if(lastItem instanceof Item){
                ((Item)lastItem).next=item;
            }
            lastItem=item;
        }
        if(item instanceof Item){
            ((Item)item).next=nullItem;
        }
        return output;
    }
    public static IItem loadItem(String key, Object item) {
        for (IModule iModule : loadedModules) {
            for(String itemType: iModule.getItemNames()){
                if(key.equalsIgnoreCase(itemType)){
                    return iModule.createItem(key, item);
                }
            }
        }
        try {
            throw new NullPointerException("Item of type "+key+" Not found.");
        } catch (NullPointerException e){
            e.printStackTrace();
            System.err.println("Item of type "+key+" Not found.");
        }
        return null;
    }
    protected IModule loadModule(Object module) {
        String type="";
        if (module instanceof String){
            type=(String)module;
        } else if (module instanceof List){
            type=(String)((List)module).get(0);
        } else if (module instanceof Map){
            type=(String)((Map)module).keySet().toArray()[0];
        }
        switch (type) {
            case "logic":
                return new LogicModule();
            case "console":
                return new ConsoleModule();
            case "context":
                return new ContextModule();
            // JDA isn't working and functionally the same as javacord anyways
            //case "discord-jda":
            //   return new com.blalp.chatdirector.modules.jda.DiscordModule((LinkedHashMap<String,LinkedHashMap<String,String>>) ((Map)module).get(type));
            case "discord":
            case "discord-javacord":
                return new com.blalp.chatdirector.modules.javacord.DiscordModule((LinkedHashMap<String,LinkedHashMap<String,String>>) ((Map)module).get(type));
            case "file":
                return new FileModule();
            case "luckperms":
                return new LuckPermsModule();
            case "replacement":
                return new ReplacementModule();
            case "sql":
                return new SQLModule((LinkedHashMap<String,LinkedHashMap<String,String>>) ((Map)module).get(type));
            default:
                return null;
        }
    }

    @Override
    public void unload() {
        loadedModules = new ArrayList<>();
        chains = new HashMap<String,Pipe>();
    }
    
}