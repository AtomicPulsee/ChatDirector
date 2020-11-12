package com.blalp.chatdirector.configuration;

import com.blalp.chatdirector.ChatDirector;

public class TimedLoad implements Runnable {
    private static TimedLoad instance;
    boolean loop=true;
    public TimedLoad(){
        if(instance!=null){
            loop=false;
        } else {
            instance=this;
        }
    }

    @Override
    public void run() {
        System.out.println("Starting Timed load");
        while (loop) {
            try {
                System.out.println("Timed load sleeping");
                Thread.sleep(10000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.out.println("Timed load attempting to unload");
            try {
                ChatDirector.instance.unload();
            } catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("Timed load attempting to load");
            try {
                ChatDirector.instance.load();
                if(ChatDirector.config.chains.size()==0){
                    throw new Exception("No CHAINS!");
                }
                System.out.println("Timed load completed.");
                loop=false;
                instance=null;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
}
