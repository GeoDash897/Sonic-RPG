/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.enemies.battleEnemies;

import game.player.randomBattle.BattleCreature;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author GeoSonicDash
 */
public class TempEnemy extends BattleCreature {
    
    private int xDrawCenterEnemy;
    private int ySpriteCenterEnemy;
    
    public TempEnemy(int x, int y) {
        super();
        this.xDrawCenterEnemy = x;
        this.ySpriteCenterEnemy = y;
    }
    
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.fillRect(xDrawCenterEnemy-32, ySpriteCenterEnemy-32, 64, 64);
    }
}
