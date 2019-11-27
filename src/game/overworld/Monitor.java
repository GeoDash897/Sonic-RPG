/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import game.sonic.Sonic;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 *
 * @author GeoSonicDash
 */
public class Monitor extends OverWorld implements DefaultObject {
    private int id;
    private int xRef;
    private int yRef;
    private int length;
    private int width;
    private Rectangle hitBox;
    private Image monitorPicture;
    private boolean ground;
    private OverWorld overworld = new OverWorld();
    private Sonic sonic = new Sonic();
    Monitor(int id, int xRef, int yRef) {
        this.id = id;
        this.xRef = xRef;
        this.yRef = yRef;
        this.ground = false;
    }
    @Override
    public void create() {
        if(id == 1) {
            monitorPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Ring Monitor.png");
        }
        length = 27;
        width = 32;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(monitorPicture, xRef, yRef, length*4, width*4, this);
        //g2.fillRect(xRef, yRef, length*4, width*4);
    }
    @Override
    public void action() {
        hitBox = new Rectangle(xRef, yRef, length*4, width*4);
        for(Ground var : overworld.getGroundArrayList()) {
            if(hitBox.intersects(var.getPixelBox(0))) {
                ground = true;
            }
        }
        if(ground == false) {
            yRef += 16;
        }     
    }
    @Override
    public void interactWithSonic() {
        if(id == 1) {
            sonic.increaseRings(10);    
        }     
    }

    @Override
    public int getID() {
       return id;
    }

    @Override
    public int getXRef() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getYRef() {
        return yRef;
    }

    @Override
    public int getLength() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public Rectangle getHitBox() {
        return hitBox;
    }
    @Override
    public String toString() {
        return "Monitor: "+xRef+", "+yRef;
    }
}
