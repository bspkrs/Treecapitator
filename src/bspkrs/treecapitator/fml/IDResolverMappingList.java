package bspkrs.treecapitator.fml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class IDResolverMappingList implements List
{
    private ArrayList<IDResolverMapping> list;
    
    public IDResolverMappingList()
    {
        list = new ArrayList<IDResolverMapping>();
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
