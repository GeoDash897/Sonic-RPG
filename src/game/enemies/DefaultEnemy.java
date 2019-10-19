/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.enemies;

import java.awt.Rectangle;

/**
 *
 * @author GeoSonicDash
 */
public interface DefaultEnemy {
    int getEnemyCoins();
    int getEnemyDefense();
    int getEnemyExp();
    int getEnemyHealth();
    String getEnemyDescription();
    Rectangle getEnemyBox();
}
