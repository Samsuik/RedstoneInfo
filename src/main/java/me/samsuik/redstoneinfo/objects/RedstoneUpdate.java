package me.samsuik.redstoneinfo.objects;

import org.bukkit.Location;

public class RedstoneUpdate {

    public final Location location;
    public final int relativeTick;
    public final int updateNum;
    public final int power;
    public int order = -1;

    public RedstoneUpdate(Location location, int relativeTick, int updateNum, int power) {
        this.location = location;
        this.relativeTick = relativeTick;
        this.updateNum = updateNum;
        this.power = power;
    }

}
