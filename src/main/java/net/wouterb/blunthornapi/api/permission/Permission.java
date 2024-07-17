package net.wouterb.blunthornapi.api.permission;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.wouterb.blunthornapi.api.context.ActionContext;
import net.wouterb.blunthornapi.api.context.BlockActionContext;
import net.wouterb.blunthornapi.core.data.IEntityDataSaver;

import java.util.regex.Pattern;

public class Permission {
    /**
     * @param context ActionContext
     * @param modId ID of the mod of which the data needs to be checked.
     * @return whether the object is locked or not
     */
    public static boolean isObjectLocked(ActionContext context, String modId) {
        if (context.isClient()) return false;
        ServerPlayerEntity player = context.getServerPlayer();
        if (player.isCreative()) return false;

        NbtList nbtList = getListOfLockedObjects(player, context.getLockType(), modId);
        if (nbtList.contains(NbtString.of(context.getObjectId()))) return true;

        for (NbtElement entry : nbtList) {
            String nbtString = entry.asString();

            // Check if the object is part of a tag
            if (nbtString.startsWith("#")) {
                System.out.println(nbtString);
                String tag = nbtString.replace("#", "");
                System.out.println("Object in tag: " + context.isObjectInTag(tag));
                if (context instanceof BlockActionContext blockActionContext) {
                    if (blockActionContext.isObjectInTag(tag)) {
                        return true;
                    }
                }
            }

            // Check if object matches with any ID's containing the '*' wildcard
            if (nbtString.contains("*")) {
                if (matchesWildcardPattern(context.getObjectId(), nbtString))
                    return true;
            }
        }

        return false;
    }


    /**
     * @param objectId the ID to check.
     * @param wildcardId the ID containing the wildcard to check against.
     * @return whether the objectId matches the wildcardId or not.
     */
    public static boolean matchesWildcardPattern(String objectId, String wildcardId) {
        String regex = convertWildcardToRegex(wildcardId, '*');
        return Pattern.matches(regex, objectId);
    }


    /**
     * @param player the player of which the list needs to be retrieved.
     * @param lockType the LockType of which the list needs to be retrieved.
     * @param modId the mod of which the list needs to be retrieved.
     * @return the list corresponding with the given data
     */
    public static NbtList getListOfLockedObjects(PlayerEntity player, LockType lockType, String modId){
        NbtCompound nbt = ((IEntityDataSaver) player).blunthornapi$getPersistentData(modId);
        return nbt.getList(lockType.toString(), NbtElement.STRING_TYPE);
    }

    /**
     * @param wildcardString the string that contains the wildcard.
     * @param wildcard the wildcard to check for.
     * @return regex string.
     */
    public static String convertWildcardToRegex(String wildcardString, Character wildcard) {
        StringBuilder regex = new StringBuilder();
        for (char c : wildcardString.toCharArray()) {
            if (c == wildcard) {
                regex.append(".").append(wildcard);
            } else {
                regex.append(Pattern.quote(String.valueOf(c)));
            }
        }
        return regex.toString();
    }
}
