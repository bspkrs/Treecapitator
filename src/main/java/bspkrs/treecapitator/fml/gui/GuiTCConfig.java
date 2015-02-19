package bspkrs.treecapitator.fml.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import bspkrs.treecapitator.TreecapitatorMod;
import bspkrs.treecapitator.config.TCConfigHandler;
import bspkrs.treecapitator.util.Reference;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class GuiTCConfig extends GuiConfig
{
    public GuiTCConfig(GuiScreen parent)
    {
        super(parent, getProps(), Reference.MODID, false, false, GuiConfig.getAbridgedConfigPath(TCConfigHandler.instance().getConfig().toString()));
    }

    @SuppressWarnings("rawtypes")
    private static List<IConfigElement> getProps()
    {
        // Make sure the local objects contain our local settings
        TreecapitatorMod.instance.nbtManager().registerLocalInstances();

        Set<String> processed = new TreeSet<String>();
        List<IConfigElement> props = new ArrayList<IConfigElement>();
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
                props.add(new ConfigElement(TCConfigHandler.instance().getConfig().getCategory(catName)));
        }

        return props;
    }
}
