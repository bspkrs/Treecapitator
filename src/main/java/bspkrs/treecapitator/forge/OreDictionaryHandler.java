package bspkrs.treecapitator.forge;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import bspkrs.helpers.item.ItemHelper;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.registry.TreeDefinition;
import bspkrs.treecapitator.registry.TreeRegistry;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.BlockID;

public class OreDictionaryHandler
{
    private static OreDictionaryHandler instance;
    private static boolean              hasRun = false;
    
    public static OreDictionaryHandler instance()
    {
        if (instance == null)
            instance = new OreDictionaryHandler();
        
        return instance;
    }
    
    public void generateAndRegisterOreDictionaryTreeDefinitions()
    {
        if (!hasRun)
        {
            hasRun = true;
            if (TCSettings.allowOreDictionaryLookup)
            {
                TCLog.info("Scanning Ore Dictionary for unregistered tree blocks...");
                
                // Get leaves first so they can be added to all generic trees
                List<BlockID> leafList = new LinkedList<BlockID>();
                
                for (String oreName : TCSettings.oreDictionaryLeafStrings.split(","))
                {
                    if (!oreName.trim().isEmpty())
                    {
                        for (ItemStack itemStack : OreDictionary.getOres(oreName.trim()))
                        {
                            Item item = itemStack.getItem();
                            BlockID blockID = new BlockID(ItemHelper.getUniqueID(item));
                            if (item instanceof ItemBlock && !leafList.contains(blockID))
                                leafList.add(blockID);
                        }
                    }
                }
                
                // register a tree definition for each ore type searched on
                for (String oreName : TCSettings.oreDictionaryLogStrings.split(","))
                {
                    if (!oreName.trim().isEmpty())
                    {
                        TreeDefinition genericTree = new TreeDefinition();
                        
                        for (ItemStack itemStack : OreDictionary.getOres(oreName.trim()))
                        {
                            Item item = itemStack.getItem();
                            if (item instanceof ItemBlock)
                            {
                                BlockID blockID = new BlockID(ItemHelper.getUniqueID(item));
                                if (!TreeRegistry.instance().isRegistered(blockID))
                                    genericTree.addLogID(blockID);
                            }
                        }
                        
                        if (!genericTree.getLogList().isEmpty())
                        {
                            for (BlockID blockID : leafList)
                                genericTree.addLeafID(blockID);
                            
                            for (BlockID blockID : TreeRegistry.instance().masterDefinition().getLeafList())
                                genericTree.addLeafID(blockID);
                            
                            TCLog.debug("Registering generic Ore Dictionary tree %s...", oreName.trim());
                            TreeRegistry.instance().registerTree(oreName.trim(), genericTree);
                        }
                    }
                }
                
                TCLog.info("Ore Dictionary processing complete.");
            }
            else
                TCLog.info("Skipping Ore Dictionary processing.");
        }
    }
}
