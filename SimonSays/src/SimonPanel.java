import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class SimonPanel extends JPanel{

	private int width = 500;
	private int height = 500;
	private int x = 150;
	private int y = 150;
	private int degree = 45; 
	private int level = 0;
	private int random;
	private int count = 0;
	private Map<SimonColor, Shape> dirShapeMap = new LinkedHashMap<>();
	private SimonColor activeColor = null;
	private int lightUpSpd = 1000;
	private Timer displayColorTimer;
	SimonColor randomColor;
	private ArrayList<SimonColor> comp = new ArrayList<SimonColor>();
	private SimonShape frame;
	
	//disables keyBinding when color sequence is running
	private boolean keyEnabled = false;
	
	
	
	public SimonPanel(SimonShape jf)
	{	
		
		this.frame = jf;
		for (SimonColor simonColor: SimonColor.values()) {
	         Shape shape = new Arc2D.Double(x, y, width, height, degree, 90, Arc2D.PIE);
	         dirShapeMap.put(simonColor, shape);
	         degree += 90;
	      }
		
		setKeyBindings();
	}
	
	
	public void randomColorChange()
	{
			level++;
			JOptionPane.showMessageDialog(this, "Level " + level);
			random = (int) (Math.random()*4);
	        randomColor = SimonColor.values()[random];
	        comp.add(randomColor);
	      
	         new Timer(100, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					displaySimonColorList(comp);
					((Timer) e.getSource()).stop();	
					
				}	
	         }).start();
	         
	}

	   public void displaySimonColorList(List<SimonColor> dirList) {
	      displayColorTimer = new Timer(lightUpSpd, new DisplayColorTimerListener(dirList));
	      displayColorTimer.start();    
	   }

	
	public void paintComponent(Graphics g)
	{	
		super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    
	      for (SimonColor sc : dirShapeMap.keySet()) {
	         Color color = (activeColor == sc) ? sc.getActiveColor() : sc.getPassiveColor();
	         g2.setColor(color);
	         g2.fill(dirShapeMap.get(sc));
	      }
		
	}
	
	 private class TurnOffListener implements ActionListener {
	      @Override
	      public void actionPerformed(ActionEvent e) {
	         activeColor = null;
	         repaint();
	      }
	   }

	private class DisplayColorTimerListener implements ActionListener {
		
	      private List<SimonColor> SimonColorList;
	      private int counter = 0;
	      private Timer turnOffTimer;
	      
	      public DisplayColorTimerListener(List<SimonColor> SimonColorList) {
	         this.SimonColorList = SimonColorList;
	      }

	      @Override
	      public void actionPerformed(ActionEvent e) {
	         Timer timer = (Timer) e.getSource();
	         if (counter == SimonColorList.size()) {
	            activeColor = null;
	            keyEnabled = true;
	            timer.stop();
	         } else {
	            activeColor = SimonColorList.get(counter);
	           // System.out.println(SimonColorList.get(counter));
	            
	            counter++;

	            // so there's a time gap in the display, so that same colors will 
	            // be distinct
	            int turnOffDelay = timer.getDelay() / 2; // turn off 1/2 time into display
	            turnOffTimer = new Timer(turnOffDelay, new TurnOffListener());
	            turnOffTimer.setRepeats(false);
	            turnOffTimer.start();
	         }
	         repaint();         
	      }
	   }
	
	
	
	
	public void setKeyBindings()
	{
		  Map<SimonColor, Integer> dirToKeyMap = new HashMap<>();
	      dirToKeyMap.put(SimonColor.GREEN, KeyEvent.VK_UP);
	      dirToKeyMap.put(SimonColor.RED, KeyEvent.VK_LEFT);
	      dirToKeyMap.put(SimonColor.BLUE, KeyEvent.VK_DOWN);
	      dirToKeyMap.put(SimonColor.YELLOW, KeyEvent.VK_RIGHT);
		
	      int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
	      InputMap inputMap = getInputMap(condition);
	      ActionMap actionMap = getActionMap();
	      boolean keyReleased;
	      int keyCode;
	      KeyStroke pressedKeyStroke; 
	      KeyStroke releasedKeyStroke;
	      
	      
	      for (SimonColor dir : SimonColor.values()) {
	          keyCode = dirToKeyMap.get(dir);
	          
	          keyReleased = false; // key pressed
	          pressedKeyStroke = KeyStroke.getKeyStroke(keyCode, 0, keyReleased);
	          inputMap.put(pressedKeyStroke, pressedKeyStroke.toString());
	          actionMap.put(pressedKeyStroke.toString(), new KeyBindingAction(dir, keyReleased));

	          keyReleased = true; // key released
	          releasedKeyStroke = KeyStroke.getKeyStroke(keyCode, 0, keyReleased);
	          inputMap.put(releasedKeyStroke, releasedKeyStroke.toString());
	          actionMap.put(releasedKeyStroke.toString(), new KeyBindingAction(dir, keyReleased));
	      }
	
	
	}
	
	 private class KeyBindingAction extends AbstractAction {
	      private SimonColor color;
	      private boolean keyReleased;
	      

	      public KeyBindingAction(SimonColor color, boolean keyReleased) {
	         this.color = color;
	         this.keyReleased = keyReleased;
	      }

	     
	      public void actionPerformed(ActionEvent e) {
 
	    	  if (keyEnabled)
	    	  {
	    	  activeColor = color;
	    	  	  
	    		  if (!keyReleased) //if key is pressed
	    			  	repaint();	
	    		  else { //if key is released
	    			  if (activeColor != comp.get(count)) {
	    				  	JOptionPane.showMessageDialog(frame, "Incorrect");
	    				  	frame.setVisible(false);
	    				  	frame.dispose();
	    				  	return;
  		  					}
	    			  else { 
	    				  	 count++;
	    				 if (count == comp.size()) {
	    				  		 JOptionPane.showMessageDialog(frame, "Correct!");
	    				  		 
	    				  		 if (lightUpSpd > 400)
	    		    		     lightUpSpd = lightUpSpd - 20;
	    				  		 //System.out.println("Current Speed: " + lightUpSpd);
	    				  		 keyEnabled = false;
	    		    		     count = 0;
	    		    		     randomColorChange();  
	    				  	}    				  	
	    			  	}
	    			  activeColor = null;
	    			  repaint();
	    		  	}  
	    	  }
	      }
	   }
}
