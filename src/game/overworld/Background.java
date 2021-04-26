/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

/**Controls obtaining/storing/drawing the correct background image for each {@code Room}.
 *
 * @author GeoSonicDash
 */
public class Background implements Picture {
    private Image background;
    public Background(String fileName) {
        getImage(fileName);
    }
    
    private void getImage(String fileName) {
        String temp = String.valueOf(fileName);
        background = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\backgrounds\\"+temp+" Background.png");
    }
    
    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(background, -250, 0, 1920, 1080, null);
    }

    @Override
    public int getLayer() {
        return 0;
    }
    
}
