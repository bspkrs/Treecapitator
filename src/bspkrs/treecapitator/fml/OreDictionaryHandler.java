package bspkrs.treecapitator.fml;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.logging.ILogAgent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.oredict.OreDictionary;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TCSettings;
import bspkrs.treecapitator.TreeDefinition;
import bspkrs.treecapitator.TreeRegistry;
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
                
                World world = new FakeWorld(null, null, null, new WorldSettings(new WorldInfo(new NBTTagCompound())), null, null);
                
                // Get leaves first so they can be added to all generic trees
                List<BlockID> leafList = new LinkedList<BlockID>();
                
                for (Block block : Block.blocksList)
                    if (block != null)
                    {
                        BlockID blockID = new BlockID(block);
                        if (!leafList.contains(blockID) && block.isLeaves(world, 0, 0, 0))
                            leafList.add(blockID);
                    }
                
                for (String oreName : TCSettings.oreDictionaryLeafStrings.split(","))
                {
                    if (!oreName.trim().isEmpty())
                    {
                        for (ItemStack itemStack : OreDictionary.getOres(oreName.trim()))
                        {
                            Item item = itemStack.getItem();
                            BlockID blockID = new BlockID(((ItemBlock) item).getBlockID());
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
                                BlockID blockID = new BlockID(((ItemBlock) item).getBlockID());
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
                
                TreeDefinition genericTree = new TreeDefinition();
                
                for (Block block : Block.blocksList)
                    if (block != null)
                    {
                        BlockID blockID = new BlockID(block);
                        if (!TreeRegistry.instance().isRegistered(blockID) && block.isWood(world, 0, 0, 0))
                            genericTree.addLogID(blockID);
                    }
                
                if (!genericTree.getLogList().isEmpty())
                {
                    for (BlockID blockID : leafList)
                        genericTree.addLeafID(blockID);
                    
                    for (BlockID blockID : TreeRegistry.instance().masterDefinition().getLeafList())
                        genericTree.addLeafID(blockID);
                    
                    TCLog.debug("Registering generic Block.isWood() tree...");
                    TreeRegistry.instance().registerTree("Block.isWood()", genericTree);
                }
                
                TCLog.info("Ore Dictionary processing complete.");
            }
            else
                TCLog.info("Skipping Ore Dictionary processing.");
        }
    }
    
    private class FakeWorld extends World
    {
        
        public FakeWorld(ISaveHandler par1iSaveHandler, String par2Str, WorldProvider par3WorldProvider, WorldSettings par4WorldSettings, Profiler par5Profiler, ILogAgent par6iLogAgent)
        {
            super(par1iSaveHandler, par2Str, par3WorldProvider, par4WorldSettings, par5Profiler, par6iLogAgent);
        }
        
        @Override
        protected IChunkProvider createChunkProvider()
        {
            return null;
        }
        
        @Override
        public Entity getEntityByID(int i)
        {
            return null;
        }
        
    }
}
