package com.blalp.chatdirector;

import java.io.File;

import com.blalp.chatdirector.configuration.ConfigurationBungee;
import com.blalp.chatdirector.model.Item;
import com.blalp.chatdirector.modules.bungee.BungeeCommand;
import com.blalp.chatdirector.modules.bungee.BungeeCommandItem;
import com.blalp.chatdirector.modules.bungee.BungeeInputItemDaemon;
import com.blalp.chatdirector.modules.common.ReloadItem;
import com.blalp.chatdirector.modules.multichat.MultiChatInputItemDaemon;

import net.md_5.bungee.api.plugin.Plugin;


public class ChatDirectorBungee extends Plugin {
    public static ChatDirectorBungee instance;
    private ChatDirector chatDirector;

    public ChatDirectorBungee() {
        try {
            instance = this;
            chatDirector = new ChatDirector(new ConfigurationBungee(this.getDataFolder().getAbsolutePath()+File.separatorChar+"config.yml"));
            BungeeInputItemDaemon.instance=new BungeeInputItemDaemon();
            getProxy().getPluginManager().registerListener(this, BungeeInputItemDaemon.instance);
            MultiChatInputItemDaemon.instance=new MultiChatInputItemDaemon();
            getProxy().getPluginManager().registerListener(this, MultiChatInputItemDaemon.instance);
            this.getDataFolder().mkdirs();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("YIKES! Some error. Registering /chatdirector for you so you can reload.");
            registerReload();
        }
    }
    
    private void registerReload(){
        // In case anything goes wrong, register the reload command
        Item item = new BungeeCommandItem("chatdirector","chatdirector.reload");
        item.next=new ReloadItem();
        for(BungeeCommand command : BungeeCommand.commands){
            if(command.item.equals(item)){
                command.load();
            }
        }
    }

    @Override
    public void onEnable() {
        try {
            chatDirector.load();
            if(chatDirector.chains.size()==0){
                throw new Exception("NO CHAINS!");
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("YIKES! Some error. Registering /chatdirector for you so you can reload.");
            registerReload();
        }
    }

    @Override
    public void onDisable() {
        chatDirector.unload();
    }
}
