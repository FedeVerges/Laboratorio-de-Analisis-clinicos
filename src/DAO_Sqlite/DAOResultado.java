/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO_Sqlite;

import Clases.Resultado;
import java.util.ArrayList;

/**
 *
 * @author fede_
 */
public interface DAOResultado {

    public ArrayList<Resultado> readResultado(int codigoOrden);

    public Resultado createResultado();

    public void insertResultado(Resultado resultado);
    
    public void updateResultado(Resultado resultado);
    
    public void deteleResultadoOrden(Resultado r);
    
    public void deteleResultadoAnalisis(Resultado r);

}
