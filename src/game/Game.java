package game;
import game.overworld.OverWorld;
import game.sonic.*;
import java.awt.*;//needed for graphics
import javax.swing.*;//needed for JFrame window
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
    Author: GeoDash897  Date:10/5/19    Updated:11/29/19
*/
public class Game extends JFrame implements KeyListener, ActionListener {
//opens program
    private static int wide;
    private static int high;
    private static boolean debug;
    public static Font debugStat;
    public static Font dialog;
/***********************************************************/
    public Game() {//constructor for JPanel
        add(new JP());
    }//close Jpanel Contructor
/***********************************************************/
    public static void main(String[] args) {//start main method
        Music.standard();  
        System.setProperty("sun.java2d.opengl", "True");
        Game w = new Game();
        w.setTitle("RPGTest");
        w.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wide = (int) w.getWidth();//gets the width of the screen
        high = (int) w.getHeight();//gets the height of the screen
        w.setVisible(true);
        w.addKeyListener(w);  
        try {
            debugStat = Font.createFont(Font.TRUETYPE_FONT, new File("src\\game\\resources\\clacon.ttf")).deriveFont(25f);
            dialog = Font.createFont(Font.TRUETYPE_FONT, new File("src\\game\\resources\\clacon.ttf")).deriveFont(40f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(debugStat);
            ge.registerFont(dialog);
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            g2.setFont(debugStat);
            g2.setColor(Color.CYAN);
            OverWorld overWorld = new OverWorld();//creates object OverWorld (which in turn creates everything else)
            overWorld.standard(g2);
            super.paintComponent(g2);//allows for painting and
            repaint();
        } 
    }
    public static boolean getDebug() {
        return debug;
    }
    public static void setDebug() {
        debug = !debug;
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
        if(e.getKeyCode() == e.VK_X) {
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
              getPressInput(e);
        }
        if (e.getKeyCode() == e.VK_ENTER) {
            
        }
        if (e.getKeyCode() == e.VK_ESCAPE) {
            getPressInput(e);
        }
    }//end keypressed
/***********************************************************/
    
}//close program


