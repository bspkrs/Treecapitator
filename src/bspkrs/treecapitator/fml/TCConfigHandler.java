package bspkrs.treecapitator.fml;

public class TCConfigHandler
{
    private static TCConfigHandler instance;
    
    public static TCConfigHandler instance()
    {
        if (instance == null)
            new TCConfigHandler();
        
        return instance;
    }
    
    protected TCConfigHandler()
    {
        instance = this;
    }
}
