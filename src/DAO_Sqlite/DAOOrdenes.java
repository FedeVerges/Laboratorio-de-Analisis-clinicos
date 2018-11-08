/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO_Sqlite;

import Base_de_Datos.ConnectionMethods;
import Clases.Obra_Social;
import Clases.Orden;
import Clases.Paciente;
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
public class DAOOrdenes {
    public static ArrayList<Orden> readOrden(){
        return DataSrc.readOrden();
    }
    
    public static Orden createOrden(){
        return new Orden("","",0,"","");

    }
    
    public static void guardarOrden(Orden orden){
        DataSrc.insertOrden(orden);
    }
    
    public static void modificarOrden(Orden orden){
       DataSrc.updateOrden(orden);
    }
    
    public static void modificarOrdenResultado(Orden orden){
        DataSrc.updateOrdenResultado(orden);
    }
    
}
