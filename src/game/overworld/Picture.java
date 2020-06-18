/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import java.awt.Graphics2D;
import java.util.Comparator;

/**Represents a game element that has an Image that needs to be displayed on screen.
 *
 * @author GeoSonicDash
 */
public interface Picture {
    void draw(Graphics2D g2);
    int getLayer();
    /**Compares the layer value of the game element in ascending order-
     * the game elements with the lower layer value are drawn first.
     */
    public static Comparator<Picture> pictureCompareLayer = new Comparator<Picture>() {
	    //Compares Picture objects based on Picture's layer value
            @Override
	    public int compare(Picture picture1, Picture picture2) {	    	
	      String layer1 = String.valueOf(picture1.getLayer());
	      String layer2 = String.valueOf(picture2.getLayer());	      
	      //ascending order
	     return layer1.compareTo(layer2);
            }
    };
}
