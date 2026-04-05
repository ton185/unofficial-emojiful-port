package com.hrznstudio.emojiful;

import com.hrznstudio.emojiful.ClientEmojiHandler;
import com.hrznstudio.emojiful.config.EmojifulConfig;
import com.hrznstudio.emojiful.gui.EmojifulChatScreen;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

@Mod(
    modid = Constants.MOD_ID,
    useMetadata = true,
    clientSideOnly = true,
    guiFactory = "com.hrznstudio.emojiful.config.EmojifulGuiFactory",
    acceptableRemoteVersions = "*"
)
public class EmojifulMod {

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configFile = new File(event.getModConfigurationDirectory(), "emojiful.cfg");
        EmojifulConfig.init(configFile);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        EmojifulConfig.initialized = true;
    }

    /**
     * Intercept GuiChat opening and replace it with our custom emoji-aware screen.
     */
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof EmojifulChatScreen) return;
        if (event.gui instanceof GuiChat) {
            // defaultInputFieldText is private; use reflection to preserve any pre-filled text
            // (e.g. from /command screen). Falls back to empty string safely.
            String initialText = "";
            try {
                java.lang.reflect.Field f = GuiChat.class.getDeclaredField("defaultInputFieldText");
                f.setAccessible(true);
                Object val = f.get(event.gui);
                if (val instanceof String) initialText = (String) val;
            } catch (Exception ignored) {}
            event.gui = new EmojifulChatScreen(initialText);
        }
    }
}
