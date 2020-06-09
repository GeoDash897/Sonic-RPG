package game.display;

import game.GameLoop;
import game.input.PlayerInput;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
/**This class handles JFrame, JPanel, and KeyListener implementations.
 * 
 * @author GeoSonicDash
 */
public class Display {

	private JFrame frame;
        
	private String title;
	private int width, height;
	
	public Display(String title, int width, int height){
            this.title = title;
            this.width = width;
            this.height = height;
            setUpJFrame();
	}
	
	private void setUpJFrame(){
            frame = new JFrame(title);
            frame.setSize(width, height);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);               
            frame.setResizable(true);	
	}
	
	public JFrame getFrame(){
		return frame;
	}
        
        /**Adds GameLoop (JPanel) to JFrame- starts the paintComponent method.
         * 
         * @param a get instance of JPanel needed for JFrame + to access end method (allow to run on exit).
         */
        public void addJPanel(GameLoop a) {
            frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                a.end();//Runs the end method when the window is closed
            }
            });
            frame.add(a);
            frame.setVisible(true);	
        }
        
        public void addKeyListener(PlayerInput a) {
            frame.addKeyListener(a); 
        }
}
