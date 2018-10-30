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
public class Registro_de_cobros {
    private Float ingresos;
    private Float precioPorUnidadBParticular;
    private String obraSocial;
    private Orden orden;

    public Registro_de_cobros(Float ingresos, Float precioPorUnidadBParticular, String obraSocial, Orden orden) {
        this.ingresos = ingresos;
        this.precioPorUnidadBParticular = precioPorUnidadBParticular;
        this.obraSocial = obraSocial;
        this.orden = orden;
    }

    public Float getIngresos() {
        return ingresos;
    }

    public Float getPrecioPorUnidadBParticular() {
        return precioPorUnidadBParticular;
    }

    public String getObraSocial() {
        return obraSocial;
    }

    public Orden getOrden() {
        return orden;
    }
    
    public void setIngresos(Float ingresos) {
        this.ingresos = ingresos;
    }

    public void setPrecioPorUnidadBParticular(Float precioPorUnidadBParticular) {
        this.precioPorUnidadBParticular = precioPorUnidadBParticular;
    }

    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }
    
    
    
    
}
