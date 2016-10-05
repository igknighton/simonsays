
import javax.swing.JFrame;

public class SimonShape extends JFrame{

	
	
	
	private SimonPanel simon;
	
	public SimonShape ()
	{
		
		setSize(800,800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		
		simon =  new SimonPanel(this);
		simon.setFocusable(true);
		simon.setOpaque(true);
		this.add(simon);
		setVisible(true);
		simon.requestFocusInWindow();		
		simon.randomColorChange();
	}
	
	

	
}

