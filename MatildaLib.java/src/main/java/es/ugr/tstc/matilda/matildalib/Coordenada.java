/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ugr.tstc.matilda.matildalib;

/**
 *
 * @author Juanjo Ramos
 */
public class Coordenada {
    float x,y,z;

    public Coordenada(){
        
    }
    
    public Coordenada(float[] coordinate) {
        x=coordinate[0];
        y=coordinate[1];
        z=coordinate[2];   
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

   public float[] getArray() {
        float []coord={x,y,z};          
        return coord;
    }
    
}
