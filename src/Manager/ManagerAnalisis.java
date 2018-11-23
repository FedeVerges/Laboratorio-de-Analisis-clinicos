/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import Base_de_Datos.ConnectionMethods;
import Clases.Analisis;
import Clases.Descartable;
import DAO_Sqlite.DAOAnalisis;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.JCheckBox;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author fede_
 */
public class ManagerAnalisis {

    DAOAnalisis daoAnalisis = new DAOAnalisis();

    /**
     * Este metodo retorna todos los analsis de la base de datos.
     *
     * @return
     */
    public ArrayList<Analisis> recuperarFilas() {
        return daoAnalisis.readListaAnalisis();

    }

    public Analisis cargarAnalisis(int codigo, String nombre, String indicacionesPrevias, int cantidadUnidadesB, Descartable d, Boolean consentimiento, String valoresReferencia) {
        Analisis aux = daoAnalisis.createAnalisis();
        aux.setCodigo(codigo);
        aux.setNombre(nombre);
        aux.setIndicacionesPrevias(indicacionesPrevias);
        aux.setCantidadUnidadesB(cantidadUnidadesB);
        aux.setConsentimiento(consentimiento);
        aux.setValoresReferencia(valoresReferencia);
        aux.setDescartable(d);
        daoAnalisis.guardarAnalisis(aux);
        return aux;

    }

    public ArrayList<Analisis> recuperarAnalisisSeleccionados(DefaultTableModel modelo, JTable tabla) {
        ArrayList<Analisis> lista = new ArrayList();
        int fila = tabla.getRowCount();
        int i;
        for (i = 0; i < fila; i++) {
            if ((Boolean) (modelo.getValueAt(i, 0)) == true) {
                Analisis a = new Analisis((Integer) (modelo.getValueAt(i, 1)), (modelo.getValueAt(i, 2)).toString(), (modelo.getValueAt(i, 3)).toString(), (Integer) (modelo.getValueAt(i, 4)), (Boolean) (modelo.getValueAt(i, 5)), (modelo.getValueAt(i, 7)).toString());
                lista.add(a);

            }
        }
        return lista;

    }
    /**
     * Este metodo selecciona un analisis de la tabla que le pasamos por parametro.
     * se agrega el parametro filamas debido a que la tabla de analisis que se encuentra en listado
     * analisis tiene una fila menos que la tabla de analisis para seleccionar.
     * @param modelo
     * @param tabla
     * @param filamas
     * @return 
     */
    public Analisis seleccionarAnalisis(DefaultTableModel modelo, JTable tabla, int filamas) {
        int filaSeleccionada = tabla.getSelectedRow();

        Analisis a = new Analisis((Integer) (modelo.getValueAt(filaSeleccionada, 0+filamas)), (modelo.getValueAt(filaSeleccionada, 1+filamas)).toString(), (modelo.getValueAt(filaSeleccionada, 2+filamas)).toString(), (Integer) (modelo.getValueAt(filaSeleccionada, 3+filamas)), (Boolean) (modelo.getValueAt(filaSeleccionada, 4+filamas)), (modelo.getValueAt(filaSeleccionada, 6+filamas)).toString());
        System.out.println(a.getCodigo());
        return a;
    }

    public ArrayList<Analisis> recuperarCodigos(DefaultTableModel modelo, JTable tabla) {

        ArrayList<Analisis> listaCodigos = new ArrayList<>();
        int fila = tabla.getRowCount();
        int i;
        try {
            for (i = 0; i < fila; i++) {
                int pruebaCasteoCodigo = (Integer) (modelo.getValueAt(i, 0));
                String pruebaCasteoNombre = (modelo.getValueAt(i, 1)).toString();
                System.out.println("El codigo casteado en recuperar codigo es: " + pruebaCasteoCodigo);
                System.out.println("El nombre de analisis en recuperar codigo es: " + pruebaCasteoNombre);
                Analisis a = new Analisis((Integer) (modelo.getValueAt(i, 0)), (modelo.getValueAt(i, 1)).toString());
                listaCodigos.add(a);

            }
        } catch (Exception e) {
        }
        return listaCodigos;
    }
    
    // controlar bien cuando se puede eliminar un analisis
    
    public void deleteAnalisis(Analisis a){
        daoAnalisis.deteleAnalisis(a);
    }
    /*
    public String recuperarValorReferencia(int codigoAnalisis){
        // consultarle a fede por la query
    }
     */
    //  public  recuperarAnalisisSeleccionados(int codigo, String nombre, String indicacionesPrevias, int cantidadUnidadesB, String consentimiento, int costoDescartables, String valoresReferencia) {
}
