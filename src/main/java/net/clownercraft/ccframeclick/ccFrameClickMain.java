package net.clownercraft.ccframeclick;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ccFrameClickMain extends JavaPlugin {
    private static ccFrameClickMain instance;

    @Override
    public void onEnable() {
        instance = this;

        //load config
        saveDefaultConfig();
        Config.init();

        //register command
        this.getCommand("ccframe").setExecutor(new ccFrameCommand());

        //register listeners
        Bukkit.getPluginManager().registerEvents(new ccFrameClickListener(), this);

    }

    @Override
    public void onDisable() {
        //Save config for safety
        Config.save();
    }


    static ccFrameClickMain getInstance() {
        return instance;
    }
}
