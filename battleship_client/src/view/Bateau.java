package view;

import javax.swing.JLabel;

public class Bateau extends JLabel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Flotte shipBoard;
	private int id;
	private int length;
	private boolean horizontal;
	private int row;
	private int column;
	private int hit;

	public Bateau(Flotte sb, int id,int length,int row, int column){
		this.shipBoard = sb;
		this.id = id;
		
		this.row = row;
		this.column = column;
		this.length = length;
		this.horizontal = true;
		
		hit = 0;
		this.setBounds(shipBoard.getShipBounds(this));
	}

	public int getId(){
		return id;
	}

	public int getLength() {
		return length;
	}

	public boolean isHorizontal() {
		return horizontal;
	}
	
	public void setHorizontal(boolean h){
		horizontal = h;
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
	
	public int getHit(){
		return hit;
	}
	
	public void setHit(){
		hit++;
	}
	
	public void resetHit(){
		hit = 0;
	}
}
