package com.blalp.chatdirector.internalModules.common;

import java.util.Map;

import com.blalp.chatdirector.model.IItem;

public class NullItem implements IItem {

    @Override
    public String process(String string, Map<String,String> context) {
        return null;
    }

    @Override
    public void work(String string, Map<String,String> context) {
        return;
    }

    @Override
    public void startWork(String string, boolean newThread, Map<String, String> context) {
        return;
    }
    
}