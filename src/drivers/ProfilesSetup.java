package drivers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import networking.Profile;

public class ProfilesSetup 
{
	public static void main(String[] args)
	{
		try
		{
			ArrayList<String> list = new ArrayList<String>();
			list.add("jzhao");
			list.add("cgregory");
			ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream("profiles.txt"));
			objOutStream.writeObject(list);

			ObjectInputStream objInStream = new ObjectInputStream(new FileInputStream("profiles.txt"));
			ArrayList<String> test = (ArrayList<String>) objInStream.readObject();
			System.out.println("SUCCESS");
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
}
