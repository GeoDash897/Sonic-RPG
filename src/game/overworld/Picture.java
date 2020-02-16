/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import java.awt.Graphics2D;
import java.util.Comparator;

/**
 *
 * @author GeoSonicDash
 */
public interface Picture {
    void draw(Graphics2D g2);
    int getLayer();
    public static Comparator<Picture> pictureCompareLayer = new Comparator<Picture>() {
	    //Compares Student objects based on Student's year
            @Override
	    public int compare(Picture student1, Picture student2) {	    	
	      String layer1 = String.valueOf(student1.getLayer());
	      String layer2 = String.valueOf(student2.getLayer());	      
	      //ascending order
	     return layer1.compareTo(layer2);
            }
    };
}
