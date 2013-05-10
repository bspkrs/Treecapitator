package bspkrs.treecapitator;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.src.ModLoader;

public enum TCLog
{
    INSTANCE;
    
    private Logger logger;
    
    public Logger getLogger()
    {
        if (logger == null)
            init();
        
        return logger;
    }
    
    private void init()
    {
        if (logger != null)
            return;
        
        logger = Logger.getLogger("TreeCapitator");
        logger.setParent(ModLoader.getLogger());
    }
    
    public static void info(String format, Object... args)
    {
        INSTANCE.log(Level.INFO, format, args);
    }
    
    public static void log(Level level, Throwable exception, String format, Object... args)
    {
        INSTANCE.getLogger().log(level, String.format(format, args), exception);
    }
    
    public static void severe(String format, Object... args)
    {
        INSTANCE.log(Level.SEVERE, format, args);
    }
    
    public static void warning(String format, Object... args)
    {
        INSTANCE.log(Level.WARNING, format, args);
    }
    
    private void log(Level level, String format, Object... data)
    {
        getLogger().log(level, String.format(format, data));
    }
    
    public static void debug(String format, Object... args)
    {
        if (TCSettings.allowDebugLogging)
            TCLog.info("[DEBUG] " + format, args);
    }
}
