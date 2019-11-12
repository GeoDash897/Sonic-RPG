/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

/**
 *
 * @author GeoSonicDash
 */
public class InteractObject {
    private int xCenter;
    private int yCenter;
    private int xDraw;
    private int yDraw;
    private int id; 
    private int type;
    public InteractObject(int id, int type, int xCenter, int yCenter) {
        this.id = id;
        this.type = type;
        this.xCenter = xCenter;
        this.yCenter = yCenter;
    }
    public void action() {
        
    }
}
