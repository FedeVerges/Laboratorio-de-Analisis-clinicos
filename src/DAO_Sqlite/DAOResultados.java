/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO_Sqlite;

import java.util.ArrayList;

/**
 *
 * @author fede_
 */
public class DAOResultados {

    public static ArrayList<Resultado> readResultado() {
        return DataSrc.readResultado();
    }

    public static Resultado createResultado() {
        return new Resultado("", "", 0, "", "");

    }

    public static void guardarResultado(Resultado orden) {
        DataSrc.insertResultado(orden);
    }

}
