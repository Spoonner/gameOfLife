package client;

import java.awt.*;
import java.applet.*;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.util.ArrayList;

import api.CellSpaceInterface;
import api.Compute;

public class GameOfLife extends Frame {
	static public ArrayList<CellSpaceInterface> cellSpace;
	static public boolean gameThread;
	static public ArrayList<Thread> list = new ArrayList<Thread>();
	static public int genTime = 500;
	static public int threads;
	private final String slow = "Slow";
	private final String fast = "Fast";
	private final String hyper = "Hyper";
	public final String startLabel = "Start";
	private final String stopLabel = "Stop";
	private final String exitLabel = "Exit";
	static public Label genLabel;
	static public Button startstopButton;
	static public long time;
	public GameOfLife() {}
	
	public GameOfLife(ArrayList<CellSpaceInterface> cs, int threads){
		this.cellSpace = cs;
		this.threads = threads;
		init();
	}
	
	public boolean isGameThread() {
		return gameThread;
	}
	
	public void init() {
		
		setTitle("Game Of Life");
		// set background
		setBackground(new Color(0x999999));
		
		setSize(cellSpace.get(0).getCellSize()*cellSpace.size()*cellSpace.get(0).getCellCols()+15, cellSpace.get(0).getCellSize()*cellSpace.size()*cellSpace.get(0).getCellRows());

		Choice speed = new Choice();
		speed.addItem(slow);
		speed.addItem(fast);
		speed.addItem(hyper);

		genLabel = new Label("Generations: 0             ");

		startstopButton = new Button(startLabel);

		Panel controls = new Panel();
		controls.add(startstopButton);
		controls.add(new Button(exitLabel));
		controls.add(speed);
		controls.add(genLabel);

		setLayout(new BorderLayout());
		add("South", controls);
		for (int i=0; i<cellSpace.size(); i++){
			add("North", (Component) cellSpace.get(i));
		}
		show();
		resize(preferredSize());
		validate();
	}

	// no start() to prevent starting immediately
	public void start2() {
		if (gameThread == false) {
			time = System.currentTimeMillis();
			gameThread = true;
			for(int i=0;i<threads;i++){
				GOLMachine golt = new GOLMachine(cellSpace.get(i));
				Thread gameThr = new Thread(golt);
				list.add(i,gameThr);
				gameThr.start();
			}
	
			
			
		}
	}

	public void stop() {
		if (gameThread != false) {
			startstopButton.setLabel(startLabel);
			for(int i=threads;i>=threads;i--){
				Thread gameThr = list.remove(i-1);
				gameThr.stop();
			}
			gameThread = false;
			long time_el = time - System.currentTimeMillis();
			genLabel.setText(String.valueOf(time_el));

		}
	}

	public boolean action(Event evt, Object arg) {
		if (startLabel.equals(arg)) // start
		{
			start2();
			startstopButton.setLabel(stopLabel);
			return true;
		} else if (stopLabel.equals(arg)) // stop
		{
			stop();
			startstopButton.setLabel(startLabel);
			return true;
		} else if (slow.equals(arg)) // slow
		{
			genTime = 500;
			return true;
		} else if (fast.equals(arg)) // fast
		{
			genTime = 100;
			return true;
		} else if (hyper.equals(arg)) // hyperspeed
		{
			genTime = 0;
			return true;
		} else if (exitLabel.equals(arg))
		{
			try {
				stop();
				dispose();
			}
			catch (Exception e ) {
				dispose();
			}
			return true;
		}
		return false;
	}

}
