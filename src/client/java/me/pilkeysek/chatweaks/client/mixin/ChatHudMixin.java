package me.pilkeysek.chatweaks.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.pilkeysek.chatweaks.client.ChatweaksClient;
import me.pilkeysek.chatweaks.client.util.MorseCodeUtil;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V")
    public void injected(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci, @Local(argsOnly = true) LocalRef<Text> messageRef, @Local(argsOnly = true) LocalRef<MessageIndicator> messageIndicatorRef) {
        if(message.getString().matches(ChatweaksClient.config.filteringNest.filterRegex()) && ChatweaksClient.config.filteringNest.useFilter()) {
            messageIndicatorRef.set(MessageIndicator.system());
            Text oldMessage = message;
            messageRef.set(Text.literal("Message Filtered. Hover to reveal")
                    .setStyle(Style.EMPTY
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, oldMessage))
                            .withColor(Formatting.RED)
                            .withItalic(true)));
            return;
        }
        if(ChatweaksClient.config.morseCodeNest.translationEnabled()) {
            String translatedString = MorseCodeUtil.translateStringFromMorse(messageRef.get().getString());
            if(!translatedString.trim().isEmpty()) {
                Formatting color = Formatting.AQUA;
                messageRef.set(messageRef.get().copy().append(Text.literal(" -> " + translatedString).formatted(color)));
            }
        }
    }
}
