/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO_Sqlite;

import Base_de_Datos.ConnectionMethods;
import Clases.Paciente;
import com.sun.org.apache.bcel.internal.generic.D2F;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author fede_
 */
public class DAOPacientes implements DAOPaciente{

    @Override
    public ArrayList<Paciente> read(){
       return DataSrc.readPaciente();

    }

    public String readObrasocialPaciente(int dni) {
       return DataSrc.readObrasocialPaciente(dni);
        
    }
    
    @Override
    public Paciente create(){
        Paciente p;
        p = new Paciente("", "", 0,Long.MIN_VALUE, "", 0, "");
        return p;
    }

    @Override
    public void insert(Paciente p) {
        DataSrc.insertPaciente(p);
        
    }

    @Override
    public void insertObrasocial_Paciente(Paciente p) {
        DataSrc.insertObrasocial_Paciente(p.getObraSocial().get(0).getNombre(),p.getDni());
    }
    @Override
    public void update(Paciente p) {
        DataSrc.updatePaciente(p);
        
    }

    @Override
    public void delete(Paciente p) {
        DataSrc.deletePaciente(p);
    }

}
