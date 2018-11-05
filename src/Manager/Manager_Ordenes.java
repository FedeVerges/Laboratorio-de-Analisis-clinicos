/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import Base_de_Datos.ConnectionMethods;
import Clases.Analisis;
import Clases.Obra_Social;
//import Clases.Analisis;
import Clases.Orden;
import Clases.Paciente;
import Clases.Resultado;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author fede_
 */
public class Manager_Ordenes {

    public ArrayList<Orden> recuperarFilas() {
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

    public String[] recuperarColumnas() {
        String columnas[];
        columnas = new String[]{"Numero de Orden", "Fecha de ingreso", "Medico", "DNI Paciente", "Paciente", "Obra Social"};
        return columnas;

    }

    public int cargarOrden(String fecha, String medico, int dni, String nombrePaciente, String nombreObraSocial) {
        PreparedStatement ps = null;
        int codigo = 0;
        String insertSql = "INSERT INTO 'ORDEN' VALUES (?,?,?,?,?,?,?,?)";
        try {
            Orden orden = new Orden(fecha, medico, dni, nombrePaciente, nombreObraSocial);
            orden.setNumero(retornarCodigo());
            codigo = orden.getNumero();
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
        return codigo;
    }

    public ArrayList<Resultado> recuperarFilasResultados(int codigoOrden) {

        Statement statement = null;
        String query = "SELECT * FROM RESULTADOS WHERE RESULTADOS.ORDEN_NUMERO = " + codigoOrden;

        ArrayList<Resultado> datosResultados = new ArrayList<Resultado>();
        Resultado a;
        try {
            statement = ConnectionMethods.getConection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {

                a = new Resultado((resultSet.getInt("ORDEN_NUMERO")), (resultSet.getInt("CODIGO_ANALISIS")), (resultSet.getString("NOMBRE_ANALISIS")));
                System.out.println((resultSet.getInt("ORDEN_NUMERO")));
                datosResultados.add(a);
                System.out.println("El codigo de la orden es" + a.getCodigoOrden());
                System.out.println("El codigo del analisis es:" + a.getCodigoAnalisis());
                System.out.println("El nombre del analisis es " + a.getNombreAnalisis());
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            ConnectionMethods.close(statement);
        }

        return datosResultados;
    }

    public void recuperarResultadosDeTabla(DefaultTableModel modelo, JTable tabla, String bio) throws SQLException {
        String bioquimico = bio;
        ArrayList<Analisis> lista = new ArrayList();
        int fila = tabla.getRowCount();
        int i;
        //AGREGAR CONTROLES!!!
        for (i = 0; i < fila; i++) {
            {
                Resultado r = new Resultado((Integer) (modelo.getValueAt(i, 0)), (Integer) (modelo.getValueAt(i, 1)), (modelo.getValueAt(i, 2).toString()), (modelo.getValueAt(i, 3)).toString());
                System.out.println("En recuperarResultadosDeTabla tengo nombre de analisis:" + r.getNombreAnalisis());
                System.out.println("En recuperarResultadosDeTabla tengo codigo de analisis:" + r.getCodigoAnalisis());
                System.out.println("En recuperarResultadosDeTabla tengo codigo de orden: " + r.getCodigoOrden());

                r.getNombreAnalisis();
                r.getValorTomado();
                r.getCodigoAnalisis();
                r.getCodigoOrden();
                cargarResultado(r);
                finalizarOrden(r.getCodigoOrden(), bioquimico);
            }
        }

    }

    public void finalizarOrden(int CodigoOrden, String bioquimico) throws SQLException {
        PreparedStatement ps = null;

        int codigo = CodigoOrden;
        String bio = bioquimico;

        String insertSql = "UPDATE ORDEN SET ESTADO = 'TERMINADO', OR_BIOQUIMICO = " + "'" + bio + "'" + " where OR_NUMERO =" + codigo;

        try {
            ps = ConnectionMethods.getConection().prepareStatement(insertSql);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionMethods.close(ps);
        }

    }

    public void cargarResultado(Resultado r) throws SQLException {
        PreparedStatement ps = null;
        String resultado = r.getValorTomado();
        System.out.println("valor tomado es: " + resultado);
        int codigo = r.getCodigoAnalisis();
        int numeroOrden = r.getCodigoOrden();

        String insertSql = "UPDATE RESULTADOS SET RES_VALOR = " + "'" + resultado + "'" + " where ORDEN_NUMERO =" + numeroOrden + " AND CODIGO_ANALISIS = " + codigo;

        try {
            ps = ConnectionMethods.getConection().prepareStatement(insertSql);
          
            ps.executeUpdate();
            System.out.println("EXITOOOO GATOOO");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionMethods.close(ps);
        }

    }
    
    public ArrayList<Orden> recuperarFilasTerminadas() {
        Statement statement = null;

        String query = "Select * FROM ORDEN,PACIENTE,'OBRA SOCIAL' WHERE ORDEN.ESTADO ='TERMINADO' AND ORDEN.P_DNI = PACIENTE.P_DNI AND ORDEN.OBRA_SOCIAL = 'OBRA SOCIAL'.O_NOMBRE ";

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
                    a.setBioquimico(resultSet.getString("OR_BIOQUIMICO"));

                    datosOrdenes.add(a);
                    System.out.println(a.getEstado());
                    System.out.println(a.getNumero());

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

    public int retornarCodigo() {
        Statement statement = null;
        int codigo = 0;

        String query = "SELECT max(ORDEN.OR_NUMERO) FROM ORDEN";
        try {
            statement = ConnectionMethods.getConection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            codigo = resultSet.getInt("max(ORDEN.OR_NUMERO)") + 1;

        } catch (SQLException e) {

        } finally {
            ConnectionMethods.close(statement);
        }
        return codigo;

    }
}
