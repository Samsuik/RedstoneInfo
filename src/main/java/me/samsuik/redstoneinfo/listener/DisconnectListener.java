package me.samsuik.redstoneinfo.listener;

import me.samsuik.redstoneinfo.config.PlayerInfo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;

public class DisconnectListener implements Listener {

    private final Collection<PlayerInfo> enabled;

    public DisconnectListener(Collection<PlayerInfo> enabled) {
        this.enabled = enabled;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.enabled.remove(new PlayerInfo(event.getPlayer()));
    }

}
