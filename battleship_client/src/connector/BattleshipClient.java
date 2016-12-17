package connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class BattleshipClient {

	private static final int PORT = 8051;

	private static final String hostname = "192.168.0.13"; // adapter au serveur

	/**
	 * public BattleshipClient() { PrintWriter out = null; BufferedReader
	 * networkIn = null;
	 * 
	 * System.out.println("(démarrage du client) veuillez patienter...");
	 * System.out.println("(le serveur est) " + hostname + ":" + PORT); try {
	 * 
	 * Socket theSocket = new Socket(); theSocket.connect(new
	 * InetSocketAddress(hostname, PORT), 200); int localPort =
	 * theSocket.getLocalPort(); System.out.println("Client démarré sur le port"
	 * + ":" + localPort);
	 * 
	 * networkIn = new BufferedReader(new InputStreamReader(
	 * theSocket.getInputStream()));
	 * 
	 * BufferedReader userIn = new BufferedReader(new InputStreamReader(
	 * System.in));
	 * 
	 * out = new PrintWriter(theSocket.getOutputStream());
	 * 
	 * System.out.println("Connecté au serveur");
	 * 
	 * while (true) { System.out.println("entrez une requête au clavier (je
	 * comprends [circle x y r])");//et aussi [fin] String theLine =
	 * userIn.readLine(); if (theLine.equals(".")) break; out.println(theLine);
	 * out.flush(); Thread.sleep(100);
	 * 
	 * System.out.println(networkIn.readLine());// le serveur ne retourne qu'une
	 * seule ligne } } catch (IOException e) { System.err.println(e);
	 * System.out.println("plus de connexion"); } catch (InterruptedException e)
	 * { System.err.println(e); System.out.println("plus de connexion"); }
	 * finally { try { if (networkIn != null) networkIn.close(); if (out !=
	 * null) out.close(); } catch (IOException ex) { } } }
	 * 
	 * public static boolean portIsOpen(String ip, int port, int timeout) { try
	 * { Socket socket = new Socket(); socket.connect(new InetSocketAddress(ip,
	 * port), timeout); socket.close(); return true; } catch (Exception ex) {
	 * return false; } }
	 * 
	 * 
	 * private static final String prefix = "192.168.1";
	 * 
	 * public static void scanOpenedPorts(String[] args) { for (int j = 1; j <
	 * 255; j++) { String ip = prefix + "." + j; System.out.println("\n" + ip);
	 * for (int i = 1; i < 100; i++) { if (portIsOpen(ip, i, 200))
	 * System.out.println("port " + i + " is open"); else System.out.print(".");
	 * } } }
	 */

    public static Socket socket = null;
    public static Thread t1;
    
    public static void main(String[] args) {
	    try {
	        System.out.println("Demande de connexion");
	        socket = new Socket("192.168.1.38",8051);
	        System.out.println("Connexion établie avec le serveur, authentification :"); // Si le message s'affiche c'est que je suis connecté
	        t1 = new Thread(new Connexion(socket));
	        t1.start();
	    } catch (UnknownHostException e) {
	      System.err.println("Impossible de se connecter à l'adresse "+socket.getLocalAddress());
	    } catch (IOException e) {
	      System.err.println("Aucun serveur à l'écoute du port "+socket.getLocalPort());
	    }
    }
}
