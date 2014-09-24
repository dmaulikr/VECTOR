package graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import networking.Profile;

import physics.Configuration;

public class GamePanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private Configuration config;
	private Profile profile;
	private static int BALL_RADIUS = 20;
	private BufferedImage redImage = null;
	private BufferedImage blueImage = null;
	private BufferedImage background = null;

	public GamePanel(Configuration config, Profile profile, int frameWidth, int frameHeight)
	{
		super();
		this.setLayout(null);
		this.config = config;
		this.profile = profile;
		this.setBounds(0, 0, frameWidth, frameHeight + 30);
	}

	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		super.paint(g2);

		g2.drawImage(background, 0, 0, null);

		for(int i = 0; i < 3; i++)
		{
			double x = config.getXorY('x', 'a', i);
			double y = config.getXorY('y', 'a', i);
			try
			{
				g2.drawImage(ImageIO.read(new File("images/" + ProfilePanel.colorNames[profile.getColorIndex()] + "ball40.png")), (int)(x - BALL_RADIUS), (int)(y - BALL_RADIUS), null);
			}
			catch (IOException e) {System.err.print("IOERR");}
		}
		for(int i = 0; i < 3; i++)
		{
			double x = config.getXorY('x', 'b', i);
			double y = config.getXorY('y', 'b', i);
			try
			{
				g2.drawImage(ImageIO.read(new File("images/" + ProfilePanel.colorNames[profile.getColorIndex() + 1] + "ball40.png")), (int)(x - BALL_RADIUS), (int)(y - BALL_RADIUS), null);
			}
			catch (IOException e) {System.err.print("IOERR");}
		}
	}
}