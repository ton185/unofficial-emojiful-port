package com.hrznstudio.emojiful.mixin;

import com.hrznstudio.emojiful.ClientEmojiHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fires emoji loading after Minecraft finishes constructing.
 * In 1.8.9, Minecraft's constructor is <init>, same as in 1.21.
 */
@Mixin(Minecraft.class)
public abstract class MinecraftEmojifulMixin {

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void emojiful_initEmojis(CallbackInfo ci) {
        ClientEmojiHandler.setup();
    }
}
