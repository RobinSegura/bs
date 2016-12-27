package connector;

import java.io.Serializable;
import engine.PlayerStatus;

public class Communication implements Serializable {

	private static final long serialVersionUID = 1L;		
	private int sender;
	private PlayerStatus messageType;
	private int[][][] shipPosition;
	private int[][] shipLocation;
	private int row;
	private int column;
	private boolean flag;
	private boolean shipDestroyed;
	
	public int getSender() {
		return sender;
	}
	
	public void setSender(int sender) {
		this.sender = sender;
	}
	
	public PlayerStatus getMessageType() {
		return messageType;
	}

	public void setMessageType(PlayerStatus messageType) {
		this.messageType = messageType;
	}

	public int[][][] getShipPosition() {
		return shipPosition;
	}
	
	public void setShipPosition(int[][][] shipPosition) {
		this.shipPosition = shipPosition;
	}

	public int getRow() {
		return row;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void setColumn(int column) {
		this.column = column;
	}
	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int[][] getShipLocation() {
		return shipLocation;
	}

	public void setShipLocation(int[][] shipLocation) {
		this.shipLocation = shipLocation;
	}

	public boolean isShipDestroyed() {
		return shipDestroyed;
	}

	public void setShipDestroyed(boolean shipDestroyed) {
		this.shipDestroyed = shipDestroyed;
	}
}
