package bspkrs.treecapitator.compat;

import bspkrs.util.CommonUtils;

public class IDResolverMapping
{
    public int    oldID;
    public int    newID;
    public String modClassName;
    public String idType;
    
    public IDResolverMapping(String configLine)
    {
        //@formatter:off
        /*
         * #IDResolver Known / Set IDs file. Please do not edit manually. 
         * SAVEVERSION=v2
         * ItemID.30080|ic2.core.IC2=30080 
         * BlockID.3271|inficraft.infiblocks.InfiBlocks=3271
         * ItemID.5595|xolova.blued00r.divinerpg.DivineRPG=31676 
         * ItemID.13287|forestry.Forestry=13287
         * BlockID.2205|extrabiomes.Extrabiomes=2205 
         * BlockID.624|xolova.blued00r.divinerpg.DivineRPG=3881
         *
         * @formatter:on
         */
        String[] entry = configLine.split("\\|");
        String[] oldSide = entry[0].split("\\.");
        String[] newSide = entry[1].split("=");
        
        oldID = CommonUtils.parseInt(oldSide[1].trim(), 0);
        newID = CommonUtils.parseInt(newSide[1].trim(), 0);
        modClassName = newSide[0].trim();
        idType = oldSide[0].trim();
    }
    
    public boolean isStaticMapping()
    {
        return oldID == newID;
    }
}
