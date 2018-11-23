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
public interface DAOA {

    public Analisis readAnalisis(int codigoAnalisis);

    public ArrayList<Analisis> readListaAnalisis();

    public Analisis createAnalisis();

    public void guardarAnalisis(Analisis analisis);

    public void modificarAnalisis(Analisis analisis);

    public void deteleAnalisis(Analisis analisis);

}
