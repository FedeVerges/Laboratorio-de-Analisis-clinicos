/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO_Sqlite;

import Clases.Paciente;
import java.util.ArrayList;

/**
 *
 * @author fede_
 */
public interface DAOPaciente {

    public ArrayList<Paciente> read();

    public String readObrasocialPaciente(int dni);

    public Paciente create();

    public void insert(Paciente p);

    public void insertObrasocial_Paciente(Paciente p);

    public void update(Paciente p);

    public void delete(Paciente p);

}
