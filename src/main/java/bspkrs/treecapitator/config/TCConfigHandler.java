package bspkrs.treecapitator.config;

import java.io.File;

import bspkrs.treecapitator.registry.ModConfigRegistry;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.CommonUtils;
import bspkrs.util.config.Configuration;

public class TCConfigHandler
{
    private static TCConfigHandler instance;
    public Configuration           config;
    
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
        config.load();
        
        TCLog.info("Loading configuration file %s", file.getAbsolutePath().replace(CommonUtils.getMinecraftDir(), "./"));
        TCSettings.instance().syncConfiguration(config);
        ModConfigRegistry.instance().syncConfiguration(config);
        
        config.save();
    }
}
