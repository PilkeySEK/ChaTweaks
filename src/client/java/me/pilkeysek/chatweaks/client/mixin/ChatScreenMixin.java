package me.pilkeysek.chatweaks.client.mixin;

import me.pilkeysek.chatweaks.client.ChatweaksClient;
import me.pilkeysek.chatweaks.client.Util;
import me.pilkeysek.chatweaks.client.config.ChatweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.StringHelper;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @Inject(at = @At("HEAD"), method = "sendMessage(Ljava/lang/String;Z)V", cancellable = true)
    private void injected(String chatText, boolean addToHistory, CallbackInfo ci) {
        if(normalize(chatText).isEmpty()) return;
        ChatweaksConfig config = ChatweaksClient.config;
        StringBuilder newChatText = new StringBuilder(chatText);
        boolean prefixWasAdded = false;
        boolean suffixWasAdded = false;
        if(config.prefixNest.usePrefix()
                && !Util.startsWithAny(config.prefixNest.disablePrefixIfPrefix(), chatText)
                && !Util.endsWithAny(config.prefixNest.disablePrefixIfSuffix(), chatText)
        ) {
            newChatText.insert(0, config.prefixNest.prefix());
            prefixWasAdded = true;
            if(!ci.isCancelled()) ci.cancel();
        }
        if(config.suffixNest.useSuffix()
                && !Util.startsWithAny(config.suffixNest.disableSuffixIfPrefix(), chatText)
                && !Util.endsWithAny(config.suffixNest.disableSuffixIfSuffix(), chatText)
        ) {
            suffixWasAdded = true;
            newChatText.append(config.suffixNest.suffix());
            if(!ci.isCancelled()) ci.cancel();
        }
        if(!ci.isCancelled()) return;

        String normalizedNewChatText = normalize(newChatText.toString());

        if(normalizedNewChatText.isEmpty()) return;

        if(addToHistory) {
            String historyString = normalizedNewChatText;
            if(prefixWasAdded && !config.prefixNest.addPrefixToHistory()) {
                historyString = historyString.substring(config.prefixNest.prefix().length());
            }
            if(suffixWasAdded && !config.suffixNest.addSuffixToHistory()) {
                historyString = historyString.substring(historyString.length() - config.prefixNest.prefix().length() - 1, historyString.length() - 1);
            }
            MinecraftClient.getInstance().inGameHud.getChatHud().addToMessageHistory(historyString);
        }
        assert MinecraftClient.getInstance().player != null;
        if(normalizedNewChatText.startsWith("/")) MinecraftClient.getInstance().player.networkHandler.sendChatCommand(normalizedNewChatText.substring(1));
        else MinecraftClient.getInstance().player.networkHandler.sendChatMessage(normalizedNewChatText);
    }
        @Unique
        private String normalize(String chatText) {
         return StringHelper.truncateChat((String) StringUtils.normalizeSpace(chatText.trim()));
    }
}
