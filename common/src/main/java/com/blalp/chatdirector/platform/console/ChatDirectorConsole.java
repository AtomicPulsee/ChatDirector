package com.blalp.chatdirector.platform.console;

import java.io.Console;

import com.blalp.chatdirector.ChatDirector;
import com.blalp.chatdirector.configuration.Configuration;
import com.blalp.chatdirector.configuration.TimedLoad;

public class ChatDirectorConsole {
    public static void main(String[] args) {
        ChatDirector chatDirector = new ChatDirector(new Configuration(), "config.yml");
        try {
            chatDirector.load();
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(new TimedLoad()).start();
        }
        System.out.println("Finished Loading");
        Console console = System.console();
        String line;
        while (true) {
            System.out.print("ChatDirector > ");
            line = console.readLine();
            if (line.equals("reload")) {
                console.writer().println("Reloading...");
                new Thread(new TimedLoad()).start();
            } else {
                console.writer().println("Only valid command is reload.");
            }
        }
    }
}