package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import networking.Profile;

public class ProfilePanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private boolean doneCheck = false;
	private final Profile profile;

	public final static String[] colorNames = {"darkgray", "lightgray", "white", "red", "orange", "yellow", "blue", "cyan", "green", "darkblue", "purple", "pink"};
	public final static Color[] colors = {Color.DARK_GRAY, Color.GRAY, Color.WHITE, Color.RED, new Color(250, 130, 0), Color.YELLOW, Color.BLUE, Color.CYAN, new Color(50, 180, 50), new Color(40, 40, 130), new Color(150, 50, 200), new Color(255, 40, 150)};

	public ProfilePanel(final Profile profile)
	{
		super();
		this.setLayout(null);
		this.profile = profile;

		JButton doneButton = new JButton("Done");
		doneButton.setBounds(440, 500, 100, 20);
		doneButton.setFont(new Font("Lucida", 1, 12));
		doneButton.setFocusable(false);
		doneButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				doneCheck = true;
			}});
		this.add(doneButton);

		JTextArea statisticsArea = new JTextArea();
		statisticsArea.setBounds(640, 80, 200, 400);
		statisticsArea.setEditable(false);
		statisticsArea.setFont(new Font("Lucida", 1, 13));
		statisticsArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.DARK_GRAY),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		statisticsArea.setText("Username: " + profile.getUsername() + "\n\n" + 
				"Level: " + profile.getLevel() + "\n\n" +
				"Games played: " + profile.getGamesPlayed() + "\n\n" + 
				"Games won: " + profile.getWins() + "\n\n" + 
				"Games lost: " + profile.getLosses() + "\n\n" + 
				"Games tied: " + profile.getTies()
		);
		this.add(statisticsArea);

		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				Point p = new Point(me.getX(), me.getY());
				int choice = getSpaceClicked(p);
				if(choice != -1)
					setColor(choice);
				repaint();
			}
		});
	}

	public void setDoneCheck(boolean bool)
	{
		doneCheck = bool;
	}

	public boolean getDoneCheck()
	{
		return doneCheck;
	}

	public void setColor(int colorIndex)
	{
		profile.setColorIndex(colorIndex);
	}

	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		super.paint(g2);

		int buttonWidth = 80;
		int buttonHeight = 110;
		int xBuffer = 10;
		int yBuffer = 10;
		int colorCounter = 0;
		for(int y = 0; y < 4; y++)
		{
			for(int x = 0; x < 3; x++)
			{
				Rectangle current = (new Rectangle(40 + x * buttonWidth + x * xBuffer, 40 + y * buttonHeight + y * yBuffer, buttonWidth, buttonHeight));
				g2.setColor(colors[colorCounter]);
				g2.fill(current);
				g2.setColor(Color.BLACK);
				g2.setStroke(new BasicStroke(3));
				g2.draw(current);
				g2.setStroke(new BasicStroke(1));
				colorCounter++;
			}
		}

		g2.setColor(colors[profile.getColorIndex()]);
		try
		{
			g2.drawImage(ImageIO.read(new File("images/" + colorNames[profile.getColorIndex()] + "ball200.png")), 380, 180, null);
		}
		catch(IOException ioe){System.err.println("IOERR");}
	}

	public int getSpaceClicked(Point p)
	{
		int buttonWidth = 80;
		int buttonHeight = 110;
		int xBuffer = 10;
		int yBuffer = 10;
		int colorCounter = 0;
		for(int y = 0; y < 4; y++)
		{
			for(int x = 0; x < 3; x++)
			{
				Rectangle current = (new Rectangle(40 + x * buttonWidth + x * xBuffer, 40 + y * buttonHeight + y * yBuffer, buttonWidth, buttonHeight));
				if(current.contains(p))
					return colorCounter;
				colorCounter++;
			}
		}

		return -1;
	}
}