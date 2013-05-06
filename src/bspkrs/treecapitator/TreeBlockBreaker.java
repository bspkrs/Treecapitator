package bspkrs.treecapitator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import bspkrs.util.BlockID;
import bspkrs.util.CommonUtils;
import bspkrs.util.Coord;

public class TreeBlockBreaker
{
    public EntityPlayer          player;
    public boolean               shouldFell;
    private Coord                startPos;
    private ItemStack            axe;
    private ItemStack            shears;
    private final TreeDefinition treeDef;
    private final List<BlockID>  masterLogList;
    private final List<BlockID>  logList;
    private final List<BlockID>  leafList;
    private final BlockID        vineID;
    private float                currentAxeDamage, currentShearsDamage = 0.0F;
    private int                  numLogsBroken;
    private int                  numLeavesSheared;
    private float                logDamageMultiplier;
    private float                leafDamageMultiplier;
    
    public TreeBlockBreaker(EntityPlayer entityPlayer, TreeDefinition treeDef)
    {
        player = entityPlayer;
        shouldFell = false;
        this.treeDef = treeDef;
        masterLogList = TreeRegistry.instance().masterLogList();
        logList = treeDef.getLogList();
        leafList = treeDef.getLeafList();
        vineID = new BlockID(Block.vine.blockID);
        logDamageMultiplier = TreeCapitator.damageMultiplier;
        leafDamageMultiplier = TreeCapitator.damageMultiplier;
        numLogsBroken = 0;
        numLeavesSheared = 0;
        
    }
    
    @Deprecated
    public TreeBlockBreaker(EntityPlayer entityPlayer, ArrayList<BlockID> logIDList, ArrayList<BlockID> leafIDList)
    {
        this(entityPlayer, new TreeDefinition(logIDList, leafIDList));
    }
    
    public static boolean isBreakingPossible(World world, EntityPlayer entityPlayer)
    {
        if (!isBreakingEnabled(entityPlayer))
        {
            TCLog.debug("Chopping disabled due to player state or gamemode.");
            return false;
        }
        
        ItemStack axe = entityPlayer.getCurrentEquippedItem();
        if (!world.isRemote)
            if ((isAxeItemEquipped(entityPlayer) || !TreeCapitator.needItem))
            {
                if (!entityPlayer.capabilities.isCreativeMode && TreeCapitator.allowItemDamage && axe != null
                        && (axe.isItemStackDamageable() && (axe.getMaxDamage() - axe.getItemDamage() <= TreeCapitator.damageMultiplier))
                        && !TreeCapitator.allowMoreBlocksThanDamage)
                {
                    TCLog.debug("Chopping disabled due to axe durability.");
                    return false;
                }
                
                return true;
            }
            else
                TCLog.debug("Player does not have an axe equipped.");
        return false;
    }
    
    public static boolean isBreakingEnabled(EntityPlayer player)
    {
        return (TreeCapitator.sneakAction.equalsIgnoreCase("none")
                || (TreeCapitator.sneakAction.equalsIgnoreCase("disable") && !player.isSneaking())
                || (TreeCapitator.sneakAction.equalsIgnoreCase("enable") && player.isSneaking()))
                && !(player.capabilities.isCreativeMode && TreeCapitator.disableInCreative);
    }
    
    public void onBlockHarvested(World world, int x, int y, int z, int md, EntityPlayer entityPlayer)
    {
        TCLog.debug("In TreeBlockBreaker.onBlockHarvested() " + x + ", " + y + ", " + z);
        player = entityPlayer;
        startPos = new Coord(x, y, z);
        
        if (!world.isRemote)
        {
            if (isBreakingEnabled(entityPlayer))
            {
                Coord topLog = getTopLog(world, new Coord(x, y, z));
                if (!TreeCapitator.allowSmartTreeDetection || leafList.size() == 0 || hasXLeavesInDist(world, topLog, treeDef.maxLeafIDDist(), treeDef.minLeavesToID()))
                {
                    if (isAxeItemEquipped() || !TreeCapitator.needItem)
                    {
                        TCLog.debug("Proceeding to chop tree...");
                        List<Coord> listFinal = new ArrayList<Coord>();
                        TCLog.debug("Finding log blocks...");
                        List<Coord> logs = addLogs(world, new Coord(x, y, z));
                        addLogsAbove(world, new Coord(x, y, z), listFinal);
                        
                        TCLog.debug("Destroying log blocks...");
                        destroyBlocks(world, logs);
                        if (TreeCapitator.destroyLeaves && leafList.size() != 0)
                        {
                            TCLog.debug("Finding leaf blocks...");
                            for (Coord pos : listFinal)
                            {
                                List<Coord> leaves = addLeaves(world, pos);
                                //removeLeavesWithLogsAround(world, leaves);
                                destroyBlocksWithChance(world, leaves, 0.5F, hasShearsInHotbar(player));
                            }
                        }
                        
                        /*
                         * Apply remaining damage if it rounds to a non-zero value
                         */
                        if (currentAxeDamage > 0.0F && axe != null)
                        {
                            currentAxeDamage = Math.round(currentAxeDamage);
                            
                            for (int i = 0; i < MathHelper.floor_double(currentAxeDamage); i++)
                                axe.getItem().onBlockDestroyed(axe, world, 17, x, y, z, player);
                        }
                        
                        if (currentShearsDamage > 0.0F && shears != null)
                        {
                            currentShearsDamage = Math.round(currentShearsDamage);
                            
                            for (int i = 0; i < Math.floor(currentShearsDamage); i++)
                                if (TreeCapitator.isForge && shears.itemID == Item.shears.itemID)
                                    shears.damageItem(1, player);
                                else
                                    shears.getItem().onBlockDestroyed(shears, world, 18, x, y, z, player);
                        }
                    }
                    else
                        TCLog.debug("Axe item is not equipped.");
                }
                else
                    TCLog.debug("Could not identify tree.");
            }
            else
                TCLog.debug("Tree Chopping is disabled due to player state or gamemode.");
        }
        else
            TCLog.debug("World is remote, exiting TreeBlockBreaker.");
    }
    
    /**
     * Returns the block hardness based on whether the player is holding an axe-type item or not
     */
    public float getBlockHardness()
    {
        return this.isAxeItemEquipped() ? TreeCapitator.logHardnessModified : TreeCapitator.logHardnessNormal;
    }
    
    /**
     * Returns the block hardness based on whether the player is holding an axe-type item or not
     */
    public static float getBlockHardness(EntityPlayer entityPlayer)
    {
        return isAxeItemEquipped(entityPlayer) ? TreeCapitator.logHardnessModified : TreeCapitator.logHardnessNormal;
    }
    
    private Coord getTopLog(World world, Coord pos)
    {
        while (logList.contains(new BlockID(world, pos.x, pos.y + 1, pos.z)))
            pos.y++;
        
        TCLog.debug("Top Log: " + pos.x + ", " + pos.y + ", " + pos.z);
        return pos;
    }
    
    private boolean hasXLeavesInDist(World world, Coord pos, int range, int limit)
    {
        TCLog.debug("Attempting to identify tree...");
        int i = 0;
        for (int x = -range; x <= range; x++)
            /* lower bound kept at -1 */
            for (int y = -1; y <= range; y++)
                for (int z = -range; z <= range; z++)
                    if (x != 0 || y != 0 || z != 0)
                    {
                        BlockID blockID = new BlockID(world, pos.x + x, pos.y + y, pos.z + z);
                        if (isLeafBlock(blockID))
                        {
                            TCLog.debug("Found leaf block: %s", blockID);
                            i++;
                            if (i >= limit)
                                return true;
                        }
                        else
                            TCLog.debug("Not a leaf block: %s", blockID);
                    }
        
        TCLog.debug("Number of leaf blocks is less than the limit. Found: %s", i);
        return false;
    }
    
    public int leavesInDist(World world, Coord pos, int range)
    {
        int i = 0;
        for (int x = -range; x <= range; x++)
            for (int y = -1; y <= range; y++)
                for (int z = -range; z <= range; z++)
                    if (x != 0 || y != 0 || z != 0)
                    {
                        BlockID blockID = new BlockID(world, pos.x + x, pos.y + y, pos.z + z);
                        if (isLeafBlock(blockID))
                        {
                            TCLog.debug("Found leaf block: %s", blockID);
                            i++;
                        }
                        else
                            TCLog.debug("Not a leaf block: %s", blockID);
                    }
        
        TCLog.debug("Leaves within " + range + " blocks of " + pos.x + ", " + pos.y + ", " + pos.z + " : " + i);
        return i;
    }
    
    public int leavesAround(World world, Coord pos)
    {
        return leavesInDist(world, pos, 1);
    }
    
    /**
     * Defines whether or not a player can break the block with current tool and sets the local axe object if possible
     */
    private boolean isAxeItemEquipped()
    {
        ItemStack item = player.getCurrentEquippedItem();
        if (TreeCapitator.isAxeItem(item))
        {
            axe = item;
            return true;
        }
        else
        {
            axe = null;
            return false;
        }
    }
    
    /**
     * Defines whether or not a player can break the block with current tool
     */
    public static boolean isAxeItemEquipped(EntityPlayer entityPlayer)
    {
        ItemStack item = entityPlayer.getCurrentEquippedItem();
        if (TreeCapitator.isAxeItem(item))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Defines whether or not a player has a shear-type item in the hotbar
     */
    private boolean hasShearsInHotbar(EntityPlayer entityplayer)
    {
        return shearsHotbarIndex(entityplayer) != -1;
    }
    
    /**
     * Returns the index of the left-most shear-type item in the hotbar and sets the local shears object if possible
     */
    private int shearsHotbarIndex(EntityPlayer entityPlayer)
    {
        for (int i = 0; i < 9; i++)
        {
            ItemStack item = entityPlayer.inventory.mainInventory[i];
            
            if (item != null && item.stackSize > 0 && CommonUtils.isItemInList(item.itemID, item.getItemDamage(), TreeCapitator.shearIDList))
            {
                shears = item;
                return i;
            }
        }
        shears = null;
        return -1;
    }
    
    public boolean isLeafBlock(BlockID blockID)
    {
        return leafList.contains(blockID) || leafList.contains(new BlockID(blockID.id, blockID.metadata & 7));
    }
    
    private void destroyBlocks(World world, List<Coord> list)
    {
        destroyBlocksWithChance(world, list, 1.0F, false);
    }
    
    private void destroyBlocksWithChance(World world, List<Coord> list, float f)
    {
        destroyBlocksWithChance(world, list, f, false);
    }
    
    private void destroyBlocksWithChance(World world, List<Coord> list, float f, boolean canShear)
    {
        TCLog.debug("Breaking identified blocks...");
        while (list.size() > 0)
        {
            Coord pos = list.remove(0);
            int id = world.getBlockId(pos.x, pos.y, pos.z);
            if (id != 0)
            {
                Block block = Block.blocksList[id];
                int metadata = world.getBlockMetadata(pos.x, pos.y, pos.z);
                
                if ((((vineID.equals(new BlockID(block, metadata)) && TreeCapitator.shearVines)
                        || (isLeafBlock(new BlockID(block, metadata)) && TreeCapitator.shearLeaves)) && canShear)
                        && !(player.capabilities.isCreativeMode && TreeCapitator.disableCreativeDrops))
                {
                    world.spawnEntityInWorld(new EntityItem(world, pos.x, pos.y, pos.z, new ItemStack(id, 1, block.damageDropped(metadata))));
                    
                    if (TreeCapitator.allowItemDamage && !player.capabilities.isCreativeMode && shears != null && shears.stackSize > 0)
                    {
                        canShear = damageShearsAndContinue(world, id, pos.x, pos.y, pos.z);
                        numLeavesSheared++;
                        
                        if (canShear && TreeCapitator.useIncreasingItemDamage && numLeavesSheared % TreeCapitator.increaseDamageEveryXBlocks == 0)
                            leafDamageMultiplier += TreeCapitator.damageIncreaseAmount;
                    }
                }
                else if (!(player.capabilities.isCreativeMode && TreeCapitator.disableCreativeDrops))
                {
                    block.dropBlockAsItem(world, pos.x, pos.y, pos.z, metadata, 0);
                    
                    if (TreeCapitator.allowItemDamage && !player.capabilities.isCreativeMode && axe != null && axe.stackSize > 0
                            && !vineID.equals(new BlockID(block, metadata)) && !isLeafBlock(new BlockID(block, metadata)) && !pos.equals(startPos))
                    {
                        if (!damageAxeAndContinue(world, id, pos.x, pos.y, pos.z))
                            list.clear();
                        
                        numLogsBroken++;
                        
                        if (TreeCapitator.useIncreasingItemDamage && numLogsBroken % TreeCapitator.increaseDamageEveryXBlocks == 0)
                            logDamageMultiplier += TreeCapitator.damageIncreaseAmount;
                    }
                }
                if (world.blockHasTileEntity(pos.x, pos.y, pos.z))
                    world.removeBlockTileEntity(pos.x, pos.y, pos.z);
                
                world.setBlock(pos.x, pos.y, pos.z, 0, 0, 3);
            }
        }
    }
    
    /**
     * Damages the axe-type item and returns true if we should continue destroying logs
     */
    private boolean damageAxeAndContinue(World world, int id, int x, int y, int z)
    {
        if (axe != null)
        {
            currentAxeDamage += logDamageMultiplier;
            
            for (int i = 0; i < (int) Math.floor(currentAxeDamage); i++)
                axe.getItem().onBlockDestroyed(axe, world, id, x, y, z, player);
            
            currentAxeDamage -= Math.floor(currentAxeDamage);
            
            if (axe != null && axe.stackSize < 1)
                player.destroyCurrentEquippedItem();
        }
        return !TreeCapitator.needItem || TreeCapitator.allowMoreBlocksThanDamage || isAxeItemEquipped();
    }
    
    /**
     * Damages the shear-type item and returns true if we should continue shearing leaves/vines
     */
    private boolean damageShearsAndContinue(World world, int id, int x, int y, int z)
    {
        if (shears != null)
        {
            int shearsIndex = shearsHotbarIndex(player);
            currentShearsDamage += leafDamageMultiplier;
            
            for (int i = 0; i < Math.floor(currentShearsDamage); i++)
                // Shakes fist at Forge!
                if (TreeCapitator.isForge && shears.itemID == Item.shears.itemID)
                    shears.damageItem(1, player);
                else
                    shears.getItem().onBlockDestroyed(shears, world, id, x, y, z, player);
            
            currentShearsDamage -= Math.floor(currentShearsDamage);
            
            if (shears != null && shears.stackSize < 1 && shearsIndex != -1)
                player.inventory.setInventorySlotContents(shearsIndex, (ItemStack) null);
        }
        return TreeCapitator.allowMoreBlocksThanDamage || hasShearsInHotbar(player);
    }
    
    private List<Coord> addLogs(World world, Coord pos)
    {
        int index = 0, lowY = pos.y, x, y, z;
        List<Coord> list = new ArrayList<Coord>();
        Coord newPos;
        
        list.add(pos);
        
        do
        {
            Coord currentLog = list.get(index);
            
            for (x = -1; x <= 1; x++)
                for (y = (treeDef.onlyDestroyUpwards() ? 0 : -1); y <= 1; y++)
                    for (z = -1; z <= 1; z++)
                        if (logList.contains(new BlockID(world, currentLog.x + x, currentLog.y + y, currentLog.z + z)))
                        {
                            newPos = new Coord(currentLog.x + x, currentLog.y + y, currentLog.z + z);
                            
                            if (treeDef.maxLogBreakDist() == -1 || (Math.abs(newPos.x - startPos.x) <= treeDef.maxLogBreakDist()
                                    && Math.abs(newPos.z - startPos.z) <= treeDef.maxLogBreakDist()))
                                if (!list.contains(newPos) && (newPos.y >= lowY || !treeDef.onlyDestroyUpwards()))
                                    list.add(newPos);
                        }
        }
        while (++index < list.size());
        
        return list;
    }
    
    private void addLogsAbove(World world, Coord position, List<Coord> listFinal)
    {
        List<Coord> listAbove = new ArrayList<Coord>();
        List<Coord> list;
        Coord newPosition;
        int counter, index, x, z;
        
        listAbove.add(position);
        
        do
        {
            list = listAbove;
            listAbove = new ArrayList<Coord>();
            
            for (Coord pos : list)
            {
                counter = 0;
                for (x = -1; x <= 1; x++)
                    for (z = -1; z <= 1; z++)
                        if (logList.contains(new BlockID(world, pos.x + x, pos.y + 1, pos.z + z)))
                        {
                            if (!listAbove.contains(newPosition = new Coord(pos.x + x, pos.y + 1, pos.z + z)))
                                listAbove.add(newPosition);
                            
                            counter++;
                        }
                
                if (counter == 0)
                    listFinal.add(pos.clone());
            }
            
            index = -1;
            while (++index < listAbove.size())
            {
                Coord pos = listAbove.get(index);
                for (x = -1; x <= 1; x++)
                    for (z = -1; z <= 1; z++)
                        if (logList.contains(new BlockID(world, pos.x + x, pos.y, pos.z + z)))
                            if (!listAbove.contains(newPosition = new Coord(pos.x + x, pos.y, pos.z + z)))
                                listAbove.add(newPosition);
            }
        }
        while (listAbove.size() > 0);
    }
    
    public List<Coord> addLeaves(World world, Coord pos)
    {
        int index = -1;
        List<Coord> list = new ArrayList<Coord>();
        
        //addConnectedLeavesInRange(world, pos, treeDef.maxLeafBreakDist(), 0, list);
        addLeavesInDistance(world, pos, treeDef.maxLeafBreakDist(), list);
        
        while (++index < list.size())
        {
            Coord pos2 = list.get(index);
            if (CommonUtils.getSphericalDistance(pos, pos2) < treeDef.maxLeafBreakDist())
                addLeavesInDistance(world, pos2, 1, list);
        }
        
        return list;
    }
    
    public void addLeavesInDistance(World world, Coord pos, int range, List list)
    {
        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range; y++)
                for (int z = -range; z <= range; z++)
                {
                    int blockID = world.getBlockId(x + pos.x, y + pos.y, z + pos.z);
                    int md = world.getBlockMetadata(x + pos.x, y + pos.y, z + pos.z);
                    Block block = Block.blocksList[blockID];
                    if (isLeafBlock(new BlockID(blockID, md)) || vineID.equals(new BlockID(blockID)))
                    {
                        int metadata = world.getBlockMetadata(x + pos.x, y + pos.y, z + pos.z);
                        if (!treeDef.requireLeafDecayCheck() || ((metadata & 8) != 0 && (metadata & 4) == 0))
                        {
                            Coord newPos = new Coord(x + pos.x, y + pos.y, z + pos.z);
                            if (!list.contains(newPos) && !hasLogClose(world, newPos, 1))
                                list.add(newPos);
                        }
                    }
                }
    }
    
    public void addConnectedLeavesInRange(World world, Coord pos, int range, int distance, List list)
    {
        // So far this method is utter shit and doesn't work right
        if (!list.contains(pos))
        {
            int blockID = world.getBlockId(pos.x, pos.y, pos.z);
            int md = world.getBlockMetadata(pos.x, pos.y, pos.z);
            if (Math.abs(distance) <= range)
            {
                if (isLeafBlock(new BlockID(blockID, md)) || vineID.equals(new BlockID(blockID)) || distance == 0)
                {
                    if ((!treeDef.requireLeafDecayCheck() || ((md & 8) != 0 && (md & 4) == 0)) && distance != 0)
                    {
                        list.add(pos);
                    }
                    
                    for (int x = -1; x <= 1; x++)
                        for (int y = -1; y <= 1; y++)
                            for (int z = -1; z <= 1; z++)
                            {
                                addConnectedLeavesInRange(world, new Coord(pos.x + x, pos.y + y, pos.z + z), range, distance + 1, list);
                            }
                }
            }
        }
    }
    
    /**
     * Removes leaf/vine blocks from the list if they still have a log block neighbor (ie- if they are part of another tree)
     * 
     * Deprecated in favor of just not adding those leaves to the list in the first place.
     */
    @Deprecated
    public void removeLeavesWithLogsAround(World world, List<Coord> list)
    {
        for (int i = 0; i < list.size();)
            if (hasLogClose(world, list.get(i), 1))
                list.remove(i);
            else
                i++;
    }
    
    /**
     * Returns true if a log block is within i blocks of pos
     */
    public boolean hasLogClose(World world, Coord pos, int i)
    {
        for (int x = -i; x <= i; x++)
            for (int y = -i; y <= i; y++)
                for (int z = -i; z <= i; z++)
                {
                    Coord neighbor = new Coord(x + pos.x, y + pos.y, z + pos.z);
                    int neighborID = world.getBlockId(neighbor.x, neighbor.y, neighbor.z);
                    /*
                     * Use TreeCapitator.logIDList here so that we find ANY type of log block, not just the type for this tree
                     */
                    if ((x != 0 || y != 0 || z != 0) && neighborID != 0 &&
                            masterLogList.contains(new BlockID(world, neighbor.x, neighbor.y, neighbor.z))
                            && !neighbor.equals(startPos))
                        return true;
                }
        
        return false;
    }
}
