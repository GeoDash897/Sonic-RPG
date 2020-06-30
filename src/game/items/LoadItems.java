/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.items;

import game.items.DefaultItem.ItemType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**Loads all the variables for {@code DefaultItems}.
 *
 * @author GeoSonicDash
 */
public class LoadItems {
    
    public static DefaultItem loadItem(String itemString) {    
        ArrayList<String> stats = getItemStats(itemString);
        String itemType = stats.get(0).substring(0, stats.get(0).indexOf(":"));
        if(itemType.equals("DEFAULT")) {
            return new DefaultItem(stats, ItemType.ITEMTYPE_DEFAULT);           
        }
        else if(itemType.equals("SELLABLE")) {
            return new SellableItem(stats, ItemType.ITEMTYPE_SELLABLE);
        }
        return null;
    }
    
    public static ArrayList<String> getItemStats(String itemString) {
        File itemFile = new File("src/game/resources/gameInfo/ITEMS.txt");
        boolean foundItem = false;
        boolean foundDescription = false;
        boolean endLoop = false;
        String statString = "";       
        try {
            String description = "";            
            BufferedReader br = new BufferedReader(new FileReader(itemFile));
            String currentLine = br.readLine();
            while(currentLine != null && !endLoop) {
                if(foundItem && !foundDescription && currentLine.equals(itemString+" DESCRIPTION")) {
                    foundDescription = true;
                }
                else if(foundItem && foundDescription) {
                    if(!currentLine.equals(itemString+" DESCRIPTION-END")) {
                        description+=currentLine;
                    }
                    else {
                        endLoop = true;
                    }
                }
                if(foundItem && !foundDescription) {
                    statString+=" "+currentLine;                   
                }
                else if(currentLine.contains(":"+itemString)) {
                    foundItem = true;
                    statString+=currentLine;
                }
                currentLine = br.readLine();
            }
            ArrayList<String> stats = new ArrayList<String>(Arrays.asList(statString.split(" ")));
            stats.add(description);//Using this method, the item's main description will always be last in the arrayList           
            return stats;
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }       
        return null;
    }
    
}
