package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection
{
	private int port;
	private String ID;
	protected Profile profile;

	private Socket connection;
	protected ObjectOutputStream out;
	protected ObjectInputStream in;
	
	private Thread inputThread;

	private String inputData;

	public ServerConnection(int port, Socket connection, String ID)
	{
		this.port = port;
		this.ID = ID;
		this.connection = connection;

		try
		{
			out = new ObjectOutputStream(this.connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(this.connection.getInputStream());
		}
		catch(IOException e)
		{
			System.err.println("I/O EXCEPTION");
			e.printStackTrace();
			closeConnections();
		}

		Input input = new Input();
		inputThread = new Thread(input);
		inputThread.start(); 
	}
	
	public void setProfile(Profile temp)
	{
		profile = temp;
	}
	
	public Profile getProfile()
	{
		return profile;
	}

	public int getPort()
	{
		return port;
	}

	public Socket getConnection()
	{
		return connection;
	}

	public String getID()
	{
		return ID;
	}

	public boolean closeConnections()
	{
		try
		{
			inputThread.interrupt();
			in.close();
			out.close();
			return true;
		}
		catch(IOException e)
		{
			System.err.println("I/O EXCEPTION");
			e.printStackTrace();
		}

		return false;
	}

	public String getData()
	{
		//System.out.println("GET DATA CALLED");
		if(inputData != null)
		{
			String temp = inputData;
			inputData = null;
			return temp;
		}

		return null;
	}

	public void sendData(String data)
	{
		//System.out.println("SEND DATA CALLED");
		//System.out.println("SENDING DATA");
		try 
		{
			out.writeObject(data);
			out.flush();
		} 
		catch(IOException e) 
		{
			System.err.println("I/O EXCEPTION");
			e.printStackTrace();
			closeConnections();
		}
	}

	private class Input implements Runnable
	{
		@Override
		public void run() 
		{
			do
			{
				try 
				{
					Thread.sleep(500);
				} 
				catch(InterruptedException e) 
				{
					System.err.println("THREAD INTERRUPTED");
					e.printStackTrace();
				}

				try
				{
					inputData = (String) in.readObject();
					String[] temp = inputData.split("•");
					if(temp[0].equals("CHAT") || temp[0].equals("COMBO") || temp[0].equals("ONLINEPLAYERS") || temp[0].equals("PROFILE") || temp[0].equals("CHALLENGE"))
						inputData = temp[0] + "•" + ID + "•" + temp[2];
					System.out.println(inputData);
				} 
				catch(IOException e) 
				{
					System.err.println("I/O EXCEPTION");
					e.printStackTrace();
					closeConnections();
					break;
				} 
				catch(ClassNotFoundException e) 
				{
					System.err.println("UNKNOWN DATA FORMAT RECIEVED");
					e.printStackTrace();
				} 
			}while(true);
		}
	}
}
