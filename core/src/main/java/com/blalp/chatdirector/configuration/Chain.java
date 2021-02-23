package com.blalp.chatdirector.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.blalp.chatdirector.ChatDirector;
import com.blalp.chatdirector.model.Context;
import com.blalp.chatdirector.model.IItem;
import com.blalp.chatdirector.model.IValid;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@JsonDeserialize(using = ChainDeserializer.class)
public class Chain implements IValid, Runnable {
    public List<IItem> items = new ArrayList<>();
    int index;
    Context context;

    /**
     * This Starts execution at an item on a new thread.
     * 
     * @param item
     * @param context
     */
    public void runAsync(IItem item, Context context) {
        this.index = items.indexOf(item);
        if (index == -1) {
            ChatDirector.logger.log(Level.SEVERE, item + " not found in this " + this + " chain.");
            return;
        }
        this.context = context;
        new Thread(this).start();
    }

    /**
     * This Starts execution at an item.
     * 
     * @param item
     * @param context
     * @return Modified Contexts
     */
    public Context runAt(IItem item, Context context) {
        index = items.indexOf(item);
        if (index == -1) {
            ChatDirector.logger.log(Level.SEVERE, item + " not found in this " + this + " chain.");
            return new Context().halt();
        }
        return runAt(items.indexOf(item), context);
    }

    /**
     * This Starts execution at an index.
     * 
     * @param indexOf
     * @return Modified Contexts
     */
    private Context runAt(int indexOf, Context context) {
        Context workingContext = new Context();
        Context output;
        for (int i = indexOf; i < items.size(); i++) {
            if (ChatDirector.getConfig().debug) {
                ChatDirector.logger.log(Level.WARNING,
                        "Starting process of " + items.get(i) + " with context " + context.toString());
            }
            output = items.get(i).process(context);
            if (ChatDirector.getConfig().debug) {
                ChatDirector.logger.log(Level.WARNING,
                        "Ended process of " + items.get(i) + " with context " + output.toString());
            }
            // Setup LAST and CURRENT contexts
            if (output != null) {
                // Only if CURRENT was changed and not null set LAST
                if (context.get("CURRENT") != null && output.get("CURRENT") != null
                        && !output.getCurrent().equals(context.getCurrent())) {
                    output.put("LAST", context.get("CURRENT"));
                }
                workingContext.merge(output);
                context.merge(output);
                if (context.isHalt()) {
                    if (ChatDirector.getConfig().debug) {
                        ChatDirector.logger.log(Level.WARNING, "Quitting chain, Halt received. " + this);
                    }
                    break;
                }
            } else {
                if (ChatDirector.getConfig().debug) {
                    ChatDirector.logger.log(Level.WARNING, "Quitting chain. " + this);
                }
                break;
            }
        }
        return workingContext;
    }

    /**
     * @param item
     * @return Whether or not items contains item
     */
    public boolean contains(IItem item) {
        return items.contains(item);
    }

    /**
     * Runs chain from Start
     * 
     * @param context
     * @return Modified Contexts
     */
    public Context run(Context context) {
        Context output = context;
        output.merge(runAt(0, context));
        return output;
    }

    @Override
    public boolean isValid() {
        for (IItem item : items) {
            if (!item.isValid()) {
                ChatDirector.logger.log(Level.SEVERE, item + " is not valid.");
                return false;
            }
        }
        return true;
    }

    @Override
    public void run() {
        runAt(index, context);
    }

    public void addItem(IItem item) {
        items.add(item);
        ChatDirector.addItem(item, this);
    }
}