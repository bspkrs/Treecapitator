package bspkrs.treecapitator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import bspkrs.util.ItemID;

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
    }
    
    protected void initVanillaLists()
    {
        vanAxeList = new ArrayList<ItemID>();
        vanShearsList = new ArrayList<ItemID>();
    }
    
    protected void initVanillaItemLists()
    {
        initVanillaLists();
        vanAxeList.add(new ItemID(Item.axeWood));
        vanAxeList.add(new ItemID(Item.axeStone));
        vanAxeList.add(new ItemID(Item.axeIron));
        vanAxeList.add(new ItemID(Item.axeGold));
        vanAxeList.add(new ItemID(Item.axeDiamond));
        
        vanShearsList.add(new ItemID(Item.shears));
    }
    
    protected void readFromNBT(NBTTagCompound ntc)
    {
        // TODO
    }
    
    protected void writeToNBT(NBTTagCompound ntc)
    {
        // TODO
    }
    
    public void registerAxe(ItemID axe)
    {
        if (!axeList.contains(axe))
            axeList.add(axe);
    }
    
    public void registerShears(ItemID shears)
    {
        if (!shearsList.contains(shears))
            shearsList.add(shears);
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
    
    public boolean isAxe(ItemID itemID)
    {
        return axeList.contains(itemID);
    }
    
    public boolean isAxe(Item item)
    {
        return axeList.contains(new ItemID(item));
    }
    
    public boolean isAxe(ItemStack itemStack)
    {
        return axeList.contains(new ItemID(itemStack));
    }
    
    public boolean isShears(ItemID itemID)
    {
        return shearsList.contains(itemID);
    }
    
    public boolean isShears(Item item)
    {
        return shearsList.contains(new ItemID(item));
    }
    
    public boolean isShears(ItemStack itemStack)
    {
        return shearsList.contains(new ItemID(itemStack));
    }
}
