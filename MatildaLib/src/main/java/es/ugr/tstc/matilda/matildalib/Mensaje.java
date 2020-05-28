/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ugr.tstc.matilda.matildalib;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matilda
 */
public class Mensaje {

static final String SP=" ";
static final String DEL=":";
static final String DEL2=";";
static final String DEL3="#";
static final String ENDOL="\n";

static final String RegisterString="REGISTER";
static final String RegisterReplyString="REGISTER_REPLY";

    private String username;
    private String room;
    private String mesh;
    private String body_texture;
    private String hair_texture;
    private String game_server_address;
    private int game_server_port;
    private String playerID;

    void buildRegisterReply(String playerID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    enum ERROR {noError};
    enum MessageType {invalidMessage,PLAYERS_LIST_UPDATE,START_MATCH,REGISTER_REQUEST, REGISTER_REPLY
    }
    
    // Campos del mensaje:
    MessageType tipo=MessageType.invalidMessage;
 

    private String contrasenia="+";
    private String direccionServidorRegistro="locahost";
    private int puertoServidorRegistro=9999;
    
    String servidor="localhost";
    int puerto=9090;
    
    Mensaje(){
        
    }

    Mensaje(BufferedReader in) {
        String mensajeBruto=leerMensaje(in);
        
        if (interpretaMensaje(mensajeBruto)!=ERROR.noError){
            tipo=MessageType.invalidMessage;
        }
    }
    
    private ERROR interpretaMensaje(String mensajeBruto) {
        ERROR error=ERROR.noError;
        
        String[] campos = mensajeBruto.split(SP);
        
        // Si es la inicialización:
        if(campos[0].compareTo(RegisterString)==0){
            buildRegisterRequestMessage(campos);
        } 
        
        return error;
    }
    
    ERROR buildRegisterRequestMessage(String[] campos_){
        ERROR error=ERROR.noError;
        tipo=MessageType.REGISTER_REQUEST;
        
    String[] campos = campos_[1].split(DEL);
        
            username=campos[0];
            room=campos[1];
            mesh=campos[2];
            body_texture=campos[3];
            hair_texture=campos[4];
            game_server_address=campos[5];
            game_server_port=Integer.parseInt(campos[6]);
            
            return error;
    }

    ERROR buildRegisterReplyMessage(String playerID){
        ERROR error=ERROR.noError;
        tipo=MessageType.REGISTER_REPLY;
            
          this.playerID=playerID;
            
            return error;
    }
    
    String serialize(){
        String linea="";
        
        switch(getType()){
            case REGISTER_REPLY:
                linea=RegisterReplyString+SP+"OK"+SP+playerID+ENDOL;
                break;
        }
        
        return linea;
    }
    
    private String leerMensaje(BufferedReader in) {
        String mensaje=null;
        
        try {
            // suponemos mensajes orientados a líneas de texto:
            mensaje=in.readLine();
            //System.out.println("> Raw: \""+mensaje+"\"");
            
        } catch (IOException ex) {
            Logger.getLogger(Mensaje.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return mensaje;
    }
    
    
    
    MessageType getType(){
        return tipo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getMesh() {
        return mesh;
    }

    public void setMesh(String mesh) {
        this.mesh = mesh;
    }

    public String getBodyTexture() {
        return body_texture;
    }

    public void setBody_texture(String body_texture) {
        this.body_texture = body_texture;
    }

    public String getHairTexture() {
        return hair_texture;
    }

    public void setHair_texture(String hair_texture) {
        this.hair_texture = hair_texture;
    }

    public String getGameServerAddress() {
        return game_server_address;
    }

    public void setGame_server_address(String game_server_address) {
        this.game_server_address = game_server_address;
    }

    public int getGameServerPort() {
        return game_server_port;
    }

    public void setGame_server_port(int game_server_port) {
        this.game_server_port = game_server_port;
    }

    public MessageType getTipo() {
        return tipo;
    }

    public void setTipo(MessageType tipo) {
        this.tipo = tipo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getDireccionServidorRegistro() {
        return direccionServidorRegistro;
    }

    public void setDireccionServidorRegistro(String direccionServidorRegistro) {
        this.direccionServidorRegistro = direccionServidorRegistro;
    }

    public int getPuertoServidorRegistro() {
        return puertoServidorRegistro;
    }

    public void setPuertoServidorRegistro(int puertoServidorRegistro) {
        this.puertoServidorRegistro = puertoServidorRegistro;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }
   
}
