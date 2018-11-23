/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author fede_
 */
public class IteradorOrdenes {
    private Orden[] ordenes;
    private int posicion;

    public IteradorOrdenes(Orden[] ordenes) {
        this.ordenes = ordenes;
        this.posicion = 0;
    }
    
    public Orden index(int pos){
        return ordenes[pos];
    }
    
    public Boolean hasNext(){
        if(this.posicion<ordenes.length){
            return true;
        }else
            return false;
    }
    
    public Orden next(){
        Orden orden= ordenes[this.posicion];
        this.posicion++;
        return orden;
    }
    
}
