package com.blalp.chatdirector.modules.bungee;

import java.util.ArrayList;

import com.blalp.chatdirector.ChatDirector;
import com.blalp.chatdirector.model.Context;
import com.blalp.chatdirector.model.ILoadable;
import com.blalp.chatdirector.platform.bungee.ChatDirectorBungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

public class BungeeCommand extends Command implements ILoadable {
    public static ArrayList<BungeeCommand> commands = new ArrayList<>();
    public BungeeCommandItem item;
    public BungeeCommand(String name,BungeeCommandItem item) {
        super(name);
        this.item=item;
        commands.add(this);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Context context = BungeeModule.instance.getContext(sender);
        String string = item.command;
        for (int i = 0; i < args.length; i++) {
            string += " "+args[i];
            context.put("COMMAND_ARG_"+i, args[i]);
        }
        context.put("COMMAND_NAME", item.command);
        context.put("COMMAND_PERMISSION", item.permission);
        context.put("CURRENT", string);
        ChatDirector.run(item, context, true);
    }

    @Override
    public void load() {
        ProxyServer.getInstance().getPluginManager().registerCommand(ChatDirectorBungee.instance, this);
    }

    @Override
    public void unload() {
        ProxyServer.getInstance().getPluginManager().unregisterCommand(this);
    }
    
}
