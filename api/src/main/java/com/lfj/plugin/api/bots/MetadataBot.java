package com.lfj.plugin.api.bots;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfj.plugin.api.MetadataInterface;

public record MetadataBot(
     @JsonProperty("bot_name") String name,
     @JsonProperty("author") String author,
     @JsonProperty("bot_type") String botType,
     @JsonProperty("version") String version,
     @JsonProperty("main_class") String mainClass
) implements MetadataInterface { }
