package me.samsuik.redstoneinfo.listener;

import me.samsuik.redstoneinfo.config.PlayerInfo;
import me.samsuik.redstoneinfo.objects.UpdateRoot;
import me.samsuik.redstoneinfo.objects.RedstoneUpdate;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import java.util.*;

public class RedstoneListener implements Listener, Runnable {

    private final List<UpdateRoot> updateRoots = new ArrayList<>();
    private final Collection<PlayerInfo> players;
    private int currentTick = 0;

    public RedstoneListener(Collection<PlayerInfo> players) {
        this.players = players;
    }

    @Override
    public void run() {
        // Handle the order within the roots
        for (UpdateRoot root : updateRoots) {
            root.handleOrder();
        }

        // Update players
        for (PlayerInfo playerInfo : players) {
            Location location = playerInfo.player.getLocation();
            UpdateRoot root = getRoot(location);
            playerInfo.notify(root);
        }

        // It has not been updated in 30 seconds, so we should remove it
        this.updateRoots.removeIf((root) -> currentTick - root.getUpdateTick() > 600);

        // Advance current tick
        this.currentTick++;
    }

    @EventHandler
    public void onRedstone(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();

        // Get the closest root or create one from our location
        UpdateRoot root = getOrCreateRoot(location);
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

    private UpdateRoot getOrCreateRoot(Location location)  {
        UpdateRoot root = getRoot(location);

        if (root == null) {
            // It doesn't exist, let's create one!
            root = new UpdateRoot(location, currentTick);
            updateRoots.add(root);
        }

        return root;
    }

    private UpdateRoot getRoot(Location location) {
        // Get any nearby root if one exists
        for (UpdateRoot root : updateRoots) {
            if (location.distanceSquared(root.location) < 256 * 256) {
                return root;
            }
        }

        return null;
    }

}
