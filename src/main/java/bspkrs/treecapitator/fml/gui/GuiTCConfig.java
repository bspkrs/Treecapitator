package bspkrs.treecapitator.fml.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.client.gui.GuiScreen;
import bspkrs.treecapitator.TreecapitatorMod;
import bspkrs.treecapitator.config.TCConfigHandler;
import bspkrs.treecapitator.util.Reference;
import bspkrs.util.config.ConfigProperty;
import bspkrs.util.config.gui.GuiConfig;
import bspkrs.util.config.gui.IConfigProperty;

public class GuiTCConfig extends GuiConfig
{
    public GuiTCConfig(GuiScreen parent) throws NoSuchMethodException, SecurityException
    {
        super(parent, getProps(), false, Reference.MODID, true, GuiConfig.getAbridgedConfigPath(TCConfigHandler.instance().getConfig().toString()));
    }
    
    private static List<IConfigProperty> getProps()
    {
        // Make sure the local objects contain our local settings
        TreecapitatorMod.instance.nbtManager().registerLocalInstances();
        
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
        
        return props;
    }
}
