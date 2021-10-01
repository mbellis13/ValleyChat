import java.io.PrintWriter;
import java.net.Socket;

public class OutputHandler implements Runnable
{

	
	@Override
	public void run() {
		while(true)
		{
			Message message = null;
			try {
				message = ServerMain.getNextMessage();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				
			
				if(message != null)
				{
					Socket client = ServerMain.findSocket(message.getRecipient());
				
					PrintWriter out = new PrintWriter(client.getOutputStream(),true);
					out.println("MESSAGE");
					out.println(message.getSender());
					out.println(message.getMessage());
					
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}

	
}
