/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import static game.Launcher.debugStat;
import game.display.Display;
import game.input.PlayerInput;
import game.overworld.OverWorld;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author GeoSonicDash
 */
public class GameLoop extends JPanel {
    private static int length;
    private static int width;
    private static String title;
    private static boolean debug; 
    private static boolean isPainting;  
    private ArrayList<Thread> objectThreads;
    private File temp;
    private OverWorld overWorld;
    private PlayerInput pIRemade;
    private Display display;
    
    public GameLoop(String title, int length, int width) {
        this.length = length;
        this.width = width;
        this.title = title;
        this.isPainting = true;       
        create();
    }
    
    private void create() {
        //creates TempSave.txt- file where everything is temp. saved during gameplay
        createTempSave();
        display = new Display(title, length, width);
        Container c = display.getFrame().getContentPane();
        setOpaque(false);//allows for setting a color background in JPanel
        c.setBackground(Color.gray);//background color can be changed
        pIRemade = new PlayerInput();
        overWorld = new OverWorld();
    }
       
    public void start() {
        display.addKeyListener(pIRemade);
        display.addJPanel(this);//Adding the JPanel invokes the paintComponent method- game actually starts
        /*I put it in a separate method call in order to make sure that the container, setOpaque, and setBackgroundColor
        methods have run before adding the JPanel to the JFrame*/
    }
    
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
                bw.write(currentLine);
                bw.newLine();    
                currentLine = br.readLine();
            }
            bw.flush();
            bw.close();
            br.close();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }    
    
    @Override
    public void paintComponent(Graphics g) {//opens paint method
        /*Create Graphics2D object from Graphics (use to draw everything),
            use that to set RenderingHints to draw everything based on speed > quality*/
            Graphics2D g2 = (Graphics2D) g;
            RenderingHints rh = new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
            g2.setRenderingHints(rh);
            g2.setFont(debugStat);
            g2.setColor(Color.CYAN); 
            if(PlayerInput.checkIsPressedOnce(KeyEvent.VK_ESCAPE)) {
                debug = !debug;
            }
            pIRemade.standard();//Needed for buttonPressTimer/buttonReleaseTimer to function (allows them to increase)
            overWorld.standard();//main method for game             
            overWorld.draw(g2);
            pIRemade.draw(g2);
            if(isPainting) {
                repaint();    
            }    
            else {
                end();
            }
    }
    
    public void end() {
        temp.deleteOnExit();
        System.out.println("Code Died :(");
        System.exit(0);
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
}
