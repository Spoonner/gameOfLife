package client;

import api.CellSpaceInterface;

public class GOLMachine extends GameOfLife implements Runnable {
	private int threadNumber = 1;
	private CellSpaceInterface cellSpace;
	
	
	public GOLMachine(CellSpaceInterface cellSpace) {
		this.cellSpace = cellSpace;
	}
	
	public void run() {
		try {
			
			while (isGameThread()) {
				
				if (cellSpace.getGenerations() == 300)
				{
					long time = System.currentTimeMillis() - GameOfLife.time;
					GameOfLife.genLabel.setText(GameOfLife.genLabel.getText()+", " + String.valueOf(time));
					stop();
				}
				
				int from,to;
				if (threadNumber == 1) {
					from = 0;
				} else from = (threadNumber-1)*(cellSpace.getCellCols()/threads)-1;
				if (threadNumber == threads) {
					to = cellSpace.getCellCols();
				} else to = threadNumber*(cellSpace.getCellCols()/threads)+1;
				cellSpace.next( from, to);
				cellSpace.repaint();
				GameOfLife.genLabel.setText("Genarations: "+ String.valueOf(cellSpace.getGenerations()));
				try {
					list.get(threadNumber-1).sleep(genTime);
				} catch (InterruptedException e) {
				}
			}
			stop();
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
