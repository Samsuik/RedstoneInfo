package me.samsuik.redstoneinfo.holograms;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    private final List<HologramPart> parts = new ArrayList<>();

    private final Player player;
    private final double x;
    private final double y;
    private final double z;
    private int line;

    public Hologram(Player player, double x, double y, double z) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private double lineOffset() {
        return 0.25 * line++;
    }

    public void addLine(String line) {
        parts.add(new HologramPart(player, line, x, y - lineOffset() , z));
    }

    public void create() {
        for (HologramPart part : parts) {
            part.create();
        }
    }

    public void destroy() {
        for (HologramPart part : parts) {
            part.destroy();
        }
    }

}
