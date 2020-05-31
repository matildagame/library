/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ugr.tstc.matilda.cobertura;

import java.util.Map;

/**
 *
 * @author matilda
 */
public class Event {

    private Map<String, CharacterDescription> playersList;
    private String room;
    private String playerID;
    private float[] coordinate;
    private boolean running;

    String show() {
       return " > "+eventType;
    }

    enum TYPE {
        message, event
    };
    TYPE eventType;

    enum EVENTS {
        evCambiarLista, evIniciarPartida, evFinPartida, evActualizarRutas
    };
    EVENTS eventSubtype;

    LaberintoMessage message = null;

    Event(LaberintoMessage message) {
        eventType = TYPE.message;
        this.message = message;
    }

   Event(EVENTS eventSubtype,String playerID,float [] coordinateOrigin,boolean running){
      eventType=TYPE.event;
       this.eventSubtype=eventSubtype;
       this.playerID=playerID;
       this.coordinate=coordinateOrigin;
       this.running=running;
    }
    
    Event(EVENTS eventSubtype, Map<String, CharacterDescription> playersList) {
        eventType=TYPE.event;
        this.eventSubtype=eventSubtype;
        this.playersList=playersList;
    }
    
    Event(EVENTS events, String room) {
        eventType=TYPE.event;
        this.eventSubtype=EVENTS.evIniciarPartida;
        this.room=room;
    }


    boolean isMessage() {
        return eventType == TYPE.message;
    }

    boolean isEvent() {
        return eventType == TYPE.event;
    }
    
    LaberintoMessage getMessage(){
        return message;
    }

    EVENTS getSubtype(){
        return eventSubtype;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public float[] getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(float[] coordinate) {
        this.coordinate = coordinate;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
