/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author GeoSonicDash
 */
public class Draw {   
    public static void drawInLayers(Graphics2D g2, ArrayList<Picture>listOfObjects) {
        Collections.sort(listOfObjects,Picture.pictureCompareLayer);         
        for(int i = 0; i < listOfObjects.size(); i++) { 
            listOfObjects.get(i).draw(g2);
        }
    }
}
