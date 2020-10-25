package com.blalp.chatdirector.modules.bukkit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.blalp.chatdirector.ChatDirector;
import com.blalp.chatdirector.model.IItem;
import com.blalp.chatdirector.modules.Module;

public class BukkitModule extends Module {

    @Override
    public void load() {
        ChatDirector.addFormatter(new BukkitFormatter());
        if(BukkitCommandInputDaemon.instance!=null){
            BukkitCommandInputDaemon.instance.load();
        }
    }

    @Override
    public void unload() {
        if(BukkitCommandInputDaemon.instance!=null){
            BukkitCommandInputDaemon.instance.unload();
        }
    }
    @Override
    public String[] getItemNames() {
        return new String[]{"bukkit-input","bukkit-output","bukkit-playerlist","bukkit-command"};
    }

    @Override
    public IItem createItem(String type, Object config) {
        switch (type){
            case "bukkit-input":
                Map<String,Object> configList = (Map<String,Object>)config;
                BukkitInputItem item = new BukkitInputItem();
                if(configList.containsKey("server-stopped")){
                    item.serverStopped= (boolean) configList.get("server-stopped");
                }
                if(configList.containsKey("server-started")){
                    item.serverStarted=(boolean) configList.get("server-started");
                }
                if(configList.containsKey("chat")){
                    item.chat=(boolean) configList.get("chat");
                }
                if(configList.containsKey("check-canceled")){
                    item.checkCanceled=(boolean) configList.get("check-canceled");
                }
                if(configList.containsKey("format")){
                    item.format= (String) configList.get("format");
                }
                if(configList.containsKey("join")){
                    item.join=(boolean) configList.get("join");
                }
                if(configList.containsKey("leave")){
                    item.leave=(boolean) configList.get("leave");
                }
                if(configList.containsKey("new-join")){
                    item.newJoin=(boolean) configList.get("new-join");
                }
                if(configList.containsKey("cancel-chat")){
                    item.cancelChat=(boolean) configList.get("cancel-chat");
                }
                return item;
            case "bukkit-output":
                BukkitOutputItem itemOutput = new BukkitOutputItem();
                if(((LinkedHashMap<String,String>)config).containsKey("permission")) {
                    itemOutput.permission=((LinkedHashMap<String,String>)config).get("permission");
                }
                return itemOutput;
            case "bukkit-playerlist":
                BukkitPlayerlistItem itemPlayerlist = new BukkitPlayerlistItem();
                LinkedHashMap<String,Object> configMapPlayerlist = ((LinkedHashMap<String,Object>)config);
                if(configMapPlayerlist.containsKey("format")) {
                    itemPlayerlist.format= (String) configMapPlayerlist.get("format");
                }
                if(configMapPlayerlist.containsKey("format-no-players")) {
                    itemPlayerlist.formatNoPlayers= (String) configMapPlayerlist.get("format-no-players");
                }
                if(configMapPlayerlist.containsKey("ignore-case")) {
                    itemPlayerlist.ignoreCase= (boolean) configMapPlayerlist.get("ignore-case");
                }
                if(configMapPlayerlist.containsKey("trigger-word")) {
                    itemPlayerlist.triggerWord= (String) configMapPlayerlist.get("trigger-word");
                }
                if(configMapPlayerlist.containsKey("format-player")) {
                    itemPlayerlist.formatPlayer= (String) configMapPlayerlist.get("format-player");
                }
                return itemPlayerlist;
            case "bukkit-command":
                if(BukkitCommandInputDaemon.instance==null){
                    new BukkitCommandInputDaemon();
                }
                LinkedHashMap<String,Object> configMap = ((LinkedHashMap<String,Object>)config);
                BukkitCommandInputItem item2 = new BukkitCommandInputItem((String)configMap.get("command"), (String)configMap.get("permission"));
                if (configMap.containsKey("args")){
                    if(configMap.get("args") instanceof ArrayList<?>){
                        item2.args=((ArrayList<?>)configMap.get("args")).toArray(item2.args);
                    } else {
                        try {
                            throw new NullPointerException("args needs to be a list.");
                        } catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                }
                if(configMap.containsKey("format")){
                    item2.format=(String)configMap.get("format");
                }
                BukkitCommandInputDaemon.instance.addItem(item2);
                return item2;
        }
        return null;
    }
}