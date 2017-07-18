package api;

public interface CellSpaceInterface {

	public int getGenerations();

	public int getCellSize();
 

	public int getCellRows();

	public int getCellCols();

	public void next(int from, int to);

	public void repaint();
}
