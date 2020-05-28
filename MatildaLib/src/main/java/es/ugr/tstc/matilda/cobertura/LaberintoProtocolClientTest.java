/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ugr.tstc.matilda.cobertura;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matilda
 */
public class LaberintoProtocolClientTest {
    
    public static void main(String[] args){
        int port=9090;
        String serverAddress="localhost";
        
        new LaberintoProtocolClientTest(serverAddress,port);
    }
    private List<CharacterDescription> playersList;

    LaberintoProtocolClientTest(String serverAddress, int port) {
        
        Random random=new Random();
        String username="Matilda"+random.nextInt();
    
        try {
            String linea="";
            int error=0;
            
            Socket socket = new Socket(serverAddress,port);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            
            
            LaberintoMessage mensaje=new LaberintoMessage();
            
            mensaje.buildMJoinRequest(username, 
                    new CharacterDescription(username, 
                            "/root/Personajes/Matilda/matilda.mesh", 
                            "/root/Personajes/Matilda/materials/matilda1.material", 
                            "/root/Personajes/Matilda/materials/hair1.material", 
                            ""), "Room00");
            out.print(mensaje.serialize());
            out.flush();
            
           linea=in.readLine();
           
           System.out.println("Client: leído > "+linea+" > "+ mensaje.getType());
                     
           mensaje.parsePacket(linea);
       

           
           switch(mensaje.getType()){
               case mJoinResponse:
                   switch(mensaje.getCode()){
                       case ErrorCode:
                           System.err.println("Client: No me deja unirme.");
                           error=1;
                           break;
                       case OKCode:
                           System.out.println("Client: Confirmación con éxito. Asignador identificador: \""+mensaje.getPlayerID()+"\"");
                               break;
                   }
                   break;
               default:
                   System.err.println("Client: Error. Recibido: \""+mensaje.serialize()+"\"");
                   error=1;
                   break;
           }
           
           // Esperemos a comenzar la partida:
           if(error==0){
           boolean empezarPartida=false;
           do {
               System.out.println("Esperando comenzar partida...");
               linea=in.readLine();
               mensaje.parsePacket(linea);
               
               switch(mensaje.getType()){
                   case mPlayerList:
                       System.out.println("Client: Nuevo listado de jugadores: ");
                       
                       playersList=mensaje.getPlayersList();
                       mostrarListaJugadores(playersList);
                       break;
                       
                   case mStartMatch:
                  System.out.println("Client: Comienza la partida!");
                       mostrarSpawnJugadores(playersList,mensaje.getSpawnPlayersList());
                       empezarPartida=true;
                       break;
               }
           
           } while(!empezarPartida);
           }
            
        } catch (IOException ex) {
            Logger.getLogger(LaberintoProtocolClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void mostrarListaJugadores(List<CharacterDescription> playersList) {
        for(CharacterDescription jugador:playersList){
            System.out.println(" > "+jugador.getPlayerID()+"/"+jugador.getName());
        }
    }

    private void mostrarSpawnJugadores(List<CharacterDescription> playersList, Map<String, float[]> spawnPlayersList) {
         for(CharacterDescription jugador:playersList){
            System.out.println(" > "+jugador.getPlayerID()+"/"+jugador.getName()+"/en "+spawnPlayersList.get(jugador.getPlayerID()));
        }
    }
}
