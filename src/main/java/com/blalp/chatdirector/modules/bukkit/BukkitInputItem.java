package com.blalp.chatdirector.modules.bukkit;

import com.blalp.chatdirector.model.Item;

public class BukkitInputItem extends Item {
    public boolean chat=false,checkCanceled=false,join=false,leave=false,serverStarted=false,serverStopped=false,newJoin=false;
    public String format;

    @Override
    public String process(String string) {
        return string;
    }
    public boolean isChat() {
        return chat;
    }
    public boolean isCheckCanceled() {
        return checkCanceled;
    }
    public boolean isJoin() {
        return join;
    }
    public boolean isLeave() {
        return leave;
    }
    public boolean isServerStarted() {
        return serverStarted;
    }
    public boolean isServerStopped() {
        return serverStopped;
    }
    public String getFormat() {
        return format;
    }
	public boolean isNewJoin() {
		return newJoin;
	}

}