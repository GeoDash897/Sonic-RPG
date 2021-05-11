/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.player.randomBattle;

import game.overworld.Picture;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author GeoSonicDash
 */
public class BattleCreature implements Picture {
    private int level;
    private int exp;
    private int health;
    private int maxHealth;
    private int attack;
    private int defense;
    private int speed;
    private int layer;
    private boolean isAttacking;
    
    private Rectangle hitBox;
    
    public BattleCreature() {
        this.level = 1;
        this.exp = 1;
        this.health = 1;
        this.maxHealth = this.health;
        this.attack = 1;
        this.defense = 1;
        this.speed = 1;
        this.layer = 1;
        this.hitBox = null;
        this.isAttacking = false;
    }
    
    public void battleStandard() {
        
    }
    
    public void draw(Graphics2D g2) {
        
    }
    
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isAttacking() {
        return isAttacking;
    }
    
    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    @Override
    public int getLayer() {
        return layer;
    }
    
    
}
