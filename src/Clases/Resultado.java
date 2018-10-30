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
public class Resultado {
    private String valorTomado;
    private int codigoAnalisis;
    private int codigoOrden;
    private String nombreAnalisis;

    public Resultado(int codigoOrden,int codigoAnalisis,  String nombreAnalisis) {
        this.codigoAnalisis = codigoAnalisis;
        this.codigoOrden=codigoOrden;
        this.nombreAnalisis=nombreAnalisis;
    }

    public Resultado(int codigoOrden, int codigoAnalisis, String nombreAnalisis,String valorTomado) {
        this.valorTomado = valorTomado;
        this.codigoAnalisis = codigoAnalisis;
        this.codigoOrden = codigoOrden;
        this.nombreAnalisis = nombreAnalisis;
    }
    
    

    public String getValorTomado() {
        return valorTomado;
    }

    public int getCodigoAnalisis() {
        return codigoAnalisis;
    }

    public int getCodigoOrden() {
        return codigoOrden;
    }

    public String getNombreAnalisis() {
        return nombreAnalisis;
    }
    

    public void setValorTomado(String valorTomado) {
        this.valorTomado = valorTomado;
    }

    public void setCodigoAnalisis(int codigoAnalisis) {
        this.codigoAnalisis = codigoAnalisis;
    }

    public void setCodigoOrden(int codigoOrden) {
        this.codigoOrden = codigoOrden;
    }

    public void setNombreAnalisis(String nombreAnalisis) {
        this.nombreAnalisis = nombreAnalisis;
    }
    
    
    
    
}
