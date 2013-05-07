package bspkrs.treecapitator;

import bspkrs.util.Configuration;
import bspkrs.util.Const;

public class Strings
{
    public final static String VERSION_NUMBER        = Const.MCVERSION + ".r01";
    
    public static final String OAK                   = "vanilla_oak";
    public static final String SPRUCE                = "vanilla_spruce";
    public static final String BIRCH                 = "vanilla_birch";
    public static final String JUNGLE                = "vanilla_jungle";
    public static final String MUSH_BROWN            = "vanilla_huge_brown_mushroom";
    public static final String MUSH_RED              = "vanilla_huge_red_mushroom";
    
    public static final String LOGS                  = "logs";
    public static final String LOG_VALS              = "logValues";
    public static final String LEAVES                = "leaves";
    public static final String LEAF_VALS             = "leafValues";
    public static final String MOD_ID                = "modID";
    public static final String CONFIG_PATH           = "configPath";
    public static final String BLOCK_VALUES          = "blockValues";
    public static final String ITEM_VALUES           = "itemValues";
    public static final String axeIDList             = "axeIDList";
    public static final String shearIDList           = "shearIDList";
    public static final String SHIFT_INDEX           = "useShiftedItemID";
    public static final String IDR_MOD_ID            = "idResolverModID";
    @Deprecated
    public static final String TREE_BLOCK_CTGY       = "2_tree_definitions";
    @Deprecated
    public static final String THIRD_PARTY_CFG_CTGY  = "1_third_party_configs";
    public static final String BLOCK_CTGY            = "block_settings";
    public static final String ITEM_CTGY             = "item_settings";
    public static final String LEAF_CTGY             = "leaf_and_vine_settings";
    public static final String MISC_CTGY             = "miscellaneous_settings";
    public static final String ID_RES_CTGY           = "id_resolver_settings";
    public static final String GENERAL               = Configuration.CATEGORY_GENERAL;
    public static final String onlyDestroyUpwards    = "onlyDestroyUpwards";
    public static final String requireLeafDecayCheck = "requireLeafDecayCheck";
    public static final String maxLogBreakDist       = "maxLogBreakDist";
    public static final String maxLeafIDDist         = "maxLeafIDDist";
    public static final String maxLeafBreakDist      = "maxLeafBreakDist";
    public static final String minLeavesToID         = "minLeavesToID";
    public static final String breakSpeedModifier    = "breakSpeedModifier";
    public static final String id                    = "id";
    public static final String metadata              = "metadata";
    
    public final static String maxBreakDistanceDesc  = "The maximum horizontal distance that the log breaking effect will travel (use -1 for no limit).";
    
}
