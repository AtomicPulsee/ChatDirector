package com.blalp.chatdirector.common.modules.context;

import com.blalp.chatdirector.core.ChatDirector;
import com.blalp.chatdirector.core.model.Context;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class ContextSetItem extends ContextItem {
    String value = "%CURRENT%";

    @Override
    public Context process(Context context) {
        Context output = new Context();
        output.put(ChatDirector.format(this.key, context), ChatDirector.format(value, context));
        return output;
    }

    @Override
    public boolean isValid() {
        if (value == null) {
            return false;
        }
        return super.isValid();
    }
}
