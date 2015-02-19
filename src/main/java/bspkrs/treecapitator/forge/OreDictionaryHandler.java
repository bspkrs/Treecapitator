package bspkrs.treecapitator.forge;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.oredict.OreDictionary;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.registry.ModConfigRegistry;
import bspkrs.treecapitator.registry.TreeDefinition;
import bspkrs.treecapitator.registry.TreeRegistry;
import bspkrs.treecapitator.util.Reference;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.BlockID;

public class OreDictionaryHandler
{
    private static OreDictionaryHandler instance;

    public static OreDictionaryHandler instance()
    {
        if (instance == null)
            instance = new OreDictionaryHandler();

        return instance;
    }

    public boolean generateAndRegisterOreDictionaryTreeDefinitions()
    {
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
                        if (item instanceof ItemBlock)
                        {
                            BlockID blockID = new BlockID(GameData.getItemRegistry().getNameForObject(item).toString());
                            if (!leafList.contains(blockID))
                                leafList.add(blockID);
                        }
                    }
                }
            }

            boolean didRegisterATree = false;
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
                            BlockID blockID = new BlockID(GameData.getItemRegistry().getNameForObject(item).toString());
                            if (!TreeRegistry.instance().isRegistered(blockID) && !TreeRegistry.instance().blacklist().contains(blockID))
                                genericTree.addLogID(blockID);
                        }
                    }

                    if (!genericTree.getLogList().isEmpty())
                    {
                        for (BlockID blockID : leafList)
                            genericTree.addLeafID(blockID);

                        for (BlockID blockID : TreeRegistry.instance().masterDefinition().getLeafList())
                            genericTree.addLeafID(blockID);

                        TCLog.info("Registering generic Ore Dictionary tree %s...", oreName.trim());
                        TreeRegistry.instance().registerTree(oreName.trim(), genericTree);
                        ModConfigRegistry.instance().appendTreeToModConfig(Reference.MINECRAFT, oreName.trim(), genericTree);
                        didRegisterATree = true;
                    }
                }
            }

            TCLog.info("Ore Dictionary processing complete.");
            return didRegisterATree;
        }

        TCLog.info("Skipping Ore Dictionary processing.");
        return false;
    }
}
