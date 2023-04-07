package me.samsuik.redstoneinfo;

import me.samsuik.redstoneinfo.command.RedstoneInfoCommand;
import me.samsuik.redstoneinfo.config.Configuration;
import me.samsuik.redstoneinfo.config.PlayerInfo;
import me.samsuik.redstoneinfo.listener.RedstoneListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class RedstoneInfo extends JavaPlugin {

    @Override
    public void onEnable() {
        // First of all, load our configuration.
        Configuration.load(this);

        // Used to keep track of what players have this feature enabled.
        Map<UUID, PlayerInfo> players = new HashMap<>();

        // Create the command, and pass our set for it to use as well.
        RedstoneInfoCommand command = new RedstoneInfoCommand(players);
        getCommand("redstoneinfo").setExecutor(command);

        // Listen for redstone changes and later display their update order to players
        RedstoneListener redstoneListener = new RedstoneListener(players);
        Bukkit.getPluginManager().registerEvents(redstoneListener, this);
        Bukkit.getScheduler().runTaskTimer(this, redstoneListener, 0, 0);
    }

}
