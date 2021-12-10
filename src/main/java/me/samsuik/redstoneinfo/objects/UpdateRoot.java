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

    public void handleOrder() {
        int order = 0;

        for (RedstoneUpdate update : updateMap.values()) {
            if (update.getOrder() == -1) {
                update.setOrder(order++);
            }
        }

        updateNum = 0;
    }

}
