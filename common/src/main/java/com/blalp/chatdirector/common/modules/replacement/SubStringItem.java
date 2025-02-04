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
public class SubStringItem implements IItem {
    int start = 0;
    int end = -1;

    @Override
    public Context process(Context context) {
        if (end == -1) {
            return new Context(context.getCurrent().substring(start));
        } else {
            return new Context(context.getCurrent().substring(start, end));
        }
    }

    @Override
    public boolean isValid() {
        return true;
    }

}
