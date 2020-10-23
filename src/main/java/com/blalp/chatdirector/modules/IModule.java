package com.blalp.chatdirector.modules;

import com.blalp.chatdirector.internalModules.format.IFormatter;
import com.blalp.chatdirector.model.IItem;
import com.blalp.chatdirector.model.ILoadable;

public interface IModule extends ILoadable {
    public String[] getItemNames();
    public IItem createItem(String type, Object config);
    public IFormatter getFormatter();
}