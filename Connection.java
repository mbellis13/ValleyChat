import java.net.Socket;

public class Connection 
{
	private String username;
	private Socket client;
	
	public Connection(String username, Socket client)
	{
		this.username = username;
		this.client = client;
	}
	
	
	public String getUsername()
	{
		return username;
	}
	
	public Socket getSocket()
	{
		return client;
	}

}
