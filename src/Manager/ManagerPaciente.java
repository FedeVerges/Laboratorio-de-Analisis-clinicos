/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import Base_de_Datos.ConnectionMethods;
import Clases.Paciente;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author fede_
 */
public class ManagerPaciente {

    public ArrayList<Paciente> recuperarFilas() {
        Statement statement = null;
        String query = "SELECT * FROM 'PACIENTE'";
        String queryObraSocial;
        ArrayList<Paciente> datosPaciente = new ArrayList<Paciente>();
        Paciente p;
        try {
            statement = ConnectionMethods.getConection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            String obraSocial;

            while (resultSet.next()) {
                p = new Paciente((resultSet.getString("P_NOMBRE")), (resultSet.getString("P_APELLIDO")), (resultSet.getInt("P_DNI")), (resultSet.getLong("P_TELEFONO")), (resultSet.getString("P_FECHA_NACIMIENTO")), (resultSet.getInt("P_EDAD")), (resultSet.getString("P_SEXO")));
                datosPaciente.add(p);

            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            ConnectionMethods.close(statement);
        }
        return datosPaciente;
    }

    public void cargarPaciente(String nombre, String apellido, int dni, Long telefono, String fNacimiento, int edad, String sexo) throws SQLException {
        PreparedStatement ps = null;
        String insertSql = "INSERT INTO 'PACIENTE' VALUES (?,?,?,?,?,?,?)";
        try {
            ps = ConnectionMethods.getConection().prepareStatement(insertSql);
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setInt(3, dni);
            ps.setLong(4, telefono);
            ps.setString(5, fNacimiento);
            ps.setInt(6, edad);
            ps.setString(7, sexo);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "error al cargar en la base de datos en la tabla 'PACIENTE'");

        } finally {
            ConnectionMethods.close(ps);
            
        }

    }

    public void cargarObraSocialPaciente(String nombreObraSocial, int dni) {
        PreparedStatement ps = null;
        String insertSql = "INSERT INTO 'RELACION PACIENTE_OBRA' VALUES (?,?)";
        try {
            ps = ConnectionMethods.getConection().prepareStatement(insertSql);
            ps.setInt(1, dni);
            ps.setString(2, nombreObraSocial);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "error al cargar en la tabla 'OBRA SOCIAL'");

        } finally {
            ConnectionMethods.close(ps);
        }
    }

    public String[] recuperarColumnas() {
        String columnas[];

        Statement statement = null;
        String query = "SELECT * FROM 'PACIENTE'";

        try {
            statement = ConnectionMethods.getConection().createStatement();

            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            columnas = new String[]{resultSetMetaData.getColumnName(1), resultSetMetaData.getColumnName(2), resultSetMetaData.getColumnName(3), resultSetMetaData.getColumnName(4), resultSetMetaData.getColumnName(5), resultSetMetaData.getColumnName(6), resultSetMetaData.getColumnName(7), resultSetMetaData.getColumnName(8)};
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            ConnectionMethods.close(statement);
        }
        return columnas;

    }

}
