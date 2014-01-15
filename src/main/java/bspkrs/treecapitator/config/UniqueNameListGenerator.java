package bspkrs.treecapitator.config;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import bspkrs.util.CommonUtils;
import cpw.mods.fml.common.registry.GameData;

public class UniqueNameListGenerator
{
    private static UniqueNameListGenerator instance;
    
    public static UniqueNameListGenerator instance()
    {
        if (instance == null)
            instance = new UniqueNameListGenerator();
        
        return instance;
    }
    
    public void run()
    {
        File listFile = new File(new File(CommonUtils.getConfigDir()), "UniqueNames.txt");
        
        try
        {
            ArrayList<String> blockList = new ArrayList();
            ArrayList<String> itemList = new ArrayList();
            
            for (Object obj : GameData.blockRegistry.func_148742_b())
                blockList.add((String) obj);
            
            for (Object obj : GameData.itemRegistry.func_148742_b())
                itemList.add((String) obj);
            
            Collections.sort(blockList);
            Collections.sort(itemList);
            
            if (listFile.exists())
                listFile.delete();
            
            listFile.createNewFile();
            
            PrintWriter out = new PrintWriter(new FileWriter(listFile));
            
            out.println();
            out.println();
            out.println("**********************************************");
            out.println("*  ####   #       ###    ###   #   #   ####  *");
            out.println("*  #   #  #      #   #  #   #  #  #   #      *");
            out.println("*  ####   #      #   #  #      ###     ###   *");
            out.println("*  #   #  #      #   #  #      #  #       #  *");
            out.println("*  ####   #####   ###    ####  #   #  ####   *");
            out.println("**********************************************");
            out.println();
            out.println();
            
            for (String s : blockList)
            {
                out.println(s);
            }
            
            out.println();
            out.println();
            out.println("***************************************");
            out.println("*  #####  #####  #####  #   #   ####  *");
            out.println("*    #      #    #      ## ##  #      *");
            out.println("*    #      #    ###    # # #   ###   *");
            out.println("*    #      #    #      #   #      #  *");
            out.println("*  #####    #    #####  #   #  ####   *");
            out.println("***************************************");
            out.println();
            out.println();
            
            for (String s : itemList)
            {
                out.println(s);
            }
            
            out.println();
            out.println();
            
            out.close();
            
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
