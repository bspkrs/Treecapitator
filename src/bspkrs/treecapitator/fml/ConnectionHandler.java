package bspkrs.treecapitator.fml;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import bspkrs.treecapitator.TCLog;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;

public class ConnectionHandler implements IConnectionHandler
{
    private static int i = 0;
    
    /**
     * 2) Called when a player logs into the server SERVER SIDE
     * 
     * @param player
     * @param netHandler
     * @param manager
     */
    @Override
    public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager)
    {
        TCLog.debug("************************ Server: playerLoggedIn: %d", ++i);
    }
    
    /**
     * If you don't want the connection to continue, return a non-empty string here If you do, you can do other stuff here- note no FML
     * negotiation has occured yet though the client is verified as having FML installed
     * 
     * SERVER SIDE
     * 
     * @param netHandler
     * @param manager
     */
    @Override
    public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager)
    {
        TCLog.debug("************************ Server: connectionReceived: %d", ++i);
        return null;
    }
    
    /**
     * 1) Fired when a remote connection is opened CLIENT SIDE
     * 
     * @param netClientHandler
     * @param server
     * @param port
     */
    @Override
    public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager)
    {
        TCLog.debug("************************ Client: connectionOpened: %d", ++i);
    }
    
    /**
     * 
     * 1) Fired when a local connection is opened
     * 
     * CLIENT SIDE
     * 
     * @param netClientHandler
     * @param server
     */
    @Override
    public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager)
    {
        TCLog.debug("************************ Client: connectionOpened: %d", ++i);
    }
    
    /**
     * Fired when a connection closes
     * 
     * ALL SIDES
     * 
     * @param manager
     */
    @Override
    public void connectionClosed(INetworkManager manager)
    {
        TCLog.debug("************************ Client/Server: connectionClosed: %d", ++i);
    }
    
    /**
     * 3) Fired when the client established the connection to the server
     * 
     * CLIENT SIDE
     * 
     * @param clientHandler
     * @param manager
     * @param login
     */
    @Override
    public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login)
    {
        TCLog.debug("************************ Client: clientLoggedIn: %d", ++i);
        TreeCapitatorClient.instance().onClientConnect();
    }
}
