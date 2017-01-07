package connector;

import java.io.*;
import view.GUI;

public class ServerReader extends Thread {
	private static volatile ServerReader instance = null;
	GUI clfrm;
	ObjectInputStream reader;

	public static ServerReader getInstance(ObjectInputStream reader) {
        if (instance == null) {
                synchronized (ServerReader .class){
                        if (instance == null) {
                                instance = new ServerReader(reader);
                        }
              }
        }
        return instance;
	}	
	
	private ServerReader(ObjectInputStream reader){
		this.reader = reader;
	}

    public void setClfrm(GUI clfrm) {
		this.clfrm = clfrm;
	}

	public void run() {
        Object message;
        try {
            while ((message = reader.readObject()) != null) 
            {
            	clfrm.messageReceived(message);
            }

        } catch (ClassNotFoundException e) {
        	e.printStackTrace();
			clfrm.serverDisconnect();
			
		} catch (IOException ex) {
			ex.printStackTrace();
			clfrm.serverDisconnect();
        }
    }
}  
