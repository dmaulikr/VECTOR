package drivers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * This class is only to be run if you want to reset all password data back to default settings
 * WARNING: THIS WILL DELETE ALL COLLECTED PASSWORD DATA
 * @author Chris Gregory and Jason Zhao
 */
public class CombosSetup 
{
	public static void main(String[] args)
	{
		try
		{
			ArrayList<String> list = new ArrayList<String>();
			BufferedReader temp = new BufferedReader(new FileReader("combos setup.txt"));
			String str = "";
			while((str = temp.readLine()) != null)
			{
				list.add(str);
			}

			ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream("combos.txt"));
			objOutStream.writeObject(list);

			ObjectInputStream objInStream = new ObjectInputStream(new FileInputStream("combos.txt"));
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
