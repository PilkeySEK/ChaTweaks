package me.pilkeysek.chatweaks.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import me.pilkeysek.chatweaks.client.ChatweaksClient;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class ChatweaksCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("chatweaks")
                .then(ClientCommandManager.literal("config")
                        .executes(source -> {
                            ChatweaksClient.openConfig();
                            return Command.SINGLE_SUCCESS;
                        }))
        );
    }
}
