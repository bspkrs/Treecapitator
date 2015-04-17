package bspkrs.treecapitator.fml.gui;

import java.util.List;

import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiConfigCustomCategoryListEntry extends CategoryEntry
{
    public GuiConfigCustomCategoryListEntry(GuiConfig parentGuiConfig, GuiConfigEntries parentPropertyList, IConfigElement prop)
    {
        super(parentGuiConfig, parentPropertyList, prop);

        List<IConfigElement> props = configElement.getChildElements();
        // TODO: create a custom IGuiConfigListEntry class for adding a new mod config
        // TODO: create a custom IGuiConfigListEntry class that extends this class and provides a button for removing the mod config
        // TODO: create a custom IGuiConfigListEntry class that adds a new tree to a mod config

        childScreen = new GuiConfig(owningScreen, props, owningScreen.title, owningScreen.modID,
                configElement.requiresWorldRestart(), configElement.requiresMcRestart(),
                ((owningScreen.titleLine2 == null ? "" : owningScreen.titleLine2 + " > ") + name));
    }
}