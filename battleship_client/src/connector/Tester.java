package connector;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Tester {

	/**
	 * 
	 */
	private int port;
	private String hostname; // adapter au serveur
	public Socket socket;
	public Thread t1;


	public Tester(String ip1, String ip2, String ip3, String ip4, int port) {
		hostname = (ip1 + "." + ip2 + "." + ip3 + "." + ip4);
		this.port = port;

	}
	
	public boolean test() throws UnknownHostException, IOException {
		System.out.println("Demande de connexion à " + hostname + ":" + port);
		// TODO Auto-generated method stub
		socket = new Socket(hostname, port);
		if (socket.isConnected()){
			new Thread(new Connexion(socket, this,hostname, port)).start();
			return true;				
		}else{
			return false;
		}
	}
}
