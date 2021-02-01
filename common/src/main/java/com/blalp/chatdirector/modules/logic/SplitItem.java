package com.blalp.chatdirector.modules.logic;

import java.util.ArrayList;

import com.blalp.chatdirector.modules.common.PassItem;
import com.blalp.chatdirector.utils.ValidationUtils;
import com.blalp.chatdirector.configuration.Chain;
import com.blalp.chatdirector.model.Context;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(using = SplitDeserializer.class)
public class SplitItem extends PassItem {
    ArrayList<Chain> chains = new ArrayList<Chain>();

    @Override
    public Context process(Context context) {
        for (Chain chain : chains) {
            new Thread(chain).start();
        }
        return new Context();
    }

    @Override
    public boolean isValid() {
        return ValidationUtils.isNotNull(chains);
    }
}