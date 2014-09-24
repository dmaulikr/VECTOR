package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client
{
	//private String homeIP = "192.168.1.80";
	private String schoolIP = "10.123.2.8";
	private int isCorrectCombo = 0;
	private final int TIME_OUT = 3000;
	private String username;
	private String password;
	private String data;
	private String onlinePlayers;
	private String chatData = "";
	private boolean troubleConnecting = false;
	private String ID;
	private Socket requestSocket;
	public ObjectOutputStream out;
	public ObjectInputStream in;

	public Client(){}

	public void connect()
	{ 
		try
		{
			InetSocketAddress address = new InetSocketAddress(schoolIP, 69);
			requestSocket = new Socket();
			requestSocket.connect(address, TIME_OUT);

			out = new ObjectOutputStream(requestSocket.getOutputStream());
			in = new ObjectInputStream(requestSocket.getInputStream());
		} 
		catch(UnknownHostException e) 
		{
			System.err.println("SERVER NOT FOUND");
			setTroubleConnecting(true);
			return;
		} 
		catch(IOException e)
		{
			System.err.println("I/O EXCEPTION");
			setTroubleConnecting(true);
			return;
		}

		Input input = new Input();
		new Thread(input).start();
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getUsername()
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}

	public String getChatData()
	{
		return chatData;
	}

	public String getData()
	{
		String temp = data;
		data = null;
		return temp;
	}

	public String getID()
	{
		return ID;
	}

	public void sendData(String data)
	{
		try 
		{
			out.writeObject(data);
			out.flush();
		} 
		catch(IOException e) 
		{
			System.err.println("I/O EXCEPTION");
			e.printStackTrace();
		}
	}

	public int isCorrectCombo()
	{
		return isCorrectCombo;
	}

	public ArrayList<String> getOnlinePlayers()
	{
		ArrayList<String> temp = new ArrayList<String>();
		String[] array = onlinePlayers.split(" ");
		for(int i = 0; i < array.length; i++)
		{
			temp.add(array[i]);
		}
		return temp;
	}

	public void setTroubleConnecting(boolean troubleConnecting) {
		this.troubleConnecting = troubleConnecting;
	}

	public boolean getTroubleConnecting() {
		return troubleConnecting;
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
					String temp = (String) in.readObject();
					//System.out.println("RECIEVED DATA• " + temp);
					String[] input = temp.split("•");

					if(input[0].equals("COMBO"))
					{
						//System.out.println("CHECKING COMBO STATUS");
						if(input[2].equals("TRUE"))
						{
							isCorrectCombo = 1;
							ID = input[1];
						}
						else
							isCorrectCombo = -1;
					}
					else if(input[0].equals("FORWARD"))
					{
						data = input[2];
					}
					else if(input[0].equals("PROFILE"))
					{
						data = input[2];
					}
					else if(input[0].equals("ONLINEPLAYERS"))
					{
						onlinePlayers = input[2];
					}
					else if(input[0].equals("CHAT"))
					{
						System.out.println("[Chat Data Recieved]");
						chatData = input[2];
					}
				} 
				catch (IOException e) 
				{
					System.err.println("I/O EXCEPTION");
					e.printStackTrace();
					break;
				} 
				catch (ClassNotFoundException e) 
				{
					System.err.println("UNKNOWN DATA FORMAT RECIEVED");
					e.printStackTrace();
				}

			}while(true);
		}
	}
}
