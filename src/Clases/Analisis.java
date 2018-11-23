/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.Date;




/**
 *
 * @author fede_
 */
public class Analisis{
    private int codigo;
    private String nombre;
    private String indicacionesPrevias;
    private int cantidadUnidadesB;
    private Boolean consentimiento;
    private Descartable descartable; // cantidad de descartables consumidos.
    private String valoresReferencia;

    
    // solo usado de forma auxiliar.
    public Analisis(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidadUnidadesB=0;
        this.consentimiento=false;
        this.indicacionesPrevias="";
        this.valoresReferencia="";
    }
    
    
    public Analisis(int codigo, String nombre, String indicacionesPrevias, int cantidadUnidadesB, Boolean consentimiento, String valoresReferencia) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.indicacionesPrevias = indicacionesPrevias;
        this.cantidadUnidadesB = cantidadUnidadesB;
        this.consentimiento = consentimiento;
        this.valoresReferencia = valoresReferencia;
    }
    

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIndicacionesPrevias() {
        return indicacionesPrevias;
    }

    public int getCantidadUnidadesB() {
        return cantidadUnidadesB;
    }

    public Boolean getConsentimiento() {
        return consentimiento;
    }

    public Descartable getDescartable() {
        return this.descartable;
    }

    public String getValoresReferencia() {
        return valoresReferencia;
    }
    

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIndicacionesPrevias(String indicacionesPrevias) {
        this.indicacionesPrevias = indicacionesPrevias;
    }

    public void setCantidadUnidadesB(int cantidadUnidadesB) {
        this.cantidadUnidadesB = cantidadUnidadesB;
    }

    public void setConsentimiento(Boolean consentimiento) {
        this.consentimiento = consentimiento;
    }

    public void setDescartable(Descartable descartables) {
        this.descartable = descartable;
    }

    public void setValoresReferencia(String valoresReferencia) {
        this.valoresReferencia = valoresReferencia;
    }
    


}
