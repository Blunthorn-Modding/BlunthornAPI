package net.wouterb.blunthornapi.core.data;

import java.util.ArrayList;
import java.util.List;

public class ModRegistries {
    private static List<String> registeredModIds = new ArrayList<>();

    public static void registerModId(String mod_id) {
        registeredModIds.add(mod_id);
    }

    public static List<String> getRegisteredModIds() {
        return registeredModIds;
    }
}
