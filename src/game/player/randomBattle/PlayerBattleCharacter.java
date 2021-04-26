/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.player.randomBattle;

import game.player.PlayerBattleMenu;
import game.player.PlayerCharacter;

/**
 *
 * @author GeoSonicDash
 */
public class PlayerBattleCharacter extends BattleCreature {
    
    private int rings;
    private int maxRings;
    private boolean isBattleMenuShowing;
    private static int coins;
    
    
    PlayerCharacter playerCharacter;
    PlayerBattleMenu playerBattleMenu;
    
    public PlayerBattleCharacter(PlayerCharacter playerCharacter) {
        super();
        this.playerCharacter = playerCharacter;
        this.rings = 1;
        this.maxRings = this.rings;
        coins = 5;
        this.playerBattleMenu = new PlayerBattleMenu();
    }
    
    @Override
    public void battleStandard() {
        
    }
  
    public PlayerBattleMenu getPlayerBattleMenu() {
        return playerBattleMenu;
    }
    
}
