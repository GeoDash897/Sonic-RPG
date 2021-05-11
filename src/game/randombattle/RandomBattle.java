/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.randombattle;

import game.enemies.battleEnemies.TempEnemy;
import game.gui.GUI;
import game.overworld.Background;
import game.overworld.Draw;
import game.overworld.Picture;
import game.overworld.Room;
import game.player.randomBattle.BattleCreature;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author GeoSonicDash
 */
public class RandomBattle {
    private int turnCounter;
    
    private ArrayList<Picture> pictures;
    private ArrayList<GUI> guis;
    private ArrayList<BattleCreature> turnOrder;
    
    private Room roomOfBattle;
    
    /*Note!- the battle object has to be created first before the players are added to the battle
    (the randomBattle of the currentRoom won't be changed until the object is created first (if the method
    was run in the constructor, it would cause an error since the randomBattle of the room would be null
    (wouldn't have been changed yet*/
    public RandomBattle(Room roomOfBattle) {
        this.turnCounter = 0;
        this.roomOfBattle = roomOfBattle;
        this.turnOrder = new ArrayList<BattleCreature>();
        this.pictures = new ArrayList<Picture>();
        this.guis = new ArrayList<GUI>();
        this.pictures.add(new Background(roomOfBattle.getRoomType()+" Battle"));
    }
    
    public void battleStandard() {
        for(GUI temp : guis) {
            if(temp.isVisible()) {
                temp.standardGUI();
            }
        }
    }
    
    public void draw(Graphics2D g2) {
        Draw.drawInLayers(g2, pictures, guis);
    }
    
    public void firstTimeSetUp() {
        roomOfBattle.getOverWorld().getManager().addToNewBattle(this);
        TempEnemy temp = new TempEnemy(1000, 500);
        turnOrder.add(temp);
        pictures.add(temp);
    }
    
    public void addBattleCreature(BattleCreature temp) {
        turnOrder.add(temp);
    }
    
    public void removeBattleCreature(BattleCreature index) {
        turnOrder.remove(index);
    }
    
    public void addPicture(Picture add) {
        /*This checks to make sure sonic's picture isn't added more than once into the room's guiAL,
        if the sonic's picture isn't in it (meaning the player didn't enter it yet), add it
        I don't want to clear it out since it would be redunant (why clear out all of the pictures, just
        to add them again if the player returned to a previous room).
        This is also a good failsafe if something is accidently added again*/
        boolean check = false;
        for(int i = 0; i < pictures.size(); i++) {
            if(add == pictures.get(i)) {
                check = true;
            }
        }
        if(!check) {
            pictures.add(add);
        }       
    }
    
    public void removePicture(Picture index) {
        pictures.remove(index);
    }
    
    public void addGUI(GUI add) {
        /*This checks to make sure playerMenu isn't added more than once into the room's guiAL,
        if the playerMenu isn't in it (meaning the player didn't enter it yet), add it
        I don't want to clear it out since it would be redunant (why clear out all of the GUIS, just
        to add them again if the player returned to a previous room).
        This is also a good failsafe if something is accidently added again*/
        boolean check = false;
        for(int i = 0; i < guis.size(); i++) {
            if(add == guis.get(i)) {
                check = true;
            }
        }
        if(!check) {
            guis.add(add);
        }       
    }
}
