package view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import java.awt.Graphics2D;

public class Tableau extends JLayeredPane{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 300;
    public static final int HEIGHT = 300;
    private static final Dimension PANE_SIZE = new Dimension(WIDTH, HEIGHT);
    private GUI cframe;
	private Grille gridBoard;
	private Flotte shipBoard;
	private boolean topRect;
	private int shipDestroyed;
	MyMouseAdapter myMouseAdapter;

	public Tableau(GUI cf){
		cframe = cf;
		
			gridBoard = new Grille(this);
			gridBoard.setSize(PANE_SIZE);
			shipBoard = new Flotte(this);
			shipBoard.setSize(PANE_SIZE);
			shipBoard.setOpaque(false);

		setSize(PANE_SIZE);
        add(gridBoard, JLayeredPane.DEFAULT_LAYER);
        add(shipBoard,JLayeredPane.PALETTE_LAYER);
        
        myMouseAdapter = new MyMouseAdapter(this);
	}
	
	public Rectangle getShipBounds(Bateau s) {
		int width = 0,height = 0;
		Point cord = new Point(30 * s.getColumn(),30 * s.getRow());
		
		if(s.isHorizontal()){
			height = 30; 
			width = 30 * s.getLength();
		}
		else{
			height = 30 * s.getLength();
			width = 30;
		}
		
		gridBoard.setShipOccupiedCellFlag(s);
		
		return new Rectangle(cord.x + 2, cord.y + 2, width-4, height-4);
	}
	
	private class MyMouseAdapter extends MouseAdapter {
		private Tableau gbm;
        private int dragLabelWidthDiv2;
        private int dragLabelHeightDiv2;
        private Bateau clickedShip = null;
        private Point dropCell;
        private Point safeCell;

        public MyMouseAdapter(Tableau gb){
        	gbm = gb;
        	safeCell = new Point();
        }
        
        public void mouseClicked(MouseEvent me) {
        	if(shipBoard.getComponentAt(me.getPoint()) instanceof Bateau)
        	{
	        	clickedShip = (Bateau) shipBoard.getComponentAt(me.getPoint());
	        	safeCell.setLocation(clickedShip.getRow(),clickedShip.getColumn());
	        	gridBoard.clearShipCell(clickedShip);
	        	clickedShip.setHorizontal(!clickedShip.isHorizontal());
	        	
	        	if(gridBoard.isShipCollide(me.getPoint(), clickedShip)){
	        		clickedShip.setHorizontal(!clickedShip.isHorizontal());
	        	}
                dropCell = gbm.gridBoard.matchShipCellAtPoint(me.getPoint(),clickedShip,safeCell);
                
                clickedShip.setRow(dropCell.x);
                clickedShip.setColumn(dropCell.y);
                
                clickedShip.setBounds(getShipBounds(clickedShip));
                gridBoard.resetCellHighlight();
        	}
        }
        
        public void mousePressed(MouseEvent me) {
        	
        	if(shipBoard.getComponentAt(me.getPoint()) instanceof Bateau)
        	{
	        	clickedShip = (Bateau) shipBoard.getComponentAt(me.getPoint());
	        	safeCell.setLocation(clickedShip.getRow(),clickedShip.getColumn());
	        	
                dragLabelWidthDiv2 = 15 - 4;
                dragLabelHeightDiv2 = 15- 4;

                int x = me.getPoint().x - dragLabelWidthDiv2;
                int y = me.getPoint().y - dragLabelHeightDiv2;
                clickedShip.setLocation(x, y);

                shipBoard.repaint();
                
                dropCell = gbm.gridBoard.matchShipCellAtPoint(me.getPoint(),clickedShip,safeCell);
                
        	}
        }

        public void mouseDragged(MouseEvent me) {
            if (clickedShip == null) {
                return;
            }
            
            if(!clickedShip.getParent().getBounds().contains(me.getPoint())) return;
            
            int x = me.getPoint().x - dragLabelWidthDiv2;
            int y = me.getPoint().y - dragLabelHeightDiv2;
            
            clickedShip.setLocation(x, y);
            
            repaint();
            
            
            dropCell = gbm.gridBoard.matchShipCellAtPoint(me.getPoint(),clickedShip,safeCell);
            safeCell = dropCell;
        }

        public void mouseReleased(MouseEvent me) {
            
        	if (clickedShip == null) {
                return;
            }
    
        	gridBoard.clearShipCell(clickedShip);

            clickedShip.setRow(dropCell.x);
            clickedShip.setColumn(dropCell.y);
            clickedShip.setBounds(getShipBounds(clickedShip));            

            repaint();
            clickedShip = null;
            gridBoard.resetCellHighlight();
        }
    }

	public void enableMouseEvents(int sender){
        if(sender == 1){
			this.addMouseListener(myMouseAdapter);
	        this.addMouseMotionListener(myMouseAdapter);	        
        }else{
        	gridBoard.enableMouseEvents();
        }
        
        fade(false);
	}
	
	public void disableMouseEvents(int sender){
		if(sender == 1){
			this.removeMouseListener(myMouseAdapter);
	        this.removeMouseMotionListener(myMouseAdapter);
		}else{
			gridBoard.disableMouseEvents();
		}

		fade(true);
	}
	
	public void hideAllShip(){
		shipBoard.hideAllShip();
	}
	
	public void setOpponentShipPosition(int[][][] flags){
		gridBoard.setOpponentShipPosition(flags);
	}
	
	public int[][][] getMyShipPosition(){
		return gridBoard.getMyShipPosition();
	}
	
	public void setOpponentShipLocation(int[][] shipLocation){
		shipBoard.setOpponentShipLocation(shipLocation);
		repaint();
	}
	
	public int[][] getMyShipLocation(){
		return shipBoard.getMyShipLocation();
	}
	
	public boolean setShipHitById(int shipId){
		if(shipBoard.setToucheById(shipId)){
			System.out.println("Ship id:" + shipId + " Destroyed.");
			shipDestroyed++;
			shipBoard.showShip(shipId);
			return true;
		}
		return false;
	}

	public void setHitAtCell(int row,int column){
		gridBoard.setHitAtCell(row, column);
	}

	public void onMouseHitAtCell(int row, int column, boolean hit, boolean sdst){
		cframe.opponentMouseHit(row,column,hit,sdst);
	}
	
	 public void fade(boolean f){
		topRect = f;
		repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		if(topRect){
			Graphics2D g2d = (Graphics2D)g;
	        g2d.setColor(Color.WHITE); 
	        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
	        g2d.fillRect(0,0,this.getWidth(),this.getHeight());
		}
	}

	public void resetGameBoard() {
		gridBoard.resetGridBoard();
		gridBoard.resetCellHighlight();
		shipBoard.resetShipBoard();
		shipDestroyed = 0;
	}

	public int getShipDestroyed(){
		return shipDestroyed;
	}
}