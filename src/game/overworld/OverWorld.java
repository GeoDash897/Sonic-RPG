/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;
import game.Game;
import game.sonic.*;
import java.awt.Graphics2D;
import java.util.ArrayList;
/*
    Author: GeoDash897  Date:10/5/19    Updated:10/5/19
*/
//memes
public class OverWorld extends Game {;
    public static ArrayList<Tile> environmentTiles = new ArrayList<Tile>();
    public void standard(Graphics2D g2) {
        if(environmentTiles.size() < 25) {//limits how many tiles are created (don't want to constantly create tile objects = lag
            //Sending X,Y and angles of tiles I want to create to method
            /*createTile(799,444,45,1);
            createTile(856,386,45,1);
            createTile(912,327,45,1);   
            createTile(975,326,0,1);
            createTile(1032,327,45,0);
            createTile(1100,383,0,1);
            createTile(200,500,0,1);*/
            for(int i = 0; i < 24;i++) {
                createTile(0+(i*64),664,0,1);    
            }      
            createTile(200,500,0,1);
            //createTile(640,500,0,1);
        }
        for(Tile create : environmentTiles) {
            create.create(); //Creates the Rectangle hitboxes 
            if(create.getAngle() == 0) {
                create.drawRectangle(g2);    
            }
            else if(create.getAngle() != 0) {
                create.drawSlope(g2);    
            }
        }

        g2.drawString("OverWorld is running",100,100);
        Sonic sonic = new Sonic();
        sonic.setup(g2);
    }
    public void createTile(int xRef, int yRef, int angleOfTile, int direction) {//Actually creates the Tile Objects and adds it to the arrayList
            environmentTiles.add(new Tile(xRef,yRef,angleOfTile,direction));                
    }
}
