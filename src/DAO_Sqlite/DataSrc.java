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
import Clases.Resultado;
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
    public static Orden readOrden(int codigoORden) {
        Statement statement = null;
        String query = "Select * FROM ORDEN WHERE OR_NUMERO =" + codigoORden;
        Orden a;

        try {
            statement = ConnectionMethods.getConection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            a = new Orden((resultSet.getString("OR_FECHA")), (resultSet.getString("OR_MEDICO")), (resultSet.getInt("P_DNI")),(resultSet.getString("P_NOMBRE")),(resultSet.getString("OBRA_SOCIAL")));
            a.setNumero((resultSet.getInt("OR_NUMERO")));
            a.setEstado(resultSet.getString("ESTADO"));
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            ConnectionMethods.close(statement);
        }
        return a;
    }

    public static ArrayList<Orden> readOrdenes() {
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

    public static ArrayList<Orden> readOrdenesTerminadas() {
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
                    a.getBioquimico().setNombre(resultSet.getString("OR_BIOQUIMICO"));

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

    public static void insertOrden(Orden orden) {
        PreparedStatement ps = null;
        String insertSql = "INSERT INTO 'ORDEN' VALUES (?,?,?,?,?,?,?,?)";
        try {

            ps = ConnectionMethods.getConection().prepareStatement(insertSql);
            ps.setInt(1, orden.getNumero());
            ps.setString(2, orden.getFechaDeIngreso());
            ps.setString(3, orden.getMedico());
            ps.setInt(4, orden.getPaciente().getDni());
            ps.setString(5, orden.getPaciente().getNombre());
            ps.setString(6, orden.getObraSocial().getNombre());
            ps.setString(7, orden.getEstado());
            ps.setString(8, orden.getBioquimico().getNombre());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "error al cargar en la base de datos en la tabla 'ORDEN'");

        } finally {
            ConnectionMethods.close(ps);
        }

    }

    public static void deleteOrden(Orden orden) {
        PreparedStatement ps = null;
        String query = "DELETE FROM ORDEN WHERE OR_NUMERO = " + orden.getNumero();
        try {
            ps = ConnectionMethods.getConection().prepareStatement(query);

            ps.executeUpdate(query);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al querer borrar la orden numero " + orden.getNumero());
        }

    }

    public static void updateOrden(Orden orden) {
        PreparedStatement ps = null;
        String updateSql = "UPDATE ORDEN SET OR_MEDICO=" + orden.getMedico() + "," + "P_dni=" + orden.getPaciente().getDni() + "," + "P_NOMBRE=" + orden.getPaciente().getNombre()
                + "," + "OBRA_SOCIAL=" + orden.getObraSocial() + "," + "ESTADO = " + orden.getEstado() + "," + "OR_BIOQUIMICO =" + orden.getBioquimico()
                + "WHERE OR_NUMERO = " + orden.getNumero();

        try {
            ps = ConnectionMethods.getConection().prepareStatement(updateSql);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionMethods.close(ps);
        }
    }

    public static void updateOrdenResultado(Orden orden) {
        PreparedStatement ps = null;
        String updateSql = "UPDATE ORDEN SET ESTADO = " + orden.getEstado() + "OR_BIOQUIMICO =" + orden.getBioquimico()
                + "WHERE OR_NUMERO = " + orden.getNumero();

        try {
            ps = ConnectionMethods.getConection().prepareStatement(updateSql);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionMethods.close(ps);
        }

    }

    public static int retornarCodigo() {
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

    // Resultados // 
    public static ArrayList<Resultado> readResultado(int codigoOrden) {
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

    public static void insertResultado(Resultado r) {
        PreparedStatement ps = null;
        String insertSql = "INSERT INTO RESULTADOS VALUES (?,?,?,?)";
        try {

            ps = ConnectionMethods.getConection().prepareStatement(insertSql);
            ps.setInt(1, r.getCodigoOrden());
            ps.setInt(2, r.getCodigoAnalisis());
            ps.setString(3, r.getNombreAnalisis());
            ps.setString(4, r.getValorTomado());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "error al cargar en la base de datos en la tabla 'Resultados'");

        } finally {
            ConnectionMethods.close(ps);
        }

    }

    public static void deleteResultado(Resultado r) {
        PreparedStatement ps = null;
        String query = "DELETE FROM RESULTADOS WHERE ORDEN_NUMERO = " + r.getCodigoOrden();
        try {
            ps = ConnectionMethods.getConection().prepareStatement(query);

            ps.executeUpdate(query);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al querer borrar la orden numero " + r.getCodigoOrden());
        }
    }

    public static void updateResultado(Resultado r) {
        PreparedStatement ps = null;
        String resultado = r.getValorTomado();

        int codigo = r.getCodigoAnalisis();
        int numeroOrden = r.getCodigoOrden();
        // debo cargar el resultado nulo y despues modificarlo 

        String insertSql = "UPDATE RESULTADOS SET RES_VALOR = " + "'" + resultado + "'" + " where ORDEN_NUMERO =" + numeroOrden + " AND CODIGO_ANALISIS = " + codigo;

        try {
            ps = ConnectionMethods.getConection().prepareStatement(insertSql);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            ConnectionMethods.close(ps);
        }
    }

    // Pacientes // 
    public static ArrayList<Paciente> readPaciente() {
        Statement statement = null;
        String query = "SELECT * FROM PACIENTE";
        ArrayList<Paciente> datosPaciente = new ArrayList<>();
        Paciente p;
        try {
            statement = ConnectionMethods.getConection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

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

    public static String readObrasocialPaciente(int dni) {
        Statement statement = null;
        String query = "SELECT NOM_OBRA from 'RELACION PACIENTE_OBRA' WHERE NUM_DNI =" + dni;
        String obraSocial;
        try {
            statement = ConnectionMethods.getConection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            obraSocial = resultSet.getString("NOM_OBRA");
            statement.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error al obtener la obra social del paciente");
            return null;
        } finally {
            ConnectionMethods.close(statement);
        }
        return obraSocial;
    }

    public static void insertPaciente(Paciente p) {
        PreparedStatement ps = null;
        String insertSql = "INSERT INTO 'PACIENTE' VALUES (?,?,?,?,?,?,?)";
        try {
            ps = ConnectionMethods.getConection().prepareStatement(insertSql);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApellido());
            ps.setInt(3, p.getDni());
            ps.setLong(4, p.getTelefono());
            ps.setString(5, p.getFechaNacimiento());
            ps.setInt(6, p.getEdad());
            ps.setString(7, p.getSexo());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "error al cargar en la base de datos en la tabla 'PACIENTE'");

        } finally {
            ConnectionMethods.close(ps);

        }
    }

    public static void insertObrasocial_Paciente(String nombreObraSocial, int dni) {
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

    public static void updatePaciente(Paciente p) {
        PreparedStatement ps = null;

        // no se permite modificar el dni, es un campo unico, para cambiarlo hay que dar de baja el paciente y volverlo a insertar.
        String insertSql = "UPDATE PACIENTE SET P_NOMBRE = " + p.getNombre() + "," + "P_APELLIDO= " + p.getApellido() + ","
                + "," + "P_TELEFONO=" + p.getTelefono() + "," + "P_FECHA_NACIMIENTO=" + p.getFechaNacimiento() + "," + "P_EDAD=" + p.getEdad() + "," + "P_SEXO=" + p.getSexo()
                + "WHERE P_DNI =" + p.getDni();

        try {
            ps = ConnectionMethods.getConection().prepareStatement(insertSql);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            ConnectionMethods.close(ps);
        }
    }

    public static void deletePaciente(Paciente p) {
        PreparedStatement ps = null;
        String query = "DELETE FROM PACIENTE WHERE P_DNI =" + p.getDni();
        try {
            ps = ConnectionMethods.getConection().prepareStatement(query);

            ps.executeUpdate(query);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al querer borrar al paciente con el dni: " + p.getDni());

        }
    }

}
