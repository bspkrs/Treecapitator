package bspkrs.treecapitator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import bspkrs.util.BlockID;
import bspkrs.util.Coord;

public class BlockTree extends BlockLog
{
    private TreeCapitator breaker;
    
    public BlockTree(int i)
    {
        super(i);
        setHardness(TCSettings.logHardnessNormal);
        setStepSound(Block.soundWoodFootstep);
        setUnlocalizedName("log");
        func_111022_d("log");
    }
    
    /**
     * Called when the block is attempted to be harvested
     */
    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int md, EntityPlayer entityPlayer)
    {
        Coord blockPos = new Coord(x, y, z);
        if (!world.isRemote && TreeRegistry.instance().trackTreeChopEventAt(blockPos))
        {
            TCLog.debug("BlockID " + blockID + " is a log.");
            
            if (TreeCapitator.isBreakingPossible(world, entityPlayer, true))
            {
                
                if (TCSettings.useStrictBlockPairing)
                    breaker = new TreeCapitator(entityPlayer, TreeRegistry.instance().get(new BlockID(Block.wood.blockID, md)));
                else
                    breaker = new TreeCapitator(entityPlayer, TreeRegistry.instance().masterDefinition());
                
                breaker.onBlockHarvested(world, x, y, z, md, entityPlayer);
            }
            TreeRegistry.instance().endTreeChopEventAt(blockPos);
        }
    }
    
    /**
     * Returns the block hardness at a location. Args: world, x, y, z
     */
    @Override
    public float getBlockHardness(World par1World, int par2, int par3, int par4)
    {
        return breaker != null ? breaker.getBlockHardness() : TCSettings.logHardnessNormal;
    }
}
