package bspkrs.treecapitator;

import java.util.ArrayList;
import java.util.List;

import bspkrs.util.ItemID;

public class LawnToolRegistry
{
    private static LawnToolRegistry instance;
    
    public static LawnToolRegistry instance()
    {
        if (instance == null)
            new LawnToolRegistry();
        
        return instance;
    }
    
    private List<ItemID> axeList;
    private List<ItemID> shearsList;
    // mo' pimpin'
    private List<ItemID> hoeList;
    
    protected LawnToolRegistry()
    {
        instance = this;
        
        axeList = new ArrayList<ItemID>();
        shearsList = new ArrayList<ItemID>();
        hoeList = new ArrayList<ItemID>();
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
    
    public boolean isAxe(ItemID item)
    {
        return axeList.contains(item);
    }
    
    public boolean isShears(ItemID item)
    {
        return shearsList.contains(item);
    }
    
    public boolean isHoe(ItemID item)
    {
        return hoeList.contains(item);
    }
    
    // The hoe list isn't used for anything, but what lawn tool registry is complete without ho'z?
    public void registerHoe(ItemID hoe)
    {
        if (!hoeList.contains(hoe))
            hoeList.add(hoe);
    }
}
