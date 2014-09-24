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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import networking.Profile;

import physics.Configuration;

public class VectorPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private Configuration config;
	private Profile profile;
	private Timer time;
	private static int BALL_RADIUS = 20;
	private static int HYPOTENUSE_MAX = 100;
	private BufferedImage redImage = null;
	private BufferedImage blueImage = null;
	private BufferedImage background = null;

	private boolean vectorCheck = false;
	private boolean doneCheck = false;
	private boolean forfeitCheck = false;
	private char winningPlayer = 'n';

	private static Rectangle doneRec;
	private static Rectangle forfeitRec;
	private static Rectangle loadRec1;
	private Rectangle loadRec2;

	private double[] newXs = new double[3];
	private double[] newYs = new double[3];

	private Point oldP;
	private Graphics2D g2;
	private int currentBall;
	private char playerChar;

	public VectorPanel(Configuration config, Profile profile, char playerChar, int frameWidth, int frameHeight)
	{
		super();
		this.setLayout(null);
		this.config = config;
		this.profile = profile;
		this.setFocusable(true);
		this.setBounds(0, 0, frameWidth, frameHeight + 30);
		this.playerChar = playerChar;

		for(int i = 0; i < 3; i++)
		{
			newXs[i] = config.getXorY('x', playerChar, i);
			newYs[i] = config.getXorY('y', playerChar, i);
		}

		setWinner();

		doneRec = new Rectangle(50, 583, 150, 30);
		forfeitRec = new Rectangle(700, 583, 150, 30);
		loadRec1 = new Rectangle(250, 585, 400, 25);
		loadRec2 = new Rectangle(250, 585, 0, 25);
		currentBall = 0;

		try
		{
			redImage = ImageIO.read(new File("images/orangeball40.png"));
			blueImage = ImageIO.read(new File("images/redball40.png"));
			background = ImageIO.read(new File("images/background.jpg"));
		}
		catch (IOException e) {System.err.print("IOERR");}

		this.addMouseMotionListener(new MouseAdapter() { 
			public void mouseDragged(MouseEvent me) { 
				setNewPoints(me.getPoint());
			}
		});

		this.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent me) { 
				oldP = new Point(me.getX(), me.getY());
				if(doneRec.contains(oldP))
					doneCheck = true;
				if(forfeitRec.contains(oldP))
					forfeitCheck = true;
				if(indexOfClickedBall(me.getPoint()) != -1)
					currentBall = indexOfClickedBall(me.getPoint());
			}
		});

		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent me) {
				int d = me.getKeyCode();
				if(d == KeyEvent.VK_ENTER)
					vectorCheck = true;
				if(d == KeyEvent.VK_ESCAPE)
					forfeitCheck = true;
			}
		});

		time = new Timer(0, new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				if(loadRec2.getWidth() <= 400)
					loadRec2.setSize((int)(loadRec2.getWidth() + 1), (int)(loadRec2.getHeight()));
				else
					vectorCheck = true;
			}});
		time.setDelay(15);
		time.start();
	}

	public char getWinningPlayer()
	{
		return winningPlayer;
	}

	private char setWinner()
	{
		byte playerAOut = 0;
		byte playerBOut = 0;
		for(int i = 0; i < 3; i++)
		{
			if(config.getXorY('x', 'a', i) < -20 && config.getXorY('y', 'a', i) < -20);
			playerAOut++;
			if(config.getXorY('x', 'b', i) < -20 && config.getXorY('y', 'b', i) < -20);
			playerBOut++;
		}

		if(playerAOut == 3 && playerBOut == 3)
			return 't';
		else if(playerAOut == 3)
			return 'a';
		else if(playerBOut == 3)
			return 'b';
		else
			return 'n';
	}

	public void stopLoadTimer()
	{
		time.stop();
	}

	public void startLoadTimer()
	{
		time.start();
	}

	private int indexOfClickedBall(Point point)
	{
		Ellipse2D.Double[] ellipses = new Ellipse2D.Double[3];
		for(int i = 0; i < 3; i++)
			ellipses[i] = new Ellipse2D.Double(config.getXorY('x', playerChar, i) -
					BALL_RADIUS, config.getXorY('y', playerChar, i) - BALL_RADIUS, BALL_RADIUS * 2, BALL_RADIUS * 2);
		Point p = new Point((int)point.getX(), (int)point.getY());
		for(int i = 0; i < 3; i++)
			if(ellipses[i].contains(p))
				return i;
		return -1;
	}

	private void setNewPoints(Point point)
	{
		double centerX = config.getXorY('x', playerChar, currentBall);
		double centerY = config.getXorY('y', playerChar, currentBall);
		double newHypotenuse = Math.sqrt(Math.pow(point.getY() - centerY, 2) + Math.pow(point.getX() - centerX, 2));
		if(newHypotenuse > HYPOTENUSE_MAX)
		{
			newXs[currentBall] = centerX + (Math.cos(Math.atan2((point.getY() - centerY), (point.getX() - centerX))) * HYPOTENUSE_MAX);
			newYs[currentBall] = centerY + (Math.sin(Math.atan2((point.getY() - centerY), (point.getX() - centerX))) * HYPOTENUSE_MAX);
		}
		else
		{
			newXs[currentBall] = centerX + (Math.cos(Math.atan2((point.getY() - centerY), (point.getX() - centerX))) * newHypotenuse);
			newYs[currentBall] = centerY + (Math.sin(Math.atan2((point.getY() - centerY), (point.getX() - centerX))) * newHypotenuse);
		}
	}

	public void setConfigWithNewPoints()
	{
		double lineLengthToMagnitudeRatio = 35;
		for(int i = 0; i < 3; i++)
		{
			config.setAngle(Math.atan2(newYs[i] - config.getXorY('y', playerChar, i), newXs[i] - config.getXorY('x', playerChar, i)), playerChar, i);
		}
		for(int i = 0; i < 3; i++)
			config.setMagnitude(Math.sqrt(Math.pow(newXs[i] - config.getXorY('x', playerChar, i), 2) + 
					Math.pow(newYs[i] - config.getXorY('y', playerChar, i), 2)) / lineLengthToMagnitudeRatio, playerChar, i);
	}

	public boolean getVectorCheck()
	{
		return vectorCheck;
	}

	public void setVectorCheck(boolean bool)
	{
		vectorCheck = bool;
	}

	public boolean getDoneCheck()
	{
		return doneCheck;
	}

	public boolean getForfeitCheck()
	{
		return forfeitCheck;
	}

	public void setForfeitCheck(boolean bool)
	{
		forfeitCheck = bool;
	}

	public void setPlayerSide(char playerSide)
	{
		this.playerChar = playerSide;
	}

	public void paint(Graphics g)
	{
		g2 = (Graphics2D) g;
		super.paint(g2);

		g2.drawImage(background, 0, 0, null);

		g2.setStroke(new BasicStroke(2));

		g2.setColor(Color.LIGHT_GRAY);
		doneRec.translate(-3, -3);
		g2.fill(doneRec);
		doneRec.translate(3, 3);
		forfeitRec.translate(-3, -3);
		g2.fill(forfeitRec);
		forfeitRec.translate(3, 3);

		g2.draw(doneRec);
		g2.draw(forfeitRec);
		g2.draw(loadRec1);
		g2.setColor(Color.DARK_GRAY);
		g2.fill(doneRec);
		g2.fill(forfeitRec);
		g2.setColor(Color.LIGHT_GRAY);
		g2.fill(loadRec2);

		g2.setFont(new Font("Impact", 1, 24));
		g2.drawString("Forfeit", (int)(forfeitRec.getX()) + 38, (int)(forfeitRec.getY()) + 24);
		g2.drawString("Ready", (int)(doneRec.getX()) + 38, (int)(doneRec.getY()) + 24);

		g2.setStroke(new BasicStroke(2));
		for(int i = 0; i < 3; i++)
		{
			if(playerChar == 'a')
			{
				g2.setColor(Color.WHITE);
				g2.drawLine((int)(config.getXorY('x', 'a', i)), (int)(config.getXorY('y', 'a', i)), (int)(newXs[i]), (int)(newYs[i]));
			}
			else if(playerChar == 'b')
			{
				g2.setColor(Color.WHITE);
				g2.drawLine((int)(config.getXorY('x', 'b', i)), (int)(config.getXorY('y', 'b', i)), (int)(newXs[i]), (int)(newYs[i]));
			}
		}

		for(int i = 0; i < 3; i++)
		{
			double x = config.getXorY('x', 'a', i);
			double y = config.getXorY('y', 'a', i);
			g2.drawImage(redImage, (int)(x - BALL_RADIUS), (int)(y - BALL_RADIUS), null);
		}
		for(int i = 0; i < 3; i++)
		{
			double x = config.getXorY('x', 'b', i);
			double y = config.getXorY('y', 'b', i);
			g2.drawImage(blueImage, (int)(x - BALL_RADIUS), (int)(y - BALL_RADIUS), null);
		}

		for(int i = 0; i < 3; i++)
		{
			double x = config.getXorY('x', playerChar, i) - BALL_RADIUS;
			double y = config.getXorY('y', playerChar, i) - BALL_RADIUS;
			if(i == currentBall)
			{
				g2.setStroke(new BasicStroke(2));
				g2.setColor(Color.LIGHT_GRAY);
			}
			else
			{
				g2.setStroke(new BasicStroke(1));
				g2.setColor(Color.LIGHT_GRAY);
			}
			g2.draw(new Ellipse2D.Double(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2));
		}
	}
}