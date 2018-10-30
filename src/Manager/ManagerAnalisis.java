/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import Base_de_Datos.ConnectionMethods;
import Clases.Analisis;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.JCheckBox;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author fede_
 */
public class ManagerAnalisis {

    public ArrayList<Analisis> recuperarFilas() {
        Statement statement = null;
        String query = "SELECT * FROM 'ANALISIS'";
        ArrayList<Analisis> datosAnalisis = new ArrayList<Analisis>();
        Analisis a;
        try {
            statement = ConnectionMethods.getConection().createStatement();

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                a = new Analisis((resultSet.getInt("A_codigo")), (resultSet.getString("A_NOMBRE")), (resultSet.getString("A_INDICACIONES")), (resultSet.getInt("A_CANT.UNIDADES_B")), (resultSet.getString("A_CONSENTIMIENTO")), (resultSet.getInt("A_COSTODESCARTABLES")), (resultSet.getString("A_VALORES_REFERENCIA")));
                datosAnalisis.add(a);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "error al cargar en la base de datos en la tabla 'Analisis'");

            return null;
        } finally {
            ConnectionMethods.close(statement);
        }
        return datosAnalisis;
    }

    public void cargarAnalisis(Analisis a) throws SQLException {
        PreparedStatement ps = null;
        String insertSql = "INSERT INTO 'ANALISIS VALUES (?,?,?,?,?,?,?)";
        try {
            ps = ConnectionMethods.getConection().prepareStatement(insertSql);
            ps.setInt(0, a.getCodigo());
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getIndicacionesPrevias());
            ps.setInt(3, a.getCantidadUnidadesB());
            ps.setString(4, a.getConsentimiento());
            ps.setInt(5, a.getCostoDescartables());
            ps.setString(6, a.getValoresReferencia());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionMethods.close(ps);
        }

    }

    public String[] recuperarColumnas() {
        String columnas[];

        Statement statement = null;
        String query = "SELECT * FROM 'ANALISIS'";

        try {
            statement = ConnectionMethods.getConection().createStatement();

            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            columnas = new String[]{resultSetMetaData.getColumnName(1), resultSetMetaData.getColumnName(2), resultSetMetaData.getColumnName(3), resultSetMetaData.getColumnName(4), resultSetMetaData.getColumnName(5), resultSetMetaData.getColumnName(6), resultSetMetaData.getColumnName(7), "seleccion"};
            // System.out.println(columnas.toString());
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

    public Object[] recuperarColumnasListadoAnalisis() {
        Object columnas[];
        columnas = new Object[]{new Boolean(true), "Codigo", "Nombre", "Indicaciones", "Unidades Bioquimicas", "Consentimiento", "Descartables", "Valores Referencia"};

        return columnas;

    }

    public ArrayList<Analisis> recuperarAnalisisSeleccionados(DefaultTableModel modelo, JTable tabla) {
        ArrayList<Analisis> lista = new ArrayList();
        int fila = tabla.getRowCount();
        int i;

        for (i = 0; i < fila; i++) {
            if ((Boolean) (modelo.getValueAt(i, 0)) == true) {
                Analisis a = new Analisis((Integer) (modelo.getValueAt(i, 1)), (modelo.getValueAt(i, 2)).toString(), (modelo.getValueAt(i, 3)).toString(), (Integer) (modelo.getValueAt(i, 4)), (modelo.getValueAt(i, 5)).toString(), (Integer) (modelo.getValueAt(i, 6)), (modelo.getValueAt(i, 7)).toString());
                lista.add(a);
                a.getCodigo();

            }
        }
        return lista;

    }

    public ArrayList<Analisis> recuperarCodigos(DefaultTableModel modelo, JTable tabla) {
        ArrayList<Analisis> listaCodigos = new ArrayList<>();
        int fila = 0;
        int i;
        try {
            for (i = 0; i < fila; i++) {
                Analisis a = new Analisis((modelo.getValueAt(fila, 0)), (modelo.getValueAt(fila, 1)));
                listaCodigos.add(a);

            }
        } catch (Exception e) {
        }
        return listaCodigos;
    }

    //  public  recuperarAnalisisSeleccionados(int codigo, String nombre, String indicacionesPrevias, int cantidadUnidadesB, String consentimiento, int costoDescartables, String valoresReferencia) {
}