/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author GeoSonicDash
 */
public interface DefaultObject {
    void create();
    void draw(Graphics2D g2);
    void action();
    void interactWithSonic();
    int getID();
    int getXRef();
    int getYRef();
    int getLength();
    int getWidth();
    Rectangle getHitBox();
}
