package com.lfj.plugin.patb.botmanager.load;

import java.io.File;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class JarFileList {
    private static final List<File> FILES = new ArrayList<>();
    private static boolean isInit = false;
    public static void init(File directory) throws NoSuchFileException, UnsupportedOperationException {
        if(isInit) throw new UnsupportedOperationException("Class is initialized!");
        File[] files = directory.listFiles((dir, name) ->  name.endsWith(".jar"));
        if(files == null) throw new NoSuchFileException("Files is null!");
        FILES.addAll(Arrays.stream(files).toList());
        isInit = true;
    }
    public static List<File> getCopyFileList(){
        return new ArrayList<>(FILES);
    }
}
