package com.blalp.chatdirector.modules.vault;
import java.util.Map;

import com.blalp.chatdirector.model.PermissionItem;
import org.bukkit.Bukkit;

public class VaultContextItem extends PermissionItem {

    @Override
    public String getPrefix(String playerName) {
        return VaultModule.chat.getPlayerPrefix(Bukkit.getWorlds().get(0).getName(),Bukkit.getOfflinePlayer(playerName));
    }

    @Override
    public String getSuffix(String playerName) {
        return VaultModule.chat.getPlayerSuffix(Bukkit.getWorlds().get(0).getName(),Bukkit.getOfflinePlayer(playerName));
    }

    @Override
    public String getGroup(String playerName) {
        return VaultModule.chat.getPlayerGroups(Bukkit.getWorlds().get(0).getName(),Bukkit.getOfflinePlayer(playerName))[0];
    }
    @Override
    public String process(String string, Map<String, String> context) {
        super.process(string, context);
        if(this.context.containsKey("PLAYER_NAME")){
            this.context.put("BALANCE", Double.toString(VaultModule.economy.getBalance(Bukkit.getOfflinePlayer(this.context.get("PLAYER_NAME")))));
        }
        return string;
    }
}