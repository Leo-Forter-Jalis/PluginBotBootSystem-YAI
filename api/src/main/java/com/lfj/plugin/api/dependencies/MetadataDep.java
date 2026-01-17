package com.lfj.plugin.api.dependencies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfj.plugin.api.MetadataInterface;

public record MetadataDep (
    @JsonProperty(value = "name") String dependencyName,
    @JsonProperty(value = "author") String dependencyAuthor,
    @JsonProperty(value = "version") String dependencyVersion,
    @JsonProperty(value = "main_class") String mainClass
) implements MetadataInterface { }
