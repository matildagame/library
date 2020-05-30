/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ugr.tstc.matilda.matildalib;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matilda
 */
public class MatildaLibClientSharedLauncher {

    MatildaLibClientShared matildaClient;
    boolean salir=false;
    
    public MatildaLibClientSharedLauncher(int libPort) {
        
        
        try {
            ServerSocket serverSocket = new ServerSocket(libPort);
            
            do{
                Socket socket = serverSocket.accept();
            
            matildaClient=new MatildaLibClientShared(socket);
            // We use the default managers, but they could be the ones from the students.
            matildaClient.setLaberintoManager(new LaberintoGameManager());
             
            Thread hebra = new Thread(matildaClient); 
            hebra.start(); 
            
            } while(!salir);
            
        } catch (IOException ex) {
            Logger.getLogger(MatildaLibClientSharedLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
     public static void main(String[] args){
        int libPort=9998;
        
        if(args.length>=1){
            libPort=Integer.parseInt(args[0]);
        }
        // Let's create the server
        new MatildaLibClientSharedLauncher(libPort);
    }
}
