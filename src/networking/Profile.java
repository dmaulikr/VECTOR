package networking;

public class Profile 
{
	private String username;
	private String ID;
	private int colorIndex;
	private int points;
	private int level;
	private int gamesPlayed;
	private int wins;
	private int losses;
	private int ties;

	public Profile(String username)
	{
		this.username = username;
		colorIndex = 0;
		level = 1;
		setGamesPlayed(0);
		setWins(0);
		setLosses(0);
		setTies(0);
	}

	public void setUsername(String username) 
	{
		this.username = username;
	}

	public String getUsername() 
	{
		return username;
	}

	public void setLevel(int level) 
	{
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setColorIndex(int color) 
	{
		this.colorIndex = color;
	}

	public int getColorIndex() 
	{
		return colorIndex;
	}

	public void setGamesPlayed(int games) 
	{
		this.gamesPlayed = games;
	}

	public int getGamesPlayed() 
	{
		return gamesPlayed;
	}

	public void setWins(int wins) 
	{
		this.wins = wins;
	}

	public int getWins() 
	{
		return wins;
	}

	public void setLosses(int losses) 
	{
		this.losses = losses;
	}

	public int getLosses() 
	{
		return losses;
	}

	public void setTies(int ties) 
	{
		this.ties = ties;
	}

	public int getTies() 
	{
		return ties;
	}

	public boolean addPoints(int points) 
	{
		this.points = this.points + points;
		boolean isLevelUp = false;
		
		while(points >= Math.pow(level, 2))
		{
			level = level + 1;
			isLevelUp = true;
		}
		
		return isLevelUp;
	}

	public int getPoints() 
	{
		return points;
	}

	public void setID(String iD) 
	{
		ID = iD;
	}

	public String getID() 
	{
		return ID;
	}

	public String toCode()
	{
		return username + " " + colorIndex + " " + level + " " + gamesPlayed + " " + wins + " " + losses + " " + ties;
	}

	public void setWithCode(String code)
	{
		String[] strings = code.split(" ");
		username = strings[0];
		colorIndex = Integer.parseInt(strings[1]);
		level = Integer.parseInt(strings[2]);
		gamesPlayed = Integer.parseInt(strings[3]);
		wins = Integer.parseInt(strings[4]);
		losses = Integer.parseInt(strings[5]);
		ties = Integer.parseInt(strings[6]);
	}

	public String toString()
	{
		return "[Username: " + username + "] [Color: " + colorIndex + "] [Level: " + level + "] [Games Played: " + gamesPlayed + 
		"] [Wins: " + wins + "] [Losses: " + losses + "] [Ties: " + ties + "]";
	}
}