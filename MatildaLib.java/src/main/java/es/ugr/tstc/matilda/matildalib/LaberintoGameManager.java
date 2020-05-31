/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ugr.tstc.matilda.matildalib;

import es.ugr.tstc.matilda.cobertura.CharacterDescription;
import es.ugr.tstc.matilda.cobertura.LaberintoMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matilda
 */
public class LaberintoGameManager {

 

   
            enum ESTADOS {inicial, esperandoRegisterRequest, registrado};
            
    private MatildaLibClient matildaLib;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    ESTADOS estado=ESTADOS.inicial;
    
    public LaberintoGameManager() {
        
    }

    void setMatildaLib(MatildaLibClient matildaLib) {
        this.matildaLib=matildaLib;
    }

    int startSession(String gameServerAddress, int gameServerPort) {
              int error=0;
        try {
      
            
            socket = new Socket(gameServerAddress,gameServerPort);
            
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
     
            ReaderThread hebraLectora=new ReaderThread(in, this);
            hebraLectora.start();
            
        } catch (IOException ex) {
            Logger.getLogger(LaberintoGameManager.class.getName()).log(Level.SEVERE, null, ex);
            error=1;
        }
                    return error;
    }

    class ReaderThread extends Thread{

        private BufferedReader in;
        boolean salir=false;
        private LaberintoGameManager manager;
        
        public ReaderThread(BufferedReader in, LaberintoGameManager manager) {
            this.in=in;
            this.manager=manager;
        }

        
        @Override
        public void run() {
         String linea="";
         
         try {
            do{
             
                 linea=in.readLine();
                 System.out.println("GameManager: recibido "+linea);
                 
                 LaberintoMessage mensaje=new LaberintoMessage(linea);
             
                 switch(estado){
                     case inicial:
                         
                         break;
                     case registrado:
                         switch(mensaje.getType()){
                             case mPlayerList:
                                 manager.evPlayerList(mensaje.getPlayersList());
                                 
                                 break;
                             case mStartMatch:
                                 manager.evStartMatch(mensaje.getSpawnPlayersList());
                                 break;
                             case mUpdateRoute:
                                 manager.evUpdateRoute(mensaje.getPlayerID(),mensaje.getCoordinateOrigin(),mensaje.getRunning());
                                 break;
                         }
                         break;
                     case esperandoRegisterRequest:
                         
                         switch(mensaje.getType()){
                             case mJoinResponse:
                                 estado=ESTADOS.registrado;
                                 manager.evJoinResponse(mensaje.getCode(), mensaje.getPlayerID());
                                 break;
                         }
                         
                         break;
                                 default:
                                 break;
                 }
                            
            } while(!salir);

 
             } catch (IOException ex) {
                 Logger.getLogger(LaberintoGameManager.class.getName()).log(Level.SEVERE, null, ex);
             }
            
            
        }
        
    }
    
    int register(String username, String room, String mesh, String bodyTexture, String hairTexture) {
        int error=0;
        
        // Enviamos el mensaje
          LaberintoMessage mensaje=new LaberintoMessage();
            
            mensaje.buildMJoinRequest(username, 
                    new CharacterDescription(username, mesh, bodyTexture,hairTexture,username),room);
            out.print(mensaje.serialize());
            out.flush();

            estado=ESTADOS.esperandoRegisterRequest;
            
            return error;
    }
    
     int updatePlayerRoute(String playerID, Coordenada coordinate, boolean running) {
         int error=0;
         
           LaberintoMessage mensaje=new LaberintoMessage();
           
           mensaje.buildMUpdateRoute(playerID,coordinate,running);
          out.print(mensaje.serialize());
            out.flush();

         return error;
     }

     
   
    
     private void evPlayerList(List<CharacterDescription> playersList) {
         matildaLib.evPlayerList(playersList);
     }
    
     private void evJoinResponse(LaberintoMessage.CODES code, String playerID) {
         switch(code){
             case OKCode:
                 matildaLib.evJoinResponseOk(playerID);
                 break;
             case ErrorCode:
                 matildaLib.evJoinResponseErr();
                 break;
         }
     }

         private void evStartMatch(Map<String, float[]> spawnPlayersList) {
        matildaLib.evStartMatch(spawnPlayersList);
    }
         
            private void evUpdateRoute(String playerID, float[] coordinateOrigin, boolean running) {
     matildaLib.evUpdateRoute(playerID,coordinateOrigin,running);
            }
    
}
