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
public class DAOOrdenes implements DAOOrden {

    @Override
    public ArrayList<Orden> readOrdenes() {
        return DataSrc.readOrdenes();
    }
    public Orden readOrden(int codigoOrden){
        return DataSrc.readOrden(codigoOrden);
    }

    @Override
    public ArrayList<Orden> readOrdenesTerminadas() {
        return DataSrc.readOrdenesTerminadas();
    }

    @Override
    public Orden createOrden() {
        return new Orden("", "", 0, "", "");

    }

    @Override
    public void guardarOrden(Orden orden) {
        DataSrc.insertOrden(orden);
    }

    @Override
    public void modificarOrden(Orden orden) {
        DataSrc.updateOrden(orden);
    }

    @Override
    public void modificarOrdenResultado(Orden orden) {
        DataSrc.updateOrdenResultado(orden);
    }

    @Override
    public void deteleOrden(Orden orden) {
        DataSrc.deleteOrden(orden);
    }

    @Override
    public int retornarCodigo() {
        return DataSrc.retornarCodigo();
        
    }

}
