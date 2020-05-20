/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game.gui.Dialog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author GeoSonicDash
 */
public class LoadDialogs {
    private static int  CHAR_LIMIT;
    private ArrayList<String> speeches;
    private ArrayList<Dialog> dialogs;
    private String refName;
    private String chunk;
    public LoadDialogs(String refName) {
        this.chunk = "";
        this.refName = refName;
        this.speeches = new ArrayList<String>();
        this.dialogs = new ArrayList<Dialog>();
        CHAR_LIMIT = 80;
    }
    public ArrayList<Dialog> getDialogChain() {       
        getDialogChunk();
        spiltChunk();
        for(String temp : speeches) {
            createDialogChain(temp);
        }
        return dialogs;
    }
    
    private void getDialogChunk() {
        File file = new File("src/game/resources/dialogs/"+refName+".txt");
        try {
            if(!file.exists()) {
                file.createNewFile();
            }  
            BufferedReader br = new BufferedReader(new FileReader(file));
            String currentLine = br.readLine();
            while(currentLine != null) {
                chunk+=currentLine;
                currentLine = br.readLine();
            }
            br.close();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void spiltChunk() {
        int colonIndex = chunk.indexOf("::");
        int breakIndex = chunk.indexOf("BREAK");
        while(colonIndex != -1) {
            String speech = chunk.substring(0, breakIndex);            
            chunk = chunk.substring(breakIndex+5);
            colonIndex = chunk.indexOf("::");
            breakIndex = chunk.indexOf("BREAK");
            speeches.add(speech);
        }
    }
     
    private void createDialogChain(String speech) {
        String speakerName = speech.substring(0,speech.indexOf("::"));
        String removedName = speech.substring(speech.indexOf("::")+2);
        int fullLine = removedName.length() / CHAR_LIMIT;
        for(int i = 0; i < fullLine; i++) {
            String dialog = removedName.substring(80*i, 80*(i+1)+1);
            dialogs.add(new Dialog(speakerName, dialog));
        } 
        String tailEnd = removedName.substring((80*fullLine));
        dialogs.add(new Dialog(speakerName, tailEnd));
    }
}
