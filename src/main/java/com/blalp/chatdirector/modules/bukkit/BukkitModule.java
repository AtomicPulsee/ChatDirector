package com.blalp.chatdirector.modules.bukkit;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.blalp.chatdirector.ChatDirector;
import com.blalp.chatdirector.model.IItem;
import com.blalp.chatdirector.modules.Module;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.NullArgumentException;

public class BukkitModule extends Module {

    @Override
    public void load() {
        ChatDirector.addFormatter(new BukkitFormatter());
        new BukkitInputDaemon().load();
    }

    @Override
    public void unload() {
    }
    @Override
    public String[] getItemNames() {
        return new String[]{"bukkit-input","bukkit-output","bukkit-playerlist","bukkit-command"};
    }

    @Override
    public IItem createItem(String type, Object config) {
        switch (type){
            case "bukkit-input":
                ArrayList<String> configList = (ArrayList<String>)config;
                BukkitInputItem item = new BukkitInputItem();
                if(configList.contains("server-stopped")){
                    item.serverStopped=true;
                }
                if(configList.contains("server-started")){
                    item.serverStarted=true;
                }
                if(configList.contains("chat")){
                    item.chat=true;
                }
                if(configList.contains("check-canceled")){
                    item.checkCanceled=true;
                }
                if(configList.contains("format")){
                    try {
                        throw new NotImplementedException();
                    } catch (NotImplementedException e){
                        e.printStackTrace();
                    }
                }
                if(configList.contains("join")){
                    item.join=true;
                }
                if(configList.contains("leave")){
                    item.leave=true;
                }
                if(configList.contains("new-join")){
                    item.newJoin=true;
                }
                return item;
            case "bukkit-output":
                return new BukkitOutputItem(((LinkedHashMap<String,String>)config).get("permission"));
            case "bukkit-playerlist":
                return new BukkitPlayerlistItem();
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
                            throw new NullArgumentException("args needs to be a list.");
                        } catch (NullArgumentException e){
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