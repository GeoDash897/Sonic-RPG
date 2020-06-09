/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game.music.Music;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**First class of the project, used to create the main game loop.
 *
 * @author GeoSonicDash
 */
public class Launcher {
    public static Font debugStat;
    public static Font dialogFont;  
    public static Font statusScreen;
    public static void main (String [] args) {      
        Music.standard();  
        System.setProperty("sun.java2d.opengl", "True"); 
        //Load all custom fonts HERE
        try {
            debugStat = Font.createFont(Font.TRUETYPE_FONT, new File("src\\game\\resources\\clacon.ttf")).deriveFont(25f);
            dialogFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\game\\resources\\clacon.ttf")).deriveFont(40f);
            statusScreen = Font.createFont(Font.TRUETYPE_FONT, new File("src\\game\\resources\\clacon.ttf")).deriveFont(60f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(debugStat);
            ge.registerFont(dialogFont);
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        GameLoop gl = new GameLoop("Test", 1920, 1080);
        gl.start();
    }
}
