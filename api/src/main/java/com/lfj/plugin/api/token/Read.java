package com.lfj.plugin.api.token;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;

public class Read {
    public static String readToken(File directory){
        File file = new File(directory, "token.yaml");
        if(!file.exists()){
            try {
                file.createNewFile();
            }catch (IOException e) {e.printStackTrace();}
            return "TOKEN";
        }
        YAMLMapper yaml = new YAMLMapper(new YAMLFactory());
        TokenClass token = null;
        try {
            token = yaml.readValue(file, TokenClass.class);
        }catch (IOException e) { e.printStackTrace(); }
        if(token == null) return "TOKEN";
        return token.getToken();
    }
}
