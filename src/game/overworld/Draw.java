/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import game.gui.GUI;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;

/**Controls drawing the game elements of the current Room of the game.
 *
 * @author GeoSonicDash
 */
public class Draw {
    /**Draws the game elements of the Room
     * NOTE- This is how drawing works- the first thing that is drawn is the picture arrayList- this includes Sonic,
     * groundTiles, and gameObjects. Then all of the gui's are drawn (if they are visible).
     * @param g2 {@code Graphics2D} object needed for drawing.
     * @param listOfObjects list of pictures that need to be drawn (from the current Room).
     * @param guis list of GUI elements that need to be drawn.
     */
    public static void drawInLayers(Graphics2D g2, ArrayList<Picture>listOfObjects, ArrayList<GUI> guis) {
        Collections.sort(listOfObjects,Picture.pictureCompareLayer);         
        for(int i = 0; i < listOfObjects.size(); i++) { 
            listOfObjects.get(i).draw(g2);
        }
        if(guis != null) {
            for(GUI temp : guis) {
                if(temp.isVisible()) {
                   temp.drawGUI(g2);
                }
            } 
        }       
    }
}
