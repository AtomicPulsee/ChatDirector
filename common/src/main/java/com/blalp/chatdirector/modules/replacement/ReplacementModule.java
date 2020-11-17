package com.blalp.chatdirector.modules.replacement;

import java.util.Arrays;
import java.util.List;

import com.blalp.chatdirector.model.Context;
import com.blalp.chatdirector.modules.IModule;

public class ReplacementModule implements IModule {

    @Override
    public List<String> getItemNames() {
        return Arrays.asList("regex", "remove-colors", "resolve-colors", "sub-string", "to-lower", "to-upper",
                "to-word");
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
    public Class<?> getItemClass(String type) {
        switch (type) {
            case "regex":
                return RegexItem.class;
            // return new RegexItem(config);
            case "remove-colors":
                return RemoveColorsItem.class;
            case "resolve-colors":
                return ResolveColorsItem.class;
            case "sub-string":
                return SubStringItem.class;
            case "to-lower":
                return ToLowerItem.class;
            case "to-upper":
                return ToUpperItem.class;
            case "to-word":
                return ToWordItem.class;
            default:
                return null;
        }
    }

    @Override
    public Context getContext(Object object) {
        return new Context();
    }

}
