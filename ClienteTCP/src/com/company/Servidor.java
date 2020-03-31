/*
 * Copyright (C) 2015 jjramos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.company;

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
 * @author jjramos
 */
public class Servidor {

    boolean salir=false;

    /**
     * Método principal.
     * @param argumentos
     */
    public static void main(String []argumentos){
        int puerto=9999;

        // Por si quisiéramos pasarle argumentos por la línea de comandos:
        if(argumentos.length>0){
            puerto=Integer.parseInt(argumentos[0]);
        }

        // Creamos una objeto de esta clase, pasando como argumento el puerto donde debe escuchar:
        new Servidor(puerto);
    }

    /**
     * Constructor de esta clase servidora,
     * @param puerto
     */
    private Servidor(int puerto) {

        ServerSocket socketEscucha;

        try {

            socketEscucha=new ServerSocket(puerto);

            // Mientras que no haya que apagar el servidor:
            System.out.println("Servidor Escuchando...");

            // Esperamos una conexión:
            Socket socketConexion=socketEscucha.accept();

            // Obtenemos los canales de entrada y salida:
            PrintWriter out = new PrintWriter(socketConexion.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socketConexion.getInputStream()));

            while(!salir){



                // Leemos una petición:
                String linea=in.readLine();
                String mensaje=linea.toLowerCase();

                System.out.println("Mensaje desde el cliente: "+linea);

                /* Enviamos la respuesta: */
                out.println(linea);
                out.flush();


            }
            in.close();
            out.close();
            socketConexion.close();
            socketEscucha.close();


        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
