package bspkrs.treecapitator.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.util.Reference;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.ItemID;
import bspkrs.util.ListUtils;

public class ToolRegistry
{
    private static ToolRegistry instance;

    public static ToolRegistry instance()
    {
        if (instance == null)
            new ToolRegistry();

        return instance;
    }

    // Registry tool lists
    private List<ItemID> axeList;
    private List<ItemID> shearsList;

    // Vanilla tool lists
    private List<ItemID> vanAxeList;
    private List<ItemID> vanShearsList;

    private List<ItemID> blacklist;

    protected ToolRegistry()
    {
        instance = this;

        initLists();
        initVanillaItemLists();
    }

    protected void initLists()
    {
        axeList = new ArrayList<ItemID>();
        shearsList = new ArrayList<ItemID>();
        readBlacklistFromDelimitedString(TCSettings.itemIDBlacklist);
    }

    protected void initVanillaLists()
    {
        vanAxeList = new ArrayList<ItemID>();
        vanShearsList = new ArrayList<ItemID>();
    }

    protected void initVanillaItemLists()
    {
        initVanillaLists();
        vanAxeList.add(new ItemID(Items.wooden_axe));
        vanAxeList.add(new ItemID(Items.stone_axe));
        vanAxeList.add(new ItemID(Items.iron_axe));
        vanAxeList.add(new ItemID(Items.golden_axe));
        vanAxeList.add(new ItemID(Items.diamond_axe));

        vanShearsList.add(new ItemID(Items.shears));
    }

    public static synchronized void autoDetectAxe(World world, BlockPos pos, ItemStack item)
    {
        if ((item != null) && (item.getItem() != null) && ForgeHooks.isToolEffective(world, pos, item))
        {
            ItemID axe = new ItemID(item);
            if (!instance.isAxe(item))
                TCLog.debug("Auto Axe Detection: Attempting to register axe %s", axe);
            if (instance.registerAxe(axe))
            {
                int index = axe.id.indexOf(":");
                String modID = index == -1 ? Reference.MINECRAFT : axe.id.substring(0, index);
                ModConfigRegistry.instance().appendAxeToModConfig(modID, axe);
            }
        }
    }

    public List<ItemID> blacklist()
    {
        return new ArrayList<ItemID>(blacklist);
    }

    // This must be done after all trees are registered to avoid screwing up the registration process
    public void readBlacklistFromDelimitedString(String dList)
    {
        blacklist = ListUtils.getDelimitedStringAsItemIDList(dList, ";");
    }

    protected void readFromNBT(NBTTagCompound ntc)
    {
        axeList = ListUtils.getDelimitedStringAsItemIDList(ntc.getString(Reference.AXE_ID_LIST), ";");
        shearsList = ListUtils.getDelimitedStringAsItemIDList(ntc.getString(Reference.SHEARS_ID_LIST), ";");
        blacklist = ListUtils.getDelimitedStringAsItemIDList(ntc.getString(Reference.BLACKLIST), ";");
    }

    public void writeToNBT(NBTTagCompound ntc)
    {
        ntc.setString(Reference.AXE_ID_LIST, ListUtils.getListAsDelimitedString(axeList, ";"));
        ntc.setString(Reference.SHEARS_ID_LIST, ListUtils.getListAsDelimitedString(shearsList, ";"));
        ntc.setString(Reference.BLACKLIST, ListUtils.getListAsDelimitedString(blacklist, ";"));
    }

    public synchronized boolean registerAxe(ItemID axe)
    {
        if ((axe != null) && !blacklist.contains(axe) && !axeList.contains(axe))
        {
            axeList.add(axe);
            TCLog.debug("ToolRegistry: Successfully registered axe item %s", axe);
            return true;
        }
        else if (blacklist.contains(axe))
            TCLog.debug("ToolRegistry: Item %s is on the blacklist and will not be registered as an axe", axe);

        return false;
    }

    public synchronized boolean registerShears(ItemID shears)
    {
        if ((shears != null) && !blacklist.contains(shears) && !shearsList.contains(shears))
        {
            shearsList.add(shears);
            TCLog.debug("ToolRegistry: Successfully registered shears item %s", shears);
            return true;
        }
        else if (blacklist.contains(shears))
            TCLog.debug("ToolRegistry: Item %s is on the blacklist and will not be registered as shears", shears);

        return false;
    }

    public List<ItemID> axeList()
    {
        return new ArrayList<ItemID>(axeList);
    }

    public List<ItemID> shearsList()
    {
        return new ArrayList<ItemID>(shearsList);
    }

    public List<ItemID> vanillaAxeList()
    {
        return new ArrayList<ItemID>(vanAxeList);
    }

    public List<ItemID> vanillaShearsList()
    {
        return new ArrayList<ItemID>(vanShearsList);
    }

    public boolean isAxe(ItemStack itemStack)
    {
        if ((itemStack != null) && (itemStack.getItem() != null))
        {
            ItemID itemID = new ItemID(itemStack);
            return !blacklist.contains(itemID) && axeList.contains(itemID);
        }
        else
            return false;
    }

    public boolean isShears(ItemStack itemStack)
    {
        if ((itemStack != null) && (itemStack.getItem() != null))
        {
            ItemID itemID = new ItemID(itemStack);
            return !blacklist.contains(itemID) && shearsList.contains(itemID);
        }
        else
            return false;
    }
}
