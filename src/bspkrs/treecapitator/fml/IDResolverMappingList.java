package bspkrs.treecapitator.fml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import sharose.mods.idresolver.IDResolverBasic;
import bspkrs.treecapitator.TCLog;
import bspkrs.treecapitator.TreeCapitator;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class IDResolverMappingList implements List
{
    private ArrayList<IDResolverMapping> list;
    private static IDResolverMappingList instance;
    
    public static IDResolverMappingList instance()
    {
        if (instance == null)
            new IDResolverMappingList();
        
        return instance;
    }
    
    private IDResolverMappingList()
    {
        instance = this;
        list = new ArrayList<IDResolverMapping>();
        init();
    }
    
    private void init()
    {
        /*
         * Get IDs from ID Resolver if it's loaded
         */
        if (Loader.instance().isModLoaded(TreeCapitator.idResolverModID))
        {
            TCLog.info("ID Resolver has been detected.  Processing ID config...");
            
            Properties idrKnownIDs = null;
            
            try
            {
                idrKnownIDs = ObfuscationReflectionHelper.getPrivateValue(IDResolverBasic.class, null, "knownIDs");
            }
            catch (Throwable e)
            {
                TCLog.debug("Error getting knownIDs from ID Resolver: %s", e.getMessage());
                e.printStackTrace();
            }
            
            if (idrKnownIDs != null)
            {
                for (String key : idrKnownIDs.stringPropertyNames())
                {
                    String value = idrKnownIDs.getProperty(key);
                    try
                    {
                        if (!key.startsWith("ItemID.") && !key.startsWith("BlockID."))
                            continue;
                        
                        IDResolverMapping mapping = new IDResolverMapping(key + "=" + value);
                        
                        if (mapping.oldID != 0 && mapping.newID != 0 && !mapping.isStaticMapping())
                        {
                            // IDs are not the same, add to the list of managed IDs
                            add(mapping);
                            TCLog.debug("Adding entry: %s", key + "=" + value);
                        }
                        else
                            TCLog.debug("Ignoring entry: %s", key + "=" + value);
                    }
                    catch (Throwable e)
                    {
                        TCLog.severe("Exception caught for line: %s", key + "=" + value);
                    }
                }
            }
        }
        else
            TCLog.info("ID Resolver (Mod ID \"%s\") is not loaded.", TreeCapitator.idResolverModID);
    }
    
    public boolean hasMappingForModAndID(String className, int oldID)
    {
        Iterator i = list.iterator();
        while (i.hasNext())
        {
            IDResolverMapping mapping = (IDResolverMapping) i.next();
            if (mapping.modClassName.equals(className) && mapping.oldID == oldID)
                return true;
        }
        
        return false;
    }
    
    public IDResolverMapping getMappingForModAndOldID(String className, int oldID)
    {
        Iterator i = list.iterator();
        while (i.hasNext())
        {
            IDResolverMapping mapping = (IDResolverMapping) i.next();
            if (mapping.modClassName.equals(className) && mapping.oldID == oldID)
                return mapping;
        }
        
        return null;
    }
    
    @Override
    public int size()
    {
        return list.size();
    }
    
    @Override
    public boolean isEmpty()
    {
        return list.isEmpty();
    }
    
    @Override
    public boolean contains(Object o)
    {
        return list.contains(o);
    }
    
    @Override
    public Iterator iterator()
    {
        return list.iterator();
    }
    
    public ArrayList<IDResolverMapping> toArrayList()
    {
        return (ArrayList<IDResolverMapping>) list.clone();
    }
    
    @Override
    public Object[] toArray()
    {
        return list.toArray();
    }
    
    @Override
    public Object[] toArray(Object[] a)
    {
        return list.toArray(a);
    }
    
    @Override
    public boolean add(Object e)
    {
        return list.add((IDResolverMapping) e);
    }
    
    @Override
    public boolean remove(Object o)
    {
        return list.remove(o);
    }
    
    @Override
    public boolean containsAll(Collection c)
    {
        return list.containsAll(c);
    }
    
    @Override
    public boolean addAll(Collection c)
    {
        return list.addAll(c);
    }
    
    @Override
    public boolean addAll(int index, Collection c)
    {
        return list.addAll(index, c);
    }
    
    @Override
    public boolean removeAll(Collection c)
    {
        return list.removeAll(c);
    }
    
    @Override
    public boolean retainAll(Collection c)
    {
        return list.retainAll(c);
    }
    
    @Override
    public void clear()
    {
        list.clear();
    }
    
    @Override
    public Object get(int index)
    {
        return list.get(index);
    }
    
    @Override
    public Object set(int index, Object element)
    {
        return list.set(index, (IDResolverMapping) element);
    }
    
    @Override
    public void add(int index, Object element)
    {
        list.add(index, (IDResolverMapping) element);
    }
    
    @Override
    public Object remove(int index)
    {
        return list.remove(index);
    }
    
    @Override
    public int indexOf(Object o)
    {
        return list.indexOf(o);
    }
    
    @Override
    public int lastIndexOf(Object o)
    {
        return list.lastIndexOf(o);
    }
    
    @Override
    public ListIterator listIterator()
    {
        return list.listIterator();
    }
    
    @Override
    public ListIterator listIterator(int index)
    {
        return list.listIterator(index);
    }
    
    @Override
    public List subList(int fromIndex, int toIndex)
    {
        return list.subList(fromIndex, toIndex);
    }
}
