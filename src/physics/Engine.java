package physics;

import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Engine
{
	private static double FRICTION;
	private static double STOP_POINT;
	private boolean simulating;
	private Configuration config;

	private int frameWidth;
	private int frameHeight;

	private static int BALL_RADIUS = 20;
	private static int bannerWidth = 22;
	private int collisionCount = 0;

	public Engine(int frameWidth, int frameHeight)
	{
		FRICTION = .01;
		STOP_POINT = .1;
		simulating = true;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
	}

	public Configuration refresh(Configuration config)
	{
		this.config = config;
		friction();
		ballCollision(true);
		wallCollision();
		move();
		fallenOff();
		return config;
	}

	private void friction()
	{
		int stoppedCount = 0;
		for(int i = 0; i < 3; i++)
		{
			if(config.getMagnitude('a', i) > STOP_POINT)
				config.setMagnitude(config.getMagnitude('a', i) - FRICTION, 'a', i);
			else
			{
				stoppedCount++;
				config.setMagnitude(0, 'a', i);
			}
		}
		for(int i = 0; i < 3; i++)
		{
			if(config.getMagnitude('b', i) > STOP_POINT)
				config.setMagnitude(config.getMagnitude('b', i) - FRICTION, 'b', i);
			else
			{
				stoppedCount++;
				config.setMagnitude(0, 'b', i);
			}
		}
		if(stoppedCount == 6)
			simulating = false;
	}

	private void move()
	{
		for(int i = 0; i < 3; i++)
		{
			config.setXandY(config.getXorY('x', 'a', i) + Math.cos(config.getAngle('a', i)) * config.getMagnitude('a', i), 
					config.getXorY('y', 'a', i) + Math.sin(config.getAngle('a', i)) * config.getMagnitude('a', i), 'a', i);
			config.setXandY(config.getXorY('x', 'b', i) + Math.cos(config.getAngle('b', i)) * config.getMagnitude('b', i), 
					config.getXorY('y', 'b', i) + Math.sin(config.getAngle('b', i)) * config.getMagnitude('b', i), 'b', i);
		}
	}

	private void wallCollision()
	{
		for(int n = 1; n <= 4; n++)
		{
			for(int i = 0; i < 3; i++)
			{
				if(touchingWall(config.getXorY('x', 'a', i), config.getXorY('y', 'a', i), n))
					config.setAngle(getAngleOfReflection(config.getAngle('a', i), n), 'a', i);
			}
			for(int i = 0; i < 3; i++)
			{
				if(touchingWall(config.getXorY('x', 'b', i), config.getXorY('y', 'b', i), n))
					config.setAngle(getAngleOfReflection(config.getAngle('b', i), n), 'b', i);
			}
		}
		move();
	}

	private double getAngleOfReflection(double angle, int numWall)
	{
		if(numWall == 1)
			return angle * -1;
		else if(numWall == 2)
			return Math.PI - angle;
		else if(numWall == 3)
			return angle * -1;
		else if(numWall == 4)
			return Math.PI - angle;
		else
			return 0;
	}

	private boolean touchingWall(double x, double y, int numWall)
	{
		if(numWall == 1 && y - BALL_RADIUS <= 0)
			return true;
		else if(numWall == 2 && x + BALL_RADIUS >= frameWidth)
			return true;
		else if(numWall == 3 && y + BALL_RADIUS + bannerWidth >= frameHeight)
			return true;
		else if(numWall == 4 && x - BALL_RADIUS <= 0)
			return true;
		return false;
	}

	private void ballCollision(boolean change)
	{
		Ellipse2D.Double[] ellipses = new Ellipse2D.Double[6];
		for(int i = 0; i < 3; i++)
			ellipses[i] = new Ellipse2D.Double(config.getXorY('x', 'a', i), 
					config.getXorY('y', 'a', i), BALL_RADIUS * 2, BALL_RADIUS * 2);
		for(int i = 0; i < 3; i++)
			ellipses[i + 3] = new Ellipse2D.Double(config.getXorY('x', 'b', i), 
					config.getXorY('y', 'b', i), BALL_RADIUS * 2, BALL_RADIUS * 2);

		ArrayList<Integer> savex = new ArrayList<Integer>();
		ArrayList<Integer> savey = new ArrayList<Integer>();
		
		for(int x = 0; x < 6; x++)
		{
			for(int y = 0; y < 6; y++)
			{
				if(x != y)
				{
					if(Math.sqrt(Math.pow(ellipses[x].getX() - ellipses[y].getX(), 2) + 
							Math.pow(ellipses[x].getY() - ellipses[y].getY(), 2)) <= 2 * BALL_RADIUS)
					{
						boolean already = false;
						for(int i = 0; i < savex.size(); i++)
							if(x == savey.get(i) && y == savex.get(i))
								already = true;
						if(!already)
						{
							savex.add(x);
							savey.add(y);
							collisionCount++;
							//System.out.println("BallColl: " + collisionCount + ", x=" + x + ", y=" + y);
							changeFromBallCollision(x, y);
						}
					}
				}
			}
		}
	}

	private void changeFromBallCollision(int ball1I, int ball2I)
	{
		char ball1Char = ' ';
		char ball2Char = ' ';
		int ball1Index = 0;
		int ball2Index = 0;

		if(ball1I < 3)
		{
			ball1Char = 'a';
			ball1Index = ball1I;
		}
		else
		{
			ball1Char = 'b';
			ball1Index = ball1I - 3;
		}
		if(ball2I < 3)
		{
			ball2Char = 'a';
			ball2Index = ball2I;
		}
		else
		{
			ball2Char = 'b';
			ball2Index = ball2I - 3;
		}

		{ // Set New Magnitudes
			///*
			double mag1 = config.getMagnitude(ball1Char, ball1Index);
			double mag2 = config.getMagnitude(ball2Char, ball2Index);
			config.setMagnitude(mag2, ball1Char, ball1Index);
			config.setMagnitude(mag1, ball2Char, ball2Index);
			//*/
		}

		{ // Set New Angles
			
			
			double ball1X = config.getXorY('x', ball1Char, ball1Index);
			double ball1Y = config.getXorY('y', ball1Char, ball1Index);
			double ball2X = config.getXorY('x', ball2Char, ball2Index);
			double ball2Y = config.getXorY('y', ball2Char, ball2Index);

			double collisionX = (ball1X + ball2X) / 2;
			double collisionY = (ball1Y + ball2Y) / 2;

			/*
			double connectSlope = (ball2Y - ball1Y) / (ball2X - ball1X);
			double normalSlope = connectSlope * -1;

			double v1Angle = config.getAngle(ball1Char, ball1Index);
			double v2Angle = config.getAngle(ball2Char, ball2Index);
			double v1Magnitude = config.getAngle(ball1Char, ball1Index);
			double v2Magnitude = config.getAngle(ball2Char, ball2Index);
			
			double ball1X2 = ball1X + (v1Magnitude * Math.cos(v1Angle));
			double ball1Y2 = ball1Y + (v1Magnitude * Math.cos(v1Angle));
			double ball2X2 = ball2X + (v2Magnitude * Math.cos(v2Angle));
			double ball2Y2 = ball2Y + (v2Magnitude * Math.cos(v2Angle));
			
			double aAngle1 = Math.acos(((ball1X - collisionX) * (ball1X - ball1X2) + (ball1Y - collisionY) * (ball1Y - ball1Y2)) / 
					Math.sqrt(Math.pow(ball1X - collisionX, 2) + Math.pow(ball1Y - collisionY, 2)));
			double bAngle1 = 90 - aAngle1;
			double aAngle2 = Math.acos(((ball2X - collisionX) * (ball2X - ball2X2) + (ball2X - collisionX) * (ball2Y - ball2Y2)) / 
					Math.sqrt(Math.pow(ball2X - collisionX, 2) + Math.pow(ball2Y - collisionY, 2)));
			double bAngle2 = 90 - aAngle2;
			
			double v1a_mag = Math.cos(aAngle1) * v1Magnitude;
			double v1b_mag = Math.sin(bAngle1) * v1Magnitude;
			double v2a_mag = Math.cos(aAngle2) * v1Magnitude;
			double v2b_mag = Math.sin(bAngle2) * v1Magnitude;

			double v1NewAngle = Math.atan2((v1a_mag * Math.sin(aAngle1) + v2b_mag * Math.sin(bAngle2)), 
					(v1a_mag * Math.cos(aAngle1) + v2b_mag * Math.cos(bAngle2)));
			double v2NewAngle = Math.atan2((v2a_mag * Math.sin(aAngle2) + v1b_mag * Math.sin(bAngle1)), 
					(v2a_mag * Math.cos(aAngle2) + v1b_mag * Math.cos(bAngle1)));
			 */

			/* Proper physics
			config.setAngle(v1NewAngle, ball1Char, ball1Index);
			config.setAngle(v2NewAngle, ball2Char, ball2Index);
			*/

			///*  Perpendicular physics
			config.setAngle(Math.atan2(ball1Y - collisionY, ball1X - collisionX), ball1Char, ball1Index);
			config.setAngle(Math.atan2(ball2Y - collisionY, ball2X - collisionX), ball2Char, ball2Index);
			//*/
		}
	}

	private void fallenOff()
	{
		int radius = 150;
		Ellipse2D.Double ellipses[] = new Ellipse2D.Double[4];
		ellipses[0] = new Ellipse2D.Double(-radius + 35, -radius + 10, 2 * radius, 2 * radius);
		ellipses[1] = new Ellipse2D.Double(frameWidth - radius - 35, -radius + 10, 2 * radius, 2 * radius);
		ellipses[2] = new Ellipse2D.Double(frameWidth - radius - 35, frameHeight - radius - 35, 2 * radius, 2 * radius);
		ellipses[3] = new Ellipse2D.Double(-radius + 35, frameHeight - radius - 35, 2 * radius, 2 * radius);

		for(int i = 0; i < 3; i++)
		{
			for(int x = 0; x < 4; x++)
			{
				Point center = new Point((int)config.getXorY('x', 'a', i), (int)config.getXorY('y', 'a', i));
				if(ellipses[x].contains(center))
				{
					config.setXandY(-50, -50, 'a', i);
					config.setMagnitude(0, 'a', i);
				}
			}
		}

		for(int i = 0; i < 3; i++)
		{
			for(int x = 0; x < 4; x++)
			{
				Point center = new Point((int)config.getXorY('x', 'b', i), (int)config.getXorY('y', 'b', i));
				if(ellipses[x].contains(center))
				{
					config.setXandY(-50, -50, 'b', i);
					config.setMagnitude(0, 'b', i);
				}
			}
		}
	}

	public boolean doneSimulating()
	{
		return !simulating;
	}
}