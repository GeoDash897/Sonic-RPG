/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.items;

import java.awt.Image;
/*
    Author: GeoDash897  Date:10/5/19    Updated:10/5/19
*/
public interface DefaultItem {//This sets the default methods for every item
    public int getItemID();
    public String getName(); 
    public String getItemDescription();
    public int getValue();
    public Image getImage();
}
