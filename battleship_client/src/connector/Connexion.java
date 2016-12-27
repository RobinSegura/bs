package connector;

import java.net.*;

import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import view.ClientFrame;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.*;

public class Connexion extends JFrame implements Runnable {

	private Socket socket;
	public static Thread t;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Tester tst;

	JPanel panel;
	FlowLayout fl;

	public Connexion(Socket s, Tester tst, String hostname, int port) {
		super("Recherche");
		socket = s;
		this.tst = tst;
		setSize(500, 80);
		setResizable(true);
		JLabel label = new JLabel("Connecté à " + hostname + ":" + port + " Recherche d'un autre joueur...");
		System.out.println("Connexion établie avec le serveur, recherche de partie :");
		panel = new JPanel(); // Panel
		fl = new FlowLayout(FlowLayout.CENTER); // layoutManager
		panel.setLayout(fl);
		setLayout(new GridLayout());

		panel.add(label);
		setContentPane(panel);
		panel.setVisible(true);
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		setVisible(true);
	}

	public void run() {
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.err.println("Le serveur ne répond plus ");
			e.printStackTrace();
		}
		t = new Thread(new ClientFrame(socket, in, out, this));
		t.start();
	}
}