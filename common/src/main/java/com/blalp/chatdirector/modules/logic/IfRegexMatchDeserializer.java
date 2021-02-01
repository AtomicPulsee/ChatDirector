package com.blalp.chatdirector.modules.logic;

import java.io.IOException;

import com.blalp.chatdirector.configuration.Chain;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class IfRegexMatchDeserializer extends JsonDeserializer<IfRegexMatchesItem> {

    @Override
    public IfRegexMatchesItem deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ObjectCodec oc = p.getCodec();
        JsonNode config = oc.readTree(p);
        IfRegexMatchesItem output = new IfRegexMatchesItem();
        if (config.has("yes-chain")) {
            output.yesChain = config.get("yes-chain").traverse(oc).readValueAs(Chain.class);
        }
        if (config.has("no-chain")) {
            output.noChain = config.get("no-chain").traverse(oc).readValueAs(Chain.class);
        }
        if (config.has("regex") && config.get("regex").isTextual()) {
            output.regex = config.get("regex").asText();
        }
        if (config.has("source") && config.get("source").isTextual()) {
            output.source = config.get("source").asText();
        }
        if (config.has("invert") && config.get("invert").isTextual()) {
            output.invert = config.get("invert").asBoolean();
        }
        return output;
    }

}
