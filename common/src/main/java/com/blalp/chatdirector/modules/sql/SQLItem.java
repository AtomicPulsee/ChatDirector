package com.blalp.chatdirector.modules.sql;

import com.blalp.chatdirector.model.Item;

public abstract class SQLItem extends Item {
    public String table,name,key,connectionName;
    public boolean cache;
    public SQLItem(String table,String name,String key,String connectionName,boolean cache){
        this.table=table;
        this.name=name;
        this.key=key;
        this.connectionName=connectionName;
        this.cache=cache;
    }
}
