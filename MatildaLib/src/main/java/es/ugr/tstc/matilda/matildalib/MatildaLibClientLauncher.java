/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ugr.tstc.matilda.matildalib;

import java.util.Random;

/**
 *
 * @author matilda
 */
public class MatildaLibClientLauncher {

    MatildaLibClient matildaClient;
    
    public MatildaLibClientLauncher(int libPort) {
        matildaClient=new MatildaLibClient(libPort);
        // We use the default managers, but they could be the ones from the students.
        matildaClient.setLaberintoManager(new LaberintoGameManager());
        
        if(matildaClient.init()!=0){
            System.err.println("Error al incializar MatildaLib");
        }
    }
    
    
     public static void main(String[] args){
        int libPort=9998;
        
        if(args.length>=1){
            libPort=Integer.parseInt(args[0]);
        }
        // Let's create the server
        new MatildaLibClientLauncher(libPort);
    }
}
