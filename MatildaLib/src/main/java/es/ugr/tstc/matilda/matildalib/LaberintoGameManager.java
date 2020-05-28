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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matilda
 */
public class LaberintoGameManager {

   
    
            enum ESTADOS {inicial, esperandoRegisterRequest};
            
            
    private MatildaLibClient matildaLib;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

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
        

        ESTADOS estado=ESTADOS.inicial;
        
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
                 
                 LaberintoMessage mensaje=new LaberintoMessage(linea);
             
                 switch(estado){
                     case inicial:
                         
                         break;
                     case esperandoRegisterRequest:
                         
                         switch(mensaje.getType()){
                             case mJoinResponse:
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
        
        // Enviamos el mensaje, y esperamos la respuesta:
          LaberintoMessage mensaje=new LaberintoMessage();
            
            mensaje.buildMJoinRequest(username, 
                    new CharacterDescription(username, mesh, bodyTexture,hairTexture,username),room);
            out.print(mensaje.serialize());
            out.flush();

            
            return error;
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

    
}
