package bspkrs.treecapitator.registry;

import net.minecraft.nbt.NBTTagCompound;
import bspkrs.treecapitator.config.TCSettings;

public class RegistryNBTManager
{
    private NBTTagCompound localTCSettings;
    private NBTTagCompound localTreeRegistry;
    private NBTTagCompound localToolRegistry;
    private NBTTagCompound remoteTCSettings;
    private NBTTagCompound remoteTreeRegistry;
    private NBTTagCompound remoteToolRegistry;

    public RegistryNBTManager(NBTTagCompound tcSettingsNBT, NBTTagCompound treeRegistryNBT, NBTTagCompound toolRegistryNBT)
    {
        this();
        this.setRemoteTCSettings(tcSettingsNBT);
        this.setRemoteTreeRegistry(treeRegistryNBT);
        this.setRemoteToolRegistry(toolRegistryNBT);
    }

    public RegistryNBTManager()
    {
        this.saveCurrentTCSettingsToLocalNBT();
        this.saveCurrentTreeRegistryToLocalNBT();
        this.saveCurrentToolRegistryToLocalNBT();
        this.setRemoteTCSettings(localTCSettings);
        this.setRemoteTreeRegistry(localTreeRegistry);
        this.setRemoteToolRegistry(localToolRegistry);
    }

    public RegistryNBTManager saveAllCurrentObjectsToLocalNBT()
    {
        return this.saveCurrentTCSettingsToLocalNBT().saveCurrentToolRegistryToLocalNBT().saveCurrentTreeRegistryToLocalNBT();
    }

    protected RegistryNBTManager saveCurrentTCSettingsToLocalNBT()
    {
        localTCSettings = new NBTTagCompound();
        TCSettings.instance().writeToNBT(localTCSettings);
        return this;
    }

    protected RegistryNBTManager saveCurrentTreeRegistryToLocalNBT()
    {
        localTreeRegistry = new NBTTagCompound();
        TreeRegistry.instance().writeToNBT(localTreeRegistry);
        return this;
    }

    protected RegistryNBTManager saveCurrentToolRegistryToLocalNBT()
    {
        localToolRegistry = new NBTTagCompound();
        ToolRegistry.instance().writeToNBT(localToolRegistry);
        return this;
    }

    public RegistryNBTManager setRemoteTCSettings(NBTTagCompound ntc)
    {
        remoteTCSettings = ntc;
        return this;
    }

    public RegistryNBTManager setRemoteTreeRegistry(NBTTagCompound ntc)
    {
        remoteTreeRegistry = ntc;
        return this;
    }

    public RegistryNBTManager setRemoteToolRegistry(NBTTagCompound ntc)
    {
        remoteToolRegistry = ntc;
        return this;
    }

    public RegistryNBTManager setRemoteNBTs(NBTTagCompound set, NBTTagCompound tree, NBTTagCompound tool)
    {
        return setRemoteTCSettings(set).setRemoteTreeRegistry(tree).setRemoteToolRegistry(tool);
    }

    public void registerLocalInstances()
    {
        TCSettings.instance().readFromNBT(localTCSettings);
        TreeRegistry.instance().readFromNBT(localTreeRegistry);
        ToolRegistry.instance().readFromNBT(localToolRegistry);
    }

    public void registerRemoteInstances()
    {
        TCSettings.instance().readFromNBT(remoteTCSettings);
        TreeRegistry.instance().readFromNBT(remoteTreeRegistry);
        ToolRegistry.instance().readFromNBT(remoteToolRegistry);
    }

    public NBTTagCompound[] getPacketArray()
    {
        return new NBTTagCompound[] { localTCSettings, localTreeRegistry, localToolRegistry };
    }
}
