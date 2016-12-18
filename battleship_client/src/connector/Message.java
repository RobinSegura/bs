package connector;

import java.io.Serializable;
import engine.PlayerStatus;

/**
 * This class wraps all communication types and it is sent and received by client and server to update game play.
 */
public class Message implements Serializable {
	
	/** The serialVersionUID for serializable interface. */
	private static final long serialVersionUID = 1L;	
	
	/** The sender id. */
	private int sender;
	private PlayerStatus messageType;
	private int[][][] shipPosition;
	private int[][] shipLocation;
	private int row;
	private int column;
	private boolean flag; //hit,win
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
	
	/**
	 * Gets the column number.
	 *
	 * @return the column number
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * Sets the column number.
	 *
	 * @param column the column to send
	 */
	public void setColumn(int column) {
		this.column = column;
	}
	
	/**
	 * return hit or win flag.
	 *
	 * @return hit or win
	 */
	public boolean isFlag() {
		return flag;
	}
	
	/**
	 * Sets hit or win.
	 *
	 * @param flag hit or win
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	/**
	 * Gets the ship location array.
	 *
	 * @return shipLocation array
	 */
	public int[][] getShipLocation() {
		return shipLocation;
	}
	
	/**
	 * Sets the ship location array.
	 *
	 * @param shipLocation the shipLocation to set
	 */
	public void setShipLocation(int[][] shipLocation) {
		this.shipLocation = shipLocation;
	}
	
	/**
	 * if ship destroyed.
	 *
	 * @return true, if ship destroyed
	 */
	public boolean isShipDestroyed() {
		return shipDestroyed;
	}
	
	/**
	 * Sets if ship destroyed.
	 *
	 * @param shipDestroyed if ship destroyed
	 */
	public void setShipDestroyed(boolean shipDestroyed) {
		this.shipDestroyed = shipDestroyed;
	}
	

	
}
