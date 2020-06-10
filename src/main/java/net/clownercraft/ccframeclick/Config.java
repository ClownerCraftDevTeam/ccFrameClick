package net.clownercraft.ccframeclick;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

class Config {

    static HashMap<Location,String> frames; //Frame location, command to execute

    //message strings
    static String must_be_player, no_permission, missing_argument, click_to_add, click_to_remove, frame_removed, frame_added,reloaded;

    static void init() {
        //load main config
        frames = new HashMap<>();
        ccFrameClickMain main = ccFrameClickMain.getInstance();

        YamlConfiguration conf = (YamlConfiguration) main.getConfig();
        ConfigurationSection sec = conf.getConfigurationSection("frames");
        assert sec != null;
        Set<String> keys = sec.getKeys(false);

        for (String key: keys) {
            ConfigurationSection sec2 = sec.getConfigurationSection(key);
            assert sec2 != null;
            Location loc = sec2.getLocation("location");
            String command = sec2.getString("command");
            frames.put(loc,command);
        }

        //Load messages
        YamlConfiguration messagesConf = new YamlConfiguration();

        File messagesFile = new File(main.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            main.saveResource("messages.yml",false);
        }

        try {
            messagesConf.load(messagesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        must_be_player = messagesConf.getString("Command.must_be_player");
        no_permission = messagesConf.getString("Command.no_permission");
        missing_argument = messagesConf.getString("Command.missing_argument");
        click_to_add = messagesConf.getString("Command.click_to_add");
        click_to_remove = messagesConf.getString("Command.click_to_remove");
        frame_added = messagesConf.getString("Command.frame_added");
        frame_removed = messagesConf.getString("Command.frame_removed");
        reloaded = messagesConf.getString("Command.reloaded");

    }

    static void save() {

        ccFrameClickMain main = ccFrameClickMain.getInstance();
        YamlConfiguration conf = (YamlConfiguration) main.getConfig();

        Set<Location> keys = frames.keySet();
        conf.set("frames", null);

        int i=0;
        for (Location key : keys) {
            conf.set("frames." + i + ".location",key);
            conf.set("frames." + i + ".command",frames.get(key));
            i++;
        }

        try {
            conf.save(new File(main.getDataFolder(),"config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
