package bspkrs.treecapitator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.registry.ToolRegistry;
import bspkrs.treecapitator.registry.TreeDefinition;
import bspkrs.treecapitator.util.Reference;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.BlockID;
import bspkrs.util.CommonUtils;
import bspkrs.util.ModulusBlockID;

public class Treecapitator
{
    // The player chopping
    private World                world;
    private final EntityPlayer   player;
    private BlockPos             startPos;
    // The axe of the player currently chopping
    private ItemStack            axe;
    private ItemStack            shears;
    private final TreeDefinition treeDef;
    private final BlockID        vineID;
    private float                currentAxeDamage, currentShearsDamage = 0.0F;
    private int                  numLogsToBreak;
    private int                  numLogsBroken;
    private int                  numLeavesSheared;
    private float                logDamageMultiplier;
    private float                leafDamageMultiplier;
    private List<ItemStack>      drops;
    private BlockPos             dropPos;
    private boolean              maxAllowed = false;

    public Treecapitator(EntityPlayer entityPlayer, TreeDefinition treeDef)
    {
        player = entityPlayer;
        this.treeDef = treeDef;
        vineID = new BlockID(Blocks.vine);
        logDamageMultiplier = TCSettings.damageMultiplier;
        leafDamageMultiplier = TCSettings.damageMultiplier;
        numLogsToBreak = 0;
        numLogsBroken = 1;
        numLeavesSheared = 1;
    }

    public static boolean isBreakingPossible(EntityPlayer entityPlayer, BlockPos pos, boolean shouldLog)
    {
        ItemStack axe = entityPlayer.getCurrentEquippedItem();
        if ((isAxeItemEquipped(entityPlayer, pos) || !TCSettings.needItem))
        {
            if (!entityPlayer.capabilities.isCreativeMode && TCSettings.allowItemDamage && (axe != null)
                    && (axe.isItemStackDamageable() && ((axe.getMaxDamage() - axe.getMetadata()) <= TCSettings.damageMultiplier))
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
            if (!player.capabilities.isCreativeMode && TCSettings.allowItemDamage && (axe != null)
                    && (axe.isItemStackDamageable() && ((axe.getMaxDamage() - axe.getMetadata()) <= TCSettings.damageMultiplier))
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
            if ((item != null) && item.isItemEnchanted())
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
    public static boolean isAxeItemEquipped(EntityPlayer entityPlayer, BlockPos pos)
    {
        ItemStack item = entityPlayer.getCurrentEquippedItem();

        if (TCSettings.enableEnchantmentMode)
        {
            if ((item != null) && item.isItemEnchanted())
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
            if ((item != null) && !ToolRegistry.instance().isAxe(item) && TCSettings.allowAutoAxeDetection)
                ToolRegistry.autoDetectAxe(entityPlayer.worldObj, pos, item);

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

    public static int getTreeHeight(TreeDefinition tree, World world, BlockPos pos, EntityPlayer entityPlayer)
    {
        BlockPos startPos = pos;

        if (!tree.onlyDestroyUpwards())
            if (tree.useAdvancedTopLogLogic())
                startPos = getBottomLog(tree.getLogList(), world, startPos, false);
            else
                startPos = getBottomLogAtPos(tree.getLogList(), world, startPos, false);

        BlockPos topLog = tree.useAdvancedTopLogLogic() ? getTopLog(tree.getLogList(), world, pos, false)
                : getTopLogAtPos(tree.getLogList(), world, pos, false);

        if (!tree.allowSmartTreeDetection() || (tree.getLeafList().size() == 0)
                || hasXLeavesInDist(tree.getLeafList(), world, topLog, tree.maxLeafIDDist(), tree.minLeavesToID(), false))
            return (topLog.getY() - startPos.getY()) + 1;

        return 1;
    }

    public void onBlockHarvested(World world, BlockPos pos)
    {
        if (!world.isRemote)
        {
            TCLog.debug("In TreeCapitator.onBlockHarvested() " + pos.toString());
            this.world = world;
            startPos = pos;
            dropPos = startPos;
            drops = new ArrayList<ItemStack>();

            if (isBreakingEnabled(player))
            {
                BlockPos topLog = getTopLog(world, pos);
                if (!treeDef.allowSmartTreeDetection() || (treeDef.getLeafList().size() == 0)
                        || hasXLeavesInDist(world, topLog, treeDef.maxLeafIDDist(), treeDef.minLeavesToID()))
                {
                    if (isBreakingPossible())
                    {
                        //                        if (TCSettings.useTickBasedChopping)
                        //                            this.startTickingChop(pos);
                        //                        else
                        this.doProceduralChop(pos);
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

    //    public void startTickingChop(BlockPos pos)
    //    {
    //        // TODO!!!
    //    }
    //
    //    public class LogFinder
    //    {
    //        LinkedList<BlockPos> listFinal;
    //        LinkedList<BlockPos> logs;
    //
    //    }

    public void doProceduralChop(BlockPos pos)
    {
        long beginning = System.currentTimeMillis();
        TCLog.debug("Proceeding to chop tree...");
        LinkedList<BlockPos> listFinal = new LinkedList<BlockPos>();
        TCLog.debug("Finding log blocks...");
        long startTime = System.currentTimeMillis();
        LinkedList<BlockPos> logs = addLogs(world, pos);
        TCLog.debug("Log Discovery: %dms", System.currentTimeMillis() - startTime);
        if (logs.isEmpty() && maxAllowed)
            return;
        startTime = System.currentTimeMillis();
        addLogsAbove(world, pos, listFinal);
        TCLog.debug("Final Logs: %dms", System.currentTimeMillis() - startTime);

        TCLog.debug("Destroying %d log blocks...", logs.size());
        startTime = System.currentTimeMillis();
        destroyBlocks(world, logs);
        TCLog.debug("Log Destruction: %dms", System.currentTimeMillis() - startTime);
        if (numLogsBroken > 1)
            TCLog.debug("Number of logs broken: %d", numLogsBroken);

        if (TCSettings.destroyLeaves && (treeDef.getLeafList().size() != 0))
        {
            TCLog.debug("Finding leaf blocks...");
            List<BlockPos> leaves = new ArrayList<BlockPos>();
            startTime = System.currentTimeMillis();
            for (BlockPos BlockPos : listFinal)
            {
                addLeaves(world, BlockPos, leaves);
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
        if ((currentAxeDamage > 0.0F) && (axe != null))
        {
            currentAxeDamage = Math.round(currentAxeDamage);

            for (int i = 0; i < MathHelper.floor_double(currentAxeDamage); i++)
                axe.getItem().onBlockDestroyed(axe, world, treeDef.getLogList().get(0).getBlock(), pos, player);
        }

        if ((currentShearsDamage > 0.0F) && (shears != null))
        {
            currentShearsDamage = Math.round(currentShearsDamage);

            for (int i = 0; i < Math.floor(currentShearsDamage); i++)
                if (shears.getItem().equals(Items.shears))
                    shears.damageItem(1, player);
                else
                    shears.getItem().onBlockDestroyed(shears, world, treeDef.getLeafList().get(0).getBlock(), pos, player);
        }

        startTime = System.currentTimeMillis();
        if (TCSettings.stackDrops)
            while (drops.size() > 0)
                world.spawnEntityInWorld(new EntityItem(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), drops.remove(0)));
        TCLog.debug("Drops: %dms", System.currentTimeMillis() - startTime);

        TCLog.debug("Total: %dms", System.currentTimeMillis() - beginning);
    }

    public static List<BlockID> getLeavesForTree(World world, BlockID logID, BlockPos pos, boolean shouldLog)
    {
        List<BlockID> leaves = new ArrayList<BlockID>();
        List<BlockID> logs = new ArrayList<BlockID>();
        logs.add(logID);

        BlockPos topLogPos;
        if (TCSettings.useAdvancedTopLogLogic)
            topLogPos = getTopLog(logs, world, pos, shouldLog);
        else
            topLogPos = getTopLogAtPos(logs, world, pos, shouldLog);

        leaves = getLeavesInDist(world, topLogPos, TCSettings.maxLeafIDDist, shouldLog);

        return leaves;
    }

    private static List<BlockID> getLeavesInDist(World world, BlockPos pos, int range, boolean shouldLog)
    {
        if (shouldLog)
            TCLog.debug("Attempting to identify tree...");

        List<BlockID> leaves = new ArrayList<BlockID>();

        for (int x = -range; x <= range; x++)
            // lower bound kept at -1 
            for (int y = -1; y <= range; y++)
                for (int z = -range; z <= range; z++)
                    if ((x != 0) || (y != 0) || (z != 0))
                    {
                        BlockPos pos2 = pos.add(new BlockPos(x, y, z));
                        if (!world.isAirBlock(pos2))
                        {
                            Block block = world.getBlockState(pos2).getBlock();
                            ModulusBlockID blockID = new ModulusBlockID(world, pos2, 8);
                            if (block.isLeaves(world, pos2))
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

    private BlockPos getTopLog(World world, BlockPos pos)
    {
        if (treeDef.useAdvancedTopLogLogic())
            return getTopLog(treeDef.getLogList(), world, pos, true);
        else
            return getTopLogAtPos(treeDef.getLogList(), world, pos, true);
    }

    private static BlockPos getTopLog(List<BlockID> logBlocks, World world, BlockPos pos, boolean shouldLog)
    {
        LinkedList<BlockPos> topLogs = new LinkedList<BlockPos>();
        HashSet<BlockPos> processed = new HashSet<BlockPos>();
        BlockPos topLog = pos;

        topLogs.add(getTopLogAtPos(logBlocks, world, topLog, false));

        while (topLogs.size() > 0)
        {
            BlockPos nextLog = topLogs.pollFirst();
            processed.add(nextLog);

            // if the new pos is higher than what we've seen so far, save it as the new top log
            if (nextLog.getY() > topLog.getY())
                topLog = nextLog;

            int currentSize = topLogs.size();
            BlockPos newPos;

            for (int x = -1; x <= 1; x++)
                for (int z = -1; z <= 1; z++)
                {
                    newPos = new BlockPos(x, 1, z).add(nextLog);
                    if (((x != 0) || (z != 0)) && logBlocks.contains(new BlockID(world, newPos))
                            && !topLogs.contains(newPos) && !processed.contains(newPos))
                        topLogs.add(getTopLogAtPos(logBlocks, world, newPos, false));
                }

            // check if we found anything
            if (topLogs.size() == currentSize)
            {
                for (int x = -1; x <= 1; x++)
                    for (int z = -1; z <= 1; z++)
                    {
                        newPos = new BlockPos(x, 0, z).add(nextLog);
                        if (((x != 0) || (z != 0)) && logBlocks.contains(new BlockID(world, newPos))
                                && !topLogs.contains(newPos) && !processed.contains(newPos))
                            topLogs.add(getTopLogAtPos(logBlocks, world, newPos, false));
                    }

            }
        }

        if (shouldLog)
            TCLog.debug("Top Log: " + pos.toString());

        return topLog;
    }

    private static BlockPos getTopLogAtPos(List<? extends BlockID> logBlocks, World world, BlockPos pos, boolean shouldLog)
    {
        while (logBlocks.contains(new BlockID(world, pos.up())))
            pos = pos.up();

        if (shouldLog)
            TCLog.debug("Top Log: " + pos.toString());

        return pos;
    }

    private static BlockPos getBottomLog(List<BlockID> logBlocks, World world, BlockPos pos, boolean shouldLog)
    {
        LinkedList<BlockPos> bottomLogs = new LinkedList<BlockPos>();
        HashSet<BlockPos> processed = new HashSet<BlockPos>();
        BlockPos bottomLog = pos;

        bottomLogs.add(getBottomLogAtPos(logBlocks, world, bottomLog, false));

        while (bottomLogs.size() > 0)
        {
            BlockPos nextLog = bottomLogs.pollFirst();
            processed.add(nextLog);

            // if the new pos is lower than what we've seen so far, save it as the new bottom log
            if (nextLog.getY() < bottomLog.getY())
                bottomLog = nextLog;

            int currentSize = bottomLogs.size();
            BlockPos newPos;

            for (int x = -1; x <= 1; x++)
                for (int z = -1; z <= 1; z++)
                {
                    newPos = new BlockPos(x, -1, z).add(nextLog);
                    if (((x != 0) || (z != 0)) && logBlocks.contains(new BlockID(world, newPos))
                            && !bottomLogs.contains(newPos) && !processed.contains(newPos))
                        bottomLogs.add(getBottomLogAtPos(logBlocks, world, newPos, false));
                }

            // check if we found anything before adding adjacent BlockPoss
            if (bottomLogs.size() == currentSize)
            {
                for (int x = -1; x <= 1; x++)
                    for (int z = -1; z <= 1; z++)
                    {
                        newPos = new BlockPos(x, 0, z).add(nextLog);
                        if (((x != 0) || (z != 0)) && logBlocks.contains(new BlockID(world, newPos))
                                && !bottomLogs.contains(newPos) && !processed.contains(newPos))
                            bottomLogs.add(getBottomLogAtPos(logBlocks, world, newPos, false));
                    }

            }
        }

        if (shouldLog)
            TCLog.debug("Bottom Log: " + pos.toString());

        return bottomLog;
    }

    private static BlockPos getBottomLogAtPos(List<BlockID> logBlocks, World world, BlockPos pos, boolean shouldLog)
    {
        while (logBlocks.contains(new BlockID(world, pos.down())))
            pos = pos.down();

        if (shouldLog)
            TCLog.debug("Bottom Log: " + pos.toString());

        return pos;
    }

    private static boolean hasXLeavesInDist(List<BlockID> leafBlocks, World world, BlockPos pos, int range, int limit, boolean shouldLog)
    {
        if (shouldLog)
            TCLog.debug("Attempting to identify tree...");

        int i = 0;
        for (int x = -range; x <= range; x++)
            // lower bound kept at -1 
            for (int y = -1; y <= range; y++)
                for (int z = -range; z <= range; z++)
                    if ((x != 0) || (y != 0) || (z != 0))
                    {
                        BlockPos otherPos = new BlockPos(x, y, z).add(pos);
                        BlockID blockID = new BlockID(world, otherPos);
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

    private boolean hasXLeavesInDist(World world, BlockPos pos, int range, int limit)
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

            if ((item != null) && (item.stackSize > 0) && ToolRegistry.instance().isShears(item))
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

    private void destroyBlocks(World world, LinkedList<BlockPos> list)
    {
        destroyBlocksWithChance(world, list, 1.0F, false);
    }

    private void destroyBlocksWithChance(World world, List<BlockPos> list, float f, boolean canShear)
    {
        while (list.size() > 0)
        {
            BlockPos pos = list.remove(0);
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (!block.isAir(world, pos))
            {
                int metadata = block.getMetaFromState(state);

                BlockID blockID = new BlockID(block, metadata);
                if ((block instanceof IShearable) && (((vineID.equals(blockID) && TCSettings.shearVines)
                        || (isLeafBlock(blockID) && TCSettings.shearLeaves)) && canShear)
                        && !(player.capabilities.isCreativeMode && TCSettings.disableCreativeDrops))
                {
                    IShearable target = (IShearable) block;
                    if (target.isShearable(shears, world, pos))
                    {
                        List<ItemStack> drops = target.onSheared(shears, player.worldObj, pos,
                                EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, shears));

                        if (drops != null)
                        {
                            if (TCSettings.stackDrops)
                                addDrops(drops);
                            else if (TCSettings.itemsDropInPlace)
                                for (ItemStack itemStack : drops)
                                    world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemStack));
                            else
                                for (ItemStack itemStack : drops)
                                    world.spawnEntityInWorld(new EntityItem(world, startPos.getX(), startPos.getY(), startPos.getZ(), itemStack));
                        }

                        if (TCSettings.allowItemDamage && !player.capabilities.isCreativeMode && (shears != null) && (shears.stackSize > 0))
                        {
                            canShear = damageShearsAndContinue(world, block, pos);
                            numLeavesSheared++;

                            if (canShear && TCSettings.useIncreasingItemDamage && ((numLeavesSheared % TCSettings.increaseDamageEveryXBlocks) == 0))
                                leafDamageMultiplier += TCSettings.damageIncreaseAmount;
                        }
                    }
                }
                else if (!(player.capabilities.isCreativeMode && TCSettings.disableCreativeDrops))
                {
                    if (TCSettings.stackDrops)
                        addDrop(block, metadata, pos);
                    else if (TCSettings.itemsDropInPlace)
                        block.dropBlockAsItem(world, pos, world.getBlockState(pos), EnchantmentHelper.getFortuneModifier(player));
                    else
                        block.dropBlockAsItem(world, startPos, world.getBlockState(pos), EnchantmentHelper.getFortuneModifier(player));

                    if (TCSettings.allowItemDamage && !player.capabilities.isCreativeMode && (axe != null) && (axe.stackSize > 0)
                            && !vineID.equals(new BlockID(block, metadata)) && !isLeafBlock(new BlockID(block, metadata)) && !pos.equals(startPos))
                    {
                        if (!damageAxeAndContinue(world, block, startPos))
                            list.clear();

                        numLogsBroken++;

                        if (TCSettings.useIncreasingItemDamage && ((numLogsBroken % TCSettings.increaseDamageEveryXBlocks) == 0))
                            logDamageMultiplier += TCSettings.damageIncreaseAmount;
                    }
                }

                if (world.getTileEntity(pos) != null)
                    world.removeTileEntity(pos);

                world.setBlockToAir(pos);

                // Can't believe it took this long to realize this wasn't being done...
                player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(block)], 1);
                player.addExhaustion(0.025F);
            }
        }
    }

    private void addDrop(Block block, int metadata, BlockPos pos)
    {
        List<ItemStack> stacks = null;

        dropPos = TCSettings.itemsDropInPlace ? pos : startPos;
        IBlockState state = world.getBlockState(pos);

        if ((block.canSilkHarvest(world, pos, state, player) && EnchantmentHelper.getSilkTouchModifier(player)))
        {
            stacks = new ArrayList<ItemStack>();
            stacks.add(new ItemStack(block, 1, metadata));
        }
        else
        {
            stacks = block.getDrops(world, pos, state, EnchantmentHelper.getFortuneModifier(player));
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
                world.spawnEntityInWorld(new EntityItem(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), drop));
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
    private boolean damageAxeAndContinue(World world, Block block, BlockPos pos)
    {
        if (axe != null)
        {
            currentAxeDamage += logDamageMultiplier;

            for (int i = 0; i < (int) Math.floor(currentAxeDamage); i++)
                axe.getItem().onBlockDestroyed(axe, world, block, pos, player);

            currentAxeDamage -= Math.floor(currentAxeDamage);

            if ((axe != null) && (axe.stackSize < 1))
                player.destroyCurrentEquippedItem();
        }
        return !TCSettings.needItem || TCSettings.allowMoreBlocksThanDamage || isAxeItemEquipped();
    }

    /**
     * Damages the shear-type item and returns true if we should continue shearing leaves/vines
     */
    private boolean damageShearsAndContinue(World world, Block block, BlockPos pos)
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
                    shears.getItem().onBlockDestroyed(shears, world, block, pos, player);

            currentShearsDamage -= Math.floor(currentShearsDamage);

            if ((shears != null) && (shears.stackSize < 1) && (shearsIndex != -1))
                player.inventory.setInventorySlotContents(shearsIndex, null);
        }
        return TCSettings.allowMoreBlocksThanDamage || hasShearsInHotbar(player);
    }

    private LinkedList<BlockPos> addLogs(World world, BlockPos pos)
    {
        int index = 0, lowY = pos.getY(), x, y, z;
        LinkedList<BlockPos> list = new LinkedList<BlockPos>();
        BlockPos newPos;

        list.add(pos);

        do
        {
            BlockPos currentLog = list.get(index);

            for (x = -1; x <= 1; x++)
                for (y = (treeDef.onlyDestroyUpwards() ? 0 : -1); y <= 1; y++)
                    for (z = -1; z <= 1; z++)
                    {
                        newPos = currentLog.add(x, y, z);
                        if (!world.isAirBlock(newPos))
                            if (treeDef.getLogList().contains(new BlockID(world, newPos)))
                            {
                                if ((treeDef.maxHorLogBreakDist() == -1) || (((Math.abs(newPos.getX() - startPos.getX()) <= treeDef.maxHorLogBreakDist())
                                        && (Math.abs(newPos.getZ() - startPos.getZ()) <= treeDef.maxHorLogBreakDist()))
                                        && ((treeDef.maxVerLogBreakDist() == -1) || (Math.abs(newPos.getY() - startPos.getY()) <= treeDef.maxVerLogBreakDist()))
                                        && !list.contains(newPos) && ((newPos.getY() >= lowY) || !treeDef.onlyDestroyUpwards())))
                                {
                                    list.add(newPos);
                                    if ((TCSettings.maxNumberOfBlocksInTree != -1) && (++numLogsToBreak > TCSettings.maxNumberOfBlocksInTree))
                                    {
                                        list.clear();
                                        TCLog.debug("Number of logs in tree is more than the maximum number allowed.");
                                        maxAllowed = true;
                                        return list;
                                    }
                                }
                            }
                    }
        }
        while (++index < list.size());

        if (list.contains(pos))
            list.remove(pos);

        return list;
    }

    private void addLogsAbove(World world, BlockPos position, List<BlockPos> listFinal)
    {
        List<BlockPos> listAbove = new ArrayList<BlockPos>();
        List<BlockPos> list;
        BlockPos newPosition;
        int counter, index, x, z;

        listAbove.add(position);

        do
        {
            list = listAbove;
            listAbove = new ArrayList<BlockPos>();

            for (BlockPos pos : list)
            {
                counter = 0;
                for (x = -1; x <= 1; x++)
                    for (z = -1; z <= 1; z++)
                    {
                        newPosition = pos.add(x, 1, z);
                        if (treeDef.getLogList().contains(new BlockID(world, newPosition)))
                        {
                            if (!listAbove.contains(newPosition))
                                listAbove.add(newPosition);

                            counter++;
                        }
                    }

                if (counter == 0)
                    listFinal.add(pos);
            }

            index = -1;
            while (++index < listAbove.size())
            {
                BlockPos pos = listAbove.get(index);
                for (x = -1; x <= 1; x++)
                    for (z = -1; z <= 1; z++)
                    {
                        newPosition = pos.add(x, 0, z);
                        if (treeDef.getLogList().contains(new BlockID(world, newPosition)) && !listAbove.contains(newPosition))
                            listAbove.add(newPosition);
                    }
            }
        }
        while (listAbove.size() > 0);
    }

    public List<BlockPos> addLeaves(World world, BlockPos pos, List<BlockPos> list)
    {
        int index = -1;

        if (list == null)
            list = new ArrayList<BlockPos>();

        addLeavesInDistance(world, pos, treeDef.maxHorLeafBreakDist(), list);

        while (++index < list.size())
        {
            BlockPos pos2 = list.get(index);
            if ((treeDef.maxHorLeafBreakDist() == -1) || (CommonUtils.getHorSquaredDistance(pos, pos2) <= treeDef.maxHorLeafBreakDist()))
                addLeavesInDistance(world, pos2, 1, list);
        }

        return list;
    }

    public void addLeavesInDistance(World world, BlockPos pos, int range, List<BlockPos> list)
    {
        if (range == -1)
            range = 4;

        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range; y++)
                for (int z = -range; z <= range; z++)
                {
                    BlockPos newPos = pos.add(x, y, z);
                    if (world.isAirBlock(newPos))
                        continue;
                    IBlockState state = world.getBlockState(newPos);
                    Block block = state.getBlock();
                    int md = block.getMetaFromState(state);
                    if ((isLeafBlock(new BlockID(block, md)) || vineID.equals(new BlockID(block)))
                            && (!treeDef.requireLeafDecayCheck() || (((md & 8) != 0) && ((md & 4) == 0)))
                            && !list.contains(newPos) && !hasLogClose(world, newPos, 1))
                        list.add(newPos);
                }
    }

    /**
     * Returns true if a log block is within i blocks of pos
     */
    public boolean hasLogClose(World world, BlockPos pos, int i)
    {
        for (int x = -i; x <= i; x++)
            for (int y = -i; y <= i; y++)
                for (int z = -i; z <= i; z++)
                {
                    BlockPos neighborPos = pos.add(x, y, z);
                    if (((x != 0) || (y != 0) || (z != 0)) && !world.isAirBlock(neighborPos) &&
                            treeDef.getLogList().contains(new BlockID(world, neighborPos))
                            && !neighborPos.equals(startPos))
                        return true;
                }

        return false;
    }
}
