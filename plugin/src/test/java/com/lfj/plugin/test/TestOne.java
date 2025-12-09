package com.lfj.plugin.test;

import com.lfj.plugin.api.MetaData;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.lfj.plugin.patb.botmanager.load.ThreadManager;
import com.lfj.plugin.patb.botmanager.NewLoad;

public class TestOne {
    @Test
    public void test() {
        File directory = new File("test-pl");
        ThreadManager manager = new ThreadManager();
        NewLoad load = new NewLoad(manager, directory, null);
        load.load();
    }
}
