package bspkrs.treecapitator.registry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import bspkrs.treecapitator.config.TCSettings;
import bspkrs.treecapitator.util.Reference;
import bspkrs.treecapitator.util.TCLog;
import bspkrs.util.BlockID;
import bspkrs.util.ListUtils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class TreeDefinition
{
    protected List<BlockID>     logBlocks;
    protected List<BlockID>     leafBlocks;
    protected boolean           allowSmartTreeDetection;
    protected boolean           onlyDestroyUpwards;
    protected boolean           requireLeafDecayCheck;
    // max horizontal distance that logs will be broken
    protected int               maxHorLogBreakDist;
    protected int               maxVerLogBreakDist;
    protected int               maxLeafIDDist;
    protected int               maxHorLeafBreakDist;
    protected int               minLeavesToID;
    protected float             breakSpeedModifier;
    protected boolean           useAdvancedTopLogLogic;
    private static List<String> orderedKeys = new ArrayList<String>();
    private static Set<String>  validKeys   = new HashSet<String>();

    public TreeDefinition()
    {
        logBlocks = new ArrayList<BlockID>();
        leafBlocks = new ArrayList<BlockID>();

        allowSmartTreeDetection = TCSettings.allowSmartTreeDetection;
        onlyDestroyUpwards = TCSettings.onlyDestroyUpwards;
        requireLeafDecayCheck = TCSettings.requireLeafDecayCheck;
        maxHorLogBreakDist = TCSettings.maxHorLogBreakDist;
        maxVerLogBreakDist = TCSettings.maxVerLogBreakDist;
        maxLeafIDDist = TCSettings.maxLeafIDDist;
        maxHorLeafBreakDist = TCSettings.maxHorLeafBreakDist;
        minLeavesToID = TCSettings.minLeavesToID;
        breakSpeedModifier = TCSettings.breakSpeedModifier;
        useAdvancedTopLogLogic = TCSettings.useAdvancedTopLogLogic;
    }

    @Override
    public String toString()
    {
        return "Logs: " + ListUtils.getListAsDelimitedString(logBlocks, "; ") + "  Leaves: " + ListUtils.getListAsDelimitedString(leafBlocks, "; ");
    }

    public TreeDefinition(List<BlockID> logs, List<BlockID> leaves)
    {
        this();
        logBlocks.addAll(logs);
        leafBlocks.addAll(leaves);
    }

    public TreeDefinition(NBTTagCompound tree)
    {
        this();
        this.readFromNBT(tree);
    }

    public TreeDefinition(Configuration config, String category)
    {
        this();
        readFromConfiguration(config, category);
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof TreeDefinition))
            return false;

        if (o == this)
            return true;

        TreeDefinition td = (TreeDefinition) o;
        return td.logBlocks.equals(logBlocks) && td.leafBlocks.equals(leafBlocks);

    }

    @Override
    public int hashCode()
    {
        HashFunction hf = Hashing.md5();
        Hasher h = hf.newHasher();
        for (BlockID blockID : logBlocks)
            h.putInt(blockID.hashCode());
        for (BlockID blockID : leafBlocks)
            h.putInt(blockID.hashCode() << 8);

        return h.hash().hashCode();
    }

    public boolean hasCommonLog(TreeDefinition td)
    {
        for (BlockID blockID : td.logBlocks)
            if (logBlocks.contains(blockID))
                return true;

        return false;
    }

    public boolean isLogBlock(BlockID blockID)
    {
        return logBlocks.contains(blockID);
    }

    public boolean isLeafBlock(BlockID blockID)
    {
        return leafBlocks.contains(blockID);
    }

    public TreeDefinition addLogID(BlockID blockID)
    {
        if (!isLogBlock(blockID))
            logBlocks.add(blockID);

        return this;
    }

    public TreeDefinition addLeafID(BlockID blockID)
    {
        if (!isLeafBlock(blockID))
            leafBlocks.add(blockID);

        return this;
    }

    public TreeDefinition addAllLogIDs(List<BlockID> blockIDs)
    {
        for (BlockID blockID : blockIDs)
            if (!isLogBlock(blockID))
                logBlocks.add(blockID);

        return this;
    }

    public TreeDefinition addAllLeafIDs(List<BlockID> blockIDs)
    {
        for (BlockID blockID : blockIDs)
            if (!isLeafBlock(blockID))
                leafBlocks.add(blockID);

        return this;
    }

    public TreeDefinition append(TreeDefinition toAdd)
    {
        for (BlockID blockID : toAdd.logBlocks)
            if (!logBlocks.contains(blockID))
                logBlocks.add(blockID);

        for (BlockID blockID : toAdd.leafBlocks)
            if (!leafBlocks.contains(blockID))
                leafBlocks.add(blockID);

        return this;
    }

    public TreeDefinition appendWithSettings(TreeDefinition toAdd)
    {
        append(toAdd);

        if (toAdd.allowSmartTreeDetection != TCSettings.allowSmartTreeDetection)
            allowSmartTreeDetection = toAdd.allowSmartTreeDetection;
        if (toAdd.onlyDestroyUpwards != TCSettings.onlyDestroyUpwards)
            onlyDestroyUpwards = toAdd.onlyDestroyUpwards;
        if (toAdd.requireLeafDecayCheck != TCSettings.requireLeafDecayCheck)
            requireLeafDecayCheck = toAdd.requireLeafDecayCheck;
        if (toAdd.maxHorLogBreakDist != TCSettings.maxHorLogBreakDist)
            maxHorLogBreakDist = toAdd.maxHorLogBreakDist;
        if (toAdd.maxHorLeafBreakDist != TCSettings.maxHorLeafBreakDist)
            maxHorLeafBreakDist = toAdd.maxHorLeafBreakDist;
        if (toAdd.maxLeafIDDist != TCSettings.maxLeafIDDist)
            maxLeafIDDist = toAdd.maxLeafIDDist;
        if (toAdd.minLeavesToID != TCSettings.minLeavesToID)
            minLeavesToID = toAdd.minLeavesToID;
        if (toAdd.breakSpeedModifier != TCSettings.breakSpeedModifier)
            breakSpeedModifier = toAdd.breakSpeedModifier;
        if (toAdd.useAdvancedTopLogLogic != TCSettings.useAdvancedTopLogLogic)
            useAdvancedTopLogLogic = toAdd.useAdvancedTopLogLogic;

        return this;
    }

    @SuppressWarnings("unchecked")
    public static boolean isValidNBT(NBTTagCompound treeDefNBT)
    {
        for (String s : (Set<String>) treeDefNBT.getKeySet())
            if (!validKeys.contains(s))
                TCLog.warning("Unknown tag \"%s\" found while verifying a TreeDefinition NBTTagCompound object", s);

        if (!treeDefNBT.hasKey(Reference.TREE_NAME))
        {
            TCLog.severe("TreeDefinition NBTTagCompound objects must contain a string tag with the key \"%s\"", Reference.TREE_NAME);
            return false;
        }

        if (!(treeDefNBT.hasKey(Reference.LOGS) || treeDefNBT.hasKey(Reference.LEAVES)))
        {
            TCLog.severe("TreeDefinition NBTTagCompound objects must contain at least one string tag with the key \"%s\" or \"%s\"", Reference.LOGS, Reference.LEAVES);
            return false;
        }

        return true;
    }

    public TreeDefinition readFromNBT(NBTTagCompound treeDefNBT)
    {
        if (treeDefNBT.hasKey(Reference.ALLOW_SMART_TREE_DETECT))
            allowSmartTreeDetection = treeDefNBT.getBoolean(Reference.ALLOW_SMART_TREE_DETECT);
        if (treeDefNBT.hasKey(Reference.ONLY_DESTROY_UPWARDS))
            onlyDestroyUpwards = treeDefNBT.getBoolean(Reference.ONLY_DESTROY_UPWARDS);
        if (treeDefNBT.hasKey(Reference.REQ_DECAY_CHECK))
            requireLeafDecayCheck = treeDefNBT.getBoolean(Reference.REQ_DECAY_CHECK);
        if (treeDefNBT.hasKey(Reference.MAX_H_LOG_DIST))
            maxHorLogBreakDist = treeDefNBT.getInteger(Reference.MAX_H_LOG_DIST);
        if (treeDefNBT.hasKey(Reference.MAX_V_LOG_DIST))
            maxVerLogBreakDist = treeDefNBT.getInteger(Reference.MAX_V_LOG_DIST);
        if (treeDefNBT.hasKey(Reference.MAX_H_LEAF_DIST))
            maxHorLeafBreakDist = treeDefNBT.getInteger(Reference.MAX_H_LEAF_DIST);
        if (treeDefNBT.hasKey(Reference.MAX_LEAF_ID_DIST))
            maxLeafIDDist = treeDefNBT.getInteger(Reference.MAX_LEAF_ID_DIST);
        if (treeDefNBT.hasKey(Reference.MIN_LEAF_ID))
            minLeavesToID = treeDefNBT.getInteger(Reference.MIN_LEAF_ID);
        if (treeDefNBT.hasKey(Reference.BREAK_SPEED_MOD))
            breakSpeedModifier = treeDefNBT.getFloat(Reference.BREAK_SPEED_MOD);
        if (treeDefNBT.hasKey(Reference.USE_ADV_TOP_LOG_LOGIC))
            useAdvancedTopLogLogic = treeDefNBT.getBoolean(Reference.USE_ADV_TOP_LOG_LOGIC);

        if (treeDefNBT.hasKey(Reference.LOGS) && (treeDefNBT.getString(Reference.LOGS).length() > 0))
            logBlocks = ListUtils.getDelimitedStringAsBlockIDList(treeDefNBT.getString(Reference.LOGS), ";");
        else
            logBlocks = new ArrayList<BlockID>();

        if (treeDefNBT.hasKey(Reference.LEAVES) && (treeDefNBT.getString(Reference.LEAVES).length() > 0))
            leafBlocks = ListUtils.getDelimitedStringAsBlockIDList(treeDefNBT.getString(Reference.LEAVES), ";");
        else
            leafBlocks = new ArrayList<BlockID>();

        return this;
    }

    public void writeToNBT(NBTTagCompound treeDefNBT)
    {
        treeDefNBT.setBoolean(Reference.ALLOW_SMART_TREE_DETECT, allowSmartTreeDetection);
        treeDefNBT.setBoolean(Reference.ONLY_DESTROY_UPWARDS, onlyDestroyUpwards);
        treeDefNBT.setBoolean(Reference.REQ_DECAY_CHECK, requireLeafDecayCheck);
        treeDefNBT.setInteger(Reference.MAX_H_LOG_DIST, maxHorLogBreakDist);
        treeDefNBT.setInteger(Reference.MAX_V_LOG_DIST, maxVerLogBreakDist);
        treeDefNBT.setInteger(Reference.MAX_H_LEAF_DIST, maxHorLeafBreakDist);
        treeDefNBT.setInteger(Reference.MAX_LEAF_ID_DIST, maxLeafIDDist);
        treeDefNBT.setInteger(Reference.MIN_LEAF_ID, minLeavesToID);
        treeDefNBT.setFloat(Reference.BREAK_SPEED_MOD, breakSpeedModifier);
        treeDefNBT.setBoolean(Reference.USE_ADV_TOP_LOG_LOGIC, useAdvancedTopLogLogic);

        treeDefNBT.setString(Reference.LOGS, ListUtils.getListAsDelimitedString(logBlocks, ";"));
        treeDefNBT.setString(Reference.LEAVES, ListUtils.getListAsDelimitedString(leafBlocks, ";"));
    }

    public TreeDefinition readFromConfiguration(Configuration config, String category)
    {
        ConfigCategory cc = config.getCategory(category);

        if (cc.containsKey(Reference.ALLOW_SMART_TREE_DETECT))
            onlyDestroyUpwards = cc.get(Reference.ALLOW_SMART_TREE_DETECT)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.ALLOW_SMART_TREE_DETECT)
                    .getBoolean(TCSettings.allowSmartTreeDetection);
        if (cc.containsKey(Reference.ONLY_DESTROY_UPWARDS))
            onlyDestroyUpwards = cc.get(Reference.ONLY_DESTROY_UPWARDS)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.ONLY_DESTROY_UPWARDS)
                    .getBoolean(TCSettings.onlyDestroyUpwards);
        if (cc.containsKey(Reference.REQ_DECAY_CHECK))
            requireLeafDecayCheck = cc.get(Reference.REQ_DECAY_CHECK)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.REQ_DECAY_CHECK)
                    .getBoolean(TCSettings.requireLeafDecayCheck);
        if (cc.containsKey(Reference.MAX_H_LOG_DIST))
            maxHorLogBreakDist = cc.get(Reference.MAX_H_LOG_DIST)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.MAX_H_LOG_DIST)
                    .getInt(TCSettings.maxHorLogBreakDist);
        if (cc.containsKey(Reference.MAX_V_LOG_DIST))
            maxVerLogBreakDist = cc.get(Reference.MAX_V_LOG_DIST)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.MAX_V_LOG_DIST)
                    .getInt(TCSettings.maxVerLogBreakDist);
        if (cc.containsKey(Reference.MAX_H_LEAF_DIST))
            maxHorLeafBreakDist = cc.get(Reference.MAX_H_LEAF_DIST)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.MAX_H_LEAF_DIST)
                    .getInt(TCSettings.maxHorLeafBreakDist);
        if (cc.containsKey(Reference.MAX_LEAF_ID_DIST))
            maxLeafIDDist = cc.get(Reference.MAX_LEAF_ID_DIST)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.MAX_LEAF_ID_DIST)
                    .getInt(TCSettings.maxLeafIDDist);
        if (cc.containsKey(Reference.MIN_LEAF_ID))
            minLeavesToID = cc.get(Reference.MIN_LEAF_ID)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.MIN_LEAF_ID)
                    .getInt();
        if (cc.containsKey(Reference.BREAK_SPEED_MOD))
            breakSpeedModifier = (float) cc.get(Reference.BREAK_SPEED_MOD)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.BREAK_SPEED_MOD)
                    .getDouble(TCSettings.breakSpeedModifier);
        if (cc.containsKey(Reference.USE_ADV_TOP_LOG_LOGIC))
            useAdvancedTopLogLogic = cc.get(Reference.USE_ADV_TOP_LOG_LOGIC)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.USE_ADV_TOP_LOG_LOGIC)
                    .getBoolean(TCSettings.useAdvancedTopLogLogic);

        if (cc.containsKey(Reference.LOGS))
            logBlocks = ListUtils.getDelimitedStringAsBlockIDList(cc.get(Reference.LOGS)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.LOGS)
                    .getString(), "; ");
        if (cc.containsKey(Reference.LEAVES))
            leafBlocks = ListUtils.getDelimitedStringAsBlockIDList(cc.get(Reference.LEAVES)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.LEAVES)
                    .getString(), "; ");

        cc.setPropertyOrder(orderedKeys);

        return this;
    }

    public void writeToConfiguration(Configuration config, String category)
    {
        if (allowSmartTreeDetection != TCSettings.allowSmartTreeDetection)
        {
            config.get(category, Reference.ALLOW_SMART_TREE_DETECT, TCSettings.allowSmartTreeDetection, Reference.OPTIONAL)
                    .setValue(allowSmartTreeDetection)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.ALLOW_SMART_TREE_DETECT);
        }
        if (onlyDestroyUpwards != TCSettings.onlyDestroyUpwards)
        {
            config.get(category, Reference.ONLY_DESTROY_UPWARDS, TCSettings.onlyDestroyUpwards, Reference.OPTIONAL)
                    .setValue(onlyDestroyUpwards)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.ONLY_DESTROY_UPWARDS);
        }
        if (requireLeafDecayCheck != TCSettings.requireLeafDecayCheck)
        {
            config.get(category, Reference.REQ_DECAY_CHECK, TCSettings.requireLeafDecayCheck, Reference.OPTIONAL)
                    .setValue(requireLeafDecayCheck)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.REQ_DECAY_CHECK);
        }
        if (maxHorLogBreakDist != TCSettings.maxHorLogBreakDist)
        {
            config.get(category, Reference.MAX_H_LOG_DIST, TCSettings.maxHorLogBreakDist, Reference.OPTIONAL)
                    .setValue(maxHorLogBreakDist)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.MAX_H_LOG_DIST);
        }
        if (maxVerLogBreakDist != TCSettings.maxVerLogBreakDist)
        {
            config.get(category, Reference.MAX_V_LOG_DIST, TCSettings.maxVerLogBreakDist, Reference.OPTIONAL)
                    .setValue(maxVerLogBreakDist)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.MAX_V_LOG_DIST);
        }
        if (maxHorLeafBreakDist != TCSettings.maxHorLeafBreakDist)
        {
            config.get(category, Reference.MAX_H_LEAF_DIST, TCSettings.maxHorLeafBreakDist, Reference.OPTIONAL)
                    .setValue(maxHorLeafBreakDist)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.MAX_H_LEAF_DIST);
        }
        if (maxLeafIDDist != TCSettings.maxLeafIDDist)
        {
            config.get(category, Reference.MAX_LEAF_ID_DIST, TCSettings.maxLeafIDDist, Reference.OPTIONAL)
                    .setValue(maxLeafIDDist)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.MAX_LEAF_ID_DIST);
        }
        if (minLeavesToID != TCSettings.minLeavesToID)
        {
            config.get(category, Reference.MIN_LEAF_ID, TCSettings.minLeavesToID, Reference.OPTIONAL)
                    .setValue(minLeavesToID)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.MIN_LEAF_ID);
        }
        if (breakSpeedModifier != TCSettings.breakSpeedModifier)
        {
            config.get(category, Reference.BREAK_SPEED_MOD, TCSettings.breakSpeedModifier, Reference.OPTIONAL)
                    .setValue(breakSpeedModifier)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.BREAK_SPEED_MOD);
        }
        if (useAdvancedTopLogLogic != TCSettings.useAdvancedTopLogLogic)
        {
            config.get(category, Reference.USE_ADV_TOP_LOG_LOGIC, TCSettings.useAdvancedTopLogLogic, Reference.OPTIONAL)
                    .setValue(useAdvancedTopLogLogic)
                    .setLanguageKey(Reference.LANG_KEY_BASE + Reference.USE_ADV_TOP_LOG_LOGIC);
        }

        config.get(category, Reference.LOGS, ListUtils.getListAsDelimitedString(logBlocks, "; "))
                .setLanguageKey(Reference.LANG_KEY_BASE + Reference.LOGS);
        config.get(category, Reference.LEAVES, ListUtils.getListAsDelimitedString(leafBlocks, "; "))
                .setLanguageKey(Reference.LANG_KEY_BASE + Reference.LEAVES);

        config.setCategoryPropertyOrder(category, orderedKeys);
    }

    /*
     * Field setters
     */
    public TreeDefinition setAllowSmartTreeDetection(boolean allowSmartTreeDetection)
    {
        this.allowSmartTreeDetection = allowSmartTreeDetection;
        return this;
    }

    public TreeDefinition setOnlyDestroyUpwards(boolean onlyDestroyUpwards)
    {
        this.onlyDestroyUpwards = onlyDestroyUpwards;
        return this;
    }

    public TreeDefinition setRequireLeafDecayCheck(boolean requireLeafDecayCheck)
    {
        this.requireLeafDecayCheck = requireLeafDecayCheck;
        return this;
    }

    public TreeDefinition setMaxHorLogBreakDist(int maxHorLogBreakDist)
    {
        this.maxHorLogBreakDist = maxHorLogBreakDist;
        return this;
    }

    public TreeDefinition setMaxVerLogBreakDist(int maxVerLogBreakDist)
    {
        this.maxVerLogBreakDist = maxVerLogBreakDist;
        return this;
    }

    public TreeDefinition setMaxLeafIDDist(int maxLeafIDDist)
    {
        this.maxLeafIDDist = maxLeafIDDist;
        return this;
    }

    public TreeDefinition setMaxHorLeafBreakDist(int maxLeafBreakDist)
    {
        maxHorLeafBreakDist = maxLeafBreakDist;
        return this;
    }

    public TreeDefinition setMinLeavesToID(int minLeavesToID)
    {
        this.minLeavesToID = minLeavesToID;
        return this;
    }

    public TreeDefinition setBreakSpeedModifier(float breakSpeedModifier)
    {
        this.breakSpeedModifier = breakSpeedModifier;
        return this;
    }

    public TreeDefinition setUseAdvancedTopLogLogic(boolean useAdvancedTopLogLogic)
    {
        this.useAdvancedTopLogLogic = useAdvancedTopLogLogic;
        return this;
    }

    /**
     * Retrieves a copy of the list of logs in this TreeDefinition.
     * 
     * @return
     */
    public List<BlockID> getLogList()
    {
        return logBlocks;
    }

    /**
     * Retrieves a copy of the list of leaves in this TreeDefinition.
     * 
     * @return
     */
    public List<BlockID> getLeafList()
    {
        return leafBlocks;
    }

    /*
     * Field accessors
     */
    public boolean allowSmartTreeDetection()
    {
        return allowSmartTreeDetection;
    }

    public boolean onlyDestroyUpwards()
    {
        return onlyDestroyUpwards;
    }

    public boolean requireLeafDecayCheck()
    {
        return requireLeafDecayCheck;
    }

    public int maxHorLogBreakDist()
    {
        return maxHorLogBreakDist;
    }

    public int maxVerLogBreakDist()
    {
        return maxVerLogBreakDist;
    }

    public int maxLeafIDDist()
    {
        return maxLeafIDDist;
    }

    public int maxHorLeafBreakDist()
    {
        return maxHorLeafBreakDist;
    }

    public int minLeavesToID()
    {
        return minLeavesToID;
    }

    public float breakSpeedModifier()
    {
        return breakSpeedModifier;
    }

    public boolean useAdvancedTopLogLogic()
    {
        return useAdvancedTopLogLogic;
    }

    static
    {
        orderedKeys.add(Reference.LOGS);
        orderedKeys.add(Reference.LEAVES);
        orderedKeys.add(Reference.ALLOW_SMART_TREE_DETECT);
        orderedKeys.add(Reference.MAX_LEAF_ID_DIST);
        orderedKeys.add(Reference.MIN_LEAF_ID);
        orderedKeys.add(Reference.ONLY_DESTROY_UPWARDS);
        orderedKeys.add(Reference.REQ_DECAY_CHECK);
        orderedKeys.add(Reference.MAX_H_LOG_DIST);
        orderedKeys.add(Reference.MAX_V_LOG_DIST);
        orderedKeys.add(Reference.MAX_H_LEAF_DIST);
        orderedKeys.add(Reference.BREAK_SPEED_MOD);
        orderedKeys.add(Reference.USE_ADV_TOP_LOG_LOGIC);

        validKeys.add(Reference.TREE_NAME);
        validKeys.add(Reference.ALLOW_SMART_TREE_DETECT);
        validKeys.add(Reference.ONLY_DESTROY_UPWARDS);
        validKeys.add(Reference.REQ_DECAY_CHECK);
        validKeys.add(Reference.MAX_H_LOG_DIST);
        validKeys.add(Reference.MAX_V_LOG_DIST);
        validKeys.add(Reference.MAX_H_LEAF_DIST);
        validKeys.add(Reference.MAX_LEAF_ID_DIST);
        validKeys.add(Reference.MIN_LEAF_ID);
        validKeys.add(Reference.BREAK_SPEED_MOD);
        validKeys.add(Reference.USE_ADV_TOP_LOG_LOGIC);
        validKeys.add(Reference.LOGS);
        validKeys.add(Reference.LEAVES);
    }
}
