/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gui;

import static game.Launcher.debugStat;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

/**
 *
 * @author GeoSonicDash
 */
public class Dialog {
    private String speakerName;
    private String dialog;
    private Image speakerPic;
    public Dialog(String speakerName, String dialog) {
        this.speakerName = speakerName;
        this.dialog = dialog;
        getSpeakerPic();
    }
    
    private void getSpeakerPic() {
        switch(speakerName) {
            case "SONIC_NORMAL":
                speakerPic = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Face Normal.png");
            default:
                break;    
        }
            
    }
    
    public void draw(Graphics2D g2) {
        g2.setFont(debugStat);
        g2.setColor(Color.BLUE);
        g2.fillRect(0,0,2000,150);
        g2.setColor(Color.WHITE);
        g2.drawString(speakerName+" :"+dialog, 100, 100);
    }
    
    @Override
    public String toString() {
        return "speakerName - "+speakerName+", dialog - "+dialog;
    }
}
