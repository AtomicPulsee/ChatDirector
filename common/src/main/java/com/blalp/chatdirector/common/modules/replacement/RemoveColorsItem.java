package com.blalp.chatdirector.common.modules.replacement;

import com.blalp.chatdirector.core.model.Context;
import com.blalp.chatdirector.core.model.IItem;
import lombok.Data;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
@NoArgsConstructor
@Data
public class RemoveColorsItem implements IItem {

    @Override
    public Context process(Context context) {
        return new Context(context.getCurrent().replaceAll("(&|§)([a-z]|[0-9])", ""));
    }

    @Override
    public boolean isValid() {
        return true;
    }

}