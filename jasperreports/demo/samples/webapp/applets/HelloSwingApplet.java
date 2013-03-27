import javax.swing.*;
import java.awt.*;

public class HelloSwingApplet extends JApplet 
{
	
	// This is a hack to avoid an ugly error message in 1.1.
	public HelloSwingApplet() 
	{
		getRootPane().putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
	}
	
	public void init() 
	{
		JLabel label = new JLabel("You are successfully running a Swing applet!");
		label.setHorizontalAlignment(JLabel.CENTER);
		
		//Add border.  Should use createLineBorder, but then the bottom
		//and left lines don't appear -- seems to be an off-by-one error.
		label.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 2, Color.black));
		
		getContentPane().add(label, BorderLayout.CENTER);
	}
	
}
