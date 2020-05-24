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

/**
 *
 * @author GeoSonicDash
 */
public class Draw {
    /*NOTE- This is how drawing works- the first thing that is drawn is the picture arrayList- this includes Sonic,
    groundTiles, and gameObjects. Then all of the gui's are drawn (if they are visible)*/
    public static void drawInLayers(Graphics2D g2, ArrayList<Picture>listOfObjects, ArrayList<GUI> guis) {
        Collections.sort(listOfObjects,Picture.pictureCompareLayer);         
        for(int i = 0; i < listOfObjects.size(); i++) { 
            listOfObjects.get(i).draw(g2);
        }
        for(GUI temp : guis) {
            if(temp.isVisible()) {
               temp.drawGUI(g2);
            }
        }
    }
}
