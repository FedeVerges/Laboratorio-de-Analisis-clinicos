/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.ArrayList;

/**
 *
 * @author fede_
 */
public class Paciente {

    private String nombre;
    private String apellido;
    private int dni;
    private Long telefono;
    private String fechaNacimiento;
    private Integer edad;
    private String sexo;
    private ArrayList<Obra_Social> obraSocial;

    public Paciente(String nombre, String apellido, int dni, Long telefono, String fechaNacimiento, int edad, String sexo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.sexo = sexo;
    }

    public Paciente() {

    }

    public int getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Long getTelefono() {
        return telefono;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Integer getEdad() {
        return edad;
    }

    public String getSexo() {
        return sexo;
    }

    public ArrayList<Obra_Social> getObraSocial() {
        return obraSocial;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public void setObraSocial(String obraSocial) {
        this.obraSocial = new ArrayList<>();
        Obra_Social o = new Obra_Social();
        o.setNombre(obraSocial);
        this.obraSocial.add(o);
    }

}
