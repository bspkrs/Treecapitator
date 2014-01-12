package bspkrs.treecapitator.fml;

import java.io.File;

import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TCSettings;
import bspkrs.util.BSConfiguration;
import bspkrs.util.CommonUtils;

public class TCConfigHandler
{
    private static TCConfigHandler instance;
    
    public static TCConfigHandler instance()
    {
        if (instance == null)
            new TCConfigHandler();
        
        return instance;
    }
    
    protected static TCConfigHandler setInstance(File file)
    {
        new TCConfigHandler(file);
        return instance;
    }
    
    private TCConfigHandler()
    {
        instance = this;
    }
    
    protected BSConfiguration config;
    
    private TCConfigHandler(File file)
    {
        this();
        config = new BSConfiguration(file);
        config.load();
        
        TCLog.info("Loading configuration file %s", file.getAbsolutePath().replace(CommonUtils.getMinecraftDir(), "./"));
        TCSettings.instance().syncConfiguration(config);
        ModConfigRegistry.instance().syncConfiguration(config);
        
        config.save();
    }
}
