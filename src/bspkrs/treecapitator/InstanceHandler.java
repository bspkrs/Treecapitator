package bspkrs.treecapitator;

import net.minecraft.nbt.NBTTagCompound;

public class InstanceHandler
{
    private NBTTagCompound localTCSettings;
    private NBTTagCompound localTreeRegistry;
    private NBTTagCompound localToolRegistry;
    private NBTTagCompound remoteTCSettings;
    private NBTTagCompound remoteTreeRegistry;
    private NBTTagCompound remoteToolRegistry;
    
    public InstanceHandler(NBTTagCompound tcSettingsNBT, NBTTagCompound treeRegistryNBT, NBTTagCompound toolRegistryNBT)
    {
        this();
        this.setRemoteTCSettings(tcSettingsNBT);
        this.setRemoteTreeRegistry(treeRegistryNBT);
        this.setRemoteToolRegistry(toolRegistryNBT);
    }
    
    protected InstanceHandler()
    {
        this.saveCurrentTCSettingsToLocal();
        this.saveCurrentTreeRegistryToLocal();
        this.saveCurrentToolRegistryToLocal();
        this.setRemoteTCSettings(localTCSettings);
        this.setRemoteTreeRegistry(localTreeRegistry);
        this.setRemoteToolRegistry(localToolRegistry);
    }
    
    public InstanceHandler saveCurrentTCSettingsToLocal()
    {
        localTCSettings = new NBTTagCompound();
        TCSettings.instance().writeToNBT(localTCSettings);
        return this;
    }
    
    public InstanceHandler saveCurrentTreeRegistryToLocal()
    {
        localTCSettings = new NBTTagCompound();
        TreeRegistry.instance().writeToNBT(localTreeRegistry);
        return this;
    }
    
    public InstanceHandler saveCurrentToolRegistryToLocal()
    {
        localTCSettings = new NBTTagCompound();
        ToolRegistry.instance().writeToNBT(localToolRegistry);
        return this;
    }
    
    public InstanceHandler setRemoteTCSettings(NBTTagCompound ntc)
    {
        remoteTCSettings = ntc;
        return this;
    }
    
    public InstanceHandler setRemoteTreeRegistry(NBTTagCompound ntc)
    {
        remoteTreeRegistry = ntc;
        return this;
    }
    
    public InstanceHandler setRemoteToolRegistry(NBTTagCompound ntc)
    {
        remoteToolRegistry = ntc;
        return this;
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
}
