package view;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class Flotte extends JPanel{
	
	private Tableau tableau;

	private Bateau[] flotte;
	
	public Flotte(Tableau tab){
		this.setLayout(null);
		this.tableau = tab;
		
		flotte = new Bateau[5];
		
		for(int i = 0;i<5;i++){
			int size = ((5 - i) < 3) ? 6-i : 5-i;
			flotte[i] = new Bateau(this,i+1,size,i,0);
			flotte[i].setOpaque(true);
			this.add(flotte[i]);
		}

		flotte[0].setBackground(Color.green.darker().darker());
		flotte[1].setBackground(Color.magenta.darker());
		flotte[2].setBackground(Color.orange.darker());
		flotte[3].setBackground(Color.pink.darker().darker());
		flotte[4].setBackground(Color.yellow.darker().darker());

	}

	public Rectangle getShipBounds(Bateau s) {
		return tableau.getShipBounds(s);		
	}

	public void hideAllShip(){
		for(int i = 0;i<5;i++){
			flotte[i].setOpaque(false);
		}
		repaint();
	}

	public void hideShip(int IdBateau){
		flotte[IdBateau-1].setOpaque(false);
		repaint();
	}
	
	public void showShip(int IdBateau){
		flotte[IdBateau-1].setOpaque(true);
		repaint();
	}
	
	public boolean setToucheById(int IdBateau){
		flotte[IdBateau-1].setHit();
		if(flotte[IdBateau-1].getHit()==flotte[IdBateau-1].getLength()){
			return true;
		}
		else
			return false;
	}
	
	public void setOpponentShipLocation(int[][] shipLocation){
		for(int i = 0;i<5;i++){
			
			flotte[i].setHorizontal(shipLocation[i][0]==0);
			flotte[i].setRow(shipLocation[i][1]);
			flotte[i].setColumn(shipLocation[i][2]);
			
			flotte[i].setBounds(getShipBounds(flotte[i]));
			
		}
	}

	public int[][] getMyShipLocation(){
		int[][] shipLocation = new int[5][3];
		for(int i = 0;i<5;i++){
			shipLocation[i][0] = flotte[i].isHorizontal()?0:1;
			shipLocation[i][1] = flotte[i].getRow();
			shipLocation[i][2] = flotte[i].getColumn();
			//battleShips[i].setBounds(getShipBounds(battleShips[i]));
		}
		return shipLocation;
	}
	
	/**
	 * Reset current ship board.
	 */
	public void resetShipBoard() {
		for(int i = 0;i<5;i++){
			flotte[i].setHorizontal(true);
			flotte[i].setRow(i);
			flotte[i].setColumn(0);
			flotte[i].resetHit();
			flotte[i].setBounds(getShipBounds(flotte[i]));
		}
		repaint();
	}
	

}
