package me.samsuik.redstoneinfo.command;

import me.samsuik.redstoneinfo.config.Configuration;
import me.samsuik.redstoneinfo.config.PlayerInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class RedstoneInfoCommand implements CommandExecutor {

    private final Map<UUID, PlayerInfo> playerInfoMap;

    public RedstoneInfoCommand(Map<UUID, PlayerInfo> playerInfoMap) {
        this.playerInfoMap = playerInfoMap;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (playerInfoMap.containsKey(uuid)) {
            // Already enabled, so we should disable it.
            sender.sendMessage(Configuration.disabledMessage);
            PlayerInfo playerInfo = playerInfoMap.remove(uuid);
            playerInfo.destroy();
        } else {
            // Enable this for the player.
            sender.sendMessage(Configuration.enabledMessage);
            playerInfoMap.put(uuid, new PlayerInfo(player));
        }

        return true;
    }

}
