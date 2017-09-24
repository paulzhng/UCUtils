package de.fuzzlemann.ucutils.utils;

import lombok.val;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

/**
 * @author Fuzzlemann
 */
public class SoundUtil {

    public static final SoundEvent REPORT_RECEIVED = register("report_received");
    public static final SoundEvent CONTRACT_PLACED = register("contract_placed");
    public static final SoundEvent CONTRACT_FULFILLED = register("contract_fulfilled");
    public static final SoundEvent BOMB_PLACED = register("bomb_placed");
    public static final SoundEvent SERVICE_RECEIVED = register("service_received");
    public static final SoundEvent PLAYER_INVITED = register("player_invited");
    public static final SoundEvent PLAYER_UNINVITED = register("player_uninvited");

    private static SoundEvent register(String name) {
        val loc = new ResourceLocation("ucutils", name);
        return new SoundEvent(loc);
    }
}
