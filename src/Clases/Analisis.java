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
    private String consentimiento;
    private int costoDescartables; // cantidad de descartables consumidos.
    private String valoresReferencia;

    
    // solo usado de forma auxiliar.
    public Analisis(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidadUnidadesB=0;
        this.consentimiento="";
        this.indicacionesPrevias="";
        this.costoDescartables=0;
        this.valoresReferencia="";
    }
    
    
    public Analisis(int codigo, String nombre, String indicacionesPrevias, int cantidadUnidadesB, String consentimiento, int costoDescartables, String valoresReferencia) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.indicacionesPrevias = indicacionesPrevias;
        this.cantidadUnidadesB = cantidadUnidadesB;
        this.consentimiento = consentimiento;
        this.costoDescartables = costoDescartables;
        this.valoresReferencia = valoresReferencia;
    }
    
    // solo usado de forma auxiliar.
    public Analisis(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidadUnidadesB= 0;
        this.consentimiento="";
        this.indicacionesPrevias="";
        this.costoDescartables=0;
        this.valoresReferencia="";
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

    public String getConsentimiento() {
        return consentimiento;
    }

    public int getCostoDescartables() {
        return costoDescartables;
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

    public void setConsentimiento(String consentimiento) {
        this.consentimiento = consentimiento;
    }

    public void setCostoDescartables(int costoDescartables) {
        this.costoDescartables = costoDescartables;
    }

    public void setValoresReferencia(String valoresReferencia) {
        this.valoresReferencia = valoresReferencia;
    }
    


}
