package me.pilkeysek.chatweaks.client;

import io.wispforest.owo.config.ui.ConfigScreen;
import me.pilkeysek.chatweaks.client.command.ChatweaksCommand;
import me.pilkeysek.chatweaks.client.config.ChatweaksConfig;
import me.pilkeysek.chatweaks.client.util.MorseCodeUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class ChatweaksClient implements ClientModInitializer {
    public static ChatweaksConfig config = ChatweaksConfig.createAndLoad();

    public static KeyBinding togglePrefixKeyBinding;
    public static KeyBinding toggleSuffixKeyBinding;
    public static KeyBinding openConfigKeyBinding;
    private static KeyBinding toggleMorseCodeTranslationKeyBinding;

    @Override
    public void onInitializeClient() {
        registerKeybindings();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(togglePrefixKeyBinding.wasPressed()) {
                KeyBindingEvents.togglePrefix(client);
            }
            while(toggleSuffixKeyBinding.wasPressed()) {
                KeyBindingEvents.toggleSuffix(client);
            }
            while(openConfigKeyBinding.wasPressed()) {
                openConfig();
            }
            while(toggleMorseCodeTranslationKeyBinding.wasPressed()) {
                toggleMorseCodeTranslation();
                assert MinecraftClient.getInstance().player != null;
                if(ChatweaksClient.config.morseCodeNest.translationEnabled()) MinecraftClient.getInstance().player.sendMessage(Text.literal("Morse Code Translation is now ").formatted(Formatting.AQUA).append(Text.literal("enabled").formatted(Formatting.GREEN)));
                else MinecraftClient.getInstance().player.sendMessage(Text.literal("Morse Code Translation is now ").formatted(Formatting.AQUA).append(Text.literal("disabled").formatted(Formatting.RED)));
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            ChatweaksCommand.register(dispatcher);
        });
    }

    private void registerKeybindings() {
         togglePrefixKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.chatweaks.togglePrefix",
                 InputUtil.Type.KEYSYM,
                 GLFW.GLFW_KEY_X,
                 "category.chatweaks.main"
        ));
         toggleSuffixKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                 "key.chatweaks.toggleSuffix",
                 InputUtil.Type.KEYSYM,
                 GLFW.GLFW_KEY_Y,
                 "category.chatweaks.main"
         ));
        openConfigKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.chatweaks.openConfig",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.chatweaks.main"
        ));
        toggleMorseCodeTranslationKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.chatweaks.toggleMorseCodeTranslation",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                "category.chatweaks.main"
        ));
    }

    public static void toggleMorseCodeTranslation() {
        ChatweaksClient.config.morseCodeNest.translationEnabled(!ChatweaksClient.config.morseCodeNest.translationEnabled());
    }

    public static void openConfig() {
        ConfigScreen screen = ConfigScreen.create(config, MinecraftClient.getInstance().currentScreen);
        MinecraftClient.getInstance().send(() -> MinecraftClient.getInstance().setScreen(screen));
    }

    public static class KeyBindingEvents {
        public static void togglePrefix(MinecraftClient client) {
            config.prefixNest.usePrefix(!config.prefixNest.usePrefix()); // Toggle
            assert client.player != null;
            client.player.sendMessage(Text.literal("The prefix is now ")
                    .formatted(Formatting.AQUA)
                    .append(
                            config.prefixNest.usePrefix() ?
                                    Text.literal("enabled").formatted(Formatting.GREEN)
                                    : Text.literal("disabled").formatted(Formatting.RED)));
        }

        public static void toggleSuffix(MinecraftClient client) {
            config.suffixNest.useSuffix(!config.suffixNest.useSuffix()); // Toggle
            assert client.player != null;
            client.player.sendMessage(Text.literal("The suffix is now ")
                    .formatted(Formatting.AQUA)
                    .append(
                            config.suffixNest.useSuffix() ?
                                    Text.literal("enabled").formatted(Formatting.GREEN)
                                    : Text.literal("disabled").formatted(Formatting.RED)));
        }
    }
}
