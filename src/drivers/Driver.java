package drivers;

import graphics.GraphicsFrame;

import javax.swing.UIManager;
public class Driver
{
	private static String title = "Vector - Jason Zhao and Chris Gregory";
	private static int width = 900;
	private static int height = 600;

	public static void main(String[] args)
	{
		GraphicsFrame gf = new GraphicsFrame(title, width, height);
		gf.setDefaultCloseOperation(0);
		gf.setLocationRelativeTo(null);
		gf.setResizable(false);
		gf.setVisible(true);
		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e){}
	}
}

/*
	Exchanged information
		player color
		player name
		player level
		configuration

	Server + client constantly checking for:
		online players
		online players who are busy
		the history of the chat
		any input by the client for:
			chat message
			challenge to another player
			random challenge
			
	private String schoolIP = "10.123.2.8";
	private String homeIP = "192.168.1.80";
 */