package game;
import game.overworld.OverWorld;
import java.awt.*;//needed for graphics
import javax.swing.*;//needed for JFrame window
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * This is the main file for the game.
 * @author GeoSonicDash
 */
public class Game extends JFrame implements KeyListener, ActionListener {
//opens program
    private static boolean debug; 
    private static boolean loadTempSave = false;
    private static ArrayList<Thread> objectThreads;
    public static Font debugStat;
    public static Font dialog;    
    private File temp;
    private OverWorld overWorld;
    private PlayerInput playerInput;
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
        
        @Override
        public void paint(Graphics g) {//opens paint method
            /*Create Graphics2D object from Graphics (use to draw everything),
            use that to set RenderingHints to draw everything based on speed > quality*/
            Graphics2D g2 = (Graphics2D) g;
            RenderingHints rh = new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
            g2.setRenderingHints(rh);
            g2.setFont(debugStat);
            g2.setColor(Color.CYAN);    
            if(playerInput == null) {
                playerInput = new PlayerInput();
            }
            playerInput.standard(g2);//Needed for zPressTimer and xPressTimer to function (allows them to increase when their key is pressed)
            if(overWorld == null) {//creates object OverWorld (which in turn creates everything else)
                overWorld = new OverWorld();
            }            
            //creates TempSave.txt- file where everything is temp. saved during gameplay
            if(!loadTempSave) {
                createTempSave(); 
            }
            if(loadTempSave) {  
                //Destroys all the threads used to copy text over to TempSave.txt and start overWorld.standard method
                objectThreads.removeAll(objectThreads);
                overWorld.standard(g2);//main method for game                               
            } 
            temp.deleteOnExit();
            super.paintComponent(g2);//allows for painting and repainting
            repaint();
        } 
    }
    
    /**
     * Creates {@code TempSave.txt}, the file where everything is saved during game play.
     */
    public void createTempSave() {
        File local = new File("src/game/Area1.txt");
        temp = new File("src/game/TempSave.txt");
        if(objectThreads == null) {
            objectThreads = new ArrayList<Thread>();
        }
        try {
            if(!temp.exists()) {
                temp.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader(local));
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
            String currentLine = br.readLine();
            while(currentLine != null) {
                /*For each line, a thread is created- the thread reads the line of text
                and copys that line of text over, splits the work of reading each line*/
                Thread line = new Thread(new CopyFile(bw,currentLine));
                line.start();
                try {
                    line.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }               
                /*All the threads are added to objectThreads arrayList to store them
                This makes it easy to remove them*/
                objectThreads.add(line);             
                currentLine = br.readLine();
            }
            bw.flush();
            bw.close();
            br.close();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }    
        loadTempSave = true;
    }
    
    /** @return 
     * <ul>
     * <li>{@code false}- the debug menu is not displayed.
     * <li>{@code true}- the debug menu is displayed.
     * </ul>
     */
    public static boolean getDebug() {
        return debug;
    }
    
    /**
     * Sets the value of boolean {@code debug}.
     */
    public static void setDebug() {
        debug = !debug;
    }
    
    /**
     * Sends the input (keys that are pressed) from the player to PlayerInput class.
     * @param e KeyEvent from {@code keyPressed} method,
     * input from the player.
     */
    public void getPressInput(KeyEvent e) {
        playerInput.keyPressed(e);      
    }
    
    /**
     * Sends the input (keys that are released) from the player to PlayerInput class.
     * @param e KeyEvent from {@code keyReleased} method,
     * input from the player.
     */
    public void getReleasedInput(KeyEvent e) {
        playerInput.keyReleased(e);     
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
        if (e.getKeyCode() == e.VK_UP ) {           
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
            OverWorld ow = new OverWorld();
            ow.keyPressed(e);
        }
        if (e.getKeyCode() == e.VK_C) {
            OverWorld ow = new OverWorld();
            ow.keyPressed(e);
        }
        if (e.getKeyCode() == e.VK_ESCAPE) {
            getPressInput(e);
        }
    }//end keypressed

    /**
     * Thread used to process each line of {@code Area.txt}.
     */
    class CopyFile implements Runnable {
        private boolean isDone;
        private BufferedWriter bw;
        private String currentLine;
        public CopyFile(BufferedWriter bw, String currentLine) {
            this.isDone = false;
            this.bw = bw;
            this.currentLine = currentLine;
        }
        /**
        * Thread that takes line of {@code Area1.txt} and copies it to a {@code TempSave.txt}.
        */
        @Override
        public synchronized void run() {
            while(!isDone) {
                try {   
                    if(currentLine != null) {
                        //If the current that the thread is processing is not null, copy it to other TempSave.txt
                        bw.write(currentLine);
                        bw.newLine();    
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } 
                isDone = true; //End the thread            
            }                       
        }
    }
}//close program


