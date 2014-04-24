package bspkrs.treecapitator.config;

import java.io.File;

import bspkrs.treecapitator.TreecapitatorMod;
import bspkrs.treecapitator.registry.ModConfigRegistry;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.CommonUtils;
import bspkrs.util.config.Configuration;

public class TCConfigHandler
{
    private static TCConfigHandler instance;
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
        config = new Configuration(file);
        TCLog.info("Loading configuration file %s", file.getAbsolutePath().replace(CommonUtils.getMinecraftDir(), "./"));
        syncConfig();
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
        config.load();
        
        TCSettings.instance().syncConfiguration(config);
        ModConfigRegistry.instance().syncConfiguration(config);
        
        if (this.shouldRefreshRegistries)
        {
            ModConfigRegistry.instance().applyPrioritizedModConfigs();
            TreecapitatorMod.instance.nbtManager().saveAllCurrentObjectsToLocalNBT();
        }
        
        config.save();
    }
}
