package bspkrs.treecapitator.fml.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import bspkrs.treecapitator.TreecapitatorMod;
import bspkrs.treecapitator.config.TCConfigHandler;
import bspkrs.treecapitator.util.Reference;

public class GuiTCConfig extends GuiConfig
{
    public GuiTCConfig(GuiScreen parent)
    {
        super(parent, getProps(), Reference.MODID, false, false, GuiConfig.getAbridgedConfigPath(TCConfigHandler.instance().getConfig().toString()));
    }

    private static List<IConfigElement> getProps()
    {
        // Make sure the local objects contain our local settings
        TreecapitatorMod.instance.nbtManager().registerLocalInstances();

        List<IConfigElement> props = new ArrayList<IConfigElement>();
        for (String catName : TCConfigHandler.instance().getConfig().getCategoryNames())
            if (!catName.contains(Configuration.CATEGORY_SPLITTER))
                props.add(new ConfigElement(TCConfigHandler.instance().getConfig().getCategory(catName)));

        return props;
    }

    //    /**
    //     * This custom list entry provides the Tree and Mod Configs entry on the Treecapitator config screen. It extends the base Category entry
    //     * class and defines the IConfigElement objects that will be used to build the child screen. In this case it adds the custom entry for
    //     * adding a new mod config and lists the existing mod configs.
    //     */
    //    public static class ModConfigsEntry extends CategoryEntry
    //    {
    //        public ModConfigsEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
    //        {
    //            super(owningScreen, owningEntryList, prop);
    //        }
    //
    //        /**
    //         * This method is called in the constructor and is used to set the childScreen field.
    //         */
    //        @Override
    //        protected GuiScreen buildChildScreen()
    //        {
    //            List<IConfigElement> list = new ArrayList<IConfigElement>();
    //
    //            list.add(new DummyCategoryElement(Reference.CTGY_TREE_MOD_CFG, Reference.LANG_KEY_BASE + Reference.CTGY_TREE_MOD_CFG,
    //                    AddModConfigEntry.class));
    //            for (ConfigCategory cc : ForgeChunkManager.getModCategories())
    //                list.add(new ConfigElement(cc));
    //
    //            return new GuiConfig(owningScreen, list, owningScreen.modID,
    //                    configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart,
    //                    configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, owningScreen.title,
    //                    I18n.format("forge.configgui.ctgy.forgeChunkLoadingModConfig"));
    //        }
    //
    //        /**
    //         * By overriding the enabled() method and checking the value of the "enabled" entry this entry is enabled/disabled based on the
    //         * value of the other entry.
    //         */
    //        @Override
    //        public boolean enabled()
    //        {
    //            for (IConfigEntry entry : owningEntryList.listEntries)
    //            {
    //                if (entry.getName().equals("enabled") && (entry instanceof BooleanEntry))
    //                {
    //                    return Boolean.valueOf(entry.getCurrentValue().toString());
    //                }
    //            }
    //
    //            return true;
    //        }
    //
    //        /**
    //         * Check to see if the child screen's entry list has changed.
    //         */
    //        @Override
    //        public boolean isChanged()
    //        {
    //            if (childScreen instanceof GuiConfig)
    //            {
    //                GuiConfig child = (GuiConfig) childScreen;
    //                return (child.entryList.listEntries.size() != child.initEntries.size()) || child.entryList.hasChangedEntry(true);
    //            }
    //            return false;
    //        }
    //
    //        /**
    //         * Since adding a new entry to the child screen is what constitutes a change here, reset the child screen listEntries to the saved
    //         * list.
    //         */
    //        @Override
    //        public void undoChanges()
    //        {
    //            if (childScreen instanceof GuiConfig)
    //            {
    //                GuiConfig child = (GuiConfig) childScreen;
    //                for (IConfigEntry ice : child.entryList.listEntries)
    //                    if (!child.initEntries.contains(ice) && ForgeChunkManager.getConfig().hasCategory(ice.getName()))
    //                        ForgeChunkManager.getConfig().removeCategory(ForgeChunkManager.getConfig().getCategory(ice.getName()));
    //
    //                child.entryList.listEntries = new ArrayList<IConfigEntry>(child.initEntries);
    //            }
    //        }
    //    }
    //
    //    /**
    //     * This custom list entry provides a button that will open to a screen that will allow a user to define a new mod override.
    //     */
    //    public static class AddModConfigEntry extends CategoryEntry
    //    {
    //        public AddModConfigEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
    //        {
    //            super(owningScreen, owningEntryList, prop);
    //        }
    //
    //        @Override
    //        protected GuiScreen buildChildScreen()
    //        {
    //            List<IConfigElement> list = new ArrayList<IConfigElement>();
    //
    //            list.add(new DummyConfigElement(Reference.MOD_ID, "", ConfigGuiType.STRING, Reference.LANG_KEY_BASE + Reference.MOD_ID)
    //            .setCustomListEntryClass(ModIDEntry.class));
    //            list.add(new ConfigElement(new Property(Reference.OVERRIDE_IMC, "true", Property.Type.BOOLEAN, Reference.LANG_KEY_BASE + Reference.OVERRIDE_IMC)));
    //            list.add(new ConfigElement(new Property("maximumChunksPerTicket", "25", Property.Type.INTEGER, "forge.configgui.maximumChunksPerTicket")));
    //
    //            return new GuiConfig(owningScreen, list, owningScreen.modID,
    //                    configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart,
    //                    configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, owningScreen.title,
    //                    I18n.format("forge.configgui.ctgy.forgeChunkLoadingAddModConfig"));
    //        }
    //
    //        @Override
    //        public boolean isChanged()
    //        {
    //            return false;
    //        }
    //    }
    //
    //    /**
    //     * This custom list entry provides a Mod ID selector. The control is a button that opens a list of values to select from. This entry
    //     * also overrides onGuiClosed() to run code to save the data to a new ConfigCategory when the user is done.
    //     */
    //    public static class ModIDEntry extends SelectValueEntry
    //    {
    //        public ModIDEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
    //        {
    //            super(owningScreen, owningEntryList, prop, getSelectableValues());
    //            if (selectableValues.size() == 0)
    //                btnValue.enabled = false;
    //        }
    //
    //        private static Map<Object, String> getSelectableValues()
    //        {
    //            Map<Object, String> selectableValues = new TreeMap<Object, String>();
    //
    //            for (ModContainer mod : Loader.instance().getActiveModList())
    //                // only add mods to the list that have a non-immutable ModContainer
    //                if (!mod.isImmutable() && (mod.getMod() != null))
    //                    selectableValues.put(mod.getModId(), mod.getName());
    //
    //            return selectableValues;
    //        }
    //
    //        /**
    //         * By overriding onGuiClosed() for this entry we can perform additional actions when the user is done such as saving a new
    //         * ConfigCategory object to the Configuration object.
    //         */
    //        @Override
    //        public void onGuiClosed()
    //        {
    //            Object modObject = Loader.instance().getModObjectList().get(Loader.instance().getIndexedModList().get(currentValue));
    //            if (modObject != null)
    //            {
    //                owningEntryList.saveConfigElements();
    //                for (IConfigElement ice : owningScreen.configElements)
    //                    if ("maximumTicketCount".equals(ice.getName()))
    //                        maxTickets = Integer.valueOf(ice.get().toString());
    //                    else if ("maximumChunksPerTicket".equals(ice.getName()))
    //                        maxChunks = Integer.valueOf(ice.get().toString());
    //
    //                // TODO: create a new ConfigCategory
    //
    //                if (owningScreen.parentScreen instanceof GuiConfig)
    //                {
    //                    GuiConfig superParent = (GuiConfig) owningScreen.parentScreen;
    //                    ConfigCategory modCtgy = ForgeChunkManager.getConfigFor(modObject);
    //                    modCtgy.setPropertyOrder(ForgeChunkManager.MOD_PROP_ORDER);
    //                    ConfigElement modConfig = new ConfigElement(modCtgy);
    //
    //                    boolean found = false;
    //                    for (IConfigElement ice : superParent.configElements)
    //                        if (ice.getName().equals(currentValue))
    //                            found = true;
    //
    //                    if (!found)
    //                        superParent.configElements.add(modConfig);
    //
    //                    superParent.needsRefresh = true;
    //                    superParent.initGui();
    //                }
    //            }
    //        }
    //    }
}
