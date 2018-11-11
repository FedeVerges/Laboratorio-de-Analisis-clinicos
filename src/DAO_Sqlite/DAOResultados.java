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
public class DAOResultados implements DAOResultado {

    @Override
    public ArrayList<Resultado> readResultado(int codigoOrden) {
        return DataSrc.readResultado(codigoOrden);
    }

    @Override
    public Resultado createResultado() {
        return new Resultado(0, 0, "");

    }

    @Override
    public void insertResultado(Resultado resultado) {
        DataSrc.insertResultado(resultado);
    }
    @Override
    public void updateResultado(Resultado resultado){
        DataSrc.updateResultado(resultado);
    }

    @Override
    public void deteleResultado(Resultado r) {
        DataSrc.deleteResultado(r);
    }

}
