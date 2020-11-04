package com.blalp.chatdirector.model;

import java.util.Map;

import com.blalp.chatdirector.configuration.Configuration;

public abstract class Item implements IItem, Runnable {
    public IItem next;
    private String string;
    public Map<String,String> context;
    public abstract String process(String string,Map<String,String> context);
    public void startWork(String string, boolean newThread, Map<String,String> context) {
        this.string=string;
        this.context=context;
        if (newThread){
            new Thread(this).start();
        } else {
            run();
        }
    }
    public void run() {
        work(string,context);
    }
    public String work(String string,Map<String,String> context){
        if(Configuration.debug){
            System.out.println();
            System.out.println(getClass().getName()+"> Starting work of "+getClass().getCanonicalName()+"("+this+") with string >"+string+"<");
            System.out.print("    ");
            System.out.println(context);
        }
        this.context=context;
        string = this.process(string,this.context);
        if(Configuration.debug){
            System.out.println(getClass().getName()+"> Finished work of "+getClass().getCanonicalName()+"("+this+") with result >"+string+"<");
            System.out.print("    ");
            System.out.println(context);
            System.out.println();
        }
        if(next!=null){
            if(string==null||string.isEmpty()){
                if(Configuration.debug){
                    System.out.println("string is empty, returning.");
                }
                return "";
            }
            this.context.put("STRING", string);
            if(Configuration.debug){
                System.out.println("starting the next's work of "+next);
            }
            String temp = next.work(string,this.context);
            if(Configuration.debug){
                System.out.println("returning "+temp+ " from "+next);
            }
            return temp;
        } else {
            if(Configuration.debug){
                System.out.println("Next was null, aborting with string >"+string+"<");
            }
            return string;
        }
    }
}