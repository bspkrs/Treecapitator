package bspkrs.treecapitator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTextureTile;
import bspkrs.util.BlockID;
import bspkrs.util.CommonUtils;
import bspkrs.util.ListUtils;

public final class TCSettings
{
    
    public static String                               idResolverModID                = "IDResolver";
    public static String                               multiMineID                    = "AS_MultiMine";
    public static boolean                              enableEnchantmentMode          = false;
    public static boolean                              requireItemInAxeListForEnchant = false;
    public static String                               axeIDList                      = ListUtils.getListAsDelimitedString(ToolRegistry.instance().vanillaAxeList(), "; ");
    public static boolean                              needItem                       = true;
    public static boolean                              onlyDestroyUpwards             = true;
    public static boolean                              destroyLeaves                  = true;
    public static boolean                              requireLeafDecayCheck          = true;
    public static boolean                              shearLeaves                    = false;
    public static boolean                              shearVines                     = false;
    public static String                               shearIDList                    = ListUtils.getListAsDelimitedString(ToolRegistry.instance().vanillaShearsList(), "; ");
    public static float                                logHardnessNormal              = 2.0F;
    public static float                                logHardnessModified            = 4.0F;
    public static float                                breakSpeedModifier             = 0.3F;
    public static boolean                              disableInCreative              = false;
    public static boolean                              disableCreativeDrops           = false;
    public static boolean                              allowItemDamage                = true;
    public static boolean                              allowMoreBlocksThanDamage      = false;
    public static float                                damageMultiplier               = 1.0F;
    public static boolean                              useIncreasingItemDamage        = false;
    public static int                                  increaseDamageEveryXBlocks     = 15;
    public static float                                damageIncreaseAmount           = 1.0F;
    public static String                               sneakAction                    = "disable";
    public static int                                  maxHorLogBreakDist             = 16;
    public static int                                  maxVerLogBreakDist             = -1;
    public static boolean                              allowSmartTreeDetection        = true;
    public static int                                  maxLeafIDDist                  = 1;
    public static int                                  maxLeafBreakDist               = 4;
    public static int                                  minLeavesToID                  = 3;
    public static boolean                              useStrictBlockPairing          = true;
    public static boolean                              userConfigOverridesIMC         = false;
    public static boolean                              allowDebugOutput               = false;
    public static boolean                              allowDebugLogging              = false;
    
    public static boolean                              isForge                        = false;
    public static Block                                wood;
    
    @Deprecated
    public static ArrayList<BlockID>                   logIDList                      = new ArrayList<BlockID>();
    @Deprecated
    public static ArrayList<BlockID>                   leafIDList                     = new ArrayList<BlockID>();
    @Deprecated
    public static Map<BlockID, ArrayList<BlockID>>     logToLeafListMap               = new HashMap<BlockID, ArrayList<BlockID>>();
    @Deprecated
    public static Map<BlockID, ArrayList<BlockID>>     logToLogListMap                = new HashMap<BlockID, ArrayList<BlockID>>();
    @Deprecated
    public static Map<String, HashMap<String, String>> configBlockList                = new HashMap<String, HashMap<String, String>>();
    @Deprecated
    public static Map<String, HashMap<String, String>> thirdPartyConfig               = new HashMap<String, HashMap<String, String>>();
    @Deprecated
    public static Map<String, String>                  tagMap                         = new HashMap<String, String>();
    
    @Deprecated
    public static String getStringFromConfigBlockList()
    {
        String list = "";
        for (HashMap<String, String> group : configBlockList.values())
            list += " ! " + group.get(Strings.LOGS) + (group.containsKey(Strings.LEAVES) ? "|" + group.get(Strings.LEAVES) : "");
        return replaceThirdPartyBlockTags(list.replaceFirst(" ! ", ""));
    }
    
    //    public static String getRemoteConfig()
    //    {
    //        if (isForge && allowGetRemoteTreeConfig)
    //        {
    //            try
    //            {
    //                return CommonUtils.loadTextFromURL(new URL(Strings.remoteTreeConfigURL), TCLog.INSTANCE.getLogger(), Strings.remoteBlockIDConfig)[0];
    //            }
    //            catch (Throwable e)
    //            {
    //                TCLog.warning("Error retrieving remote tree config! Defaulting to cached copy if available or local config.");
    //            }
    //        }
    //        return Strings.remoteBlockIDConfig;
    //    }
    
    @Deprecated
    public static String getStringFromParsedLists()
    {
        String list = "";
        List<ArrayList<BlockID>> processed = new ArrayList<ArrayList<BlockID>>();
        
        for (BlockID key : logIDList)
        {
            String logPart = "";
            
            if (!processed.contains(logToLogListMap.get(key)))
            {
                processed.add(logToLogListMap.get(key));
                
                for (BlockID logID : logToLogListMap.get(key))
                    logPart += "; " + logID.id + (logID.metadata != -1 ? "," + logID.metadata : "");
                logPart = logPart.replaceFirst("; ", "");
                
                if (logPart.trim().length() > 0)
                {
                    String leafPart = "";
                    
                    for (BlockID leafID : logToLeafListMap.get(key))
                        leafPart += "; " + leafID.id + (leafID.metadata != -1 ? "," + leafID.metadata : "");
                    leafPart = leafPart.replaceFirst("; ", "");
                    
                    list += " ! " + logPart + " | " + leafPart;
                }
            }
        }
        return list.replaceFirst(" ! ", "");
    }
    
    @Deprecated
    public static void parseConfigBlockList(String list)
    {
        logIDList = new ArrayList<BlockID>();
        leafIDList = new ArrayList<BlockID>();
        logToLogListMap = new HashMap<BlockID, ArrayList<BlockID>>();
        logToLeafListMap = new HashMap<BlockID, ArrayList<BlockID>>();
        
        TCLog.debug("Parsing Tree Block Config string: %s", list);
        
        if (list.trim().length() > 0)
        {
            String[] entries = list.trim().split("!");
            for (String entry : entries)
            {
                if (entry.trim().length() > 0)
                {
                    TCLog.debug("  Parsing Tree entry: %s", entry);
                    if (entry.trim().length() > 0)
                    {
                        String[] blockTypes = entry.trim().split("\\|");
                        
                        // parse log ids [0]
                        ArrayList<BlockID> logIDs = new ArrayList<BlockID>();
                        String[] logBlocks = blockTypes[0].trim().split(";");
                        
                        TCLog.debug("    Found log ID list: %s", blockTypes[0].trim());
                        
                        for (String logBlockStr : logBlocks)
                        {
                            String[] logBlock = logBlockStr.trim().split(",");
                            
                            TCLog.debug("    Found log ID: %s", logBlockStr);
                            int blockID = CommonUtils.parseInt(logBlock[0].trim(), -1);
                            
                            if (blockID != -1)
                            {
                                int metadata = -1;
                                
                                if (logBlock.length > 1)
                                    metadata = CommonUtils.parseInt(logBlock[1].trim(), -1);
                                TCLog.debug("    ++Configured log: %s, %s", blockID, metadata);
                                
                                BlockID logID = new BlockID(blockID, metadata);
                                if (!logIDList.contains(logID))
                                {
                                    logIDList.add(logID);
                                    logIDs.add(logID);
                                }
                            }
                            else
                                TCLog.debug("Block ID %s could not be parsed as an integer.  Ignoring entry.", logBlock[0].trim());
                        }
                        
                        for (BlockID logID : logIDs)
                            logToLogListMap.put(logID, logIDs);
                        
                        ArrayList<BlockID> pairedLeaves = new ArrayList<BlockID>();
                        
                        // parse leaf ids [1]
                        if (blockTypes.length > 1)
                        {
                            String[] leafBlocks = blockTypes[1].trim().split(";");
                            
                            TCLog.debug("    Found leaf ID list: %s", blockTypes[1].trim());
                            
                            for (String block : leafBlocks)
                            {
                                if (block.trim().length() > 0)
                                {
                                    TCLog.debug("    Found leaf ID: %s", block.trim());
                                    String[] leafBlock = block.trim().split(",");
                                    int blockID = CommonUtils.parseInt(leafBlock[0].trim(), -1);
                                    
                                    if (blockID != -1)
                                    {
                                        int metadata = -1;
                                        
                                        if (leafBlock.length > 1)
                                            metadata = CommonUtils.parseInt(leafBlock[1].trim(), -1);
                                        
                                        TCLog.debug("    ++Configured leaf: %s, %s", blockID, metadata);
                                        
                                        BlockID leafID = new BlockID(blockID, metadata);
                                        if (!leafIDList.contains(leafID))
                                            leafIDList.add(leafID);
                                        
                                        if (!pairedLeaves.contains(leafID))
                                            pairedLeaves.add(leafID);
                                    }
                                    else
                                        TCLog.debug("Block ID %s could not be parsed as an integer.  Ignoring entry.", leafBlock[0].trim());
                                }
                            }
                        }
                        
                        for (BlockID logID : logIDs)
                            if (!logToLeafListMap.containsKey(logID))
                                logToLeafListMap.put(logID, pairedLeaves);
                    }
                }
            }
        }
        TCLog.info("Block ID list parsing complete.");
    }
    
    //    @Deprecated
    //    public static boolean isLogBlock(BlockID blockID)
    //    {
    //        return logIDList.contains(blockID);
    //    }
    
    public static void preInit()
    {
        preInit(false);
    }
    
    public static void preInit(boolean isForgeVersion)
    {
        isForge = isForgeVersion;
        
        if (!isForge)
        {
            Block.blocksList[Block.wood.blockID] = null;
            wood = new BlockTree(Block.wood.blockID);
            Block.blocksList[wood.blockID] = wood;
            Item.itemsList[wood.blockID] = null;
            Item.itemsList[wood.blockID] = (new ItemMultiTextureTile(wood.blockID - 256, wood, BlockLog.woodType)).setUnlocalizedName("log");
            
            //**logIDList.add(new BlockID(wood.blockID));
            TreeRegistry.instance().registerVanillaTreeDefs();
        }
        else
        {}
    }
    
    @Deprecated
    public static String replaceThirdPartyBlockTags(String input)
    {
        for (String tag : tagMap.keySet())
            input = input.replace(tag, tagMap.get(tag));
        
        return input;
    }
}
