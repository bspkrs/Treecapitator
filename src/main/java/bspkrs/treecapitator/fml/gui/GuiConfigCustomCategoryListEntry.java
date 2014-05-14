package bspkrs.treecapitator.fml.gui;

import java.util.List;

import bspkrs.util.config.gui.GuiConfig;
import bspkrs.util.config.gui.GuiPropertyList;
import bspkrs.util.config.gui.GuiPropertyList.GuiConfigCategoryListEntry;
import bspkrs.util.config.gui.IConfigProperty;

public class GuiConfigCustomCategoryListEntry extends GuiConfigCategoryListEntry
{
    public GuiConfigCustomCategoryListEntry(GuiConfig parentGuiConfig, GuiPropertyList parentPropertyList, IConfigProperty prop)
    {
        super(parentGuiConfig, parentPropertyList, prop);
        
        List<IConfigProperty> props = this.prop.getConfigPropertiesList(false);
        // TODO: create a custom IGuiConfigListEntry class for adding a new mod config
        // TODO: create a custom IGuiConfigListEntry class that extends this class and provides a button for removing the mod config
        // TODO: create a custom IGuiConfigListEntry class that adds a new tree to a mod config
        
        subGuiConfig = new GuiConfig(this.parentGuiConfig, props, this.prop.isHotLoadable(), this.parentGuiConfig.modID,
                this.parentGuiConfig.allowNonHotLoadConfigChanges, this.parentGuiConfig.title, this.prop.getQualifiedName());
    }
}