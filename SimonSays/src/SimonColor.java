import java.awt.Color;


public enum SimonColor {
GREEN(Color.green),
RED(Color.red),
BLUE(Color.blue),
YELLOW(Color.yellow);


 private Color color;
 
 private SimonColor(Color color)
 {
	 this.color = color;
 }

public Color getColor()
{
	return color;
}

public Color getActiveColor()
{
	return color.brighter();
}

public Color getPassiveColor()
{
	return color.darker();
}



}
