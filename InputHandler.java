import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class InputHandler implements Runnable
{
	
	BufferedReader in;
	Socket client;
	String username;
	
	public InputHandler(String username, Socket client) throws IOException
	{
		this.client = client;
		this.username = username;
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	}
	
	@Override
	public void run() 
	{
		String message;
		try{
			while((message = in.readLine())!=null)
			{

				String recipient = message;
				System.out.println(recipient);
				message = in.readLine();
				String sender = message;
				System.out.println(sender);
				message = in.readLine();
				System.out.println(message);
				Socket c = ServerMain.findSocket(username);
				System.out.println(c.getInetAddress());
				ServerMain.addMessage(new Message(recipient,username,message,c));
				System.out.println("message added to queue");
				
				
				
			}
		}catch(Exception e)
		{
			System.out.print("IO error");
		}
		try {
		ServerMain.removeUser(username);
		client.close();
		}
		catch(Exception e)
		{System.out.println("ish");}
		
		
		
	}
	
	public String getUsername()
	{
		return username;
	}
	

}
