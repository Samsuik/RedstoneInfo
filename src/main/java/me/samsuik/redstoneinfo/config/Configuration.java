package me.samsuik.redstoneinfo.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Configuration {

    public static String enabledMessage;
    public static String disabledMessage;

    public static void load(JavaPlugin plugin) {
        FileConfiguration config = plugin.getConfig();
        config.options().copyDefaults(true);
        plugin.saveDefaultConfig();

        enabledMessage = parse(config.getString("command.enabled"));
        disabledMessage = parse(config.getString("command.disabled"));
    }

    private static String getMessage(FileConfiguration config, String path) {
        String message = config.getString(path, null);

        if (message != null) {
            return message;
        }

        return String.join("\n", config.getStringList(path));
    }

    private static String parse(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
