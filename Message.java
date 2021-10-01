import java.net.Socket;

public class Message 
{
	private String recipient;
	private String sender;
	private String message;
	private Socket client;
	
	public Message(String r, String s, String m, Socket c)
	{
		
		recipient = r;
		sender = s;
		message = m;
		client = c;
	}
	
	public String getRecipient()
	{
		return recipient;
	}
	
	public String getSender()
	{
		return sender;
	}
	
	public String getMessage()
	{
		return message;
	}


}
