package com.hrznstudio.emojiful.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EmojifulGuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraft) {}

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return EmojifulConfigGui.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

    /** Config screen shown when the user clicks "Config" in the Forge mod list. */
    public static class EmojifulConfigGui extends GuiConfig {

        public EmojifulConfigGui(GuiScreen parent) {
            super(parent,
                    getConfigElements(),
                    "emojiful",
                    false,
                    false,
                    "Emojiful Configuration");
        }

        @Override
        public void onGuiClosed() {
            super.onGuiClosed();
            EmojifulConfig.reload();
        }

        private static List<IConfigElement> getConfigElements() {
            List<IConfigElement> list = new ArrayList<>();
            for (String category : EmojifulConfig.getConfig().getCategoryNames()) {
                // ConfigElement<List> is the correct raw type for Forge 1.8.9 category entries
                list.add(new ConfigElement(EmojifulConfig.getConfig().getCategory(category)));
            }
            return list;
        }
    }
}
