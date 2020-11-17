package com.blalp.chatdirector.modules.context;

import java.util.Arrays;
import java.util.List;

import com.blalp.chatdirector.model.Context;
import com.blalp.chatdirector.modules.IModule;

public class ContextModule implements IModule {

    @Override
    public List<String> getItemNames() {
        return Arrays.asList("get-context", "set-context", "remove-context", "resolve-context");
    }

    @Override
    public void load() {
    }

    @Override
    public void unload() {
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Context getContext(Object object) {
        return new Context();
    }

    @Override
    public Class<?> getItemClass(String type) {
        switch (type) {
            case "get-context":
                return ContextGetItem.class;
            case "resolve-context":
                return ContextResolveItem.class;
            case "set-context":
                return ContextSetItem.class;
            case "remove-context":
                return ContextRemoveItem.class;
            default:
                return null;
        }
    }
    
}
