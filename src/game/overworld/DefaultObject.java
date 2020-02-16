/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import java.awt.Rectangle;
import java.util.Comparator;

/**
 *
 * @author GeoSonicDash
 */
public interface DefaultObject {
    void create();
    void action();
    void interactWithSonic(Rectangle sensor);
    String getGroup();
    int getXRef();
    int getYRef();
    int getLength();
    int getWidth();
    int getLayer();
    boolean isSameLayer(int otherLayer);
    Rectangle getHitBox();
    public static Comparator<DefaultObject> defaultObjectCompareLayer = new Comparator<DefaultObject>() {
	    //Compares Student objects based on Student's year
            @Override
	    public int compare(DefaultObject student1, DefaultObject student2) {	    	
	      String layer1 = String.valueOf(student1.getLayer());
	      String layer2 = String.valueOf(student2.getLayer());	      
	      //ascending order
	     return layer1.compareTo(layer2);
            }
    };
}
