package com.blalp.chatdirector.modules.javacord;

import java.util.concurrent.ExecutionException;

import com.blalp.chatdirector.ChatDirector;
import com.blalp.chatdirector.model.Context;
import com.blalp.chatdirector.utils.ValidationUtils;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class DiscordDeleteMessage extends DiscordItem {

    String message;
    String channel;

    @Override
    public Context process(Context context) {
        try {
            DiscordBot.get(bot).getDiscordApi()
                    .getMessageById(ChatDirector.format(message, context),
                            DiscordBot.get(bot).getDiscordApi()
                                    .getTextChannelById(ChatDirector.format(channel, context)).get())
                    .get().delete().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return new Context();
    }

    @Override
    public boolean isValid() {
        return ValidationUtils.hasContent(channel) && ValidationUtils.hasContent(message) && super.isValid();
    }
}
