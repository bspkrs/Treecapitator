package bspkrs.treecapitator.fml.compat;

import java.lang.reflect.Method;

import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TCSettings;
import cpw.mods.fml.common.Loader;

public class MultiMineCompat
{
    private static boolean isInitialized = false;
    
    public MultiMineCompat(String exclusionList)
    {
        if (Loader.isModLoaded(TCSettings.multiMineModID) && !isInitialized)
        {
            try
            {
                Class<?> mm = Class.forName("atomicstryker.multimine.common.MultiMine");
                Method instance = mm.getMethod("instance");
                Method setExcludedBlocksString = mm.getMethod("setExcludedBlocksString", String.class);
                Object mmInstanceObject = instance.invoke(null);
                setExcludedBlocksString.invoke(mmInstanceObject, exclusionList);
                
                isInitialized = true;
                TCLog.info("MultiMine compatibility completed successfully.");
            }
            catch (Throwable e)
            {
                TCLog.severe("An error occurred while attempting to setup MultiMine compatibility.");
                e.printStackTrace();
            }
        }
    }
}
