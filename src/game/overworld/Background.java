/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import game.overworld.Room.RoomType;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

/**
 *
 * @author GeoSonicDash
 */
public class Background implements Picture {
    private Image background;
    public Background(RoomType roomType) {
        getImage(roomType);
    }
    
    private void getImage(RoomType roomType) {
        String temp = String.valueOf(roomType);
        background = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\backgrounds\\"+temp+" Background.png");
    }
    
    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(background, 0, 0, 1920, 1080, null);
    }

    @Override
    public int getLayer() {
        return 0;
    }
    
}
