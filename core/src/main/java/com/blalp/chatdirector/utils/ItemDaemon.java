package com.blalp.chatdirector.utils;

import java.util.ArrayList;
import java.util.List;

import com.blalp.chatdirector.model.IItem;
import com.blalp.chatdirector.model.ILoadable;

public abstract class ItemDaemon implements ILoadable {
    public List<IItem> items = new ArrayList<>();

    public void addItem(IItem item) {
        items.add(item);
    }

    public boolean load() {
        return true;
    }

    public boolean unload() {
        items = new ArrayList<>();
        return true;
    }
}
