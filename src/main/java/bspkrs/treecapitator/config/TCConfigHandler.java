package bspkrs.treecapitator.config;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import bspkrs.treecapitator.TreecapitatorMod;
import bspkrs.treecapitator.registry.ModConfigRegistry;
import bspkrs.treecapitator.util.Reference;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.config.ConfigChangedEvent.OnConfigChangedEvent;
import bspkrs.util.config.Configuration;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TCConfigHandler
{
    private static TCConfigHandler instance;
    private File                   fileRef;
    private Configuration          config;
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
        this.fileRef = file;
        TCLog.info("Loading configuration file %s", file.getAbsolutePath());
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
            
            fileRef.renameTo(fileBak);
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
        this.shouldRefreshRegistries = bol;
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
        
        if (this.shouldRefreshRegistries)
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
