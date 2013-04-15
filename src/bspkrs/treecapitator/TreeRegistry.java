package bspkrs.treecapitator;

import java.util.HashMap;
import java.util.Map;

public class TreeRegistry
{
    private Map<String, TreeDefinition> treeDefs;
    
    private static TreeRegistry         instance;
    
    TreeRegistry()
    {
        instance = this;
        
        treeDefs = new HashMap<String, TreeDefinition>();
        
    }
    
    public static TreeRegistry instance()
    {
        if (instance == null)
            new TreeRegistry();
        
        return instance;
    }
}
