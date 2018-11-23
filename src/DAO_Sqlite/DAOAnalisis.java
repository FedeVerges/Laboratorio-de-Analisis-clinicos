/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO_Sqlite;

import Clases.Analisis;
import java.util.ArrayList;

/**
 *
 * @author fede_
 */
public class DAOAnalisis implements DAOA {

    @Override
    public Analisis readAnalisis(int codigoAnalisis) {
        return DataSrc.readAnalisis(codigoAnalisis);
    }

    @Override
    public ArrayList<Analisis> readListaAnalisis() {
        return DataSrc.readListaAnalisis();
    }

    @Override
    public Analisis createAnalisis() {
        return new Analisis(0, "");
    }

    @Override
    public void guardarAnalisis(Analisis analisis) {
        DataSrc.insertAnalisis(analisis);
    }

    @Override
    public void modificarAnalisis(Analisis analisis) {
        DataSrc.updateAnalisis(analisis);
    }

    @Override
    public void deteleAnalisis(Analisis analisis) {
        DataSrc.deleteAnalisis(analisis);
    }

}
