package me.samsuik.redstoneinfo.holograms;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class HologramPart {

    private static final ProtocolManager PROTOCOL_MANAGER = ProtocolLibrary.getProtocolManager();
    private static int ENTITY_ID = -13600000; // high number that is unlikely to be used
    
    private final Player player;
    private final String line;
    private final double x;
    private final double y;
    private final double z;
    private final int id;

    public HologramPart(Player player, String line, double x, double y, double z) {
        this.player = player;
        this.line = line;
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = ENTITY_ID++;
    }

    public void create() {
        // Spawn packet
        PacketContainer container = PROTOCOL_MANAGER.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        StructureModifier<Integer> integers = container.getIntegers();

        integers.write(0, id);
        integers.write(1, NumberConversions.floor(x * 32.0));
        integers.write(2, NumberConversions.floor(y * 32.0));
        integers.write(3, NumberConversions.floor(z * 32.0));
        integers.write(9, 78);

        try {
            PROTOCOL_MANAGER.sendServerPacket(player, container);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        container = PROTOCOL_MANAGER.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        integers = container.getIntegers();
        integers.write(0, id);

        StructureModifier<List<WrappedWatchableObject>> watchables = container.getWatchableCollectionModifier();

        List<WrappedWatchableObject> list = new ArrayList<>();
        list.add(new WrappedWatchableObject(0, (byte) 32));
        list.add(new WrappedWatchableObject(2, line));
        list.add(new WrappedWatchableObject(3, (byte) 1));
        list.add(new WrappedWatchableObject(5, (byte) 1));
        watchables.write(0, list);

        try {
            PROTOCOL_MANAGER.sendServerPacket(player, container);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        // Destroy packet
        PacketContainer container = PROTOCOL_MANAGER.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        StructureModifier<int[]> integers = container.getIntegerArrays();
        integers.write(0, new int[]{id});

        try {
            PROTOCOL_MANAGER.sendServerPacket(player, container);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
