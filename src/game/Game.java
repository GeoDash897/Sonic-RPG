package game;
import game.overworld.OverWorld;
import game.sonic.*;
import java.awt.*;//needed for graphics
import javax.swing.*;//needed for JFrame window
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/*
    Author: GeoDash897  Date:10/5/19    Updated:10/5/19
*/
public class Game extends JFrame implements KeyListener, ActionListener {
//opens program
    private static int wide;
    private static int high;
/***********************************************************/
    public Game() {//constructor for JPanel
        add(new JP());
    }//close Jpanel Contructor
/***********************************************************/
    public static void main(String[] args) {//start main method
        System.setProperty("sun.java2d.opengl", "True");
        Game w = new Game();
        w.setTitle("RPGTest");
        w.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wide = (int) w.getWidth();//gets the width of the screen
        high = (int) w.getHeight();//gets the height of the screen
        w.setVisible(true);
        w.addKeyListener(w);
      
      
        
    }//close main
/***********************************************************/
    public class JP extends JPanel {//start JPanel CLass

        public JP() {
            Container c = getContentPane();
            setOpaque(false);//allows for setting a color background in JPanel
            c.setBackground(Color.gray);//background color can be changed
        }

        public void paint(Graphics g) {//opens paint method
            Graphics2D g2 = (Graphics2D) g;
            RenderingHints rh = new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
            g2.setRenderingHints(rh);
            super.paint(g2);//allows for painting and repainting
            OverWorld overWorld = new OverWorld();//creates object OverWorld (which in turn creates everything else)
            overWorld.standard(g2);
            repaint();
        } 
    }
    public void getPressInput(KeyEvent e) {
        Sonic sonic = new Sonic();
        sonic.keyPressed(e);      
    }
    public void getReleasedInput(KeyEvent e) {
        Sonic sonic = new Sonic();
        sonic.keyReleased(e);     
    }
    public void actionPerformed(ActionEvent e) {
    
    }

    public void keyTyped(KeyEvent e) {
        
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == e.VK_RIGHT ) {
            getReleasedInput(e);
        }
        if (e.getKeyCode() == e.VK_LEFT ) {           
            getReleasedInput(e);
        } 
        if (e.getKeyCode() == e.VK_DOWN ) {           
            getReleasedInput(e);
        }
        if (e.getKeyCode() == e.VK_Z ) {           
            getReleasedInput(e);
        } 
    }
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_RIGHT ) {
            getPressInput(e);
        }
        if (e.getKeyCode() == e.VK_LEFT ) {           
            getPressInput(e);
        }          
        if (e.getKeyCode() == e.VK_UP ) {
            getPressInput(e);
        }
        if (e.getKeyCode() == e.VK_DOWN) {
            getPressInput(e);
        }
        if (e.getKeyCode() == e.VK_Z ) {
            getPressInput(e);
        }
        if (e.getKeyCode() == e.VK_X ) {
              
        }
        if (e.getKeyCode() == e.VK_ENTER) {
            
        }                 
    }//end keypressed
/***********************************************************/
    
}//close program


