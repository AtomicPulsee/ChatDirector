package com.blalp.chatdirector.model;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemDaemon extends Loadable {
    public List<Item> items = new ArrayList<>();
    public void addItem(Item item){
        items.add(item);
    }
    public void load(){}
    public void unload(){
        items=new ArrayList<>();
    }
}
