package bspkrs.treecapitator.fml.gui;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.client.gui.GuiScreen;
import bspkrs.treecapitator.config.TCConfigHandler;
import bspkrs.util.config.ConfigProperty;
import bspkrs.util.config.Configuration;
import bspkrs.util.config.gui.GuiConfig;
import bspkrs.util.config.gui.IConfigProperty;

public class GuiTCConfig extends GuiConfig
{
    public GuiTCConfig(GuiScreen parent) throws NoSuchMethodException, SecurityException
    {
        super(parent, getProps(), Configuration.class.getDeclaredMethod("save"), TCConfigHandler.instance().getConfig(),
                TCConfigHandler.class.getDeclaredMethod("syncConfig"), TCConfigHandler.instance());
    }
    
    public GuiTCConfig(GuiScreen par1GuiScreen, IConfigProperty[] properties, Method saveAction, Object configObject, Method afterSaveAction, Object afterSaveObject)
    {
        super(par1GuiScreen, properties, saveAction, configObject, afterSaveAction, afterSaveObject);
    }
    
    private static IConfigProperty[] getProps()
    {
        Set<String> processed = new TreeSet<String>();
        List<IConfigProperty> props = new ArrayList<IConfigProperty>();
        int index = 0;
        for (String catName : TCConfigHandler.instance().getConfig().getCategoryNames())
        {
            boolean shouldAdd = true;
            for (String prevCat : processed)
                if (catName.startsWith(prevCat))
                {
                    shouldAdd = false;
                    break;
                }
            processed.add(catName);
            if (shouldAdd)
                props.add(new ConfigProperty(TCConfigHandler.instance().getConfig().getCategory(catName)));
        }
        
        return props.toArray(new IConfigProperty[] {});
    }
}
