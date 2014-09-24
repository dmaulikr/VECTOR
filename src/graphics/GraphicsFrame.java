package graphics;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import networking.Profile;
import networking.Client;

import physics.Configuration;
import physics.Engine;

public class GraphicsFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	private LoginPanel loginPanel;
	private ConnectionPanel connectionPanel;
	private HomePanel homePanel;
	private VectorPanel vectorPanel;
	private GamePanel gamePanel;
	private ProfilePanel profilePanel;
	private InstructionsPanel instructionsPanel;

	private Configuration config;
	private Engine engine;
	private Timer time;

	private boolean sentProfileRequest = false;

	private Profile profile;
	private Client client;
	private char playerSide;

	private int frameWidth;
	private int frameHeight;

	private static final int TIME_DELAY = 20;

	public GraphicsFrame(String title, int frameWidth, int frameHeight)
	{
		super(title);
		this.setLayout(null);
		this.getContentPane().setBackground(Color.BLACK);

		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;

		config = new Configuration(frameWidth, frameHeight);
		engine = new Engine(frameWidth, frameHeight);

		loginPanel = new LoginPanel();
		connectionPanel = new ConnectionPanel();
		homePanel = new HomePanel();
		instructionsPanel = new InstructionsPanel();
		
		runLoginPanel();
	}

	private void runLoginPanel()
	{
		loginPanel = new LoginPanel();
		this.setContentPane(loginPanel);
		this.setBounds(0, 0, loginPanel.getWidth(), loginPanel.getHeight());
		this.setLocationRelativeTo(null);
		client = new Client();
		if(time != null && time.isRunning())
			time.stop();
		time = new Timer(TIME_DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(loginPanel.getLoginCheck())
				{
					homePanel = new HomePanel();
					runConnectionPanel();
					System.out.println("[Login complete]");
				}
				if(loginPanel.getExitCheck())
				{
					System.out.println("[Program terminated]");
					System.exit(0);
				}
			}});
		time.start();
	}

	private void runConnectionPanel()
	{
		connectionPanel = new ConnectionPanel();
		this.setContentPane(connectionPanel);
		this.setBounds(0, 0, connectionPanel.getWidth(), connectionPanel.getHeight());
		this.setLocationRelativeTo(null);

		client.connect();
		if(client.getTroubleConnecting())
		{
			JOptionPane.showConfirmDialog(null, "Trouble connecting to server, check your internet \n " +
					"or contact the creators of Vector", "Vector", 2, 0);
			runLoginPanel();
			return;
		}
		String username = loginPanel.getUsernameInput();
		String password = loginPanel.getPasswordInput();
		client.setUsername(username);
		client.setPassword(password);
		client.sendData("COMBO•0000•" + username + " " + password);
		loginPanel.setLoginCheck(false);

		final int randomSeconds = new Random().nextInt(3) + 3;
		final int[] goodLogin = {0};
		final int[] secondsCounter = {0};

		final Timer waiter = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				secondsCounter[0]++;
			}
		});
		waiter.start();

		if(time != null && time.isRunning())
			time.stop();
		time = new Timer(TIME_DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(client.isCorrectCombo() == 1)
				{
					goodLogin[0] = 1;
					if(sentProfileRequest == false)
					{
						client.sendData("PROFILE•" + client.getID() + "•NULL");
						sentProfileRequest = true;
					}
				}
				if(client.isCorrectCombo() == -1)
					goodLogin[0] = -1;
				if(secondsCounter[0] >= randomSeconds && goodLogin[0] == 1)
				{
					waiter.stop();
					System.out.println("[Password and username accepted]");
					runHomePanel();
					return;
				}
				if(secondsCounter[0] >= randomSeconds && goodLogin[0] == -1)
				{
					waiter.stop();
					System.out.println("[Password and username declined]");
					JOptionPane.showConfirmDialog(null, "Incorrect username/password combination", "Vector", 2, 2);
					runLoginPanel();
					return;
				}
				if(connectionPanel.getCancelCheck())
				{
					System.out.println("[Connection cancelled]");
					runLoginPanel();
					return;
				}
			}});
		time.start();
	}

	private void runHomePanel()
	{
		System.out.println("[Home panel]");
		this.setBounds(0, 0, frameWidth, frameHeight);
		this.setLocationRelativeTo(null);
		this.setContentPane(homePanel);
		if(profile == null)
		{
			String profileData = client.getData();
			profile = new Profile(client.getUsername());
			profile.setWithCode(profileData);
		}
		homePanel.setUsername(profile.getUsername());
		this.addWindowListener(new WindowAdapter() {
			public void windowOpened( WindowEvent e ){
				homePanel.getOutputField().requestFocus();
			}
		}); 
		homePanel.repaint();
		if(time != null && time.isRunning())
			time.stop();
		time = new Timer(TIME_DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(homePanel.getChallengeCheck())
				{
					//TODO: See if the person accepts challenge
					; //TODO: Get the playerSide
					playerSide = 'a';
					runVectorPanel();
				}
				if(homePanel.getRandomOpponentCheck())
				{
					//TODO: See if the person accepts challenge
					; //TODO: Get the playerSide
					playerSide = 'a';
					runVectorPanel();
				}
				if(homePanel.getInstructionsCheck())
				{
					homePanel.setInstructionsCheck(false);
					runInstructionsPanel();
				}
				if(homePanel.getStatsCheck())
				{
					homePanel.setStatsCheck(false);
					runProfilePanel();
				}
				if(homePanel.getLogoutCheck())
				{
					client.sendData("LOGOUT•" + client.getID() + "•NULL");
					sentProfileRequest = false;
					homePanel.setLogoutCheck(false);
					runLoginPanel();
					profile = null;
					playerSide = ' ';
					config = new Configuration(frameWidth, frameHeight);
					return;
				}
				if(homePanel.getMessageCheck())
				{
					homePanel.setMessageCheck(false);
					String message = homePanel.getMessage();
					client.sendData("CHAT•" + client.getID() + "•" + message);
				}
				homePanel.refreshClock();
				homePanel.refreshOnlineUsers();
				homePanel.setOnlineUsers(client.getOnlinePlayers());
				homePanel.setChatText(client.getChatData());
			}});
		time.start();
	}

	private void runGamePanel()
	{
		this.setBounds(this.getX(), this.getY(), frameWidth, frameHeight + 40);
		gamePanel = new GamePanel(config, profile, frameWidth, frameHeight);
		engine = new Engine(frameWidth, frameHeight);
		this.setContentPane(gamePanel);
		if(time != null && time.isRunning())
			time.stop();
		time = new Timer(TIME_DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(engine.doneSimulating())
				{
					System.out.println("[Simulation done]");
					runVectorPanel();
				}
				else
				{
					config = engine.refresh(config);
					gamePanel.repaint();
				}
			}});
		time.start();
	}

	private void runVectorPanel()
	{
		this.setBounds(this.getX(), this.getY(), frameWidth, frameHeight + 40);
		vectorPanel = new VectorPanel(config, profile, playerSide, frameWidth, frameHeight);
		if(time != null && time.isRunning())
			time.stop();
		this.setContentPane(vectorPanel);
		time = new Timer(TIME_DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(vectorPanel.getDoneCheck())
					vectorPanel.setVectorCheck(true);
				if(vectorPanel.getForfeitCheck() || vectorPanel.getVectorCheck())
				{
					if(vectorPanel.getWinningPlayer() == 't') //TIE
					{
						;//TODO:
						System.out.println("GAME OVER");
					}
					else if(vectorPanel.getWinningPlayer() == 'a') //WINNER IS A
					{
						;//TODO:
						System.out.println("GAME OVER");
					}
					else if(vectorPanel.getWinningPlayer() == 'b') //WINNER IS B
					{
						;//TODO:
						System.out.println("GAME OVER");
					}
					if(vectorPanel.getForfeitCheck())
					{
						vectorPanel.stopLoadTimer();
						int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want\n to forfeit this game?", "Vector", 0, 2);
						if(choice == 0)
						{
							//TODO: Forfeit, set player levels with server
							runHomePanel();
						}
						else if(choice == 1)
							vectorPanel.setForfeitCheck(false);
						vectorPanel.startLoadTimer();
					}
					if(vectorPanel.getVectorCheck())
					{
						System.out.println("[Vectors done]");
						//TODO: Give the server the configuration and the receive a new updated configuration
						vectorPanel.setConfigWithNewPoints();
						repaint();
						runGamePanel();
					}
				}
				else
					vectorPanel.repaint();
			}});
		time.start();
	}

	private void runProfilePanel()
	{
		System.out.println("[Profile Panel]");
		profilePanel = new ProfilePanel(profile);
		profilePanel.setBounds(0, 0, frameWidth, frameHeight);
		this.setBounds(this.getX(), this.getY(), frameWidth, frameHeight);
		if(time != null && time.isRunning())
			time.stop();
		this.setContentPane(profilePanel);
		this.repaint();
		time = new Timer(TIME_DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(profilePanel.getDoneCheck())
					runHomePanel();
			}});
		time.start();
	}

	private void runInstructionsPanel()
	{
		System.out.println("[Instructions Panel]");
		instructionsPanel.setBounds(0, 0, frameWidth, frameHeight);
		this.setBounds(this.getX(), this.getY(), frameWidth, frameHeight);
		if(time != null && time.isRunning())
			time.stop();
		this.setContentPane(instructionsPanel);
		time = new Timer(TIME_DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(instructionsPanel.getDoneCheck())
				{
					instructionsPanel.setDoneCheck(false);
					runHomePanel();
				}
			}});
		time.start();
	}
}