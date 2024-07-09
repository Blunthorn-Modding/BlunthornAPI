package net.wouterb.blunthornapi.api;

import net.wouterb.blunthornapi.core.data.ModRegistries;

public class Api {

    public static void registerModId(String mod_id) {
        ModRegistries.registerModId(mod_id);
    }
}
