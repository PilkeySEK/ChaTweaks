package me.pilkeysek.chatweaks.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.pilkeysek.chatweaks.client.ChatweaksClient;
import me.pilkeysek.chatweaks.client.util.MorseCodeUtil;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;

public class ChatweaksCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("chatweaks")
                .then(ClientCommandManager.literal("config")
                        .executes(context -> {
                            ChatweaksClient.openConfig();
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(ClientCommandManager.literal("morseCodeTranslation")
                        .then(ClientCommandManager.literal("enable").executes(context -> {
                            ChatweaksClient.config.morseCodeNest.translationEnabled(true);
                            context.getSource().sendFeedback(Text.translatable("commands.chatweaks.morseCodeTranslation.enable").formatted(Formatting.AQUA));
                            return Command.SINGLE_SUCCESS;
                        }))
                        .then(ClientCommandManager.literal("disable").executes(context -> {
                            ChatweaksClient.config.morseCodeNest.translationEnabled(false);
                            context.getSource().sendFeedback(Text.translatable("commands.chatweaks.morseCodeTranslation.disable").formatted(Formatting.AQUA));
                            return Command.SINGLE_SUCCESS;
                        }))
                        .then(ClientCommandManager.literal("toggle").executes(context -> {
                            ChatweaksClient.toggleMorseCodeTranslation();
                            if(ChatweaksClient.config.morseCodeNest.translationEnabled()) context.getSource().sendFeedback(Text.literal("Morse Code Translation is now ").formatted(Formatting.AQUA).append(Text.literal("enabled").formatted(Formatting.GREEN)));
                            else context.getSource().sendFeedback(Text.literal("Morse Code Translation is now ").formatted(Formatting.AQUA).append(Text.literal("disabled").formatted(Formatting.RED)));
                            return Command.SINGLE_SUCCESS;
                        })))
                .then(ClientCommandManager.literal("morse").then(ClientCommandManager.argument("english", StringArgumentType.greedyString()).executes(context -> {
                    String english = context.getArgument("english", String.class);
                    String morse = MorseCodeUtil.englishToMorse(english);
                    if(morse.isEmpty()) return 0;
                    if(morse.length() > 256) {
                        context.getSource().sendFeedback(Text.literal("Cannot send translated message: Resulting morse string is too long.")
                                .formatted(Formatting.RED)
                                .append(Text.literal(" [Copy instead]")
                                        .formatted(Formatting.BLUE)
                                        .setStyle(Style.EMPTY
                                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click here to copy").formatted(Formatting.DARK_GREEN)))
                                                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, morse)))));
                        return 0;
                    }
                    Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatMessage(morse);
                    return Command.SINGLE_SUCCESS;
                })))
        );
    }
}
