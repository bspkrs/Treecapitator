package bspkrs.treecapitator.fml.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

/*
 * Don't let any access transformer stuff accidentally modify our classes. A list of package prefixes for FML to ignore
 */
@TransformerExclusions({ "bspkrs.treecapitator.fml.asm", "bspkrs.util", "bspkrs.treecapitator", "bspkrs.treecapitator.fml", "bspkrs.fml.util" })
public class TreeCapitatorCorePlugin implements IFMLLoadingPlugin
{
    @Override
    public String[] getLibraryRequestClass()
    {
        return null;
    }
    
    @Override
    public String[] getASMTransformerClass()
    {
        return new String[] { "bspkrs.treecapitator.fml.asm.ItemInWorldManagerTransformer" };
    }
    
    @Override
    public String getModContainerClass()
    {
        return null;
    }
    
    @Override
    public String getSetupClass()
    {
        return null;
    }
    
    @Override
    public void injectData(Map<String, Object> data)
    {   
        
    }
    
}
