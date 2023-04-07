package me.samsuik.redstoneinfo.objects;

import org.bukkit.Location;

import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateRoot {

    public final Map<Location, RedstoneUpdate> updateMap = new LinkedHashMap<>();
    public final Location location;
    public final int createdTick;

    private int updateNum = 0;
    private int updateTick;

    public UpdateRoot(Location location, int createdTick) {
        this.location = location;
        this.createdTick = createdTick;
        this.updateTick = createdTick;
    }

    public void setUpdateTick(int updateTick) {
        this.updateTick = updateTick;
    }

    public int getUpdateTick() {
        return updateTick;
    }

    public int getAndIncrementUpdate() {
        return updateNum++;
    }

    public void recalculateOrder() {
        int order = 0;

        for (RedstoneUpdate update : updateMap.values()) {
            if (update.order == -1) {
                update.order = order++;
            }
        }

        updateNum = 0;
    }

}
