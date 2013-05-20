package bspkrs.treecapitator.fml;

import java.io.File;

import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TCSettings;
import bspkrs.util.CommonUtils;
import bspkrs.util.Configuration;

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
    
    protected Configuration config;
    
    private TCConfigHandler(File file)
    {
        this();
        config = new Configuration(file);
        config.load();
        
        TCLog.info("Loading configuration file %s", file.getAbsolutePath().replace(CommonUtils.getMinecraftDir(), "./"));
        TCSettings.instance().readFromConfiguration(config);
        
        config.save();
    }
}
