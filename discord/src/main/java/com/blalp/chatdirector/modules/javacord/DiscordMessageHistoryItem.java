package com.blalp.chatdirector.modules.javacord;

import com.blalp.chatdirector.ChatDirector;
import com.blalp.chatdirector.model.Context;
import com.blalp.chatdirector.utils.ValidationUtils;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageSet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class DiscordMessageHistoryItem extends DiscordItem {
    String format = "%DISCORD_AUTHOR_DISPLAY_NAME% (%DISCORD_AUTHOR_ID%): %DISCORD_MESSAGE%";
    int length = 50;
    String channel;

    @Override
    public Context process(Context context) {
        Context workingContext = new Context(context);
        MessageSet messages = ((DiscordBots) ChatDirector.getConfig().getOrCreateDaemon(DiscordBots.class)).get(bot)
                .getDiscordApi().getTextChannelById(ChatDirector.format(channel, context)).get().getMessages(length)
                .join();
        String output = "";
        for (Message message : messages) {
            workingContext.merge(
                    ((DiscordModule) ChatDirector.getConfig().getModule(DiscordModule.class)).getContext(message));
            output += ChatDirector.format(format, workingContext);
        }
        return new Context(output);
    }

    @Override
    public boolean isValid() {
        return ValidationUtils.hasContent(format, channel) && super.isValid();
    }
}
