package bspkrs.treecapitator.fml.gui;

import java.util.List;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiConfigCustomCategoryListEntry extends CategoryEntry
{
    @SuppressWarnings("rawtypes")
    public GuiConfigCustomCategoryListEntry(GuiConfig parentGuiConfig, GuiConfigEntries parentPropertyList, IConfigElement prop)
    {
        super(parentGuiConfig, parentPropertyList, prop);
        
        List<IConfigElement> props = this.configElement.getChildElements();
        // TODO: create a custom IGuiConfigListEntry class for adding a new mod config
        // TODO: create a custom IGuiConfigListEntry class that extends this class and provides a button for removing the mod config
        // TODO: create a custom IGuiConfigListEntry class that adds a new tree to a mod config
        
        childScreen = new GuiConfig(this.owningScreen, props, this.owningScreen.title, this.owningScreen.modID,
                this.configElement.requiresWorldRestart(), this.configElement.requiresMcRestart(),
                ((this.owningScreen.titleLine2 == null ? "" : this.owningScreen.titleLine2) + " > " + this.name));
    }
}