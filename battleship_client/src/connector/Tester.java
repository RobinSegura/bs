package connector;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Tester {

	/**
	 * 
	 */
	private int port;
	private String hostname;
	public Socket socket;
	public Thread t1;


	public Tester(String ip1, String ip2, String ip3, String ip4, int port) {
		hostname = (ip1 + "." + ip2 + "." + ip3 + "." + ip4);
		this.port = port;

	}
	
	public boolean test() throws UnknownHostException, IOException {
		System.out.println("Demande de connexion à " + hostname + ":" + port);
		socket = new Socket(hostname, port);
		if (socket.isConnected()){
			new Thread(new Connexion(socket, this,hostname, port)).start();
			return true;				
		}else{
			return false;
		}
	}
}
