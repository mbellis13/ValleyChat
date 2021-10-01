import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerMain 
{

	private static final Lock myLock = new ReentrantLock();
	private static final Condition notEmpty = myLock.newCondition();
	private static ArrayList<Message> queue = new ArrayList<Message>();
	public static ArrayList<InputHandler> inputHandlers = new ArrayList<InputHandler>();
	public static ArrayList<OutputHandler> sendThreads = new ArrayList<OutputHandler>();
	public static ArrayList<Connection> connections = new ArrayList<Connection>();
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		ServerSocket server = new ServerSocket(9000);
		System.out.println("ready to accept connections");
		for(int i = 0; i<10; i++)
		{
			Thread temp = new Thread(new OutputHandler());
			temp.start();
		}
		while(true)
		{
			Socket client = server.accept();
			//client.setSoTimeout(10000);
			System.out.println("client accepted");
			
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(),true);
			String username = null;
			try {
				System.out.println("reading...");
				username = in.readLine();


				System.out.println("username: " + username);
				
				
				boolean contained = false;
				for(InputHandler c: inputHandlers)
				{
					if(c.getUsername().equals(username))
						contained = true;
				}
				if(!contained)
				{
					//inputHandlers.add(new InputHandler(username, client));
					Thread input = new Thread(new InputHandler(username,client));
					input.start();
					connections.add(new Connection(username,client));
					System.out.print("connected to " + username);
					for(Connection c: connections)
					{
						out.println(c.getUsername());
						
						
					}
					out.println("done");
					System.out.println("done");
					for(Connection c: connections)
					{
						if(!c.getUsername().equals(username))
							new PrintWriter(c.getSocket().getOutputStream(),true).println("ADD\n"+username);
					}
				}
				//client.setSoTimeout(0);
			}catch(SocketTimeoutException e)
			{
				System.out.println("connection dropped");
				client.close();
			}
			
				
		}
	}
	
	public static Socket findSocket(String user)
	{
		for(Connection c: connections)
		{
			if(c.getUsername().equals(user))
			{
				return c.getSocket();
			}
			
		}
		return null;
	}
	
	public static Message getNextMessage() throws InterruptedException 
	{
		myLock.lock();
		Message message;
		try {
			while(queue.size() == 0)
			{
				notEmpty.await();
			}
			
			message = queue.remove(0);
			
		}finally {
			myLock.unlock();
		}
		return message;
	}
	
	public static void addMessage(Message message)
	{
		System.out.println(message.getSender());
		System.out.println(message.getMessage());
		myLock.lock();
		System.out.println("locked");
		queue.add(message);
		System.out.println("added");
		notEmpty.signalAll();
		myLock.unlock();
		System.out.println("unlocked");
		
		
	}
	
	public static void removeUser(String user) throws IOException
	{
		Connection r=null;
		for(Connection c: connections)
		{
			if(!c.getUsername().equals(user))
				new PrintWriter(c.getSocket().getOutputStream(),true).println("REMOVE\n"+user);
			else
				r = c;
			
		}
		
		connections.remove(r);
	}
	
	
	

}
