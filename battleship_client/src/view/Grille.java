package view;

import java.awt.*;
import javax.swing.JPanel;

/**
 * The Class GridBoard.
 */
public class Grille extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Tableau gameBoard;
	private Case[][] cells = new Case[10][10];
	private int[][][] flags = new int[10][10][2];
	Rectangle rect;
	int cellWidth;
	int cellHeight;

	public Grille(Tableau gb) {

		this.gameBoard = gb;

		setLayout(new GridLayout(10, 10, 0, 0));

		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++) {
				add(cells[i][j] = new Case(i, j, this));

				flags[i][j][0] = 0;
				flags[i][j][1] = 0;

			}
		this.setPreferredSize(new Dimension(300, 300));
	}

	public void resetGridBoard() {
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++) {
				flags[i][j][0] = 0; // isShip
				flags[i][j][1] = 0; // isHit
			}
	}

	public void enableMouseEvents() {
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				cells[i][j].enableCellMouseEvents();
	}

	public void disableMouseEvents() {
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				cells[i][j].disableCellMouseEvents();
	}

	public void setShipOccupiedCellFlag(Bateau s) {

		int rowStart = s.getRow(), colStart = s.getColumn();
		int rowEnd = 0, colEnd = 0;

		if (s.isHorizontal()) {
			rowEnd = rowStart;
			colEnd = colStart + s.getLength() - 1;

		} else {
			colEnd = colStart;
			rowEnd = rowStart + s.getLength() - 1;
		}

		for (int r = rowStart; r <= rowEnd; r++) {
			for (int c = colStart; c <= colEnd; c++) {
				flags[r][c][0] = s.getId();
			}
		}
	}

	public void clearShipCell(Bateau s) {

		int rowStart = s.getRow(), colStart = s.getColumn();
		int rowEnd = 0, colEnd = 0;

		if (s.isHorizontal()) {
			rowEnd = rowStart;
			colEnd = colStart + s.getLength() - 1;

		} else {
			colEnd = colStart;
			rowEnd = rowStart + s.getLength() - 1;
		}

		for (int r = rowStart; r <= rowEnd; r++) {
			for (int c = colStart; c <= colEnd; c++) {
				flags[r][c][0] = 0;
			}
		}

	}

	public boolean isShipCollide(Point point, Bateau s) {
		int sLength = s.getLength();
		int rowStart, colStart, rowEnd, colEnd;

		Case startCell = (Case) this.getComponentAt(point);
		rowStart = startCell.getRow();
		colStart = startCell.getColumn();
		rowEnd = startCell.getRow();
		colEnd = startCell.getColumn();

		if (s.isHorizontal()) {

			if (colStart > 10 - sLength)
				colStart = 10 - sLength;
			colEnd = colStart + sLength - 1;
			if (colEnd > 10)
				colEnd = 10;

		} else {

			if (rowStart > 10 - sLength)
				rowStart = 10 - sLength;
			rowEnd = rowStart + sLength - 1;
			if (rowEnd > 10)
				rowEnd = 10;

		}

		for (int r = rowStart; r <= rowEnd; r++) {
			for (int c = colStart; c <= colEnd; c++) {
				if (flags[r][c][0] != 0 && flags[r][c][0] != s.getId()) {
					return true;
				}
			}
		}
		return false;
	}

	public void resetCellHighlight() {
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				cells[i][j].resetHighlight();
	}

	public Point matchShipCellAtPoint(Point point, Bateau s, Point safeCell) {
		int sLength = s.getLength();
		int rowStart, colStart, rowEnd, colEnd;

		Case startCell = (Case) this.getComponentAt(point);
		rowStart = startCell.getRow();
		colStart = startCell.getColumn();
		rowEnd = startCell.getRow();
		colEnd = startCell.getColumn();

		resetCellHighlight();

		if (s.isHorizontal()) {

			if (colStart > 10 - sLength)
				colStart = 10 - sLength;
			colEnd = colStart + sLength;
			if (colEnd > 10)
				colEnd = 10;

			for (int c = colStart; c < colEnd; c++) {
				// if collision set safeCell
				if (flags[rowStart][c][0] != 0 && flags[rowStart][c][0] != s.getId()) {
					rowStart = safeCell.x;
					colStart = safeCell.y;

					if (colStart > 10 - sLength)
						colStart = 10 - sLength;
					colEnd = colStart + sLength;
					if (colEnd > 10)
						colEnd = 10;

					break;
				}
			}

			for (int c = colStart; c < colEnd; c++) {
				cells[rowStart][c].highLight();
			}

		} else {

			if (rowStart > 10 - sLength)
				rowStart = 10 - sLength;
			rowEnd = rowStart + sLength;
			if (rowEnd > 10)
				rowEnd = 10;

			for (int r = rowStart; r < rowEnd; r++) {
				if (flags[r][colStart][0] != 0 && flags[r][colStart][0] != s.getId()) {
					rowStart = safeCell.x;
					colStart = safeCell.y;

					if (rowStart > 10 - sLength)
						rowStart = 10 - sLength;
					rowEnd = rowStart + sLength;
					if (rowEnd > 10)
						rowEnd = 10;

					break;
				}
			}

			for (int r = rowStart; r < rowEnd; r++) {
				cells[r][colStart].highLight();
			}
		}
		return new Point(rowStart, colStart);
	}

	public void setOpponentShipPosition(int[][][] flags2) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				flags[i][j][0] = flags2[i][j][0];
				flags[i][j][1] = flags2[i][j][0];
				System.out.print(flags[i][j][0]);
			}
			System.out.print("\n");
		}
	}

	public int[][][] getMyShipPosition() {
		return flags;
	}

	public int setHitAtCell(int row, int column) {

		flags[row][column][1] = 1; // isHit

		if (flags[row][column][0] > 0) {
			System.out.println("Ship id hit:" + flags[row][column][0]);
			cells[row][column].setTouche();
			gameBoard.setShipHitById(flags[row][column][0]);
		}
		cells[row][column].setManque();
		return flags[row][column][0];
	}

	public void onMouseHitAtCell(int row, int column) {

		flags[row][column][1] = 1; // isHit

		if (flags[row][column][0] > 0) {
			System.out.println("Ship id hit:" + flags[row][column][0]);

			cells[row][column].setTouche();
			gameBoard.onMouseHitAtCell(row, column, true, gameBoard.setShipHitById(flags[row][column][0]));
			return;
		}
		cells[row][column].setManque();
		gameBoard.onMouseHitAtCell(row, column, false, false);

	}
}