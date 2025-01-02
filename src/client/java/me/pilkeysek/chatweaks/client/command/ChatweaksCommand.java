package me.pilkeysek.chatweaks.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import me.pilkeysek.chatweaks.client.ChatweaksClient;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChatweaksCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("chatweaks")
                .then(ClientCommandManager.literal("config")
                        .executes(source -> {
                            ChatweaksClient.openConfig();
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(ClientCommandManager.literal("morseCodeTranslation")
                        .then(ClientCommandManager.literal("enable").executes(source -> {
                            ChatweaksClient.config.morseCodeNest.translationEnabled(true);
                            source.getSource().sendFeedback(Text.translatable("commands.chatweaks.morseCodeTranslation.enable").formatted(Formatting.AQUA));
                            return Command.SINGLE_SUCCESS;
                        }))
                        .then(ClientCommandManager.literal("disable").executes(source -> {
                            ChatweaksClient.config.morseCodeNest.translationEnabled(false);
                            source.getSource().sendFeedback(Text.translatable("commands.chatweaks.morseCodeTranslation.disable").formatted(Formatting.AQUA));
                            return Command.SINGLE_SUCCESS;
                        }))
                        .then(ClientCommandManager.literal("toggle").executes(source -> {
                            ChatweaksClient.toggleMorseCodeTranslation();
                            if(ChatweaksClient.config.morseCodeNest.translationEnabled()) source.getSource().sendFeedback(Text.literal("Morse Code Translation is now ").formatted(Formatting.AQUA).append(Text.literal("enabled").formatted(Formatting.GREEN)));
                            else source.getSource().sendFeedback(Text.literal("Morse Code Translation is now ").formatted(Formatting.AQUA).append(Text.literal("disabled").formatted(Formatting.RED)));
                            return Command.SINGLE_SUCCESS;
                        })))
        );
    }
}
