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
import DAO_Sqlite.DAOOrdenes;
import DAO_Sqlite.DAOResultados;
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
    DAOOrdenes daoOrdenes = new DAOOrdenes();
    DAOResultados daoResultado = new DAOResultados();

    public ArrayList<Orden> recuperarFilas() {
        return daoOrdenes.readOrdenes();
    }
    public Orden recuperarOrden(int codigoOrden){
        return daoOrdenes.readOrden(codigoOrden);
    }

    public int cargarOrden(String fecha, String medico, int dni, String nombrePaciente, String nombreObraSocial) {
        Orden orden = daoOrdenes.createOrden();
        int codigo = 0;
        orden.setFechaDeIngreso(fecha);
        orden.setMedico(medico);
        orden.getPaciente().setDni(dni);
        orden.getPaciente().setNombre(nombrePaciente);
        orden.getObraSocial().setNombre(nombreObraSocial);
        orden.setNumero(retornarCodigo());  // le asigno un numero de orden. 
         
        codigo = orden.getNumero(); // retorno el codigo de la orden.
        daoOrdenes.guardarOrden(orden);
        return codigo;
    }

    public ArrayList<Resultado> recuperarFilasResultados(int codigoOrden) {
        return daoResultado.readResultado(codigoOrden);
        
    }

    public ArrayList<Resultado> recuperarResultadosDeTabla(DefaultTableModel modelo, JTable tabla, String bio) throws SQLException {
        String bioquimico = bio;
        
        ArrayList<Resultado> lista = new ArrayList();
        int fila = tabla.getRowCount();
        int i;
        //AGREGAR CONTROLES!!!
        for (i = 0; i < fila; i++) {
            {
                Resultado r = daoResultado.createResultado();
                r.setCodigoOrden((Integer) (modelo.getValueAt(i, 0)));
                r.setCodigoAnalisis((Integer) (modelo.getValueAt(i, 1)));
                r.setNombreAnalisis(modelo.getValueAt(i, 2).toString());
                r.setValorTomado((modelo.getValueAt(i, 3)).toString());
                lista.add(r);
                
            }
        }
        return lista;

    }

    public void finalizarOrden(int CodigoOrden, String bioquimico) throws SQLException {
        Orden orden = daoOrdenes.createOrden();
        orden.getBioquimico().setNombre(bioquimico);
        orden.setNumero(CodigoOrden);
        daoOrdenes.modificarOrdenResultado(orden);
    }

    public void cargarResultado(int codigo, int codigoAnalisis,String nombre,String valor) throws SQLException {
        Resultado res = daoResultado.createResultado();
        res.setCodigoOrden(codigo);
        res.setCodigoAnalisis(codigoAnalisis);
        res.setNombreAnalisis(nombre);
        res.setValorTomado(valor);
        daoResultado.insertResultado(res);
    }

    public ArrayList<Orden> recuperarFilasTerminadas() {
        return daoOrdenes.readOrdenesTerminadas();
    }

    public int retornarCodigo() {
        return daoOrdenes.retornarCodigo();
    }
        

   
}
