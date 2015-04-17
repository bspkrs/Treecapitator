package bspkrs.treecapitator.config;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import bspkrs.treecapitator.TreecapitatorMod;
import bspkrs.treecapitator.registry.ModConfigRegistry;
import bspkrs.treecapitator.util.Reference;
import bspkrs.treecapitator.util.TCLog;

public class TCConfigHandler
{
    private static TCConfigHandler instance;
    private File                   fileRef;
    private Configuration          config;
    private Configuration          oldConfig;
    private boolean                shouldRefreshRegistries = false;

    public static TCConfigHandler instance()
    {
        if (instance == null)
            new TCConfigHandler();

        return instance;
    }

    public static TCConfigHandler setInstance(File file)
    {
        new TCConfigHandler(file);
        return instance;
    }

    private TCConfigHandler()
    {
        instance = this;
    }

    private TCConfigHandler(File file)
    {
        this();
        fileRef = file;
        try
        {
            config = new Configuration(fileRef, Reference.CONFIG_VERSION);
        }
        catch (Throwable e)
        {
            File fileBak = new File(fileRef.getAbsolutePath() + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".errored");
            TCLog.severe("An exception occurred while loading your config file. This file will be renamed to %s and a new config file will be generated.", fileBak.getName());
            e.printStackTrace();

            fileRef.renameTo(fileBak);
            config = new Configuration(fileRef, Reference.CONFIG_VERSION);
        }

        if (!Reference.CONFIG_VERSION.equals(config.getLoadedConfigVersion()))
        {
            File fileBak = new File(fileRef.getAbsolutePath() + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".old");
            TCLog.warning("Your Treecapitator config file is out of date and could cause issues. The existing file will be renamed to %s and a new one will be generated.", fileBak.getName());
            TCLog.warning("Treecapitator will attempt to copy your old settings, but custom mod/tree settings will have to be migrated manually.");

            fileRef.renameTo(fileBak);
            oldConfig = config;
            config = new Configuration(fileRef, Reference.CONFIG_VERSION);
        }

        syncConfig(true);
    }

    public Configuration getConfig()
    {
        return config;
    }

    public void setShouldRefreshRegistries(boolean bol)
    {
        shouldRefreshRegistries = bol;
    }

    public void syncConfig()
    {
        syncConfig(false);
    }

    public void syncConfig(boolean init)
    {
        if (!init)
            try
            {
                config.load();
            }
            catch (Throwable e)
            {
                File fileBak = new File(fileRef.getAbsolutePath() + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".errored");
                TCLog.severe("An exception occurred while loading your config file. This file will be renamed to %s and a new config file will be generated.", fileBak.getName());
                e.printStackTrace();

                fileRef.renameTo(fileBak);

                config = new Configuration(fileRef, Reference.CONFIG_VERSION);
            }

        TCSettings.instance().syncConfiguration(config);
        ModConfigRegistry.instance().syncConfig(config);

        if (oldConfig != null)
        {
            config.copyCategoryProps(oldConfig, new String[] { Reference.CTGY_BREAK_SPEED, Reference.CTGY_ENCHANTMENT_MODE, Reference.CTGY_ITEM, Reference.CTGY_MISC,
                    Reference.CTGY_SETTINGS, Reference.CTGY_TREE_CHOP_BEHAVIOR, Reference.CTGY_TREE_MOD_CFG });
            TCSettings.instance().syncConfiguration(config);
            ModConfigRegistry.instance().syncConfig(config);
            oldConfig = null;
        }

        if (shouldRefreshRegistries)
        {
            ModConfigRegistry.instance().applyPrioritizedModConfigs();
            TreecapitatorMod.instance.nbtManager().saveAllCurrentObjectsToLocalNBT();
        }

        config.save();
    }

    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event)
    {
        if (event.modID.equals(Reference.MODID))
        {
            config.save();
            syncConfig();
        }
    }
}
