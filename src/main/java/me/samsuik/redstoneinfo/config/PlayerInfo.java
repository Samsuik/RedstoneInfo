package me.samsuik.redstoneinfo.config;

import me.samsuik.redstoneinfo.holograms.Hologram;
import me.samsuik.redstoneinfo.objects.RedstoneUpdate;
import me.samsuik.redstoneinfo.objects.UpdateRoot;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerInfo {

    private final Map<RedstoneUpdate, Hologram> displayedMap = new IdentityHashMap<>();
    public final Player player;

    public PlayerInfo(Player player) {
        this.player = player;
    }

    public void notify(UpdateRoot root) {
        Collection<RedstoneUpdate> updates = root == null ? Collections.emptyList() : root.updateMap.values();
        List<RedstoneUpdate> updateList = new ArrayList<>(updates);

        updateList.sort(Comparator.comparingDouble((update)
                -> player.getLocation().distanceSquared(update.location)));

        Map<RedstoneUpdate, Hologram> toRemove = new HashMap<>(displayedMap);

        for (int i = 0; i < updateList.size() && i < 100; ++i) {
            RedstoneUpdate update = updateList.get(i);

            toRemove.remove(update);

            if (displayedMap.containsKey(update)) {
                continue;
            }

            Location location = update.location;
            double x = location.getBlockX() + 0.5;
            double y = location.getBlockY() - 1.6;
            double z = location.getBlockZ() + 0.5;

            Hologram hologram = new Hologram(player, x, y, z);
            hologram.addLine("T " + update.relativeTick);
            hologram.addLine("O " + update.getOrder());

            if (update.getOrder() != update.updateNum) {
                hologram.addLine("U " + update.updateNum);
            }

            hologram.create();

            displayedMap.put(update, hologram);
        }

        for (Map.Entry<RedstoneUpdate, Hologram> entry : toRemove.entrySet()) {
            entry.getValue().destroy();
            displayedMap.remove(entry.getKey());
        }
    }

    public void destroy() {
        for (Hologram hologram : displayedMap.values()) {
            hologram.destroy();
        }
    }

}
