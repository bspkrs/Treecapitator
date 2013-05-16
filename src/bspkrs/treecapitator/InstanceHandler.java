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
    
    protected InstanceHandler(NBTTagCompound tcSettingsNBT, NBTTagCompound treeRegistryNBT, NBTTagCompound toolRegistryNBT)
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
        this.remoteTCSettings = ntc;
        return this;
    }
    
    public InstanceHandler setRemoteTreeRegistry(NBTTagCompound ntc)
    {
        this.remoteTreeRegistry = ntc;
        return this;
    }
    
    public InstanceHandler setRemoteToolRegistry(NBTTagCompound ntc)
    {
        this.remoteToolRegistry = ntc;
        return this;
    }
    
    public void registerLocalInstances()
    {
        TCSettings.instance().readFromNBT(this.localTCSettings);
        TreeRegistry.instance().readFromNBT(this.localTreeRegistry);
        ToolRegistry.instance().readFromNBT(this.localToolRegistry);
    }
    
    public void registerRemoteInstances()
    {
        TCSettings.instance().readFromNBT(this.remoteTCSettings);
        TreeRegistry.instance().readFromNBT(this.remoteTreeRegistry);
        ToolRegistry.instance().readFromNBT(this.remoteToolRegistry);
    }
}
