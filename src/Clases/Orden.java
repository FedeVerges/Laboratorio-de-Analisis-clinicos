/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.ArrayList;
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
    private Paciente paciente;
    private Obra_Social obraSocial;
    private String estado;
    private Bioquimico  bioquimico;

    public Orden(String fechaDeIngreso, String Medico, int dniPaciente, String nombrePaciente, String obraSocial) {
        this.paciente = new Paciente();
        this.bioquimico = new Bioquimico();
        this.obraSocial = new Obra_Social();
        this.fechaDeIngreso = fechaDeIngreso;
        this.Medico = Medico;
        this.paciente.setDni(dniPaciente);
        this.paciente.setNombre (nombrePaciente);
        this.obraSocial.setNombre(obraSocial);
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

    public String getEstado() {
        return estado;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Obra_Social getObraSocial() {
        return obraSocial;
    
    }

    public Bioquimico getBioquimico() {
        return bioquimico;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public void setObraSocial(Obra_Social obraSocial) {
        this.obraSocial = obraSocial;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setBioquimico(Bioquimico bioquimico) {
        this.bioquimico = bioquimico;
    }

    
    

}
