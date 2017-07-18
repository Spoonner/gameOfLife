package engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import client.GameOfLife;
import api.CellSpaceInterface;
import api.Compute;

public class CellSpace extends Canvas implements CellSpaceInterface{
	private int generations;
	private int cellSize;
	private int cellRows;
	private int cellCols;
	private boolean cellUnderMouse;
	private boolean cells[][];
	private int cellsBuffer[][];
	private Image offScreenImage = null;
	private Graphics offScreenGraphics;

	public CellSpace(int cellSize, int cellCols, int cellRows) {
		cells = new boolean[cellCols][cellRows];
		cellsBuffer = new int[cellCols][cellRows];
		this.cellSize = cellSize;
		this.cellCols = cellCols;
		this.cellRows = cellRows;
		reshape(0, 0, cellSize * cellCols - 1, cellSize * cellRows - 1);
		clear();
	}

	public int getGenerations() {
		return generations;
	}

	public int getCellSize() {
		return cellSize;
	}

	public int getCellRows() {
		return cellRows;
	}

	public int getCellCols() {
		return cellCols;
	}

	public synchronized boolean mouseUp(java.awt.Event evt, int x, int y) {
		// toggle cell
		try {
			cells[x / cellSize][y / cellSize] = !cellUnderMouse;
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
		}
		repaint();
		return true;
	}

	public synchronized boolean mouseDown(java.awt.Event evt, int x, int y) {
		try {
			cellUnderMouse = cells[x / cellSize][y / cellSize];
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
		}
		return true;
	}

	public synchronized boolean mouseDrag(java.awt.Event evt, int x, int y) {
		// toggle cell
		try {
			cells[x / cellSize][y / cellSize] = !cellUnderMouse;
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
		}
		repaint();
		return true;
	}

	public synchronized void update(Graphics theG) {
		Dimension d = size();
		if ((offScreenImage == null)) {
			offScreenImage = createImage(d.width, d.height);
			offScreenGraphics = offScreenImage.getGraphics();
		}
		paint(offScreenGraphics);
		theG.drawImage(offScreenImage, 0, 0, null);
	}

	public void paint(Graphics g) {
		// draw background (MSIE doesn't do that)
		g.setColor(Color.gray);
		g.fillRect(0, 0, cellSize * cellCols - 1, cellSize * cellRows - 1);
		// draw grid
		g.setColor(getBackground());
		for (int x = 1; x < cellCols; x++) {
			g.drawLine(x * cellSize - 1, 0, x * cellSize - 1, cellSize
					* cellRows - 1);
		}
		for (int y = 1; y < cellRows; y++) {
			g.drawLine(0, y * cellSize - 1, cellSize * cellCols - 1, y
					* cellSize - 1);
		}
		// draw populated cells
		g.setColor(Color.yellow);
		for (int y = 0; y < cellRows; y++) {
			for (int x = 0; x < cellCols; x++) {
				if (cells[x][y]) {
					g.fillRect(x * cellSize, y * cellSize, cellSize - 1,
							cellSize - 1);
				}
			}
		}
	}

	// clears canvas
	public synchronized void clear() {
		generations = 0;
		for (int x = 0; x < cellCols; x++) {
			for (int y = 0; y < cellRows; y++) {
				cells[x][y] = false;
			}
		}
	}

	// create next generation of shape
	public synchronized void next(int from, int to) {
		int x;
		int y;
		generations++;
		
		
		// clear the buffer
		for (x = from; x < to; x++) {
			for (y = 0; y < cellRows; y++) {
				cellsBuffer[x][y] = 0;
			}
		}

		// count neighbors of off-edge cells
		for (x = from+1; x < to - 1; x++) {
			for (y = 1; y < cellRows - 1; y++) {
				if (cells[x][y]) {
					cellsBuffer[x - 1][y - 1]++;
					cellsBuffer[x][y - 1]++;
					cellsBuffer[x + 1][y - 1]++;
					cellsBuffer[x - 1][y]++;
					cellsBuffer[x + 1][y]++;
					cellsBuffer[x - 1][y + 1]++;
					cellsBuffer[x][y + 1]++;
					cellsBuffer[x + 1][y + 1]++;
				}
			}
		}

		// count neighbors of edge cells
		x = from+1; // start at (1,0)
		y = 0;
		int dx = 1;
		int dy = 0;
		while (true) {
			if (cells[x][y]) {
				if (x > from) {
					if (y > 0)
						cellsBuffer[x - 1][y - 1]++;
					if (y < cellRows - 1)
						cellsBuffer[x - 1][y + 1]++;
					cellsBuffer[x - 1][y]++;
				}
				if (x < to - 1) {
					if (y < cellRows - 1)
						cellsBuffer[x + 1][y + 1]++;
					if (y > 0)
						cellsBuffer[x + 1][y - 1]++;
					cellsBuffer[x + 1][y]++;
				}
				if (y > 0)
					cellsBuffer[x][y - 1]++;
				if (y < cellRows - 1)
					cellsBuffer[x][y + 1]++;
			}

			// turn clockwise at collision with edge
			if (x == to - 1 && y == 0) {
				dx = 0;
				dy = 1;
			} else if (x == to - 1 && y == cellRows - 1) {
				dx = -1;
				dy = 0;
			} else if (x == from && y == cellRows - 1) {
				dx = 0;
				dy = -1;
			} else if (x == from && y == 0) {
				// all edge cells done
				break;
			}
			x += dx;
			y += dy;
		}
		
		if (from != 0) from++;
		if (to != cellCols) to--;
		// here is the life algorithm
		for (x = from; x < to; x++) {
			for (y = 0; y < cellRows; y++) {
				
				switch (cellsBuffer[x][y]) {
				case 2:
					// no change
					break;
				case 3:
					if (cells[x][y]!=true) {
						cells[x][y] = true;
					}
					break;
				default:
					if (cells[x][y]!=false) {
						cells[x][y] = false;
					}
					break;
				}
			}
		}

	}
}
