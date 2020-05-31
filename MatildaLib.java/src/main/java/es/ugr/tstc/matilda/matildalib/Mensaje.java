/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ugr.tstc.matilda.matildalib;

import es.ugr.tstc.matilda.cobertura.CharacterDescription;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matilda
 */
public class Mensaje {

    static final String SP = " ";
    static final String DEL = ":";
    static final String DEL2 = ";";
    static final String DEL3 = "#";
    static final String ENDOL = "\n";

    static final String RegisterString = "REGISTER";
    static final String RegisterReplyString = "REGISTER_REPLY";
    static final String PlayersListString = "PLAYER_LIST";
    static final String StartMatchString = "START_MATCH";
    static final String UpdateRoute = "UPDATE_ROUTE";

    private String username;
    private String room;
    private String mesh;
    private String body_texture;
    private String hair_texture;
    private String game_server_address;
    private int game_server_port;
    private String playerID;
    private List<CharacterDescription> playersList;
    private Map<String, float[]> spawnPlayerList;
    private boolean running;
    private Coordenada coordinate;

    private ERROR parseUpdateRoute(String[] campos) {
        ERROR error = ERROR.noError;

        String[] camp = campos[1].split(DEL);

        playerID = camp[0];
        coordinate = parseCoordinate(camp[1]);
        running = (camp[2].compareTo("1") == 0);

        tipo = MessageType.UPDATE_ROUTE;

        return error;
    }

    Coordenada parseCoordinate(String coordenada_) {
        Coordenada coordenada = new Coordenada();

        String[] co = coordenada_.split(DEL2);

        coordenada.x = Float.parseFloat(co[0]);
        coordenada.y = Float.parseFloat(co[1]);
        coordenada.z = Float.parseFloat(co[2]);

        return coordenada;
    }

    enum CODE {
        OK, ERR
    };
    CODE code;

    enum ERROR {
        noError
    };

    enum MessageType {
        invalidMessage, PLAYERS_LIST_UPDATE, START_MATCH, REGISTER_REQUEST, REGISTER_REPLY, UPDATE_ROUTE
    }

    // Campos del mensaje:
    MessageType tipo = MessageType.invalidMessage;

    private String contrasenia = "+";
    private String direccionServidorRegistro = "locahost";
    private int puertoServidorRegistro = 9999;

    String servidor = "localhost";
    int puerto = 9090;

    Mensaje() {

    }

    Mensaje(BufferedReader in) {
        String mensajeBruto = leerMensaje(in);

        if (interpretaMensaje(mensajeBruto) != ERROR.noError) {
            tipo = MessageType.invalidMessage;
        }
    }

    private ERROR interpretaMensaje(String mensajeBruto) {
        ERROR error = ERROR.noError;

        String[] campos = mensajeBruto.split(SP);

        // Si es la inicialización:
        if (campos[0].compareTo(RegisterString) == 0) {
            buildRegisterRequestMessage(campos);
        } else if (campos[0].compareTo(UpdateRoute) == 0) {
            parseUpdateRoute(campos);
        }

        return error;
    }

    ERROR buildUpdateRoute(String playerID, float[] coordinateOrigin, boolean running) {
        ERROR error = ERROR.noError;

        this.playerID = playerID;
        this.coordinate = new Coordenada(coordinateOrigin);
        this.running = running;

        tipo = MessageType.UPDATE_ROUTE;

        return error;
    }

    ERROR buildRegisterRequestMessage(String[] campos_) {
        ERROR error = ERROR.noError;
        tipo = MessageType.REGISTER_REQUEST;

        String[] campos = campos_[1].split(DEL);

        username = campos[0];
        room = campos[1];
        mesh = campos[2];
        body_texture = campos[3];
        hair_texture = campos[4];
        game_server_address = campos[5];
        game_server_port = Integer.parseInt(campos[6]);

        return error;
    }

    ERROR buildRegisterReplyMessage(String playerID) {
        ERROR error = ERROR.noError;
        tipo = MessageType.REGISTER_REPLY;

        this.playerID = playerID;

        return error;
    }

    ERROR buildRegisterReply(String playerID) {
        ERROR error = ERROR.noError;

        this.playerID = playerID;
        this.tipo = MessageType.REGISTER_REPLY;
        this.code = CODE.OK;

        return error;
    }

    ERROR buildPlayersListMessage(List<CharacterDescription> playersList) {
        ERROR error = ERROR.noError;

        tipo = MessageType.PLAYERS_LIST_UPDATE;
        this.playersList = playersList;

        return error;
    }

    ERROR buildStartMatchMessage(Map<String, float[]> spawnPlayersList) {
        ERROR error = ERROR.noError;

        tipo = MessageType.START_MATCH;
        this.spawnPlayerList = spawnPlayersList;
        return error;
    }

    String serialize() {
        String linea = "";

        switch (getType()) {
            case REGISTER_REPLY:
                linea = RegisterReplyString + SP + "OK" + SP + playerID + ENDOL;
                break;
            case PLAYERS_LIST_UPDATE:
                linea = serializePlayersListUpdate(playersList);
                break;
            case START_MATCH:
                linea = serializeSpawnPlayersList(spawnPlayerList);
                break;
            case UPDATE_ROUTE:
                linea = serializeUpdateRoute(playerID, coordinate, running);
                break;
        }

        return linea;
    }

    private String serializeSpawnPlayersList(Map<String, float[]> spawnPlayerList) {
        String linea = "";

        Set<String> playersID = spawnPlayerList.keySet();

        int i = 0;
        for (String playerID : playersID) {
            float[] coordenada = spawnPlayerList.get(playerID);
            linea = linea + playerID + DEL2 + coordenada[0] + DEL2 + coordenada[1] + DEL2 + coordenada[2];
            i++;
            if (i < playersID.size()) {
                linea = linea + DEL;
            }
        }

        linea = StartMatchString + SP + linea + ENDOL; //serializePositionDescription(spawnPlayerList);

        return linea;
    }

    private String serializePositionDescription(float[] get) {
        return get[0]+DEL2+get[1]+DEL2+get[2];
    }

    private String serializePlayersListUpdate(List<CharacterDescription> playersList) {
        String linea = "";

        linea = PlayersListString + SP + serializeCharacterDescription(playersList.get(0));

        for (int i = 1; i < playersList.size(); i++) {
            linea = linea + DEL + serializeCharacterDescription(playersList.get(i));
        }

        linea = linea + ENDOL;
        return linea;
    }

    private String serializeCharacterDescription(CharacterDescription character) {
        String linea = "";

        linea = character.getPlayerID() + DEL2 + character.getName() + DEL2 + character.getCharacterMesh()
                + DEL2 + character.getMainTexture() + DEL2 + character.getHairTexure();

        return linea;
    }

    private String serializeUpdateRoute(String playerID, Coordenada coordinate, boolean running) {
        String linea = "";

        linea = UpdateRoute + SP + playerID + DEL + serializePositionDescription(coordinate.getArray()) + DEL + ((running) ? "1" : "0") + ENDOL;

        return linea;
    }

    private String leerMensaje(BufferedReader in) {
        String mensaje = null;

        try {
            // suponemos mensajes orientados a líneas de texto:
            mensaje = in.readLine();
            //System.out.println("> Raw: \""+mensaje+"\"");

        } catch (IOException ex) {
            Logger.getLogger(Mensaje.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mensaje;
    }

    MessageType getType() {
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

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public boolean getRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Coordenada getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordenada coordinate) {
        this.coordinate = coordinate;
    }

}
