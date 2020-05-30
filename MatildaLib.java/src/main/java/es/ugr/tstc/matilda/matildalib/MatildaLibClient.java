/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ugr.tstc.matilda.matildalib;

import es.ugr.tstc.matilda.cobertura.CharacterDescription;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matilda
 */
public class MatildaLibClient {
    
    int libPort=9998;

           BufferedReader in=null;
        PrintWriter out=null;
        LaberintoGameManager laberintoGameManager=null;
   Socket socket;
        

    void setLaberintoManager(LaberintoGameManager laberintoGameManager) {
        this.laberintoGameManager=laberintoGameManager;
        laberintoGameManager.setMatildaLib(this);
    }

    private void error() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void evJoinResponseOk(String playerID) {
        Mensaje mensaje=new Mensaje();
        mensaje.buildRegisterReplyMessage(playerID);
        enviarMensaje(mensaje.serialize());
    }

    public void evJoinResponseErr() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void evPlayerList(List<CharacterDescription> playersList) {
        Mensaje mensaje=new Mensaje();
        mensaje.buildPlayersListMessage(playersList);
        enviarMensaje(mensaje.serialize());
    }

    void evStartMatch(Map<String, float[]> spawnPlayersList) {
        Mensaje mensaje=new Mensaje();
        mensaje.buildStartMatchMessage(spawnPlayersList);
        enviarMensaje(mensaje.serialize());
    }
    
    enum Estados {inicial, inicializado,registrado, esperandoRespuestaRegistro};
    
    Estados estado=Estados.inicial;
    
    // to be assigned by the library user:
//    ConnectionManager connectionManager=null;
//    GameObjectManager gameObjectManager=null;
//    ChatManager chatManager=null;
//    RegistrationManager registrationManager=null;
//    
//    public void setConnectionManager(ConnectionManager cm){
//        connectionManager=cm;
//    }
//
//    public GameObjectManager getGameObjectManager() {
//        return gameObjectManager;
//    }
//
//    public void setGameObjectManager(GameObjectManager gameObjectManager) {
//        this.gameObjectManager = gameObjectManager;
//    }
//
//    public ChatManager getChatManager() {
//        return chatManager;
//    }
//
//    public void setChatManager(ChatManager chatManager) {
//        this.chatManager = chatManager;
//    }
//    
    
    
    public MatildaLibClient(Socket socket) {
      this.socket=socket;
    }
    

    public MatildaLibClient(int libPort) {
      this.libPort=libPort;
    }
    
    public int init(){
        int error=0;
        boolean salir=false;
          ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(libPort);
        
            do{
                
                estado=Estados.inicial;
            Socket socket = serverSocket.accept();
            System.out.println("New MatildaLib connection...");
                    estado=Estados.inicializado;
                    
            crearProcesador(socket);
            } while(!salir);
           
            
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(MatildaLibClient.class.getName()).log(Level.SEVERE, null, ex);
            try {
                socket.close();
            } catch (IOException ex1) {
                Logger.getLogger(MatildaLibClient.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        
        return error;
    }

    /**
     * It processes the Matlib.gd Peer.
     * @param socket 
     */
    void crearProcesador(Socket socket) {

        boolean salir=false;
        int error=0;
                    
        try {

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream(),true);
            
            
            
            // Let's read messages form Matilda.gd:
            do {
                Mensaje mensaje=new Mensaje(in);
                
                System.out.println("> Recibido mensaje Tipo: "+mensaje.getType());
                
                switch(estado){
                    case inicial:
                       
                        break;
                    case inicializado:
        
                        switch(mensaje.getType()){
                            case REGISTER_REQUEST:
                                if(laberintoGameManager.startSession(mensaje.getGameServerAddress(), mensaje.getGameServerPort())==0){
                                    if(laberintoGameManager.register(mensaje.getUsername(), mensaje.getRoom(),mensaje.getMesh(),mensaje.getBodyTexture(),mensaje.getHairTexture())==0){
                                      estado=Estados.esperandoRespuestaRegistro;
                                    }
                                } else {
                                    error();
                                }
                                
                                break;
                            default:
                                error();
                                break;
               
                        }
//                          if (mensaje.getType()==Mensaje.MessageType.libraryChatMessage){
//                            System.out.println("Chat > "+mensaje.getMessage());
//                    
//                         enviarMensaje(mensaje.getMessage());
//                }
                     
                        break;
                }
                
            } while(!salir);
            
        } catch (IOException ex) {
            Logger.getLogger(MatildaLibClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(MatildaLibClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void enviarMensaje(String message) {
        out.print(message);
        out.flush();
    }
 
    void onAndarHacia(String playerID, Coordenada origen, Coordenada destino, Coordenada []ruta){
        
    }
    
    void onAtacar(String playerID, String objectID){
        
    }
    
    void onRecibirDano(String playerID,int dano){
        
    }
    
    void onLLegaDestino(String playerID,Coordenada coordenada){
        
    }
}
