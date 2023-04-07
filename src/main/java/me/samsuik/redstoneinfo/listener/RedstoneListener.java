package me.samsuik.redstoneinfo.listener;

import me.samsuik.redstoneinfo.config.PlayerInfo;
import me.samsuik.redstoneinfo.objects.UpdateRoot;
import me.samsuik.redstoneinfo.objects.RedstoneUpdate;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class RedstoneListener implements Listener, Runnable {

    private final List<UpdateRoot> updateRoots = new ArrayList<>();
    private final Map<UUID, PlayerInfo> players;
    private int currentTick = 0;

    public RedstoneListener(Map<UUID, PlayerInfo> players) {
        this.players = players;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        players.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        players.remove(event.getPlayer().getUniqueId()); // hacky fix: changing worlds can cause issues
    }

    @Override
    public void run() {
        // Recalculate the update order within the roots
        for (UpdateRoot root : updateRoots) {
            root.recalculateOrder();
        }

        // Track players, make sure they're within hologram distance and provide updates.
        for (PlayerInfo playerInfo : players.values()) {
            Location location = playerInfo.player.getLocation();
            Collection<RedstoneUpdate> updates = getRoot(location)
                    .map((a) -> a.updateMap.values())
                    .orElse(Collections.emptyList());
            playerInfo.notify(updates);
        }

        // Remove roots that have not been updated in the past 30 seconds
        this.updateRoots.removeIf((root) -> currentTick - root.getUpdateTick() >= 30 * 20);

        // Advance current tick
        this.currentTick++;
    }

    @EventHandler
    public void onRedstone(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();

        // Get the closest root or create one from our location
        UpdateRoot root = getRoot(location).orElseGet(() -> {
            UpdateRoot n = new UpdateRoot(location, currentTick);
            updateRoots.add(n);
            return n;
        });

        RedstoneUpdate update = root.updateMap.get(location);

        // Is this really an update?
        if (update != null && update.power == event.getNewCurrent()) {
            return;
        }

        // This calculates the relative tick from when the root was last updated.
        int relativeTick = currentTick - root.createdTick;
        int updateNum = root.getAndIncrementUpdate();
        int power = event.getNewCurrent();

        // Create a new update containing our newly gathered information.
        update = new RedstoneUpdate(location, relativeTick, updateNum, power);

        // Put the update into the map for later use
        root.updateMap.put(location, update);
        root.setUpdateTick(currentTick);
    }

    private Optional<UpdateRoot> getRoot(Location location) {
        // Get any nearby root if one exists
        for (UpdateRoot root : updateRoots) {
            if (location.getWorld().equals(root.location.getWorld()) && location.distanceSquared(root.location) < 256 * 256) {
                return Optional.of(root);
            }
        }

        return Optional.empty();
    }

}
