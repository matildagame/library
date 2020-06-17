/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ugr.tstc.matilda.cobertura;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matilda
 */
public class LaberintoApp {

    // Hay que poner otra manera de decir que comience el juego... ¿Otro mensaje?
    // De momento, arranca cuando se llega a 3 jugadores esperando en la partida:
    int playersToStart=1;
    //////////////////////////////////
    /////////////////////////////////
    
    String room;
    Map<String,String> userDatabase;
    Map<String,CharacterDescription> playersList;
    Map<String,float[]> playersSpawnPositionList;
    
    List<float[]> availableSpawnPointsList;
    
    LaberintoProtocolServer coberturaProtocolServer;
    private LaberintoAppGUI gui;
    
    void init(){
        playersList=new HashMap<String,CharacterDescription>();
        playersSpawnPositionList=new HashMap<String,float[]>();
        userDatabase=new HashMap<String,String>();
        
        initUserDatabase();
        initPlayersSpawnPositionList();
        initAvailablePlayersSpawnPosition();
        
//        LaberintoApp app = this;
//
//            /* Create and display the form */
//            java.awt.EventQueue.invokeLater(new Runnable() {
//                public void run() {
//                    gui=new LaberintoAppGUI(app);
//                    new LaberintoAppGUI().setVisible(true);
//                }
//            });

    }
    
    boolean existeUsuario(String username) {
        return (userDatabase.get(username)!=null);
    }

    boolean existeHabitacion(String room) {
        return (this.room.compareTo(room)==0);
    }

    String generatePlayerID(String username, String room) {
        return username+";"+room+";"+(Math.random()*50000);
    }

    String generatePlayerID(String username, CharacterDescription characterDescription, String room) {
        return "Player"+(Math.round(Math.random()*50000));
    }

    int addPlayer(String playerID, CharacterDescription characterDescription) {
        int error = 0;

        // Si no existe ya ese jugador:
        if (playersList.get(playerID) == null) {
            // Se añade a la lista.
            playersList.put(playerID, characterDescription);
            
             // Además, notifica la llegada de un nuevo jugador:
             notifyPlayerListChange();
             notifyPlayerListChangeGUI();
             
            ////////////////////////////////////
            //fix : comienza cuando hay n jugadores:
            //////////////////////////////////
            if (playersList.size()>=playersToStart){
                startMatch();
            }
            ////////////////////////////////////
            ////////////////////////////////////
        } else {
            error = 1;
        }



        return error;
    }

    void removePlayer(String username, String room) {
        String playerID=userDatabase.get(username);
        playersList.remove(playerID);
        userDatabase.remove(username);
        
        notifyPlayerListChange();
        
    }

    List<CharacterDescription> getPlayerList() {
        List<CharacterDescription> list=new ArrayList<CharacterDescription>();
        
        for(CharacterDescription c:playersList.values()){
            list.add(c);
        }
        
        return list;
    }

    Map<String, float[]> getSpawnPlayerList() {
        return playersSpawnPositionList;
    }

    String getRoom() {
        return room;
    }

    private Map<String, String> initUserDatabase() {
        return userDatabase;
    }

    private Map<String, float[]> initPlayersSpawnPositionList() {
        return playersSpawnPositionList;
    }

    private void notifyPlayerListChange() {
        coberturaProtocolServer.notifyPlayerList(playersList);
    }

    void setRoom(String room) {
        this.room=room;
    }

    void setProtocolEngine(LaberintoProtocolServer coberturaProtocolServer) {
        this.coberturaProtocolServer=coberturaProtocolServer;
    }

    public void startMatch() {
        playersSpawnPositionList.clear();
        
        // let's assign the available positions:
        int i=0;
        
        for(String player:playersList.keySet()){
            float[] position=availableSpawnPointsList.get(i);
            playersSpawnPositionList.put(player, position);
            i++;
        }
        
        coberturaProtocolServer.evIniciarPartida(room);
    }

    private void initAvailablePlayersSpawnPosition() {
        float[][] testPositions={{-1.578f,0.0f,-18.31f},{-2.0f,0.0f,-18.31f},{0f,0f,-18f},{-3,0,-18},{0,0,-16},{0,0,-17}};
            
        availableSpawnPointsList=new ArrayList<float[]>();
        
        for(int i=0;i<testPositions.length;i++){
            float[] vector = testPositions[i];
            availableSpawnPointsList.add(vector);
        }
    }

    void delPlayer(String playerID) {
       if (playersList.get(playerID) == null) {
            // Se añade a la lista.
            playersList.remove(playerID);
            
             // Además, notifica la llegada de un nuevo jugador:
             notifyPlayerListChange();
       }
    }

    private void notifyPlayerListChangeGUI() {
       // gui.notifyPlayerListChange(playersList);
    }
}
