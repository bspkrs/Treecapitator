package bspkrs.treecapitator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.ForgeEventFactory;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.registry.ToolRegistry;
import bspkrs.treecapitator.registry.TreeDefinition;
import bspkrs.treecapitator.registry.TreeRegistry;
import bspkrs.treecapitator.util.Reference;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.BlockID;
import bspkrs.util.CommonUtils;
import bspkrs.util.Coord;
import bspkrs.util.ModulusBlockID;

public class Treecapitator
{
    // The player chopping
    private World                world;
    private EntityPlayer         player;
    private Coord                startPos;
    // The axe of the player currently chopping
    private ItemStack            axe;
    private ItemStack            shears;
    private final TreeDefinition treeDef;
    private final List<BlockID>  masterLogList;
    private final BlockID        vineID;
    private float                currentAxeDamage, currentShearsDamage = 0.0F;
    private int                  numLogsToBreak;
    private int                  numLogsBroken;
    private int                  numLeavesSheared;
    private float                logDamageMultiplier;
    private float                leafDamageMultiplier;
    private List<ItemStack>      drops;
    private Coord                dropPos;
    private boolean              maxAllowed = false;

    public Treecapitator(EntityPlayer entityPlayer, TreeDefinition treeDef)
    {
        player = entityPlayer;
        this.treeDef = treeDef;
        masterLogList = TreeRegistry.instance().masterLogList();
        vineID = new BlockID(Blocks.vine);
        logDamageMultiplier = TCSettings.damageMultiplier;
        leafDamageMultiplier = TCSettings.damageMultiplier;
        numLogsToBreak = 0;
        numLogsBroken = 1;
        numLeavesSheared = 1;
    }

    public static boolean isBreakingPossible(EntityPlayer entityPlayer, Block block, int blockMetadata, boolean shouldLog)
    {
        ItemStack axe = entityPlayer.getCurrentEquippedItem();
        if ((isAxeItemEquipped(entityPlayer, block, blockMetadata) || !TCSettings.needItem))
        {
            if (!entityPlayer.capabilities.isCreativeMode && TCSettings.allowItemDamage && axe != null
                    && (axe.isItemStackDamageable() && (axe.getMaxDurability() - axe.getMetadata() <= TCSettings.damageMultiplier))
                    && !TCSettings.allowMoreBlocksThanDamage)
            {
                if (shouldLog)
                    TCLog.debug("Chopping disabled due to axe durability.");
                return false;
            }

            return true;
        }
        else if (shouldLog)
            TCLog.debug("Player does not have an axe equipped.");

        return false;
    }

    private boolean isBreakingPossible()
    {
        axe = player.getCurrentEquippedItem();
        if ((isAxeItemEquipped() || !TCSettings.needItem))
        {
            if (!player.capabilities.isCreativeMode && TCSettings.allowItemDamage && axe != null
                    && (axe.isItemStackDamageable() && (axe.getMaxDurability() - axe.getMetadata() <= TCSettings.damageMultiplier))
                    && !TCSettings.allowMoreBlocksThanDamage)
            {
                TCLog.debug("Chopping disabled due to axe durability.");
                return false;
            }

            return true;
        }
        TCLog.debug("Player does not have an axe equipped.");

        return false;
    }

    /**
     * Defines whether or not a player can break the block with current tool and sets the local axe object if possible
     */
    private boolean isAxeItemEquipped()
    {
        ItemStack item = player.getCurrentEquippedItem();

        if (TCSettings.enableEnchantmentMode)
        {
            if (item != null && item.isItemEnchanted())
                for (int i = 0; i < item.getEnchantmentTagList().tagCount(); i++)
                {
                    NBTTagCompound tag = item.getEnchantmentTagList().getCompoundTagAt(i);
                    if (tag.getShort("id") == TCSettings.treecapitating.effectId)
                    {
                        axe = item;
                        return true;
                    }
                }

            axe = null;
            return false;
        }
        else if (ToolRegistry.instance().isAxe(item))
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
    public static boolean isAxeItemEquipped(EntityPlayer entityPlayer, Block block, int blockMetadata)
    {
        ItemStack item = entityPlayer.getCurrentEquippedItem();

        if (TCSettings.enableEnchantmentMode)
        {
            if (item != null && item.isItemEnchanted())
                for (int i = 0; i < item.getEnchantmentTagList().tagCount(); i++)
                {
                    NBTTagCompound tag = item.getEnchantmentTagList().getCompoundTagAt(i);
                    if (tag.getShort("id") == TCSettings.treecapitating.effectId)
                        return true;
                }

            return false;
        }
        else
        {
            if (item != null && !ToolRegistry.instance().isAxe(item) && TCSettings.allowAutoAxeDetection)
                ToolRegistry.autoDetectAxe(item, block, blockMetadata);

            return ToolRegistry.instance().isAxe(item);
        }
    }

    public static boolean isBreakingEnabled(EntityPlayer player)
    {
        return (TCSettings.sneakAction.equalsIgnoreCase(Reference.NONE)
                || (TCSettings.sneakAction.equalsIgnoreCase(Reference.DISABLE) && !player.isSneaking())
                || (TCSettings.sneakAction.equalsIgnoreCase(Reference.ENABLE) && player.isSneaking()))
                && !(player.capabilities.isCreativeMode && TCSettings.disableInCreative);
    }

    public static int getTreeHeight(TreeDefinition tree, World world, int x, int y, int z, int md, EntityPlayer entityPlayer)
    {
        Coord startPos = new Coord(x, y, z);

        if (!tree.onlyDestroyUpwards())
            if (tree.useAdvancedTopLogLogic())
                startPos = getBottomLog(tree.getLogList(), world, startPos, false);
            else
                startPos = getBottomLogAtPos(tree.getLogList(), world, startPos, false);

        Coord topLog = tree.useAdvancedTopLogLogic() ? getTopLog(tree.getLogList(), world, new Coord(x, y, z), false)
                : getTopLogAtPos(tree.getLogList(), world, new Coord(x, y, z), false);

        if (!tree.allowSmartTreeDetection() || tree.getLeafList().size() == 0
                || hasXLeavesInDist(tree.getLeafList(), world, topLog, tree.maxLeafIDDist(), tree.minLeavesToID(), false))
            return topLog.y - startPos.y + 1;

        return 1;
    }

    public void onBlockHarvested(World world, int x, int y, int z, int md)
    {
        if (!world.isRemote)
        {
            TCLog.debug("In TreeCapitator.onBlockHarvested() " + x + ", " + y + ", " + z);
            this.world = world;
            startPos = new Coord(x, y, z);
            dropPos = startPos.clone();
            drops = new ArrayList<ItemStack>();

            if (isBreakingEnabled(player))
            {
                Coord topLog = getTopLog(world, new Coord(x, y, z));
                if (!treeDef.allowSmartTreeDetection() || treeDef.getLeafList().size() == 0
                        || hasXLeavesInDist(world, topLog, treeDef.maxLeafIDDist(), treeDef.minLeavesToID()))
                {
                    if (isBreakingPossible())
                    {
                        long beginning = System.currentTimeMillis();
                        TCLog.debug("Proceeding to chop tree...");
                        LinkedList<Coord> listFinal = new LinkedList<Coord>();
                        TCLog.debug("Finding log blocks...");
                        long startTime = System.currentTimeMillis();
                        LinkedList<Coord> logs = addLogs(world, new Coord(x, y, z));
                        TCLog.debug("Log Discovery: %dms", System.currentTimeMillis() - startTime);
                        if (logs.isEmpty() && maxAllowed)
                            return;
                        startTime = System.currentTimeMillis();
                        addLogsAbove(world, new Coord(x, y, z), listFinal);
                        TCLog.debug("Final Logs: %dms", System.currentTimeMillis() - startTime);

                        TCLog.debug("Destroying %d log blocks...", logs.size());
                        startTime = System.currentTimeMillis();
                        destroyBlocks(world, logs);
                        TCLog.debug("Log Destruction: %dms", System.currentTimeMillis() - startTime);
                        if (numLogsBroken > 1)
                            TCLog.debug("Number of logs broken: %d", numLogsBroken);

                        if (TCSettings.destroyLeaves && treeDef.getLeafList().size() != 0)
                        {
                            TCLog.debug("Finding leaf blocks...");
                            List<Coord> leaves = new ArrayList<Coord>();
                            startTime = System.currentTimeMillis();
                            for (Coord pos : listFinal)
                            {
                                addLeaves(world, pos, leaves);
                            }
                            TCLog.debug("Leaf Discovery: %dms", System.currentTimeMillis() - startTime);
                            TCLog.debug("Destroying %d leaf blocks...", leaves.size());
                            startTime = System.currentTimeMillis();
                            destroyBlocksWithChance(world, leaves, 0.5F, hasShearsInHotbar(player));
                            TCLog.debug("Leaf Destruction: %dms", System.currentTimeMillis() - startTime);

                            if (numLeavesSheared > 1)
                                TCLog.debug("Number of leaves sheared: %d", numLeavesSheared);
                        }

                        /*
                         * Apply remaining damage if it rounds to a non-zero value
                         */
                        if (currentAxeDamage > 0.0F && axe != null)
                        {
                            currentAxeDamage = Math.round(currentAxeDamage);

                            for (int i = 0; i < MathHelper.floor_double(currentAxeDamage); i++)
                                axe.getItem().onBlockDestroyed(axe, world, treeDef.getLogList().get(0).getBlock(), x, y, z, player);
                        }

                        if (currentShearsDamage > 0.0F && shears != null)
                        {
                            currentShearsDamage = Math.round(currentShearsDamage);

                            for (int i = 0; i < Math.floor(currentShearsDamage); i++)
                                if (shears.getItem().equals(Items.shears))
                                    shears.damageItem(1, player);
                                else
                                    shears.getItem().onBlockDestroyed(shears, world, treeDef.getLeafList().get(0).getBlock(), x, y, z, player);
                        }

                        startTime = System.currentTimeMillis();
                        if (TCSettings.stackDrops)
                            while (drops.size() > 0)
                                world.spawnEntityInWorld(new EntityItem(world, dropPos.x, dropPos.y, dropPos.z, drops.remove(0)));
                        TCLog.debug("Drops: %dms", System.currentTimeMillis() - startTime);

                        TCLog.debug("Total: %dms", System.currentTimeMillis() - beginning);
                    }
                }
                else
                    TCLog.debug("Could not identify tree.");
            }
            else
                TCLog.debug("Tree Chopping is disabled due to player state or gamemode.");
        }
        else
            TCLog.debug("World is remote, skipping TreeCapitator.onBlockHarvested().");
    }

    public static List<BlockID> getLeavesForTree(World world, BlockID logID, Coord pos, boolean shouldLog)
    {
        List<BlockID> leaves = new ArrayList<BlockID>();
        List<BlockID> logs = new ArrayList<BlockID>();
        logs.add(logID);

        Coord topLogPos;
        if (TCSettings.useAdvancedTopLogLogic)
            topLogPos = getTopLog(logs, world, pos, shouldLog);
        else
            topLogPos = getTopLogAtPos(logs, world, pos, shouldLog);

        leaves = getLeavesInDist(world, topLogPos, TCSettings.maxLeafIDDist, shouldLog);

        return leaves;
    }

    private static List<BlockID> getLeavesInDist(World world, Coord pos, int range, boolean shouldLog)
    {
        if (shouldLog)
            TCLog.debug("Attempting to identify tree...");

        List<BlockID> leaves = new ArrayList<BlockID>();

        for (int x = -range; x <= range; x++)
            // lower bound kept at -1 
            for (int y = -1; y <= range; y++)
                for (int z = -range; z <= range; z++)
                    if (x != 0 || y != 0 || z != 0)
                    {
                        if (!world.isAirBlock(pos.x + x, pos.y + y, pos.z + z))
                        {
                            Block block = world.getBlock(pos.x + x, pos.y + y, pos.z + z);
                            ModulusBlockID blockID = new ModulusBlockID(world, pos.x + x, pos.y + y, pos.z + z, 8);
                            if (block.isLeaves(world, pos.x + x, pos.y + y, pos.z + z))
                            {
                                if (shouldLog)
                                    TCLog.debug("Found leaf block: %s", blockID);

                                leaves.add(blockID);
                            }
                            else if (shouldLog)
                                TCLog.debug("Not a leaf block: %s", blockID);
                        }
                    }

        if (shouldLog)
            TCLog.debug("Found %d leaves.", leaves.size());

        return leaves;
    }

    private Coord getTopLog(World world, Coord pos)
    {
        if (treeDef.useAdvancedTopLogLogic())
            return getTopLog(treeDef.getLogList(), world, pos, true);
        else
            return getTopLogAtPos(treeDef.getLogList(), world, pos, true);
    }

    private static Coord getTopLog(List<BlockID> logBlocks, World world, Coord pos, boolean shouldLog)
    {
        LinkedList<Coord> topLogs = new LinkedList<Coord>();
        HashSet<Coord> processed = new HashSet<Coord>();
        Coord topLog = pos.clone();

        topLogs.add(getTopLogAtPos(logBlocks, world, topLog, false));

        while (topLogs.size() > 0)
        {
            Coord nextLog = topLogs.pollFirst();
            processed.add(nextLog);

            // if the new pos is higher than what we've seen so far, save it as the new top log
            if (nextLog.y > topLog.y)
                topLog = nextLog;

            int currentSize = topLogs.size();
            Coord newPos;

            for (int x = -1; x <= 1; x++)
                for (int z = -1; z <= 1; z++)
                    if ((x != 0 || z != 0) && logBlocks.contains(new BlockID(world, nextLog.x + x, nextLog.y + 1, nextLog.z + z)))
                    {
                        newPos = nextLog.add(new Coord(x, 1, z));
                        if (!topLogs.contains(newPos) && !processed.contains(newPos))
                            topLogs.add(getTopLogAtPos(logBlocks, world, newPos, false));
                    }

            // check if we found anything
            if (topLogs.size() == currentSize)
            {
                for (int x = -1; x <= 1; x++)
                    for (int z = -1; z <= 1; z++)
                        if ((x != 0 || z != 0) && logBlocks.contains(new BlockID(world, nextLog.x + x, nextLog.y, nextLog.z + z)))
                        {
                            newPos = nextLog.add(new Coord(x, 0, z));
                            if (!topLogs.contains(newPos) && !processed.contains(newPos))
                                topLogs.add(getTopLogAtPos(logBlocks, world, newPos, false));
                        }

            }
        }

        if (shouldLog)
            TCLog.debug("Top Log: " + pos.x + ", " + pos.y + ", " + pos.z);

        return topLog;
    }

    private static Coord getTopLogAtPos(List<? extends BlockID> logBlocks, World world, Coord pos, boolean shouldLog)
    {
        while (logBlocks.contains(new BlockID(world, pos.x, pos.y + 1, pos.z)))
            pos.y++;

        if (shouldLog)
            TCLog.debug("Top Log: " + pos.x + ", " + pos.y + ", " + pos.z);

        return pos.clone();
    }

    private static Coord getBottomLog(List<BlockID> logBlocks, World world, Coord pos, boolean shouldLog)
    {
        LinkedList<Coord> bottomLogs = new LinkedList<Coord>();
        HashSet<Coord> processed = new HashSet<Coord>();
        Coord bottomLog = pos.clone();

        bottomLogs.add(getBottomLogAtPos(logBlocks, world, bottomLog, false));

        while (bottomLogs.size() > 0)
        {
            Coord nextLog = bottomLogs.pollFirst();
            processed.add(nextLog);

            // if the new pos is lower than what we've seen so far, save it as the new bottom log
            if (nextLog.y < bottomLog.y)
                bottomLog = nextLog;

            int currentSize = bottomLogs.size();
            Coord newPos;

            for (int x = -1; x <= 1; x++)
                for (int z = -1; z <= 1; z++)
                    if ((x != 0 || z != 0) && logBlocks.contains(new BlockID(world, nextLog.x + x, nextLog.y - 1, nextLog.z + z)))
                    {
                        newPos = new Coord(nextLog.x + x, nextLog.y - 1, nextLog.z + z);
                        if (!bottomLogs.contains(newPos) && !processed.contains(newPos))
                            bottomLogs.add(getBottomLogAtPos(logBlocks, world, newPos, false));
                    }

            // check if we found anything before adding adjacent coords
            if (bottomLogs.size() == currentSize)
            {
                for (int x = -1; x <= 1; x++)
                    for (int z = -1; z <= 1; z++)
                        if ((x != 0 || z != 0) && logBlocks.contains(new BlockID(world, nextLog.x + x, nextLog.y, nextLog.z + z)))
                        {
                            newPos = new Coord(nextLog.x + x, nextLog.y, nextLog.z + z);
                            if (!bottomLogs.contains(newPos) && !processed.contains(newPos))
                                bottomLogs.add(getBottomLogAtPos(logBlocks, world, newPos, false));
                        }

            }
        }

        if (shouldLog)
            TCLog.debug("Bottom Log: " + pos.x + ", " + pos.y + ", " + pos.z);

        return bottomLog;
    }

    private static Coord getBottomLogAtPos(List<BlockID> logBlocks, World world, Coord pos, boolean shouldLog)
    {
        while (logBlocks.contains(new BlockID(world, pos.x, pos.y - 1, pos.z)))
            pos.y--;

        if (shouldLog)
            TCLog.debug("Bottom Log: " + pos.x + ", " + pos.y + ", " + pos.z);

        return pos;
    }

    private static boolean hasXLeavesInDist(List<BlockID> leafBlocks, World world, Coord pos, int range, int limit, boolean shouldLog)
    {
        if (shouldLog)
            TCLog.debug("Attempting to identify tree...");

        int i = 0;
        for (int x = -range; x <= range; x++)
            // lower bound kept at -1 
            for (int y = -1; y <= range; y++)
                for (int z = -range; z <= range; z++)
                    if (x != 0 || y != 0 || z != 0)
                    {
                        BlockID blockID = new BlockID(world, pos.x + x, pos.y + y, pos.z + z);
                        if (blockID.isValid())
                            if (isLeafBlock(leafBlocks, blockID))
                            {
                                if (shouldLog)
                                    TCLog.debug("Found leaf block: %s", blockID);

                                i++;
                                if (i >= limit)
                                    return true;
                            }
                            else if (shouldLog)
                                TCLog.debug("Not a leaf block: %s", blockID);
                    }

        if (shouldLog)
            TCLog.debug("Number of leaf blocks is less than the limit. Found: %s", i);

        return false;
    }

    private boolean hasXLeavesInDist(World world, Coord pos, int range, int limit)
    {
        return hasXLeavesInDist(treeDef.getLeafList(), world, pos, range, limit, true);
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

            if (item != null && item.stackSize > 0 && ToolRegistry.instance().isShears(item))
            {
                shears = item;
                return i;
            }
        }
        shears = null;
        return -1;
    }

    public static boolean isLeafBlock(List<BlockID> leafBlocks, BlockID blockID)
    {
        return leafBlocks.contains(blockID) || leafBlocks.contains(new BlockID(blockID.id, blockID.metadata & 7));
    }

    public boolean isLeafBlock(BlockID blockID)
    {
        return isLeafBlock(treeDef.getLeafList(), blockID);
    }

    private void destroyBlocks(World world, LinkedList<Coord> list)
    {
        destroyBlocksWithChance(world, list, 1.0F, false);
    }

    private void destroyBlocksWithChance(World world, List<Coord> list, float f, boolean canShear)
    {
        while (list.size() > 0)
        {
            Coord pos = list.remove(0);
            Block block = world.getBlock(pos.x, pos.y, pos.z);
            if (!block.isAir(world, pos.x, pos.y, pos.z))
            {
                int metadata = world.getBlockMetadata(pos.x, pos.y, pos.z);

                BlockID blockID = new BlockID(block, metadata);
                if ((block instanceof IShearable) && (((vineID.equals(blockID) && TCSettings.shearVines)
                        || (isLeafBlock(blockID) && TCSettings.shearLeaves)) && canShear)
                        && !(player.capabilities.isCreativeMode && TCSettings.disableCreativeDrops))
                {
                    IShearable target = (IShearable) block;
                    if (target.isShearable(shears, world, pos.x, pos.y, pos.z))
                    {
                        ArrayList<ItemStack> drops = target.onSheared(shears, player.worldObj, pos.x, pos.y, pos.z,
                                EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, shears));

                        if (drops != null)
                        {
                            if (TCSettings.stackDrops)
                                addDrops(drops);
                            else if (TCSettings.itemsDropInPlace)
                                for (ItemStack itemStack : drops)
                                    world.spawnEntityInWorld(new EntityItem(world, pos.x, pos.y, pos.z, itemStack));
                            else
                                for (ItemStack itemStack : drops)
                                    world.spawnEntityInWorld(new EntityItem(world, startPos.x, startPos.y, startPos.z, itemStack));
                        }

                        if (TCSettings.allowItemDamage && !player.capabilities.isCreativeMode && shears != null && shears.stackSize > 0)
                        {
                            canShear = damageShearsAndContinue(world, block, pos.x, pos.y, pos.z);
                            numLeavesSheared++;

                            if (canShear && TCSettings.useIncreasingItemDamage && numLeavesSheared % TCSettings.increaseDamageEveryXBlocks == 0)
                                leafDamageMultiplier += TCSettings.damageIncreaseAmount;
                        }
                    }
                }
                else if (!(player.capabilities.isCreativeMode && TCSettings.disableCreativeDrops))
                {
                    if (TCSettings.stackDrops)
                        addDrop(block, metadata, pos);
                    else if (TCSettings.itemsDropInPlace)
                        block.dropBlockAsItem(world, pos.x, pos.y, pos.z, metadata, EnchantmentHelper.getFortuneModifier(player));
                    else
                        block.dropBlockAsItem(world, startPos.x, startPos.y, startPos.z, metadata, EnchantmentHelper.getFortuneModifier(player));

                    if (TCSettings.allowItemDamage && !player.capabilities.isCreativeMode && axe != null && axe.stackSize > 0
                            && !vineID.equals(new BlockID(block, metadata)) && !isLeafBlock(new BlockID(block, metadata)) && !pos.equals(startPos))
                    {
                        if (!damageAxeAndContinue(world, block, startPos.x, startPos.y, startPos.z))
                            list.clear();

                        numLogsBroken++;

                        if (TCSettings.useIncreasingItemDamage && numLogsBroken % TCSettings.increaseDamageEveryXBlocks == 0)
                            logDamageMultiplier += TCSettings.damageIncreaseAmount;
                    }
                }

                if (world.getTileEntity(pos.x, pos.y, pos.z) != null)
                    world.removeTileEntity(pos.x, pos.y, pos.z);

                world.setBlockToAir(pos.x, pos.y, pos.z);

                // Can't believe it took this long to realize this wasn't being done...
                player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(block)], 1);
                player.addExhaustion(0.025F);
            }
        }
    }

    private void addDrop(Block block, int metadata, Coord pos)
    {
        List<ItemStack> stacks = null;

        dropPos = TCSettings.itemsDropInPlace ? pos.clone() : startPos.clone();

        if ((block.canSilkHarvest(world, player, pos.x, pos.y, pos.z, metadata) && EnchantmentHelper.getSilkTouchModifier(player)))
        {
            stacks = new ArrayList<ItemStack>();
            stacks.add(new ItemStack(block, 1, metadata));
        }
        else
        {
            stacks = block.getDrops(world, pos.x, pos.y, pos.z, metadata, EnchantmentHelper.getFortuneModifier(player));
            ForgeEventFactory.fireBlockHarvesting((ArrayList<ItemStack>) stacks, world, block, pos.x, pos.y, pos.z, metadata, EnchantmentHelper.getFortuneModifier(player), 1.0f, EnchantmentHelper.getSilkTouchModifier(player), player);
        }

        addDrops(stacks);
    }

    private void addDrops(List<ItemStack> stacks)
    {
        if (stacks == null)
            return;
        for (ItemStack drop : stacks)
        {
            if (drop == null)
                continue;

            int index = -1;
            for (int i = 0; i < drops.size(); i++)
            {
                if (drops.get(i).isItemEqual(drop))
                {
                    index = i;
                    break;
                }
            }

            if (index == -1)
            {
                drops.add(drop);
                index = drops.indexOf(drop);
            }
            else
            {
                int quantity = drop.stackSize;
                drop = drops.get(index);
                drop.stackSize += quantity;
            }

            if (drop.stackSize >= drop.getMaxStackSize())
            {
                int i = drop.stackSize - drop.getMaxStackSize();
                drop.stackSize = drop.getMaxStackSize();
                world.spawnEntityInWorld(new EntityItem(world, dropPos.x, dropPos.y, dropPos.z, drop));
                if (i > 0)
                    drop.stackSize = i;
                else
                    drops.remove(index);
            }
        }
    }

    /**
     * Damages the axe-type item and returns true if we should continue destroying logs
     */
    private boolean damageAxeAndContinue(World world, Block block, int x, int y, int z)
    {
        if (axe != null)
        {
            currentAxeDamage += logDamageMultiplier;

            for (int i = 0; i < (int) Math.floor(currentAxeDamage); i++)
                axe.getItem().onBlockDestroyed(axe, world, block, x, y, z, player);

            currentAxeDamage -= Math.floor(currentAxeDamage);

            if (axe != null && axe.stackSize < 1)
                player.destroyCurrentEquippedItem();
        }
        return !TCSettings.needItem || TCSettings.allowMoreBlocksThanDamage || isAxeItemEquipped();
    }

    /**
     * Damages the shear-type item and returns true if we should continue shearing leaves/vines
     */
    private boolean damageShearsAndContinue(World world, Block block, int x, int y, int z)
    {
        if (shears != null)
        {
            int shearsIndex = shearsHotbarIndex(player);
            currentShearsDamage += leafDamageMultiplier;

            for (int i = 0; i < Math.floor(currentShearsDamage); i++)
                // Shakes fist at Forge!
                if (shears.getItem().equals(Items.shears))
                    shears.damageItem(1, player);
                else
                    shears.getItem().onBlockDestroyed(shears, world, block, x, y, z, player);

            currentShearsDamage -= Math.floor(currentShearsDamage);

            if (shears != null && shears.stackSize < 1 && shearsIndex != -1)
                player.inventory.setInventorySlotContents(shearsIndex, (ItemStack) null);
        }
        return TCSettings.allowMoreBlocksThanDamage || hasShearsInHotbar(player);
    }

    private LinkedList<Coord> addLogs(World world, Coord pos)
    {
        int index = 0, lowY = pos.y, x, y, z;
        LinkedList<Coord> list = new LinkedList<Coord>();
        Coord newPos;

        list.add(pos);

        do
        {
            Coord currentLog = list.get(index);

            for (x = -1; x <= 1; x++)
                for (y = (treeDef.onlyDestroyUpwards() ? 0 : -1); y <= 1; y++)
                    for (z = -1; z <= 1; z++)
                        if (!world.isAirBlock(currentLog.x + x, currentLog.y + y, currentLog.z + z))
                            if (treeDef.getLogList().contains(new BlockID(world, currentLog.x + x, currentLog.y + y, currentLog.z + z)))
                            {
                                newPos = new Coord(currentLog.x + x, currentLog.y + y, currentLog.z + z);

                                if (treeDef.maxHorLogBreakDist() == -1 || (Math.abs(newPos.x - startPos.x) <= treeDef.maxHorLogBreakDist()
                                        && Math.abs(newPos.z - startPos.z) <= treeDef.maxHorLogBreakDist())
                                        && (treeDef.maxVerLogBreakDist() == -1 || (Math.abs(newPos.y - startPos.y) <= treeDef.maxVerLogBreakDist()))
                                        && !list.contains(newPos) && (newPos.y >= lowY || !treeDef.onlyDestroyUpwards()))
                                {
                                    list.add(newPos);
                                    if (TCSettings.maxNumberOfBlocksInTree != -1 && ++this.numLogsToBreak > TCSettings.maxNumberOfBlocksInTree)
                                    {
                                        list.clear();
                                        TCLog.debug("Number of logs in tree is more than the maximum number allowed.");
                                        maxAllowed = true;
                                        return list;
                                    }
                                }
                            }
        }
        while (++index < list.size());

        if (list.contains(pos))
            list.remove(pos);

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
                        if (treeDef.getLogList().contains(new BlockID(world, pos.x + x, pos.y + 1, pos.z + z)))
                        {
                            if (!listAbove.contains(newPosition = new Coord(pos.x + x, pos.y + 1, pos.z + z)))
                                listAbove.add(newPosition);

                            counter++;
                        }

                if (counter == 0)
                    listFinal.add(pos);
            }

            index = -1;
            while (++index < listAbove.size())
            {
                Coord pos = listAbove.get(index);
                for (x = -1; x <= 1; x++)
                    for (z = -1; z <= 1; z++)
                        if (treeDef.getLogList().contains(new BlockID(world, pos.x + x, pos.y, pos.z + z)))
                            if (!listAbove.contains(newPosition = new Coord(pos.x + x, pos.y, pos.z + z)))
                                listAbove.add(newPosition);
            }
        }
        while (listAbove.size() > 0);
    }

    public List<Coord> addLeaves(World world, Coord pos, List<Coord> list)
    {
        int index = -1;

        if (list == null)
            list = new ArrayList<Coord>();

        addLeavesInDistance(world, pos, treeDef.maxHorLeafBreakDist(), list);

        while (++index < list.size())
        {
            Coord pos2 = list.get(index);
            if (treeDef.maxHorLeafBreakDist() == -1 || CommonUtils.getHorSquaredDistance(pos, pos2) <= treeDef.maxHorLeafBreakDist())
                addLeavesInDistance(world, pos2, 1, list);
        }

        return list;
    }

    public void addLeavesInDistance(World world, Coord pos, int range, List<Coord> list)
    {
        if (range == -1)
            range = 4;

        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range; y++)
                for (int z = -range; z <= range; z++)
                {
                    Block block = world.getBlock(x + pos.x, y + pos.y, z + pos.z);
                    int md = world.getBlockMetadata(x + pos.x, y + pos.y, z + pos.z);
                    if (isLeafBlock(new BlockID(block, md)) || vineID.equals(new BlockID(block)))
                    {
                        if (!treeDef.requireLeafDecayCheck() || ((md & 8) != 0 && (md & 4) == 0))
                        {
                            Coord newPos = new Coord(x + pos.x, y + pos.y, z + pos.z);
                            if (!list.contains(newPos) && !hasLogClose(world, newPos, 1))
                                list.add(newPos);
                        }
                    }
                }
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
                    Coord neighborPos = new Coord(x + pos.x, y + pos.y, z + pos.z);
                    if ((x != 0 || y != 0 || z != 0) && !neighborPos.isAirBlock(world) &&
                            treeDef.getLogList().contains(new BlockID(world, neighborPos.x, neighborPos.y, neighborPos.z))
                            && !neighborPos.equals(startPos))
                        return true;
                }

        return false;
    }
}
