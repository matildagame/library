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

    String show() {
       return " > "+eventType;
    }

    enum TYPE {
        message, event
    };
    TYPE eventType;

    enum EVENTS {
        evCambiarLista, evIniciarPartida, evFinPartida
    };
    EVENTS eventSubtype;

    LaberintoMessage message = null;

    Event(LaberintoMessage message) {
        eventType = TYPE.message;
        this.message = message;
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
}
