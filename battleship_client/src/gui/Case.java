 package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;


public class Case extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int row;
    private int column;
	private int isMouseOn;
    private int shipHit; 
    private Grille gridBoard;
    private ClickListener cellMouseListener;

    public Case(int row, int column, Grille gui) {

      this.row = row;
      this.column = column;
      this.gridBoard = gui;
      isMouseOn = 0;
      shipHit = 0;
      this.setPreferredSize(new Dimension(30,30));
      this.setMinimumSize(new Dimension(30, 30));
      this.setMaximumSize(new Dimension(30, 30));
      
      setBorder(new LineBorder(Color.cyan.darker(), 1)); 

      cellMouseListener = new ClickListener();
    }

	public void enableCellMouseEvents(){
			this.addMouseListener(cellMouseListener);
	}
	
	public void disableCellMouseEvents(){
			this.removeMouseListener(cellMouseListener);
	}
    
    public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
    
	public void highLight(){
    	isMouseOn=1;
    	repaint();
    }

    public void resetHighlight(){
    	isMouseOn=0;
    	shipHit=0;
    	repaint();
    }

    protected void paintComponent(Graphics g) {

      super.paintComponent(g);

      if(shipHit > 0){
    	  if(shipHit==1){
    	      g.setColor(Color.BLACK);
    	      g.fillRect(0, 0, getWidth(), getHeight());
          }else if(shipHit==2){
              g.setColor(Color.RED.brighter());
              g.fillRect(0, 0, getWidth(), getHeight());    	  
          }
      }else if(isMouseOn==1){
	      g.setColor(Color.ORANGE);
	      g.fillRect(0, 0, getWidth(), getHeight());
      }else if(isMouseOn==0){
          g.setColor(Color.LIGHT_GRAY);
          g.fillRect(0, 0, getWidth(), getHeight());    	  
      }
    }
    
    public void setTouche(){
    	shipHit = 2;
    	repaint();
    }
    
    public void setManque(){
    	shipHit = 1;
    	repaint();    	
    }

	private class ClickListener extends MouseAdapter {

    	public void mouseClicked(MouseEvent e) {
	    	if(shipHit > 0)return;
	    	shipHit = 1;
	    	gridBoard.onMouseHitAtCell(row,column);	    	
	    	repaint();
	    }

    	public void mouseEntered(MouseEvent e) {
	    	isMouseOn = 1;
	    	repaint();
	    }
	    
    	public void mouseExited(MouseEvent e) {
	    	isMouseOn = 0;
	    	repaint();
	    }
	    
	 }
}
