/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ugr.tstc.matilda.matildalib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matilda
 */
public class MatildaLib3Mockup {
 
        public static void main(String[] args){
        int libPort=9998;
        
        new MatildaLib3Mockup(libPort);
        }

    private MatildaLib3Mockup(int libPort) {
        
        
            try {
                ServerSocket serverSocket = new ServerSocket(libPort);
                
                Socket socket = serverSocket.accept();
                
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                
                String linea = in.readLine();
                
                System.out.println(linea);
                
                out.println("REGISTER_REPLY OK Player0");
                out.flush();
                
                
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MatildaLib3Mockup.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } catch (IOException ex) {
                Logger.getLogger(MatildaLib3Mockup.class.getName()).log(Level.SEVERE, null, ex);
            }
            
    }
}
