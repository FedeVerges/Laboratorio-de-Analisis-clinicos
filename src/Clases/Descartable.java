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
public class Descartable {
    
    
    
    
    /***
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * PLANTEAR LA BASE DE DATOS COMO UN MAPA CON CLAVE VALOR, DONDE LA CLAVE ES EL NOMBRE DEL ARTICULO Y EL VALOR ES LA CANTIDAD MENOR Y MAYOR DEL MISMO
     * .
     * 
     */
    private String nombre;
    private int cantidadMinima;
    private int cantidadActual;

    public Descartable(String nombre, int cantidadMinima, int cantidadActual) {
        this.nombre = nombre;
        this.cantidadMinima = cantidadMinima;
        this.cantidadActual = cantidadActual;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidadMinima() {
        return cantidadMinima;
    }

    public int getCantidadActual() {
        return cantidadActual;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCantidadMinima(int cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    public void setCantidadActual(int cantidadActual) {
        this.cantidadActual = cantidadActual;
    }
    
    public void restarStock (int costo){
        this.setCantidadActual(this.cantidadActual - costo);
    }
    
    
}
