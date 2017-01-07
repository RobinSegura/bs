package view;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import connector.Connexion;
import connector.Communication;
import connector.ServerReader;
import engine.Statut;

/**
 * The Class ClientFrame.
 * 
 * This class create client's window containing two grids with five ships.
 */
public class GUI extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Socket cSocket;
	private Connexion cnx;
	private ServerReader sReader;
	private ObjectOutputStream out;
	private Statut statut;
	private Tableau player;
	private Tableau opponent;
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

	public GUI(Socket s, ObjectInputStream in, ObjectOutputStream out, Connexion cnx) {
		super("BattleShip");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

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

		lblPlayer = new JLabel("Vous");
		lblPlayer.setBounds(10, 11, 300, 14);
		getContentPane().add(lblPlayer);

		lblOpponent = new JLabel("Adversaire");
		lblOpponent.setBounds(320, 11, 300, 14);
		getContentPane().add(lblOpponent);

		lblShipLeft = new JLabel("Bateaux détruits:");
		lblShipLeft.setBounds(10, 36, 300, 14);
		getContentPane().add(lblShipLeft);

		lblShipLeft_1 = new JLabel("Bateaux détruits:");
		lblShipLeft_1.setBounds(320, 36, 300, 14);
		getContentPane().add(lblShipLeft_1);

		lblStatus = new JLabel("Statut");
		lblStatus.setBounds(10, 61, 300, 14);
		getContentPane().add(lblStatus);

		lblMessage_1 = new JLabel("Message");
		lblMessage_1.setBounds(320, 86, 300, 14);
		getContentPane().add(lblMessage_1);

		infoText = new JLabel("Message");
		infoText.setBounds(10, 86, 300, 14);
		getContentPane().add(infoText);

		lblStatus_1 = new JLabel("Statut");
		lblStatus_1.setBounds(320, 61, 300, 14);
		getContentPane().add(lblStatus_1);

		btnPlay = new JButton("Jouer");
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				startGame();
			}
		});
		btnPlay.setEnabled(false);
		btnPlay.setBounds(109, 111, 89, 23);
		getContentPane().add(btnPlay);

		btnNewGame = new JButton("Encore");
		btnNewGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				newGame();
			}
		});
		btnNewGame.setEnabled(false);
		btnNewGame.setBounds(208, 111, 89, 23);
		getContentPane().add(btnNewGame);

		player.disableMouseEvents(1);
		opponent.disableMouseEvents(2);

		statut = Statut.NOT_CONNECTED;
		lblStatus.setText("Not Connected");
		lblShipLeft.setText("Ship Destroyed: " + player.getShipDestroyed());
		lblShipLeft_1.setText("Ship Destroyed: " + opponent.getShipDestroyed());

		infoText.setText("trying to connect\n");
		cSocket = s;

		if (cSocket.isConnected()) {
			this.out = out;
			this.sReader = ServerReader.getInstance(in);
			sReader.setClfrm(this);
			sReader.start();

			player.enableMouseEvents(1);
			btnPlay.setEnabled(true);
			statut = Statut.CONNECTED;
		} else {
			infoText.setText("Porblème réseau, essayez de redémarer\n");
		}
	}

	public void setOpponentShipPosition(int[][][] flags) {
		opponent.setOpponentShipPosition(flags);
	}

	public int[][][] getMyShipPosition() {
		return player.getMyShipPosition();
	}

	public void setOpponentShipLocation(int[][] shipLocation) {
		opponent.setOpponentShipLocation(shipLocation);
	}

	public int[][] getMyShipLocation() {
		return player.getMyShipLocation();
	}

	public void startGame() {

		cnx.stop();
		player.disableMouseEvents(1);
		Communication msg = new Communication();
		msg.setMessageType(Statut.READY);
		msg.setShipPosition(getMyShipPosition());
		msg.setShipLocation(getMyShipLocation());
		sendMessage(msg);

		statut = Statut.READY;
		lblStatus.setText("Pret");
		infoText.setText("Attente de l'adversaire");
		btnPlay.setEnabled(false);
	}

	public void newGame() {

		player.resetGameBoard();
		opponent.resetGameBoard();
		opponent.hideAllShip();

		player.enableMouseEvents(1);
		opponent.disableMouseEvents(2);

		btnPlay.setEnabled(true);
		btnNewGame.setEnabled(false);

		infoText.setText("Positioner et démarrer\n");
		lblStatus.setText("Conneté");
		lblShipLeft.setText("Bateaux détruits: " + player.getShipDestroyed());
		lblShipLeft_1.setText("Bateaux détruits: " + opponent.getShipDestroyed());

		statut = Statut.CONNECTED;
		Communication msg = new Communication();
		msg.setMessageType(Statut.CONNECTED);
		sendMessage(msg);

		repaint();
	}

	public void messageReceived(Object message) {
		Communication srMsg = (Communication) message;
		updateClient(srMsg);
	}

	private void updateClient(Communication srMsg) {

		switch (srMsg.getMessageType()) {
		case NOT_CONNECTED:
			lblStatus_1.setText("La partie va commencer");
			cnx.stop();
			break;
		case WAITINGOP:
			lblStatus_1.setText("En attente d'adversaire");
			btnPlay.setEnabled(false);
			break;
		case CONNECTED:
			lblStatus_1.setText("Connecté");
			btnPlay.setEnabled(true);

			break;
		case READY:
			lblStatus_1.setText("Pret");
			setOpponentShipPosition(srMsg.getShipPosition());
			setOpponentShipLocation(srMsg.getShipLocation());

			break;
		case TURN:
			if (statut == Statut.READY) {
				setOpponentShipPosition(srMsg.getShipPosition());
				setOpponentShipLocation(srMsg.getShipLocation());

				lblStatus.setText("En cours");
				lblStatus_1.setText("En cours");
			}

			statut = Statut.TURN;
			opponent.enableMouseEvents(2);
			if (srMsg.isFlag()) {
				player.setHitAtCell(srMsg.getRow(), srMsg.getColumn());
			}

			lblShipLeft.setText("Bateaux détruits: " + player.getShipDestroyed());
			lblShipLeft_1.setText("Bateaux détruits: " + opponent.getShipDestroyed());
			infoText.setText("Votre tour");
			lblMessage_1.setText("En attente de votre coup");

			break;
		case WAIT:
			if (statut == Statut.READY) {
				lblStatus.setText("En Cours");
				lblStatus_1.setText("En cours");
			}

			statut = Statut.WAIT;
			opponent.disableMouseEvents(2);
			if (srMsg.isFlag()) {
				player.setHitAtCell(srMsg.getRow(), srMsg.getColumn());
			}

			lblShipLeft.setText("Bateaux détruits: " + player.getShipDestroyed());
			lblShipLeft_1.setText("Bateaux détruits: " + opponent.getShipDestroyed());

			infoText.setText("En attente du jeu adverse");
			lblMessage_1.setText("Tour de l'adversaire");

			break;
		case GAMEOVER:
			statut = Statut.GAMEOVER;
			opponent.disableMouseEvents(2);
			btnNewGame.setEnabled(true);
			btnPlay.setEnabled(false);

			lblStatus.setText("Game Over");
			lblStatus_1.setText("Game Over");

			if (srMsg.isFlag()) {
				infoText.setText("Vous avec Gagné!");
				lblMessage_1.setText("L'adversaire a perdu");
			} else {
				lblMessage_1.setText("L'adversaire a gagné!");
				infoText.setText("Vous avez perdu");
			}
		}
	}

	public void opponentMouseHit(int row, int column, boolean flag, boolean sdflag) {
		Communication msg = new Communication();
		msg.setMessageType(Statut.TURN);
		msg.setRow(row);
		msg.setColumn(column);
		msg.setFlag(flag);
		msg.setShipDestroyed(sdflag);
		opponent.disableMouseEvents(2);
		sendMessage(msg);
	}

	private void sendMessage(Communication msg) {
		try {
			out.writeObject(msg);
		} catch (IOException e) {
			lblStatus.setText("Connection interrompue");
		}
	}

	public void serverDisconnect() {
		lblStatus.setBackground(Color.red);
		lblStatus.setText("Déconnecté");

		lblStatus_1.setBackground(Color.red);
		lblStatus_1.setText("Déconnecté");

		newGame();
		cSocket = null;
		btnPlay.setEnabled(false);
	}

	@Override
	public void run() {
		setSize(640, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
