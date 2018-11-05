package Manager;

import Base_de_Datos.ConnectionMethods;
import Clases.Descartable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author fede_
 */
public class ManagerDescartables {
     public ArrayList<Descartable> recuperarFilas() {
        Statement statement = null;
        String query = "SELECT * FROM 'DESCARTABLE'";
        ArrayList<Descartable> descartable = new ArrayList();
        Descartable d;
        try {
            statement = ConnectionMethods.getConection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);


            while (resultSet.next()) {
                d = new Descartable((resultSet.getString("D_Nombre")),(resultSet.getInt("Cantidad_Actual")),(resultSet.getInt("Cantidad_Minima")));
                descartable.add(d);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            ConnectionMethods.close(statement);
        }
        return descartable;
    }
     
}
