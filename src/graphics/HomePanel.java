package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class HomePanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private boolean logoutCheck = false;
	private boolean instructionsCheck = false;
	private boolean profileCheck = false;
	private boolean randomOpponentCheck = false;
	private boolean challengeCheck = false;
	private boolean messageCheck = false;

	private String username = "";
	private String newestMessage = "";
	private String newestChallenge = "";
	private ArrayList<String> onlineUsers;

	final private JList onlinePlayersList;
	final private JTextArea inputTextArea;
	final private JTextField outputTextField;
	final private DefaultListModel jListModel;
	final private JLabel usernameLabel;
	final private JButton logoutButton;
	final private JLabel clockLabel;

	public HomePanel()
	{
		super();
		this.setLayout(null);
		this.onlineUsers = new ArrayList<String>();

		usernameLabel = new JLabel("Logged in as: <" + username + ">");
		usernameLabel.setBounds(40, 20, 307, 23);
		usernameLabel.setFont(new Font("Lucida", 1, 15));
		this.add(usernameLabel);

		clockLabel = new JLabel();
		clockLabel.setBounds(420, 20, 110, 20);
		clockLabel.setFont(new Font("Lucida", 1, 13));
		this.add(clockLabel);

		JButton challengeRandomButton = new JButton("Challenge");
		challengeRandomButton.setBounds(675, 500, 170, 20);
		challengeRandomButton.setToolTipText("Challenge the selected opponent");
		challengeRandomButton.setFont(new Font("Lucida", 1, 12));
		challengeRandomButton.setFocusable(false);
		challengeRandomButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				newestChallenge = (String) onlinePlayersList.getSelectedValue();
				System.out.println(newestChallenge);
				challengeCheck = true;
			}});
		this.add(challengeRandomButton);

		JButton challengeButton = new JButton("Random Opponent");
		challengeButton.setBounds(675, 530, 170, 20);
		challengeButton.setToolTipText("Challenge a random opponent");
		challengeButton.setFont(new Font("Lucida", 1, 12));
		challengeButton.setFocusable(false);
		challengeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				randomOpponentCheck = true;
			}});
		this.add(challengeButton);

		JButton instructionsButton = new JButton("Instructions");
		instructionsButton.setBounds(130, 530, 170, 20);
		instructionsButton.setFont(new Font("Lucida", 1, 12));
		instructionsButton.setFocusable(false);
		instructionsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				instructionsCheck = true;
			}});
		this.add(instructionsButton);

		JButton profileButton = new JButton("Profile");
		profileButton.setBounds(310, 530, 170, 20);
		profileButton.setToolTipText("Wins, losses, and player settings");
		profileButton.setFont(new Font("Lucida", 1, 12));
		profileButton.setFocusable(false);
		profileButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				profileCheck = true;
			}});
		this.add(profileButton);

		logoutButton = new JButton("Logout");
		logoutButton.setBounds(560, 20, 100, 20);
		logoutButton.setFont(new Font("Lucida", 1, 12));
		logoutButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				logoutCheck = true;
			}});
		logoutButton.setFocusable(false);
		this.add(logoutButton);

		JLabel onlinePlayersLabel = new JLabel("Online Players");
		onlinePlayersLabel.setBounds(700, 20, 170, 23);
		onlinePlayersLabel.setFont(new Font("Lucida", 1, 15));
		this.add(onlinePlayersLabel);

		jListModel = new DefaultListModel();
		onlinePlayersList = new JList(jListModel);
		onlinePlayersList.setFont(new Font("Lucida", 1, 12));
		onlinePlayersList.setBounds(675, 60, 170, 420);
		onlinePlayersList.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		newestChallenge = (String) onlinePlayersList.getSelectedValue();
		this.add(onlinePlayersList);

		inputTextArea = new JTextArea();
		inputTextArea.setLineWrap(true);
		inputTextArea.setWrapStyleWord(true);
		inputTextArea.setEditable(false);
		inputTextArea.setFont(new Font("Lucida", 1, 12));
		inputTextArea.setMargin(new Insets(5, 15, 5, 5));
		JScrollPane outputScrollPane = new JScrollPane(inputTextArea);
		outputScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		outputScrollPane.setBounds(40, 60, 620, 420);
		this.add(outputScrollPane);

		outputTextField = new JTextField();
		outputTextField.setBounds(40, 500, 510, 20);
		outputTextField.setFont(new Font("Lucida", 1, 12));
		outputTextField.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				sendMessage();
			}
		});
		this.add(outputTextField);

		JButton sendButton = new JButton("Send");
		sendButton.setBounds(560, 500, 100, 20);
		sendButton.setToolTipText("Send a message");
		sendButton.setFont(new Font("Lucida", 1, 12));
		sendButton.setFocusable(false);
		sendButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				sendMessage();
			}});
		this.add(sendButton);
	}

	public void sendMessage()
	{
		String message = "";
		message = outputTextField.getText();
		outputTextField.setText("");
		boolean hasCharacters = false;
		for(int i = 0; i < message.length(); i++)
			if(message.charAt(i) != ' ')
				hasCharacters = true;
		if(hasCharacters && !message.equals(""))
		{
			messageCheck = true;
			newestMessage = message;
		}
	}

	public void setChatText(String text)
	{
		inputTextArea.setText(text);
	}

	public void refreshOnlineUsers()
	{
		String selected = (String) onlinePlayersList.getSelectedValue();
		boolean hasSelected = (selected != null && !selected.equals("") && !selected.equals(username));
		jListModel.clear();
		for(int i = 0; i < onlineUsers.size(); i++)
			jListModel.add(i, onlineUsers.get(i));
		if(hasSelected);
		for(int i = 0; i < onlineUsers.size(); i++)
			if(jListModel.get(i).equals(selected))
				onlinePlayersList.setSelectedIndex(i);
	}

	public void refreshClock()
	{
		DateFormat dateFormat = new SimpleDateFormat("h:mm:ss a");
		Date date = new Date();
		clockLabel.setText(dateFormat.format(date));
	}

	public JTextField getOutputField()
	{
		return outputTextField;
	}

	public void setOnlineUsers(ArrayList<String> usernames) {
		onlineUsers = usernames;
	}

	public ArrayList<String> getOnlineUsers() {
		return onlineUsers;
	}

	public void setUsername(String inUsername) {
		username = inUsername;
		usernameLabel.setText("Logged in as: <" + username + ">");
	}

	public void setLogoutCheck(boolean bool) {
		logoutCheck = bool;
	}

	public boolean getLogoutCheck() {
		return logoutCheck;
	}

	public void setInstructionsCheck(boolean instructionsCheck) {
		this.instructionsCheck = instructionsCheck;
	}

	public boolean getInstructionsCheck() {
		return instructionsCheck;
	}

	public String getMessage()
	{
		return newestMessage;
	}

	public void setStatsCheck(boolean profileCheck) {
		this.profileCheck = profileCheck;
	}

	public boolean getStatsCheck() {
		return profileCheck;
	}

	public void setRandomOpponentCheck(boolean randomOpponentCheck) {
		this.randomOpponentCheck = randomOpponentCheck;
	}

	public boolean getRandomOpponentCheck() {
		return randomOpponentCheck;
	}

	public void setChallengeCheck(boolean challengeCheck) {
		this.challengeCheck = challengeCheck;
	}

	public boolean getChallengeCheck() {
		return challengeCheck;
	}

	public void setMessageCheck(boolean messageCheck) {
		this.messageCheck = messageCheck;
	}

	public boolean getMessageCheck() {
		return messageCheck;
	}
}