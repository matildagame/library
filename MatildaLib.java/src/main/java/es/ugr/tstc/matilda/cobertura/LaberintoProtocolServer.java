package es.ugr.tstc.matilda.cobertura;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jjramos@ugr.es
 */
public class LaberintoProtocolServer {
    

    private LaberintoApp application;
    private List<LaberintoProtocolServerProcessor> processorList;

 
           public static void main(String[] args){
               
               int puerto=9090;
               // Application LEvel part of the server:
               LaberintoApp application=new LaberintoApp();
               application.init();
               application.setRoom("Room00");
               
               // Main part of the server: it waits for players, and
               // launches a thread for each client.
               new LaberintoProtocolServer(puerto, application);
           }

/**
 * 
 * @param puerto
 * @param application 
 */
    public LaberintoProtocolServer(int puerto, LaberintoApp application) {
       
        this.application=application;
        application.setProtocolEngine(this);
        ServerSocket socketServicio = null;
        
        processorList=new ArrayList<LaberintoProtocolServerProcessor>();
        
        boolean salirServidor = false;

        try {
            

            socketServicio = new ServerSocket(puerto);

                 do {
            Socket socketConexion = socketServicio.accept();

       
                // Por cada cliente, creamos una hebra:
                LaberintoProtocolServerProcessor processor=new LaberintoProtocolServerProcessor(socketConexion,this);
                processorList.add(processor);
                processor.start();
                
            } while (!salirServidor);

        } catch (IOException ex) {
            Logger.getLogger(LaberintoProtocolServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     void notifyPlayerList(Map<String, CharacterDescription> playersList) {
        for(LaberintoProtocolServerProcessor thread:processorList){
            thread.addEvent(new Event(Event.EVENTS.evCambiarLista,playersList));
        }
    }

    boolean existeUsuario(String username) {
        return application.existeUsuario(username);
    }

    boolean existeHabitacion(String room) {
        return application.existeHabitacion(room);
    }

    String generatePlayerID(String username, CharacterDescription characterDescription, String room) {
        return application.generatePlayerID(username, characterDescription, room);
    }

    void addPlayer(String playerID, CharacterDescription characterDescription) {
        application.addPlayer(playerID, characterDescription);
    }

    Map<String, float[]> getSpawnPlayerList() {
     return application.getSpawnPlayerList();
    }

    String getRoom() {
        return application.getRoom();
    }

    void removePlayer(String username, String room) {
     application.removePlayer(username, room);
    }

    List<CharacterDescription> getPlayerList() {
        return application.getPlayerList();
    }

    void evIniciarPartida(String room) {
        for(LaberintoProtocolServerProcessor thread:processorList){
            thread.addEvent(new Event(Event.EVENTS.evIniciarPartida,room));
        }
    }

    void delPlayer(String playerID) {
        application.delPlayer(playerID);
    }

    void updateRoute(String playerID, float[] coordinateOrigin, boolean running) {
       
        for(LaberintoProtocolServerProcessor thread:processorList){
            thread.addEvent(new Event(Event.EVENTS.evActualizarRutas,playerID,coordinateOrigin,running));
        }
 
    }


}
