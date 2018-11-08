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
public class DataSrc {

    // Ordenes //
    
    public static void insertOrden(Orden orden) {
        PreparedStatement ps = null;
        String insertSql = "INSERT INTO 'ORDEN' VALUES (?,?,?,?,?,?,?,?)";
        try {

            ps = ConnectionMethods.getConection().prepareStatement(insertSql);
            ps.setInt(1, orden.getNumero());
            ps.setString(2, orden.getFechaDeIngreso());
            ps.setString(3, orden.getMedico());
            ps.setInt(4, orden.getDniPaciente());
            ps.setString(5, orden.getNombrePaciente());
            ps.setString(6, orden.getObraSocial());
            ps.setString(7, orden.getEstado());
            ps.setString(8, orden.getBioquimico());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "error al cargar en la base de datos en la tabla 'ORDEN'");

        } finally {
            ConnectionMethods.close(ps);
        }

    }

    public static void deleteOrden(Orden orden) {
        Statement statement = null;
        String query = "DELETE FROM ORDEN WHERE OR_NUMERO = "+orden.getNumero();

    }

    public static ArrayList<Orden> readOrden() {
        Statement statement = null;

        String query = "Select * FROM ORDEN,PACIENTE,'OBRA SOCIAL' WHERE ORDEN.ESTADO ='PENDIENTE' AND ORDEN.P_DNI = PACIENTE.P_DNI AND ORDEN.OBRA_SOCIAL = 'OBRA SOCIAL'.O_NOMBRE ";

        ArrayList<Orden> datosOrdenes = new ArrayList<Orden>();
        Orden a;

        try {
            statement = ConnectionMethods.getConection().createStatement();

            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    Paciente p = new Paciente((resultSet.getString("P_NOMBRE")), (resultSet.getString("P_APELLIDO")), (resultSet.getInt("P_DNI")), (resultSet.getLong("P_TELEFONO")), (resultSet.getString("P_FECHA_NACIMIENTO")), (resultSet.getInt("P_EDAD")), (resultSet.getString("P_SEXO")));
                    Obra_Social obra = new Obra_Social((resultSet.getString("O_NOMBRE")), resultSet.getLong("O_TELEFONO"), resultSet.getFloat("O_PUB"));

                    a = new Orden((resultSet.getString("OR_FECHA")), (resultSet.getString("OR_MEDICO")), (p.getDni()), (p.getNombre()), (obra.getNombre()));
                    a.setNumero((resultSet.getInt("OR_NUMERO")));
                    a.setEstado(resultSet.getString("ESTADO"));

                    datosOrdenes.add(a);
                }
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            ConnectionMethods.close(statement);
        }
        return datosOrdenes;

    }

    public static void updateOrden(Orden orden) {
        PreparedStatement ps = null;
        String updateSql = "UPDATE ORDEN SET OR_MEDICO=" + orden.getMedico() + "P_dni=" + orden.getDniPaciente() + "P_NOMBRE=" + orden.getNombrePaciente()
                + "OBRA_SOCIAL=" + orden.getObraSocial() + "ESTADO = " + orden.getEstado() + "OR_BIOQUIMICO =" + orden.getBioquimico()+
                "WHERE OR_NUMERO = "+ orden.getNumero();

        try {
            ps = ConnectionMethods.getConection().prepareStatement(updateSql);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionMethods.close(ps);
        }
    }
    
    public static void updateOrdenResultado(Orden orden){
         PreparedStatement ps = null;
        String updateSql = "UPDATE ORDEN SET ESTADO = " + orden.getEstado() + "OR_BIOQUIMICO =" + orden.getBioquimico()+
                "WHERE OR_NUMERO = "+ orden.getNumero();

        try {
            ps = ConnectionMethods.getConection().prepareStatement(updateSql);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionMethods.close(ps);
        }
        
    }
    
    // Resultados // 
    
}
