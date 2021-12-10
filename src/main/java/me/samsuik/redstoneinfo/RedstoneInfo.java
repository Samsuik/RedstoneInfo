package me.samsuik.redstoneinfo;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import me.samsuik.redstoneinfo.command.RedstoneInfoCommand;
import me.samsuik.redstoneinfo.config.Configuration;
import me.samsuik.redstoneinfo.config.PlayerInfo;
import me.samsuik.redstoneinfo.listener.DisconnectListener;
import me.samsuik.redstoneinfo.listener.RedstoneListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class RedstoneInfo extends JavaPlugin {

    @Override
    public void onEnable() {
        // First of all, load our configuration.
        Configuration.load(this);

        // Used to keep track of what players have this enabled.
        Map<UUID, PlayerInfo> players = new HashMap<>();

        // Create the command, and pass our set for it to use as well.
        RedstoneInfoCommand command = new RedstoneInfoCommand(players);
        getCommand("redstoneinfo").setExecutor(command);

        // We're listening for players disconnecting so we can remove them from the set
        DisconnectListener disconnectListener = new DisconnectListener(players.values());
        Bukkit.getPluginManager().registerEvents(disconnectListener, this);

        // Listen for redstone changes and display them
        RedstoneListener redstoneListener = new RedstoneListener(players.values());
        Bukkit.getPluginManager().registerEvents(redstoneListener, this);
        Bukkit.getScheduler().runTaskTimer(this, redstoneListener, 0, 0);
    }

}
