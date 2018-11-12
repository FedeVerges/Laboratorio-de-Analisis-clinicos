/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO_Sqlite;

import Clases.Orden;
import java.util.ArrayList;

/**
 *
 * @author fede_
 */
public interface DAOOrden {

    public Orden readOrden(int codigoOrden);

    public ArrayList<Orden> readOrdenes();

    public ArrayList<Orden> readOrdenesTerminadas();

    public Orden createOrden();

    public void guardarOrden(Orden orden);

    public void modificarOrden(Orden orden);

    public void modificarOrdenResultado(Orden orden);

    public void deteleOrden(Orden orden);

    public int retornarCodigo();

}
