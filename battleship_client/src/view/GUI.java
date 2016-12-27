	package view;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import connector.Connexion;
import connector.Communication;
import connector.ServerReader;
import engine.PlayerStatus;



/**
 * The Class ClientFrame.
 * 
 * This class create client's window containing two grids with five ships.
 */
public class GUI extends JFrame implements Runnable {
	
	private Socket cSocket;
	private Connexion cnx;
	private ServerReader sReader;
	private ObjectOutputStream out;
	private PlayerStatus status;
	private Tableau player;
	private Tableau opponent;
	private boolean isConnected;
	private boolean playerWin;
	private boolean gameOver;
	private JLabel lblPlayer;
	private JLabel lblOpponent;
	private JLabel lblShipLeft;
	private JLabel lblShipLeft_1;	
	private JLabel lblStatus;
	private JLabel lblStatus_1;	
	private JLabel infoText;
	private JLabel lblMessage_1;	
	private JButton btnPlay;
	private JButton btnNewGame;
	
	public GUI(Socket s, ObjectInputStream in, ObjectOutputStream out, Connexion cnx){
		super("BattleShip");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		isConnected = false;
		gameOver = false;
		playerWin = false;
		this.cnx = cnx;
		
		player = new Tableau(this);
		player.setBackground(Color.LIGHT_GRAY);
		player.setBounds(10, 150, 300, 300);
		getContentPane().add(player);

		opponent = new Tableau(this);
		opponent.setBackground(Color.LIGHT_GRAY);
		opponent.setBounds(320, 150, 300, 300);
		getContentPane().add(opponent);
		opponent.disableMouseEvents(2);
		opponent.hideAllShip();
		
		lblPlayer = new JLabel("Player");
		lblPlayer.setBounds(10, 11, 300, 14);
		getContentPane().add(lblPlayer);
		
		lblOpponent = new JLabel("Opponent");
		lblOpponent.setBounds(320, 11, 300, 14);
		getContentPane().add(lblOpponent);
		
		lblShipLeft = new JLabel("Ship Destroyed:");
		lblShipLeft.setBounds(10, 36, 300, 14);
		getContentPane().add(lblShipLeft);
		
		lblShipLeft_1 = new JLabel("Ship Desroyed:");
		lblShipLeft_1.setBounds(320, 36, 300, 14);
		getContentPane().add(lblShipLeft_1);
		
		lblStatus = new JLabel("Status");
		lblStatus.setBounds(10, 61, 300, 14);
		getContentPane().add(lblStatus);
		
		lblMessage_1 = new JLabel("Message");
		lblMessage_1.setBounds(320, 86, 300, 14);
		getContentPane().add(lblMessage_1);
		
		infoText = new JLabel("Message");
		infoText.setBounds(10, 86, 300, 14);
		getContentPane().add(infoText);
		
		lblStatus_1 = new JLabel("Status");
		lblStatus_1.setBounds(320, 61, 300, 14);
		getContentPane().add(lblStatus_1);
		
		btnPlay = new JButton("Play");
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				startGame();
			}
		});
		btnPlay.setEnabled(false);
		btnPlay.setBounds(109, 111, 89, 23);
		getContentPane().add(btnPlay);
		
		btnNewGame = new JButton("New Game");
		btnNewGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				newGame();
			}
		});
		btnNewGame.setEnabled(false);
		btnNewGame.setBounds(208, 111, 89, 23);
		getContentPane().add(btnNewGame);

		//pack();
		player.disableMouseEvents(1);
		opponent.disableMouseEvents(2);
		
		status = PlayerStatus.NOT_CONNECTED;
		lblStatus.setText("Not Connected");
		lblShipLeft.setText("Ship Destroyed: " + player.getShipDestroyed());
		lblShipLeft_1.setText("Ship Destroyed: " + opponent.getShipDestroyed());
		
		infoText.setText("trying to connect\n");
		cSocket = s;
		
		if(cSocket.isConnected()){
			this.out = out;
			this.sReader = ServerReader.getInstance(in);
			sReader.setClfrm(this);
			sReader.start();
			
			player.enableMouseEvents(1);
			btnPlay.setEnabled(true);
			status = PlayerStatus.CONNECTED;
			startGame();
		}else{
	        infoText.setText("Porblème réseau, essayez de redémarer\n");
		}
	}
	
	/**
	 * Sets the opponent ship position.
	 *
	 * @param flags integer array of ship position.
	 */
	public void setOpponentShipPosition(int[][][] flags){
		opponent.setOpponentShipPosition(flags);
	}
	
	/**
	 * Gets the player's ship position.
	 *
	 * @return player's ship position array.
	 */
	public int[][][] getMyShipPosition(){
		return player.getMyShipPosition();
	}
	
	/**
	 * Sets the opponent ship location.
	 *
	 * @param shipLocation opponent's ship layout.
	 */
	public void setOpponentShipLocation(int[][] shipLocation){
		opponent.setOpponentShipLocation(shipLocation);
	}
	
	/**
	 * Gets the player's ship location.
	 *
	 * @return player's ship layout.
	 */
	public int[][] getMyShipLocation(){
		return player.getMyShipLocation();
	}
	
	public void startGame(){
		//Start Button Clicked

		player.disableMouseEvents(1);
		
		//Send ship flags to server
		//wait for ship flags to arrive
		Communication msg = new Communication();
		msg.setMessageType(PlayerStatus.READY);
		msg.setShipPosition(getMyShipPosition());
		msg.setShipLocation(getMyShipLocation());
		sendMessage(msg);
		
		status = PlayerStatus.READY;
		lblStatus.setText("Ready");
		infoText.setText("Waiting for opponent");
		btnPlay.setEnabled(false);
	}

	public void newGame(){
		
		player.resetGameBoard();
		opponent.resetGameBoard();
		opponent.hideAllShip();
		
		player.enableMouseEvents(1);
		opponent.disableMouseEvents(2);
		
		btnPlay.setEnabled(true);
		btnNewGame.setEnabled(false);
		
		infoText.setText("Set Ships and start game\n");
		lblStatus.setText("Conneted");
		lblShipLeft.setText("Ship Destroyed: " + player.getShipDestroyed());
		lblShipLeft_1.setText("Ship Destroyed: " + opponent.getShipDestroyed());
		
		status = PlayerStatus.CONNECTED;
		Communication msg = new Communication();
		msg.setMessageType(PlayerStatus.CONNECTED);
		sendMessage(msg);
		
		repaint();
	}
    
	/**
	 * Game over status.
	 *
	 * @param win Set if game won or lost.
	 */
	public void gameOverStatus(boolean win){
		gameOver = true;
		this.playerWin = win; 		
	}	
	
	/**
	 * Message object received from server.
	 *
	 * @param message the Message object.
	 */
	public void messageReceived(Object message) {
		// TODO Auto-generated method stub
		Communication srMsg = (Communication) message;
		updateClient(srMsg);
		
		//lblMessage.setText("Message received " + srMsg.getMessageType());
		
	}
	
	/**
	 * Update the client with received message.
	 *
	 * @param srMsg the Message Object.
	 */
	private void updateClient(Communication srMsg) {
		
		switch(srMsg.getMessageType()){
		case NOT_CONNECTED:
			lblStatus_1.setText("Not Connected");
			break;
		case WAITINGOP:
			lblStatus_1.setText("En attente d'adversaire");
			btnPlay.setEnabled(false);
		case CONNECTED:
			lblStatus_1.setText("Connected");
			btnPlay.setEnabled(true);
			cnx.dispose();
			break;
		case READY:
			//Set oponent ships
			lblStatus_1.setText("Ready");
			setOpponentShipPosition(srMsg.getShipPosition());
			setOpponentShipLocation(srMsg.getShipLocation());			
			
			break;
		case TURN:
			//if first turn game start
			if(status == PlayerStatus.READY){
				setOpponentShipPosition(srMsg.getShipPosition());
				setOpponentShipLocation(srMsg.getShipLocation());
				
				lblStatus.setText("Game Started");
				lblStatus_1.setText("Game Started");	
			}
			
			status = PlayerStatus.TURN;
			opponent.enableMouseEvents(2);
			if(srMsg.isFlag()){
			//with coordinate
				player.setHitAtCell(srMsg.getRow(), srMsg.getColumn());	
			}
			
			lblShipLeft.setText("Ship Destroyed: " + player.getShipDestroyed());
			lblShipLeft_1.setText("Ship Destroyed: " + opponent.getShipDestroyed());
			infoText.setText("Your turn");
			lblMessage_1.setText("Waiting for your turn");
			
			break;
		case WAIT:
			if(status == PlayerStatus.READY){				
				lblStatus.setText("Game Started");
				lblStatus_1.setText("Game Started");	
			}
			
			status = PlayerStatus.WAIT;
			opponent.disableMouseEvents(2);			
			if(srMsg.isFlag()){
			//with coordinate
				player.setHitAtCell(srMsg.getRow(), srMsg.getColumn());
			}
			
			lblShipLeft.setText("Ship Destroyed: " + player.getShipDestroyed());
			lblShipLeft_1.setText("Ship Destroyed: " + opponent.getShipDestroyed());
			
			infoText.setText("Waiting for opponent's turn");
			lblMessage_1.setText("Opponent's turn");
			
			break;
		case GAMEOVER:
			status = PlayerStatus.GAMEOVER;
			opponent.disableMouseEvents(2);
			btnNewGame.setEnabled(true);
			
			lblStatus.setText("Game Over");
			lblStatus_1.setText("Game Over");
			
			if(srMsg.isFlag()){
				infoText.setText("You won!");
				lblMessage_1.setText("Opponent lost");
			}else{
				lblMessage_1.setText("Opponent won!");
				infoText.setText("You lost");				
			}
		}
	}
	
	public void opponentMouseHit(int row, int column, boolean flag, boolean sdflag){
		Communication msg = new Communication();
		msg.setMessageType(PlayerStatus.TURN);
		msg.setRow(row);
		msg.setColumn(column);
		msg.setFlag(flag);
		msg.setShipDestroyed(sdflag);
		opponent.disableMouseEvents(2);
		sendMessage(msg);
	}
	
	/**
	 * Send Message object to server.
	 *
	 * @param msg the Message object to send.
	 */
	private void sendMessage(Communication msg){
		try {
			out.writeObject(msg);
		} catch (IOException e) {
			lblStatus.setText("Connection interrompue");
		}
	}
	
	/**
	 * Handles server connection error and resets client.
	 */
	public void serverDisconnect() {
		// TODO Auto-generated method stub
		lblStatus.setBackground(Color.red);
		lblStatus.setText("Disconnected");

		lblStatus_1.setBackground(Color.red);
		lblStatus_1.setText("Disconnected");
		
		newGame();
		cSocket = null;
		btnPlay.setEnabled(false);
		//stop and reset game
		//reset socket
	}		
	
	@Override
	public void run() {
		setSize(640, 500); //300,300
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
