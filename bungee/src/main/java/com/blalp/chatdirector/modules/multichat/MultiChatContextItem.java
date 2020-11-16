package com.blalp.chatdirector.modules.multichat;

import java.util.Optional;
import java.util.UUID;

import com.blalp.chatdirector.model.Context;
import com.blalp.chatdirector.model.IItem;

import xyz.olivermartin.multichat.bungee.PlayerMeta;
import xyz.olivermartin.multichat.bungee.PlayerMetaManager;

public class MultiChatContextItem implements IItem {

    @Override
    public Context process(Context context) {
        Context output = new Context();
        if(context.containsKey("PLAYER_UUID")){
            Optional<PlayerMeta> playerMeta = PlayerMetaManager.getInstance().getPlayer(UUID.fromString(context.get("PLAYER_UUID")));
            if(playerMeta.isPresent()){
                output.put("PLAYER_PREFIX", playerMeta.get().prefix);
                output.put("PLAYER_SUFFIX", playerMeta.get().suffix);
                output.put("PLAYER_NICK", playerMeta.get().nick);
                output.put("PLAYER_WORLD", playerMeta.get().world);
                output.put("PLAYER_NAME", playerMeta.get().name);
            }
        }
        return output;
    }

    @Override
    public boolean isValid() {
        return true;
    }
    
}
