
package com.company;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author jjramos
 */
public class Cliente {


    /**
     * Método principal de la clase:
     * @param argumentos
     */
    public static void main(String []argumentos){

        //int puerto=8767;
        int puerto=9999;
        String direccionServidor="127.0.0.1";

        // Por si quisiéramos pasarle argumentos por la línea de comandos:
        if(argumentos.length==2){
            direccionServidor=argumentos[0];
            puerto=Integer.parseInt(argumentos[1]);
        }
        // Creamos un cliente:
        new Cliente(direccionServidor,puerto);
    }

    /**
     * Constructor del cliente.
     * @param direccionServidor Dirección o nombre del servidor.
     * @param puerto  Puerto donde escucha el servidor.
     */
    private Cliente(String direccionServidor, int puerto) {
        Socket socketConexion;

        try {
            // Abrimos la conexión.
            // Socket:This class implements client sockets (also called just "sockets"). A socket is an endpoint for communication between two machines.
            socketConexion=new Socket(direccionServidor, puerto);

            // Obtenemos los canales de entrada y salida:
            PrintWriter out = new PrintWriter(socketConexion.getOutputStream(),true);

            // Para leer lineas terminadas en \n
            BufferedReader in = new BufferedReader(new InputStreamReader(socketConexion.getInputStream()));

            // Para leer de la línea de comandos:
            BufferedReader inConsola = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.print("Introduce un mensaje para el servidor (fin para salir): ");
                String mensaje = inConsola.readLine();
                mensaje = mensaje.toLowerCase();

                // Envía el mensaje:
                out.println(mensaje);

                // Para leer linea de texto
                String res = in.readLine();
                System.out.println("Server Echo: "+res);


                if (res.equals("error!")){
                    // Operacion en el cliente
                    System.out.println("Castigado 1 segundos por equivocarte...");
                    TimeUnit.SECONDS.sleep(1);
                }

                if (mensaje.equals("fin")) {
                    System.out.println("¡Hasta la proxima!");
                    break;
                }
            }

            socketConexion.close();
            inConsola.close();
            in.close();
            out.close();

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}