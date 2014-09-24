package networking;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Server Command Protocol (machine based)
 * Standard string format: REFERENCE•PORT•DATA
 * Example command: CHAT•0000•Hello, World!
 * 
 * CHAT•(return ID)•DATA
 * COMBO•(return ID)•DATA
 * FORWARD•(destination ID)•DATA
 */
public class Server
{
	private int port = 69;
	private String ID = "0000";

	private ServerSocket providerSocket;
	private ArrayList<ServerConnection> connections;
	private ArrayList<Profile> profiles;
	private Queue<String> queue;
	private ComboChecker combo;
	
	private String chatDialog = "<Server>: Chat session has been started. \n";

	public static void main(String[] args)
	{
		Server server = new Server();
		server.run();
	}

	Server()
	{
		profiles = new ArrayList<Profile>();
		connections = new ArrayList<ServerConnection>();
		queue = new LinkedList<String>();
		combo = new ComboChecker();

		try 
		{
			ObjectInputStream objInStream = new ObjectInputStream(new FileInputStream("profiles.txt"));
			ArrayList<String> temp = (ArrayList<String>) objInStream.readObject();

			for(int i = 0; i < temp.size(); i++)
			{
				profiles.add(new Profile(temp.get(i)));
			}

			//System.out.println(temp.toString());
			//System.out.println(profiles.toString());
		} 
		catch (FileNotFoundException e) 
		{
			System.err.println("INPUT FILE NOT FOUND");
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			System.err.println("I/O EXCEPTION");
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) 
		{
			System.err.println("UNKNOWN DATA FORMAT RECIEVED");
			e.printStackTrace();
		}
	}

	void run()
	{
		Input input = new Input();
		Output output = new Output();
		Backup backup = new Backup();
		new Thread(input).start(); 
		new Thread(output).start();
		new Thread(backup).start();

		try
		{
			System.out.println("Listening on port number: " + port);
			providerSocket = new ServerSocket(port, 10);

			do
			{
				connections.add(new ServerConnection(port, providerSocket.accept(), ID));
				String name = connections.get(connections.size() - 1).getConnection().getInetAddress().getHostName();
				System.out.println("Connection established with: " + name);
				System.out.println("ID: " + ID);
				ID = "" + (Integer.parseInt(ID) + 1);
				for(int i = 0; i < 6 - ID.length(); i++)
					ID = "0" + ID;
			}while(true);
		}
		catch(IOException e)
		{
			System.err.println("I/O EXCEPTION");
			e.printStackTrace();
		}
		finally
		{
			for(int i = 0; i < connections.size(); i++)
			{
				if(!connections.get(i).closeConnections())
					System.err.println("ERROR CLOSING CONNECTIONS");
			}
		}
	}

	private boolean isUniqueProfile(String username)
	{
		for(int i = 0; i < profiles.size(); i++)
		{
			if(profiles.get(i).getUsername().equals(username))
				return false;
		}

		return true;
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
					System.err.println("I/O EXCEPTION");
					e.printStackTrace();
				}

				for(int i = 0; i < connections.size(); i++)
				{
					String temp = connections.get(i).getData();
					//System.out.println("DATA RECIEVED (server)• " + temp);
					if(temp != null)
					{
						String[] list = temp.split("•");
						if(list[0].equals("CHAT"))
						{
							//System.out.println("[Processing Chat Request]");
							//System.out.println("Data Recieved• " + list[2]);
							String username = connections.get(i).getProfile().getUsername();
							chatDialog = chatDialog + "<" + username + ">: " + list[2] + "\n";
							//System.out.println(chatDialog);
							queue.add("CHAT•0000•" + chatDialog);
						}
						else if(list[0].equals("COMBO"))
						{

							if(combo.isCorrectCombo(list[2]) == true)
							{
								queue.add("COMBO•" + connections.get(i).getID() + "•TRUE");
								queue.add("CHAT•" + connections.get(i).getID() + "•" + chatDialog);
								//System.out.println("DATA ADDED TO QUEUE");
								
								String[] string = list[2].split(" ");
								String username = string[0];

								if(isUniqueProfile(username) == true)
								{
									connections.get(i).setProfile(new Profile(username));
									connections.get(i).getProfile().setID(connections.get(i).getID());
								}
								else
								{
									//System.out.println("SETTING PROFILE");
									for(int c = 0; c < profiles.size(); c++)
									{
										if(profiles.get(c).getUsername().equals(username))
											connections.get(i).setProfile(profiles.get(c));
									}

									connections.get(i).getProfile().setID(connections.get(i).getID());
								}

							}
							else
								queue.add("COMBO•" + list[1] + "•FALSE");
						}
						else if(list[0].equals("FORWARD"))
						{
							queue.add("FORWARD" + list[1] + "•" + list[2]);
						}
						else if(list[0].equals("CHALLENGE"))
						{
							queue.add(list[0] + "•" + list[1] + "•" + list[2]);
						}
						else if(list[0].equals("PROFILE"))
						{
							//System.out.println("PROFILE DATA REQUEST RECIEVED");
							queue.add(list[0] + "•" + list[1] + "•" + connections.get(i).getProfile().toCode());
						}
						else if(list[0].equals("LOGOUT"))
						{
							connections.get(i).closeConnections();
							connections.remove(i);
						}

						//TODO: Add necessary input commands
					}
				}
			}while(true);
		}
	}

	private class Output implements Runnable
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

				if(queue.isEmpty() == false)
				{
					//System.out.println("ITERATE");

					String[] temp = queue.poll().split("•");
					if(temp[0].equals("CHAT"))
					{
						//System.out.println("[Sending Chat Data]");
						//System.out.println("Data• " + temp[2]);
						for(int i = 0; i < connections.size(); i++)
							connections.get(i).sendData(temp[0] + "•" + connections.get(i).getID() + "•" + temp[2]);
					}
					else if(temp[0].equals("COMBO"))
					{
						for(int i = 0; i < connections.size(); i++)
						{
							if(connections.get(i).getID().equals(temp[1]))
							{
								//System.out.println("SENDING DATA (server)");
								connections.get(i).sendData(temp[0] + "•" + temp[1] + "•" + temp[2]); 
							}
						}
					}
					else if(temp[0].equals("FORWARD"))
					{
						for(int i = 0; i < connections.size(); i++)
						{
							if(connections.get(i).getID().equals(temp[1]))
								connections.get(i).sendData(temp[0] + "•" + connections.get(i).getID() + "•" + temp[2]);
						}
					}
					else if(temp[0].equals("CHALLENGE"))
					{
						for(int i = 0; i < connections.size(); i++)
						{
							if(connections.get(i).getID().equals(temp[1]))
								connections.get(i).sendData(temp[0] + "•" + connections.get(i).getID() + "•" + temp[2]);
						}
					}
					else if(temp[0].equals("PROFILE"))
					{
						for(int i = 0; i < connections.size(); i++)
						{
							if(connections.get(i).getID().equals(temp[1]))
								connections.get(i).sendData(temp[0] + "•" + temp[1] + "•" + temp[2]);
						}
					}

					String onlinePlayers = "";
					for(int i = 0; i < connections.size(); i++)
					{
						onlinePlayers = onlinePlayers + connections.get(i).getProfile().getUsername() + " ";
					}

					//System.out.println("Online Players• " + onlinePlayers);
					for(int i = 0; i < connections.size(); i++)
					{
						connections.get(i).sendData("ONLINEPLAYERS" + "•" + connections.get(i).getID() + "•" + onlinePlayers);
					}
					//TODO: Add necessary output commands
				}
			}while(true);
		}
	}

	private class ComboChecker
	{
		private ArrayList<String> combos;

		private ComboChecker()
		{
			try 
			{
				ObjectInputStream objInStream = new ObjectInputStream(new FileInputStream("combos.txt"));
				combos = (ArrayList<String>) objInStream.readObject();
				//System.out.println(combos.toString());
			} 
			catch(FileNotFoundException e) 
			{
				System.err.println("DATA FILE NOT FOUND");
				e.printStackTrace();
			} 
			catch(IOException e)
			{
				System.err.println("I/O EXCEPTION");
				e.printStackTrace();
			} 
			catch(ClassNotFoundException e) 
			{
				System.err.println("UNKNOWN DATA FORMAT RECIEVED");
				e.printStackTrace();
			}
		}

		private boolean isCorrectCombo(String combo)
		{
			for(int i = 0; i < combos.size(); i++)
			{
				if(combos.get(i).equalsIgnoreCase(combo))
					return true;
			}

			return false;
		}
	}

	private class Backup implements Runnable
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
					ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream("profiles.txt"));
					ArrayList<String> temp = new ArrayList<String>();
					boolean isOnline;

					for(int i = 0; i < profiles.size(); i++)
					{
						//System.out.println("BACKUP LOOP ITERATE");
						isOnline = false;
						
						for(int c = 0; c < connections.size(); c++)
						{
							if(connections.get(c).getProfile() != null && connections.get(c).getProfile().getUsername().equals(profiles.get(i).getUsername()))
							{
								temp.add(connections.get(c).getProfile().getUsername());
								isOnline = true;
							}
						}
						
						if(isOnline == false)
							temp.add(profiles.get(i).getUsername());
					}

					//System.out.println("BACKUP• " + temp.toString());
					objOutStream.writeObject(temp);
				}
				catch(IOException e)
				{
					System.err.println("I/O EXCEPTION");
					e.printStackTrace();
				} 
			}while(true);
		}
	}
}
