package bspkrs.treecapitator.util;

import java.util.HashSet;
import java.util.Set;

import bspkrs.util.BlockID;
import bspkrs.util.ItemID;

public class TCUtils
{
    public static String getSetAsDelimitedString(Set<?> list, String delimiter)
    {
        String r = "";

        for (Object o : list)
            r += delimiter + o.toString();

        return r.replaceFirst(delimiter, "");
    }

    public static Set<BlockID> getDelimitedStringAsBlockIDHashSet(String dList, String delimiter)
    {
        Set<BlockID> list = new HashSet<BlockID>();

        for (String format : dList.split(delimiter))
            list.add(new BlockID(format));

        return list;
    }

    public static Set<ItemID> getDelimitedStringAsItemIDHashSet(String dList, String delimiter)
    {
        Set<ItemID> list = new HashSet<ItemID>();

        for (String format : dList.split(delimiter))
            list.add(new ItemID(format));

        return list;
    }
}
