/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import game.sonic.*;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 *
 * @author GeoSonicDash
 */
public class Monitor extends OverWorld implements DefaultObject {
    private int group; 
    private MonitorType monitorType;
    private int xRef;
    private int yRef;
    private int length;
    private int width;
    private int layer;
    private Rectangle hitBox;
    private Image monitorPicture;
    private boolean ground;
    private boolean observable = false;
    private OverWorld overworld = new OverWorld();
    private Sonic sonic = new Sonic();
    Monitor(MonitorType monitorType, int layer, int xRef, int yRef) {
        group = 1;
        this.monitorType = monitorType;
        this.layer = layer;
        this.xRef = xRef;
        this.yRef = yRef;
        this.ground = false;
    }
    @Override
    public void create() {
        if(monitorType == MonitorType.MONITOR_RING) {
            monitorPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Ring Monitor.gif");
        }
        if(monitorType == MonitorType.MONITOR_SPEED) {
            monitorPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Speed Monitor 1.png");
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
    public void interactWithSonic(Rectangle sensor) {
        if(monitorType == MonitorType.MONITOR_RING) {
            sonic.increaseRings(10);    
        } 
        else if(monitorType == MonitorType.MONITOR_SPEED) {
            sonic.changeOWPowerUp(1);    
        }
    }
    @Override
    public int getGroup() {
        return group;
    }
    public MonitorType getMonitorType() { 
        return monitorType;
    }
    @Override
    public int getXRef() {
        return xRef;
    }

    @Override
    public int getYRef() {
        return yRef;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public int getWidth() {
        return width;
    }
    @Override
    public int getLayer() {
        return layer;
    }
    @Override
    public Rectangle getHitBox() {
        return hitBox;
    }
    @Override
    public String toString() {
        return "Monitor: "+monitorType+" "+layer+" "+xRef+" "+yRef;
    }
    public enum MonitorType {
        MONITOR_RING,
        MONITOR_SPEED
    }
}
