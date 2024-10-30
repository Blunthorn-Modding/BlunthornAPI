package net.wouterb.blunthornapi.core.util;

import com.google.gson.*;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.wouterb.blunthornapi.BlunthornAPI;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class ItemLogger {

    public static KeyBinding logItemKeyBind;
    private static final Path ITEM_LOG_FILE_PATH = Path.of(String.valueOf(Path.of(String.valueOf(FabricLoader.getInstance().getConfigDir()))), "blunthornapi\\item_id_log.json");
    private static final File ITEM_LOG_FILE = ITEM_LOG_FILE_PATH.toFile();

    public static void register() {
        logItemKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.blunthornapi.logitemid",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F8,
                "category.blunthornapi.keybinds"
        ));
    }

    public static void logItemId(MinecraftClient client, String itemId) {
        if (client.player == null) return;
        if (!(client.currentScreen instanceof HandledScreen<?>)) return;

        sendClickableMessage(client.player, "Logging ID: ", itemId);
        appendItemIDToJsonFile(itemId);
    }

    private static void appendItemIDToJsonFile(String itemId) {
        JsonArray jsonArray = getStoredLogArray();
        jsonArray.add(itemId);

        if (!ITEM_LOG_FILE.exists()) {
            ITEM_LOG_FILE.getParentFile().mkdirs();
        }

        try (FileWriter writer = new FileWriter(ITEM_LOG_FILE)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(jsonArray, writer);
        } catch (IOException e) {
            BlunthornAPI.LOGGER.error(e.toString());
        }
    }

    private static JsonArray getStoredLogArray() {
        if (ITEM_LOG_FILE.exists()) {
            try (FileReader reader = new FileReader(ITEM_LOG_FILE)) {
                JsonElement jsonElement = JsonParser.parseReader(reader);
                if (jsonElement.isJsonArray()) {
                    return jsonElement.getAsJsonArray();
                }
            } catch (IOException e) {
                BlunthornAPI.LOGGER.error(e.toString());

            }
        }
        return new JsonArray();
    }

    public static void sendClickableMessage(ClientPlayerEntity player, String message, String copyText) {
        Text mainMessage = Text.literal(message).formatted(Formatting.WHITE);

        Text clickableText = Text.literal("[" + copyText + "]")
                .formatted(Formatting.GREEN)
                .styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copyText))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to copy"))));

        Text finalMessage = mainMessage.copy().append(clickableText);
        player.sendMessage(finalMessage);
    }
}
