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
public class MatildaLibClientShared extends MatildaLibClient implements Runnable{

    public MatildaLibClientShared(Socket socket) {
        super(socket);
        
        // We have a connection to the MatildaLib.gd already:
        estado=Estados.inicializado;
    }


    @Override
    public void run() {
        this.crearProcesador(socket);
    }
    
}
