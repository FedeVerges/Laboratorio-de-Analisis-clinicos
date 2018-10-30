/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author fede_
 */
public class Orden {
    private int numero;
    private String fechaDeIngreso;
    private String Medico;
    private int dniPaciente;
    private String nombrePaciente;
    private String obraSocial;
    private String estado;
    private String bioquimico;

    public Orden(String fechaDeIngreso, String Medico, int dniPaciente,String nombrePaciente, String obraSocial) {
        this.fechaDeIngreso = fechaDeIngreso;
        this.Medico = Medico;
        this.dniPaciente = dniPaciente;
        this.nombrePaciente = nombrePaciente;
        this.obraSocial = obraSocial;
        this.estado = "PENDIENTE";
    }

    
    public int getNumero() {
        return numero;
    }
    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getFechaDeIngreso() {
        return fechaDeIngreso;
    }

    public void setFechaDeIngreso(String fechaDeIngreso) {
        this.fechaDeIngreso = fechaDeIngreso;
    }

    public String getMedico() {
        return Medico;
    }

    public void setMedico(String Medico) {
        this.Medico = Medico;
    }

    public int getDniPaciente() {
        return dniPaciente;
    }

    public void setDniPaciente(int dniPaciente) {
        this.dniPaciente = dniPaciente;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }
    

    public String getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getBioquimico() {
        return bioquimico;
    }

    public void setBioquimico(String bioquimico) {
        this.bioquimico = bioquimico;
    }
    
    

    

    
    
    
    
    
    
    
}
