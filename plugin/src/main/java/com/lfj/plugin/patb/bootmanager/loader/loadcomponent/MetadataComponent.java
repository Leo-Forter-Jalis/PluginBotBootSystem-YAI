package com.lfj.plugin.patb.bootmanager.loader.loadcomponent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfj.plugin.patb.bootmanager.MetadataType;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Optional;

public class MetadataComponent {
    private MetadataComponent(){
    }
    public static Optional<Object> loadMetadata(URLClassLoader classLoader, String resourceName, MetadataType type) throws IOException {
        InputStream metadataIS = classLoader.getResourceAsStream(resourceName);
        ObjectMapper mapper = new ObjectMapper();
        return Optional.ofNullable(mapper.readValue(metadataIS, type.getMetadataType()));
    }
}
