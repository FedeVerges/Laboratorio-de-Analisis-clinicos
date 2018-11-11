 
package Manager;

import Base_de_Datos.ConnectionMethods;
import Clases.Paciente;
import DAO_Sqlite.DAOPacientes;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author fede_
 */
public class ManagerPaciente {
    DAOPacientes daoPaciente = new DAOPacientes();
    

    public ArrayList<Paciente> recuperarFilas() {
        return daoPaciente.read();
    }

    public String recuperarObraSocial(int dni) {
        return daoPaciente.readObrasocialPaciente(dni);

    }

    public void cargarPaciente(String nombre, String apellido, int dni, Long telefono, String fNacimiento, int edad, String sexo) throws SQLException {
       Paciente p = daoPaciente.create();
       p.setNombre(nombre);
       p.setApellido(apellido);
       p.setDni(dni);
       p.setTelefono(telefono);
       p.setFechaNacimiento(fNacimiento);
       p.setEdad(edad);
       p.setSexo(sexo);
       daoPaciente.insert(p);
    }

    public void cargarObraSocialPaciente(String nombreObraSocial, int dni) {
        Paciente p = daoPaciente.create();
        p.setDni(dni);
        p.setObraSocial(nombreObraSocial);
        daoPaciente.insertObrasocial_Paciente(p);
    }

    public Paciente recupararPaciente(DefaultTableModel modelo, JTable tabla) {
        String nombre = (modelo.getValueAt(tabla.getSelectedRow(), 0).toString());
        String Apellido = (modelo.getValueAt(tabla.getSelectedRow(), 1).toString());
        int dni = ((Integer) (modelo.getValueAt(tabla.getSelectedRow(), 2)));
        Long telefono = (Long) (modelo.getValueAt(tabla.getSelectedRow(), 3));
        String fechaN = (modelo.getValueAt(tabla.getSelectedRow(), 4).toString());
        int edad = (Integer) (modelo.getValueAt(tabla.getSelectedRow(), 5));
        String sexo = (modelo.getValueAt(tabla.getSelectedRow(), 6).toString());
        Paciente p = new Paciente(nombre, Apellido, dni, telefono, fechaN, edad, sexo);
        return p;
    }

}
