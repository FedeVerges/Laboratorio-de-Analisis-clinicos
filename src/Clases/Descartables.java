/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.HashMap;

/**
 *
 * @author fede_
 */
public class Descartables {

    private HashMap<String, Descartable> descartable;

    public Descartables(HashMap<String, Descartable> descartable) {
        this.descartable = descartable;
    }

    public HashMap<String, Descartable> getDescartable() {
        return descartable;
    }

    public void setDescartable(HashMap<String, Descartable> descartable) {
        this.descartable = descartable;
    }
    
    public void agregarDescartable(Descartable d){
        this.descartable.put(d.getNombre(), d);
    }
    
    

}
