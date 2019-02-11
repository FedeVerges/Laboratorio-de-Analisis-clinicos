/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacesGraficas;

import Clases.Analisis;
import Clases.Bioquimico;
import Clases.Descartable;
import Clases.Obra_Social;
import Clases.Orden;
import Clases.Paciente;
import Clases.Resultado;
import Manager.ManagerAnalisis;
import Manager.ManagerPaciente;
import Manager.Manager_Ordenes;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import sun.security.util.Password;

/**
 *
 * @author fede_
 */
public class Principal extends javax.swing.JFrame {

    private DefaultTableModel modeloTablaAnalisis;
    private DefaultTableModel modeloTablaAnalisisParaSeleccionar;
    private DefaultTableModel modeloTablaOrdenesPendientes;
    private DefaultTableModel modeloTablaOrdenesTerminadas;
    private DefaultTableModel modeloTablaAnalisisResultados;
    private DefaultTableModel modeloTablaAnalisisSelecionados;
    private DefaultTableModel modeloTablaListadoPacientes;

    private DefaultComboBoxModel modeloComboBox;
    private DefaultComboBoxModel modeloComboBoxNombreMedicos;
    

    private DefaultTableModel modeloTablaPaciente;
    private Manager_Ordenes managerOrdenes = new Manager_Ordenes();
    private int flag = 2;// se utiliza para indicar que se esta cargando un paciente nuevo.

    // datos //
    private ArrayList<Analisis> datosAnalisis;
    private ArrayList<Paciente> datosPaciente;
    private ArrayList<Bioquimico> bioquimicos;
    Bioquimico dueño1 = new Bioquimico("Federico", "Verges", 41221778, "20-41221778-1");
    Bioquimico dueño2 = new Bioquimico("Noelia", "Perez", 18400600, "22-18400600-8");
    
    

    // private HashMap<String, Password> usuarios;

    
    // managers //
    private ManagerAnalisis ma = new ManagerAnalisis();
    private ManagerPaciente mp = new ManagerPaciente();
    private Manager_Ordenes mo = new Manager_Ordenes();

    /**
     * Creates new form Principal
     */
    public Principal() {
        // usuarios = new HashMap<>();
        
        
        //Tabla emergente del boton

        // creo datos locales de analisis
        datosAnalisis = ma.recuperarFilas();
        datosPaciente = mp.recuperarFilas();
        bioquimicos = new ArrayList<>();
        bioquimicos.add(dueño1);
        bioquimicos.add(dueño2);

        /*
        usuarios.put(bioquimicos.get(0).getNombre(),"12345");
        usuarios.put(bioquimicos.get(1).getNombre(),"asdf");
         */
        initComponents();
        // Inicializar ComboBox con los Bioquimicos.
        // jComboBox_NombreBioquimicos.setSelectedIndex(0);
        String bio [] = new String[2];
        bio[0]= bioquimicos.get(0).getNombre();
        bio[1]= bioquimicos.get(1).getNombre();
        
        ComboBoxModel modelo_ComboBoxNombreMedicos = new DefaultComboBoxModel(bio);
        
         jComboBox_NombreBioquimicos.setModel(modelo_ComboBoxNombreMedicos);
        //Tablas pestaña Impimir Resultados:
        modeloTablaOrdenesTerminadas = (DefaultTableModel) jTable2_TablaOrdenesTerminadas.getModel();
        jTable2_TablaOrdenesTerminadas.setModel(modeloTablaOrdenesTerminadas);

        modeloComboBox = (DefaultComboBoxModel) jComboBox_ObraSocial.getModel();
        jComboBox_ObraSocial.setModel(modeloComboBox);
        
        // PESTAÑA CARGAR RESULTADOS
        
        //Tablas
        modeloTablaOrdenesPendientes = (DefaultTableModel) jTable2_TablaOrdenesPendientes.getModel();
        jTable2_TablaOrdenesPendientes.setModel(modeloTablaOrdenesPendientes);
        jTable3_TablaPestanaOrdenesPendientes.setModel(modeloTablaOrdenesPendientes);

        // Tabla Pacientes
        modeloTablaPaciente = (DefaultTableModel) jTable_ListadoPacientes.getModel();
        jTable_ListadoPacientes.setModel(modeloTablaPaciente);

        // Tabla analisis de los resultados
        modeloTablaAnalisisResultados = (DefaultTableModel) jTable4_cargarValoresAnalisis.getModel();
        jTable4_cargarValoresAnalisis.setModel(modeloTablaAnalisisResultados);

        //Pestaña cargar ordenes 
        // Tabla analisis seleccionados
        modeloTablaAnalisisSelecionados = (DefaultTableModel) jTable_AnalisisSeleccionados.getModel();
        jTable_AnalisisSeleccionados.setModel(modeloTablaAnalisisSelecionados);

        // tabla analisis para seleccionar.
        modeloTablaAnalisisParaSeleccionar = (DefaultTableModel) jTableAnalasisParaSeleccionar.getModel();
        jTableAnalasisParaSeleccionar.setModel(modeloTablaAnalisisParaSeleccionar);
        // tabla de analisis.
        modeloTablaAnalisis = (DefaultTableModel) jTable_Analisis.getModel();
        jTable_Analisis.setModel(modeloTablaAnalisis);

        // tabla Listado de Pacientes
        modeloTablaListadoPacientes = (DefaultTableModel) jTable_ListadoPacientes.getModel();
        jTable_ListadoPacientes.setModel(modeloTablaListadoPacientes);

        // Tomar siempre la fecha actual de JdateChooser
        cargarTablaAnalisis(datosAnalisis);
        cargarTablaAnalisisParaSeleccionar(datosAnalisis);
        cargarTablaOrdenesPendientes();
        //Tabla perteneciente a la pestaña "Mostrar Ordenes pendientes"
        cargarTablaOrdenesPendientes2();
        cargarTablaOrdenesTerminadas();
        cargarTablaListadoPacientes(datosPaciente);
        bloquear();

        jTabbedPane_menuPestañas.setEnabledAt(4, false);
    }

    // Metodo para cargar la tabla de los analisis que pueden ser seleccionados.
    public void cargarTablaAnalisisParaSeleccionar(ArrayList<Analisis> datos) {
        modeloTablaAnalisisParaSeleccionar.setRowCount(0);
        ManagerAnalisis ma = new ManagerAnalisis();
        int fila = 0;
        for (Analisis i : datos) {
            modeloTablaAnalisisParaSeleccionar.addRow(new Object[8]);
            jTableAnalasisParaSeleccionar.setValueAt(false, fila, 0);
            jTableAnalasisParaSeleccionar.setValueAt(i.getCodigo(), fila, 1);
            jTableAnalasisParaSeleccionar.setValueAt(i.getNombre(), fila, 2);
            jTableAnalasisParaSeleccionar.setValueAt(i.getIndicacionesPrevias(), fila, 3);
            jTableAnalasisParaSeleccionar.setValueAt(i.getCantidadUnidadesB(), fila, 4);
            jTableAnalasisParaSeleccionar.setValueAt(i.getConsentimiento(), fila, 5);
//            jTableAnalasisParaSeleccionar.setValueAt(i.getDescartable().getNombre(), fila, 6);
            jTableAnalasisParaSeleccionar.setValueAt(i.getValoresReferencia(), fila, 7);

            fila++;
        }
    }

    // metodo para bloquear los campos de texto de los datos del paciente.
    public void bloquear() {

        jTextField_Nombre.setEnabled(false);
        jTextField_Apellido.setEnabled(false);
        jTextField_dni.setEnabled(false);
        jTextField_Edad.setEnabled(false);
        jTextField_telefono.setEnabled(false);
        jDateChooser_fechaNacimiento.setEnabled(false);
        jRadioButton_femenino.setEnabled(false);
        jRadioButton_masculino.setEnabled(false);

    }

    // Metodo para listar los analisis por numero de orden.
    public void listarAnalisisPorOrden(int codigoOrden) {
        Manager_Ordenes ma = new Manager_Ordenes();
        ArrayList<Resultado> datos = ma.recuperarFilasResultados(codigoOrden);
        int fila = 0;
        for (Resultado i : datos) {
            modeloTablaAnalisis.addRow(new Object[3]);
            jTable4_cargarValoresAnalisis.setValueAt(i.getCodigoOrden(), fila, 0);
            jTable4_cargarValoresAnalisis.setValueAt(i.getNombreAnalisis(), fila, 1);
            jTable4_cargarValoresAnalisis.setValueAt("Ingrese valor", fila, 2);
            fila++;
        }
    }

    /*
     Metodo para cargar la tabla Ordendes terminadas de la pestana "Imprimir Resultados" 
     */
    public void cargarTablaOrdenesTerminadas() {
        modeloTablaOrdenesTerminadas.setRowCount(0);
        Manager_Ordenes mo = new Manager_Ordenes();
        ArrayList<Orden> datos = mo.recuperarFilasTerminadas();

        int fila = 0;
        for (Orden i : datos) {
            modeloTablaOrdenesTerminadas.addRow(new Object[7]);
            jTable2_TablaOrdenesTerminadas.setValueAt(i.getNumero(), fila, 0);
            jTable2_TablaOrdenesTerminadas.setValueAt(i.getFechaDeIngreso(), fila, 1);
            jTable2_TablaOrdenesTerminadas.setValueAt(i.getMedico(), fila, 2);
            jTable2_TablaOrdenesTerminadas.setValueAt(i.getPaciente().getDni(), fila, 3);
            jTable2_TablaOrdenesTerminadas.setValueAt(i.getPaciente().getNombre(), fila, 4);
            jTable2_TablaOrdenesTerminadas.setValueAt(i.getObraSocial().getNombre(), fila, 5);
            jTable2_TablaOrdenesTerminadas.setValueAt(i.getBioquimico().getNombre(), fila, 6);

            fila++;
        }
    }

    /* 
    Metodo para cargar la tabla de pestaña cargar resultados.
     */
    public void cargarTablaOrdenesPendientes() {
        modeloTablaOrdenesPendientes.setRowCount(0);
        Manager_Ordenes mo = new Manager_Ordenes();
        ArrayList<Orden> datos = mo.recuperarFilas();

        int fila = 0;
        for (Orden i : datos) {
            modeloTablaOrdenesPendientes.addRow(new Object[6]);
            jTable2_TablaOrdenesPendientes.setValueAt(i.getNumero(), fila, 0);
            jTable2_TablaOrdenesPendientes.setValueAt(i.getFechaDeIngreso(), fila, 1);
            jTable2_TablaOrdenesPendientes.setValueAt(i.getMedico(), fila, 2);
            jTable2_TablaOrdenesPendientes.setValueAt(i.getPaciente().getDni(), fila, 3);
            jTable2_TablaOrdenesPendientes.setValueAt(i.getPaciente().getNombre(), fila, 4);
            jTable2_TablaOrdenesPendientes.setValueAt(i.getObraSocial().getNombre(), fila, 5);

            fila++;
        }
    }

    /* 
    Metodo para cargar la tabla de pestaña ordenes pendientes.
     */
    public void cargarTablaOrdenesPendientes2() {
        Manager_Ordenes mo = new Manager_Ordenes();
        ArrayList<Orden> datos = mo.recuperarFilas();
        modeloTablaOrdenesPendientes.setRowCount(0);

        int fila = 0;
        for (Orden i : datos) {
            modeloTablaOrdenesPendientes.addRow(new Object[6]);
            jTable3_TablaPestanaOrdenesPendientes.setValueAt(i.getNumero(), fila, 0);
            jTable3_TablaPestanaOrdenesPendientes.setValueAt(i.getFechaDeIngreso(), fila, 1);
            jTable3_TablaPestanaOrdenesPendientes.setValueAt(i.getMedico(), fila, 2);
            jTable3_TablaPestanaOrdenesPendientes.setValueAt(i.getPaciente().getDni(), fila, 3);
            jTable3_TablaPestanaOrdenesPendientes.setValueAt(i.getPaciente().getNombre(), fila, 4);
            jTable3_TablaPestanaOrdenesPendientes.setValueAt(i.getObraSocial().getNombre(), fila, 5);

            fila++;
        }
    }

    public void cargarTablaAnalisisResultado(Integer codigoOrden) {
        Manager_Ordenes mo = new Manager_Ordenes();
        modeloTablaAnalisisResultados.setRowCount(0);

        ArrayList<Resultado> datos = mo.recuperarFilasResultados(codigoOrden);
        int fila = 0;
        for (Resultado i : datos) {
            modeloTablaAnalisisResultados.addRow(new Object[datos.size()]);
            jTable4_cargarValoresAnalisis.setValueAt(i.getCodigoOrden(), fila, 0);
            jTable4_cargarValoresAnalisis.setValueAt(i.getCodigoAnalisis(), fila, 1);
            jTable4_cargarValoresAnalisis.setValueAt(i.getNombreAnalisis(), fila, 2);
            jTable4_cargarValoresAnalisis.setValueAt("Ingrese valor", fila, 3);
            fila++;
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup_sexo = new javax.swing.ButtonGroup();
        jDialog_ListadoPacientes = new javax.swing.JDialog();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable_ListadoPacientes = new javax.swing.JTable();
        datosAnalisis_CargarOrden9 = new javax.swing.JLabel();
        jTextField_buscadorPacientes = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jDialog_ListadoAnalisis = new javax.swing.JDialog();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_Analisis = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jTextField_buscadorAnalisisBoton = new javax.swing.JTextField();
        jComboBox_buscadorAnalisisBoton = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton_eliminarAnalisis = new javax.swing.JButton();
        jDialog_Login = new javax.swing.JDialog();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField_Usuario = new javax.swing.JTextField();
        jTextField_contraseña = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jDialog_cargarTextosLargos = new javax.swing.JDialog();
        jScrollPane13 = new javax.swing.JScrollPane();
        jEditorPane_valoresReferencia = new javax.swing.JEditorPane();
        jScrollPane14 = new javax.swing.JScrollPane();
        jEditorPane_indicacionesPrevias = new javax.swing.JEditorPane();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jDialog_altaAnalisis = new javax.swing.JDialog();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField_codigoAnalisisNuevo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField_nombreAnalisisNuevo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField_uBAnalisisNuevo = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField_cantidadDescartable = new javax.swing.JTextField();
        jTextField_nombreDescartable = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jCheckBox_consentimiento = new javax.swing.JCheckBox();
        jButton_confirmarNuevoAnalisis = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton_abrirIndicaciones = new javax.swing.JButton();
        jButton_abrirValoresReferencia = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButton_Femenino = new javax.swing.JButton();
        jComboBox_NombreBioquimicos = new javax.swing.JComboBox<>();
        jTabbedPane_menuPestañas = new javax.swing.JTabbedPane();
        jPanel_CargarOrden = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        fecha_CargarOrden = new javax.swing.JLabel();
        Medico_cargarOrden = new javax.swing.JLabel();
        jTextField_Medico_CargarOrden = new javax.swing.JTextField();
        datosPaciente_CargarOrden = new javax.swing.JLabel();
        jButton_ListadoPaciente_CargarOrden = new javax.swing.JButton();
        jTextField_Nombre = new javax.swing.JTextField();
        jTextField_Apellido = new javax.swing.JTextField();
        jTextField_dni = new javax.swing.JTextField();
        jTextField_telefono = new javax.swing.JTextField();
        fecha_CargarOrden1 = new javax.swing.JLabel();
        fecha_CargarOrden2 = new javax.swing.JLabel();
        datosAnalisis_CargarOrden1 = new javax.swing.JLabel();
        jRadioButton_masculino = new javax.swing.JRadioButton();
        jRadioButton_femenino = new javax.swing.JRadioButton();
        jButton_CargarOrden = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea_cargarOrden = new javax.swing.JTextArea();
        jTextField_Edad = new javax.swing.JTextField();
        datosAnalisis_CargarOrden5 = new javax.swing.JLabel();
        jButton1CargarLista = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableAnalasisParaSeleccionar = new javax.swing.JTable();
        jButton_ObraSocial_CargarOrden = new javax.swing.JLabel();
        jComboBox_ObraSocial = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable_AnalisisSeleccionados = new javax.swing.JTable();
        jButton_ListadoPaciente_CargarOrden1 = new javax.swing.JButton();
        jTextField_buscadorAnalisis = new javax.swing.JTextField();
        jComboBox_buscador = new javax.swing.JComboBox<>();
        jDateChooserfechaIngreso = new com.toedter.calendar.JDateChooser();
        jDateChooser_fechaNacimiento = new com.toedter.calendar.JDateChooser();
        jButton_Analisis_CargarOrden1 = new javax.swing.JButton();
        jPanel2_cargarResultados = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2_TablaOrdenesPendientes = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea1_valoresDeReferencia = new javax.swing.JTextArea();
        datosAnalisis_CargarOrden2 = new javax.swing.JLabel();
        datosAnalisis_CargarOrden3 = new javax.swing.JLabel();
        datosAnalisis_CargarOrden4 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable4_cargarValoresAnalisis = new javax.swing.JTable();
        jButton2CargarResultado = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTextFieldResultadosBuscarOrden = new javax.swing.JTextField();
        jPanel2ListarOrdenesPendientes = new javax.swing.JPanel();
        datosAnalisis_CargarOrden6 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTable3_TablaPestanaOrdenesPendientes = new javax.swing.JTable();
        jPanel3ImprimirResultados = new javax.swing.JPanel();
        datosAnalisis_CargarOrden7 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable2_TablaOrdenesTerminadas = new javax.swing.JTable();
        jButton_imprimirResultado = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTextArea_VistaPrevia = new javax.swing.JTextArea();
        datosAnalisis_CargarOrden8 = new javax.swing.JLabel();
        jPanel_stock = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField_nombreStock = new javax.swing.JTextField();
        jLabel_nombreStock = new javax.swing.JLabel();
        jLabel_cantidadMinima = new javax.swing.JLabel();
        jLabel_cantidadActual = new javax.swing.JLabel();
        jButton_ActualzarStock = new javax.swing.JButton();
        jSpinner1 = new javax.swing.JSpinner();
        jSpinner2 = new javax.swing.JSpinner();
        jButton1 = new javax.swing.JButton();

        jTable_ListadoPacientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Apellido", "Dni", "Telefono", "Fecha de Nacimiento", "Edad", "Sexo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable_ListadoPacientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_ListadoPacientesMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(jTable_ListadoPacientes);

        datosAnalisis_CargarOrden9.setFont(new java.awt.Font("Ebrima", 3, 18)); // NOI18N
        datosAnalisis_CargarOrden9.setText("Listado Pacientes");

        jTextField_buscadorPacientes.setText("Ingrese aqui lo que desea buscar...");
        jTextField_buscadorPacientes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField_buscadorPacientesFocusGained(evt);
            }
        });
        jTextField_buscadorPacientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_buscadorPacientesActionPerformed(evt);
            }
        });
        jTextField_buscadorPacientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField_buscadorPacientesKeyPressed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 102, 102));
        jButton4.setText("Eliminar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog_ListadoPacientesLayout = new javax.swing.GroupLayout(jDialog_ListadoPacientes.getContentPane());
        jDialog_ListadoPacientes.getContentPane().setLayout(jDialog_ListadoPacientesLayout);
        jDialog_ListadoPacientesLayout.setHorizontalGroup(
            jDialog_ListadoPacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_ListadoPacientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog_ListadoPacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog_ListadoPacientesLayout.createSequentialGroup()
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 840, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialog_ListadoPacientesLayout.createSequentialGroup()
                        .addComponent(datosAnalisis_CargarOrden9, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_buscadorPacientes, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jDialog_ListadoPacientesLayout.setVerticalGroup(
            jDialog_ListadoPacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_ListadoPacientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog_ListadoPacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(datosAnalisis_CargarOrden9, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_buscadorPacientes, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog_ListadoPacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                    .addGroup(jDialog_ListadoPacientesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jDialog_ListadoAnalisis.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog_ListadoAnalisis.setLocation(new java.awt.Point(150, 0));
        jDialog_ListadoAnalisis.setResizable(false);
        jDialog_ListadoAnalisis.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable_Analisis.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jTable_Analisis.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre", "Indicaciones", "Unidades Bioquimicas", "Consentimiento", "Descartables", "Valores Referencia"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable_Analisis);

        jDialog_ListadoAnalisis.getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 53, 710, 382));

        jLabel2.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 1, 16)); // NOI18N
        jLabel2.setText("Analisis");
        jDialog_ListadoAnalisis.getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 11, 112, -1));

        jTextField_buscadorAnalisisBoton.setFont(new java.awt.Font("Microsoft New Tai Lue", 0, 14)); // NOI18N
        jTextField_buscadorAnalisisBoton.setText("ingrese el analisis que desea buscar");
        jTextField_buscadorAnalisisBoton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField_buscadorAnalisisBotonFocusGained(evt);
            }
        });
        jTextField_buscadorAnalisisBoton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField_buscadorAnalisisBotonKeyTyped(evt);
            }
        });
        jDialog_ListadoAnalisis.getContentPane().add(jTextField_buscadorAnalisisBoton, new org.netbeans.lib.awtextra.AbsoluteConstraints(116, 13, 390, -1));

        jComboBox_buscadorAnalisisBoton.setFont(new java.awt.Font("Microsoft New Tai Lue", 0, 14)); // NOI18N
        jComboBox_buscadorAnalisisBoton.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre", "Codigo" }));
        jDialog_ListadoAnalisis.getContentPane().add(jComboBox_buscadorAnalisisBoton, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 11, 120, 29));

        jButton2.setFont(new java.awt.Font("Microsoft New Tai Lue", 0, 14)); // NOI18N
        jButton2.setText("Nuevo");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jDialog_ListadoAnalisis.getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 50, 86, 30));

        jButton_eliminarAnalisis.setBackground(new java.awt.Color(255, 102, 102));
        jButton_eliminarAnalisis.setFont(new java.awt.Font("Microsoft New Tai Lue", 0, 14)); // NOI18N
        jButton_eliminarAnalisis.setText("Eliminar");
        jButton_eliminarAnalisis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_eliminarAnalisisActionPerformed(evt);
            }
        });
        jDialog_ListadoAnalisis.getContentPane().add(jButton_eliminarAnalisis, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 100, 86, 31));

        jDialog_Login.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        jLabel4.setText("Usuario");

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        jLabel5.setText("Contraseña");

        jTextField_Usuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_UsuarioActionPerformed(evt);
            }
        });

        jTextField_contraseña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_contraseñaActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(102, 255, 102));
        jButton6.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        jButton6.setText("Aceptar");

        jButton7.setBackground(new java.awt.Color(255, 102, 102));
        jButton7.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        jButton7.setText("Cancelar");

        javax.swing.GroupLayout jDialog_LoginLayout = new javax.swing.GroupLayout(jDialog_Login.getContentPane());
        jDialog_Login.getContentPane().setLayout(jDialog_LoginLayout);
        jDialog_LoginLayout.setHorizontalGroup(
            jDialog_LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_LoginLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog_LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jDialog_LoginLayout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7))
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField_Usuario)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField_contraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jDialog_LoginLayout.setVerticalGroup(
            jDialog_LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_LoginLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField_Usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField_contraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jDialog_LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton7))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jEditorPane_valoresReferencia.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jScrollPane13.setViewportView(jEditorPane_valoresReferencia);

        jEditorPane_indicacionesPrevias.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jScrollPane14.setViewportView(jEditorPane_indicacionesPrevias);

        jLabel14.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 1, 16)); // NOI18N
        jLabel14.setText("Valores de referencia");

        jLabel15.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 1, 16)); // NOI18N
        jLabel15.setText("Indicaciones Previas");

        jButton8.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 14)); // NOI18N
        jButton8.setText("Aceptar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog_cargarTextosLargosLayout = new javax.swing.GroupLayout(jDialog_cargarTextosLargos.getContentPane());
        jDialog_cargarTextosLargos.getContentPane().setLayout(jDialog_cargarTextosLargosLayout);
        jDialog_cargarTextosLargosLayout.setHorizontalGroup(
            jDialog_cargarTextosLargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_cargarTextosLargosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog_cargarTextosLargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jDialog_cargarTextosLargosLayout.createSequentialGroup()
                        .addGroup(jDialog_cargarTextosLargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDialog_cargarTextosLargosLayout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(223, 223, 223))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog_cargarTextosLargosLayout.createSequentialGroup()
                                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jDialog_cargarTextosLargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jDialog_cargarTextosLargosLayout.setVerticalGroup(
            jDialog_cargarTextosLargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_cargarTextosLargosLayout.createSequentialGroup()
                .addGroup(jDialog_cargarTextosLargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog_cargarTextosLargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei UI Light", 1, 16)); // NOI18N
        jLabel6.setText("Cargar Nuevo Analisis");

        jLabel7.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 1, 16)); // NOI18N
        jLabel7.setText("NBU");

        jTextField_codigoAnalisisNuevo.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jTextField_codigoAnalisisNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_codigoAnalisisNuevoActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 1, 16)); // NOI18N
        jLabel8.setText("Nombre");

        jTextField_nombreAnalisisNuevo.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 1, 16)); // NOI18N
        jLabel9.setText("Indicaciones");

        jTextField_uBAnalisisNuevo.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 1, 16)); // NOI18N
        jLabel10.setText("Unidades Bioquimicas");

        jLabel11.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 1, 16)); // NOI18N
        jLabel11.setText("Consentimiento");

        jLabel12.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 1, 16)); // NOI18N
        jLabel12.setText("Descartables");

        jTextField_cantidadDescartable.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jTextField_cantidadDescartable.setText("Cantidad");

        jTextField_nombreDescartable.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jTextField_nombreDescartable.setText("Nombre");

        jLabel13.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 1, 16)); // NOI18N
        jLabel13.setText("Valores Referencia");

        jCheckBox_consentimiento.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jCheckBox_consentimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_consentimientoActionPerformed(evt);
            }
        });

        jButton_confirmarNuevoAnalisis.setBackground(new java.awt.Color(102, 255, 102));
        jButton_confirmarNuevoAnalisis.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jButton_confirmarNuevoAnalisis.setText("Confirmar");
        jButton_confirmarNuevoAnalisis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_confirmarNuevoAnalisisActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(255, 102, 102));
        jButton9.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jButton9.setText("Cancelar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton_abrirIndicaciones.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jButton_abrirIndicaciones.setText("Abrir");
        jButton_abrirIndicaciones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton_abrirIndicaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_abrirIndicacionesActionPerformed(evt);
            }
        });

        jButton_abrirValoresReferencia.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jButton_abrirValoresReferencia.setText("Abrir");
        jButton_abrirValoresReferencia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton_abrirValoresReferencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_abrirValoresReferenciaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog_altaAnalisisLayout = new javax.swing.GroupLayout(jDialog_altaAnalisis.getContentPane());
        jDialog_altaAnalisis.getContentPane().setLayout(jDialog_altaAnalisisLayout);
        jDialog_altaAnalisisLayout.setHorizontalGroup(
            jDialog_altaAnalisisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_altaAnalisisLayout.createSequentialGroup()
                .addGroup(jDialog_altaAnalisisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog_altaAnalisisLayout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jDialog_altaAnalisisLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jDialog_altaAnalisisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog_altaAnalisisLayout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(jButton_abrirIndicaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jDialog_altaAnalisisLayout.createSequentialGroup()
                                .addComponent(jButton_confirmarNuevoAnalisis, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jDialog_altaAnalisisLayout.createSequentialGroup()
                                .addGroup(jDialog_altaAnalisisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(91, 91, 91)
                                .addGroup(jDialog_altaAnalisisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField_nombreAnalisisNuevo)
                                    .addComponent(jTextField_codigoAnalisisNuevo)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog_altaAnalisisLayout.createSequentialGroup()
                                .addGroup(jDialog_altaAnalisisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(36, 36, 36)
                                .addGroup(jDialog_altaAnalisisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jDialog_altaAnalisisLayout.createSequentialGroup()
                                        .addComponent(jTextField_nombreDescartable, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField_cantidadDescartable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jDialog_altaAnalisisLayout.createSequentialGroup()
                                        .addComponent(jButton_abrirValoresReferencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(4, 4, 4))))))
                    .addGroup(jDialog_altaAnalisisLayout.createSequentialGroup()
                        .addGroup(jDialog_altaAnalisisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jDialog_altaAnalisisLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jDialog_altaAnalisisLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jDialog_altaAnalisisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField_uBAnalisisNuevo)
                            .addGroup(jDialog_altaAnalisisLayout.createSequentialGroup()
                                .addComponent(jCheckBox_consentimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(62, 62, 62))
        );
        jDialog_altaAnalisisLayout.setVerticalGroup(
            jDialog_altaAnalisisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_altaAnalisisLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel6)
                .addGroup(jDialog_altaAnalisisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog_altaAnalisisLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialog_altaAnalisisLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jTextField_codigoAnalisisNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField_nombreAnalisisNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jDialog_altaAnalisisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_abrirIndicaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jDialog_altaAnalisisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog_altaAnalisisLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog_altaAnalisisLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_uBAnalisisNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(jDialog_altaAnalisisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox_consentimiento))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialog_altaAnalisisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_nombreDescartable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_cantidadDescartable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialog_altaAnalisisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_abrirValoresReferencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addGroup(jDialog_altaAnalisisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_confirmarNuevoAnalisis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Laboratorio de Analisis Bioquimicos");
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(0, 0, 204));
        setForeground(new java.awt.Color(204, 204, 204));
        setLocation(new java.awt.Point(50, 0));
        setName("Principal"); // NOI18N

        jPanel4.setBackground(new java.awt.Color(240, 235, 235));

        jButton_Femenino.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jButton_Femenino.setText("Iniciar Sesion");
        jButton_Femenino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_FemeninoActionPerformed(evt);
            }
        });

        jComboBox_NombreBioquimicos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_NombreBioquimicos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_NombreBioquimicosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jButton_Femenino)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jComboBox_NombreBioquimicos, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton_Femenino, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox_NombreBioquimicos, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jTabbedPane_menuPestañas.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane_menuPestañas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 3));
        jTabbedPane_menuPestañas.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 2, 14)); // NOI18N
        jTabbedPane_menuPestañas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane_menuPestañasMouseClicked(evt);
            }
        });

        jPanel_CargarOrden.setBackground(new java.awt.Color(240, 235, 235));
        jPanel_CargarOrden.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 16)); // NOI18N
        jLabel1.setText("Ingrese los datos de la Orden");

        fecha_CargarOrden.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        fecha_CargarOrden.setText("Fecha");

        Medico_cargarOrden.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        Medico_cargarOrden.setText("Medico");

        jTextField_Medico_CargarOrden.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jTextField_Medico_CargarOrden.setText("ingrese el nombre del medico");
        jTextField_Medico_CargarOrden.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField_Medico_CargarOrden.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        jTextField_Medico_CargarOrden.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField_Medico_CargarOrdenFocusGained(evt);
            }
        });

        datosPaciente_CargarOrden.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 16)); // NOI18N
        datosPaciente_CargarOrden.setText("Datos Paciente");

        jButton_ListadoPaciente_CargarOrden.setBackground(new java.awt.Color(150, 177, 255));
        jButton_ListadoPaciente_CargarOrden.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jButton_ListadoPaciente_CargarOrden.setText("Pacientes");
        jButton_ListadoPaciente_CargarOrden.setBorder(null);
        jButton_ListadoPaciente_CargarOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ListadoPaciente_CargarOrdenActionPerformed(evt);
            }
        });

        jTextField_Nombre.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jTextField_Nombre.setText("ingrese el nombre");
        jTextField_Nombre.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField_Nombre.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        jTextField_Nombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField_NombreFocusGained(evt);
            }
        });
        jTextField_Nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField_NombreKeyTyped(evt);
            }
        });

        jTextField_Apellido.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jTextField_Apellido.setText("ingrese el apellido");
        jTextField_Apellido.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField_Apellido.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        jTextField_Apellido.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField_ApellidoFocusGained(evt);
            }
        });
        jTextField_Apellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_ApellidoActionPerformed(evt);
            }
        });

        jTextField_dni.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jTextField_dni.setText("ingrese el dni");
        jTextField_dni.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField_dni.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        jTextField_dni.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField_dniFocusGained(evt);
            }
        });
        jTextField_dni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_dniActionPerformed(evt);
            }
        });

        jTextField_telefono.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jTextField_telefono.setText("ingrese el telefono");
        jTextField_telefono.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField_telefono.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        jTextField_telefono.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField_telefonoFocusGained(evt);
            }
        });

        fecha_CargarOrden1.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        fecha_CargarOrden1.setText("Fecha de Nacimieto");

        fecha_CargarOrden2.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        fecha_CargarOrden2.setText("Sexo");

        datosAnalisis_CargarOrden1.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 16)); // NOI18N
        datosAnalisis_CargarOrden1.setText("Análisis Seleccionados");

        jRadioButton_masculino.setBackground(new java.awt.Color(204, 204, 204));
        buttonGroup_sexo.add(jRadioButton_masculino);
        jRadioButton_masculino.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jRadioButton_masculino.setText("Masculino");
        jRadioButton_masculino.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRadioButton_masculino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_masculinoActionPerformed(evt);
            }
        });

        jRadioButton_femenino.setBackground(new java.awt.Color(204, 204, 204));
        buttonGroup_sexo.add(jRadioButton_femenino);
        jRadioButton_femenino.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jRadioButton_femenino.setText("Femenino");
        jRadioButton_femenino.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton_CargarOrden.setBackground(new java.awt.Color(150, 177, 255));
        jButton_CargarOrden.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jButton_CargarOrden.setText("Cargar Orden");
        jButton_CargarOrden.setBorder(null);
        jButton_CargarOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CargarOrdenActionPerformed(evt);
            }
        });

        jTextArea_cargarOrden.setColumns(20);
        jTextArea_cargarOrden.setRows(5);
        jTextArea_cargarOrden.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane4.setViewportView(jTextArea_cargarOrden);

        jTextField_Edad.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jTextField_Edad.setText("ingrese la edad");
        jTextField_Edad.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField_Edad.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        jTextField_Edad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField_EdadFocusGained(evt);
            }
        });

        datosAnalisis_CargarOrden5.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 16)); // NOI18N
        datosAnalisis_CargarOrden5.setText("Listado Análisis");

        jButton1CargarLista.setBackground(new java.awt.Color(150, 177, 255));
        jButton1CargarLista.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jButton1CargarLista.setText("Cargar Analisis ");
        jButton1CargarLista.setBorder(null);
        jButton1CargarLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1CargarListaActionPerformed(evt);
            }
        });

        jTableAnalasisParaSeleccionar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTableAnalasisParaSeleccionar.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jTableAnalasisParaSeleccionar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Seleccione", "Codigo", "Nombre", "Indicaciones ", "Unidades Bioquimicas", "Consentimiento", "Descartables", "Valores Referencia"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableAnalasisParaSeleccionar.setRowHeight(20);
        jTableAnalasisParaSeleccionar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableAnalasisParaSeleccionarMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableAnalasisParaSeleccionar);

        jButton_ObraSocial_CargarOrden.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jButton_ObraSocial_CargarOrden.setText("Obra Social");

        jComboBox_ObraSocial.setBackground(new java.awt.Color(204, 204, 255));
        jComboBox_ObraSocial.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jComboBox_ObraSocial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Swiss Medical", "Femesa", "Osde", "Sancor Salud", "Particular" }));
        jComboBox_ObraSocial.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jComboBox_ObraSocial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_ObraSocialActionPerformed(evt);
            }
        });

        jTable_AnalisisSeleccionados.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable_AnalisisSeleccionados.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jTable_AnalisisSeleccionados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable_AnalisisSeleccionados.setRowHeight(20);
        jTable_AnalisisSeleccionados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_AnalisisSeleccionadosMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(jTable_AnalisisSeleccionados);

        jButton_ListadoPaciente_CargarOrden1.setBackground(new java.awt.Color(150, 177, 255));
        jButton_ListadoPaciente_CargarOrden1.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jButton_ListadoPaciente_CargarOrden1.setText("Cargar Nuevo");
        jButton_ListadoPaciente_CargarOrden1.setBorder(null);
        jButton_ListadoPaciente_CargarOrden1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ListadoPaciente_CargarOrden1ActionPerformed(evt);
            }
        });

        jTextField_buscadorAnalisis.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jTextField_buscadorAnalisis.setText("Ingrese aqui lo que desea buscar...");
        jTextField_buscadorAnalisis.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField_buscadorAnalisis.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        jTextField_buscadorAnalisis.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField_buscadorAnalisisFocusGained(evt);
            }
        });
        jTextField_buscadorAnalisis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_buscadorAnalisisActionPerformed(evt);
            }
        });
        jTextField_buscadorAnalisis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField_buscadorAnalisisKeyPressed(evt);
            }
        });

        jComboBox_buscador.setBackground(new java.awt.Color(204, 204, 255));
        jComboBox_buscador.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jComboBox_buscador.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre", "Codigo" }));
        jComboBox_buscador.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jComboBox_buscador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_buscadorActionPerformed(evt);
            }
        });

        jDateChooserfechaIngreso.setBackground(new java.awt.Color(204, 204, 204));

        jDateChooser_fechaNacimiento.setBackground(new java.awt.Color(153, 153, 153));

        jButton_Analisis_CargarOrden1.setBackground(new java.awt.Color(150, 177, 255));
        jButton_Analisis_CargarOrden1.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jButton_Analisis_CargarOrden1.setText("Análisis");
        jButton_Analisis_CargarOrden1.setBorder(null);
        jButton_Analisis_CargarOrden1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Analisis_CargarOrden1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_CargarOrdenLayout = new javax.swing.GroupLayout(jPanel_CargarOrden);
        jPanel_CargarOrden.setLayout(jPanel_CargarOrdenLayout);
        jPanel_CargarOrdenLayout.setHorizontalGroup(
            jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_CargarOrdenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField_Nombre)
                    .addComponent(jTextField_Apellido)
                    .addComponent(jTextField_dni)
                    .addGroup(jPanel_CargarOrdenLayout.createSequentialGroup()
                        .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Medico_cargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fecha_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField_Medico_CargarOrden)
                            .addGroup(jPanel_CargarOrdenLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jDateChooserfechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jTextField_telefono)
                    .addComponent(jTextField_Edad)
                    .addGroup(jPanel_CargarOrdenLayout.createSequentialGroup()
                        .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton_ObraSocial_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fecha_CargarOrden1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser_fechaNacimiento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox_ObraSocial, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel_CargarOrdenLayout.createSequentialGroup()
                        .addComponent(fecha_CargarOrden2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jRadioButton_masculino)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton_femenino))
                    .addGroup(jPanel_CargarOrdenLayout.createSequentialGroup()
                        .addComponent(datosPaciente_CargarOrden, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_ListadoPaciente_CargarOrden1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton_ListadoPaciente_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel_CargarOrdenLayout.createSequentialGroup()
                        .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel_CargarOrdenLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(datosAnalisis_CargarOrden5, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_buscadorAnalisis, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_buscador, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1CargarLista, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton_Analisis_CargarOrden1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_CargarOrdenLayout.createSequentialGroup()
                        .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(datosAnalisis_CargarOrden1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_CargarOrdenLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane4))))
                .addContainerGap())
        );
        jPanel_CargarOrdenLayout.setVerticalGroup(
            jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel_CargarOrdenLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_CargarOrdenLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(12, 12, 12)
                        .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fecha_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateChooserfechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Medico_cargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel_CargarOrdenLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jTextField_Medico_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(datosPaciente_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_ListadoPaciente_CargarOrden1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_ListadoPaciente_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField_Apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_dni, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_Edad, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(fecha_CargarOrden1, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                            .addComponent(jDateChooser_fechaNacimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fecha_CargarOrden2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton_masculino)
                            .addComponent(jRadioButton_femenino))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_ObraSocial_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox_ObraSocial, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel_CargarOrdenLayout.createSequentialGroup()
                        .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField_buscadorAnalisis, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox_buscador, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(datosAnalisis_CargarOrden5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1CargarLista, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_Analisis_CargarOrden1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(datosAnalisis_CargarOrden1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_CargarOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_CargarOrdenLayout.createSequentialGroup()
                                .addComponent(jScrollPane4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)))
                .addGap(11, 11, 11))
        );

        jTabbedPane_menuPestañas.addTab("Cargar Orden", jPanel_CargarOrden);

        jPanel2_cargarResultados.setBackground(new java.awt.Color(240, 235, 235));

        jTable2_TablaOrdenesPendientes.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jTable2_TablaOrdenesPendientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numero de Orden", "Fecha de Ingreso", "Medico", "DNI Paciente", "Paciente", "Obra Social"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2_TablaOrdenesPendientes.setRowHeight(20);
        jTable2_TablaOrdenesPendientes.setSelectionBackground(new java.awt.Color(153, 153, 153));
        jTable2_TablaOrdenesPendientes.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jTable2_TablaOrdenesPendientes.getTableHeader().setReorderingAllowed(false);
        jTable2_TablaOrdenesPendientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2_TablaOrdenesPendientesMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable2_TablaOrdenesPendientes);

        jTextArea1_valoresDeReferencia.setEditable(false);
        jTextArea1_valoresDeReferencia.setColumns(10);
        jTextArea1_valoresDeReferencia.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jTextArea1_valoresDeReferencia.setRows(5);
        jTextArea1_valoresDeReferencia.setAutoscrolls(false);
        jScrollPane5.setViewportView(jTextArea1_valoresDeReferencia);

        datosAnalisis_CargarOrden2.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 16)); // NOI18N
        datosAnalisis_CargarOrden2.setText("Seleccione una orden sin terminar de la lista:");

        datosAnalisis_CargarOrden3.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 16)); // NOI18N
        datosAnalisis_CargarOrden3.setText("Valores de referencia:");

        datosAnalisis_CargarOrden4.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 16)); // NOI18N
        datosAnalisis_CargarOrden4.setText("Cargue los valores :");

        jTable4_cargarValoresAnalisis.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jTable4_cargarValoresAnalisis.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numero de Orden", "Codigo de Analisis", "Nombre de Analisis", "Resultado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4_cargarValoresAnalisis.setRowHeight(20);
        jTable4_cargarValoresAnalisis.setSelectionBackground(new java.awt.Color(255, 102, 102));
        jTable4_cargarValoresAnalisis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4_cargarValoresAnalisisMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTable4_cargarValoresAnalisis);

        jButton2CargarResultado.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jButton2CargarResultado.setText("Cargar resultado");
        jButton2CargarResultado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2CargarResultadoActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jButton3.setText("Limpiar tabla");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTextFieldResultadosBuscarOrden.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jTextFieldResultadosBuscarOrden.setForeground(new java.awt.Color(0, 0, 51));
        jTextFieldResultadosBuscarOrden.setText("Ingrese el nombre del paciente a buscar:");
        jTextFieldResultadosBuscarOrden.setToolTipText("Ingrese el nombre del paciente a buscar");
        jTextFieldResultadosBuscarOrden.setInheritsPopupMenu(true);
        jTextFieldResultadosBuscarOrden.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldResultadosBuscarOrdenMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTextFieldResultadosBuscarOrdenMouseEntered(evt);
            }
        });
        jTextFieldResultadosBuscarOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldResultadosBuscarOrdenActionPerformed(evt);
            }
        });
        jTextFieldResultadosBuscarOrden.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTextFieldResultadosBuscarOrdenPropertyChange(evt);
            }
        });
        jTextFieldResultadosBuscarOrden.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldResultadosBuscarOrdenKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2_cargarResultadosLayout = new javax.swing.GroupLayout(jPanel2_cargarResultados);
        jPanel2_cargarResultados.setLayout(jPanel2_cargarResultadosLayout);
        jPanel2_cargarResultadosLayout.setHorizontalGroup(
            jPanel2_cargarResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2_cargarResultadosLayout.createSequentialGroup()
                .addGroup(jPanel2_cargarResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2_cargarResultadosLayout.createSequentialGroup()
                        .addComponent(datosAnalisis_CargarOrden4, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2CargarResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1245, Short.MAX_VALUE)
                    .addComponent(jScrollPane6)
                    .addGroup(jPanel2_cargarResultadosLayout.createSequentialGroup()
                        .addComponent(datosAnalisis_CargarOrden2, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldResultadosBuscarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2_cargarResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(datosAnalisis_CargarOrden3, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel2_cargarResultadosLayout.setVerticalGroup(
            jPanel2_cargarResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2_cargarResultadosLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2_cargarResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(datosAnalisis_CargarOrden2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datosAnalisis_CargarOrden3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldResultadosBuscarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2_cargarResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2_cargarResultadosLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2_cargarResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(datosAnalisis_CargarOrden4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2CargarResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE))
                    .addComponent(jScrollPane5))
                .addGap(113, 113, 113))
        );

        jTextFieldResultadosBuscarOrden.getAccessibleContext().setAccessibleName("");

        jTabbedPane_menuPestañas.addTab("Cargar Resultados", jPanel2_cargarResultados);

        jPanel2ListarOrdenesPendientes.setBackground(new java.awt.Color(240, 235, 235));

        datosAnalisis_CargarOrden6.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 1, 16)); // NOI18N
        datosAnalisis_CargarOrden6.setText("Listado de ordenes pendientes:");

        jTable3_TablaPestanaOrdenesPendientes.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jTable3_TablaPestanaOrdenesPendientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numero de Orden", "Fecha de Ingreso", "Medico", "DNI Paciente", "Paciente", "Obra Social"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3_TablaPestanaOrdenesPendientes.getTableHeader().setReorderingAllowed(false);
        jTable3_TablaPestanaOrdenesPendientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3_TablaPestanaOrdenesPendientesMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(jTable3_TablaPestanaOrdenesPendientes);

        javax.swing.GroupLayout jPanel2ListarOrdenesPendientesLayout = new javax.swing.GroupLayout(jPanel2ListarOrdenesPendientes);
        jPanel2ListarOrdenesPendientes.setLayout(jPanel2ListarOrdenesPendientesLayout);
        jPanel2ListarOrdenesPendientesLayout.setHorizontalGroup(
            jPanel2ListarOrdenesPendientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2ListarOrdenesPendientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(datosAnalisis_CargarOrden6, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1173, Short.MAX_VALUE))
            .addGroup(jPanel2ListarOrdenesPendientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2ListarOrdenesPendientesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 1548, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel2ListarOrdenesPendientesLayout.setVerticalGroup(
            jPanel2ListarOrdenesPendientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2ListarOrdenesPendientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(datosAnalisis_CargarOrden6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(663, Short.MAX_VALUE))
            .addGroup(jPanel2ListarOrdenesPendientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2ListarOrdenesPendientesLayout.createSequentialGroup()
                    .addGap(60, 60, 60)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(51, Short.MAX_VALUE)))
        );

        jTabbedPane_menuPestañas.addTab("Listar Ordenes Pendientes", jPanel2ListarOrdenesPendientes);

        jPanel3ImprimirResultados.setBackground(new java.awt.Color(240, 235, 235));

        datosAnalisis_CargarOrden7.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 1, 16)); // NOI18N
        datosAnalisis_CargarOrden7.setText("Listado de ordenes terminadas:");

        jTable2_TablaOrdenesTerminadas.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jTable2_TablaOrdenesTerminadas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numero de Orden", "Fecha de Ingreso", "Medico", "DNI Paciente", "Paciente", "Obra Social", "Biquimico"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2_TablaOrdenesTerminadas.setRowHeight(20);
        jTable2_TablaOrdenesTerminadas.getTableHeader().setReorderingAllowed(false);
        jTable2_TablaOrdenesTerminadas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2_TablaOrdenesTerminadasMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(jTable2_TablaOrdenesTerminadas);

        jButton_imprimirResultado.setFont(new java.awt.Font("Microsoft YaHei UI Light", 0, 14)); // NOI18N
        jButton_imprimirResultado.setText("Imprimir Resultado");
        jButton_imprimirResultado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_imprimirResultadoActionPerformed(evt);
            }
        });

        jTextArea_VistaPrevia.setColumns(20);
        jTextArea_VistaPrevia.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        jTextArea_VistaPrevia.setRows(5);
        jScrollPane11.setViewportView(jTextArea_VistaPrevia);

        datosAnalisis_CargarOrden8.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 1, 16)); // NOI18N
        datosAnalisis_CargarOrden8.setText("Vista Previa");

        javax.swing.GroupLayout jPanel3ImprimirResultadosLayout = new javax.swing.GroupLayout(jPanel3ImprimirResultados);
        jPanel3ImprimirResultados.setLayout(jPanel3ImprimirResultadosLayout);
        jPanel3ImprimirResultadosLayout.setHorizontalGroup(
            jPanel3ImprimirResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3ImprimirResultadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3ImprimirResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3ImprimirResultadosLayout.createSequentialGroup()
                        .addGroup(jPanel3ImprimirResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3ImprimirResultadosLayout.createSequentialGroup()
                                .addComponent(datosAnalisis_CargarOrden7, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 753, Short.MAX_VALUE))
                            .addComponent(jScrollPane10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3ImprimirResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(datosAnalisis_CargarOrden8, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3ImprimirResultadosLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton_imprimirResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3ImprimirResultadosLayout.setVerticalGroup(
            jPanel3ImprimirResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3ImprimirResultadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3ImprimirResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(datosAnalisis_CargarOrden7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datosAnalisis_CargarOrden8, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3ImprimirResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                    .addComponent(jScrollPane11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_imprimirResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111))
        );

        jTabbedPane_menuPestañas.addTab("Imprimir Resultados", jPanel3ImprimirResultados);

        jPanel_stock.setBackground(new java.awt.Color(240, 235, 235));

        jLabel3.setFont(new java.awt.Font("Ebrima", 3, 18)); // NOI18N
        jLabel3.setText("Stock");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Cantidad Actual"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(jTable1);

        jTextField_nombreStock.setText("Ingrese el nombre del producto");

        jLabel_nombreStock.setFont(new java.awt.Font("Ebrima", 2, 14)); // NOI18N
        jLabel_nombreStock.setText("Nombre :");

        jLabel_cantidadMinima.setFont(new java.awt.Font("Ebrima", 2, 14)); // NOI18N
        jLabel_cantidadMinima.setText("Cantidad Minima :");

        jLabel_cantidadActual.setFont(new java.awt.Font("Ebrima", 2, 14)); // NOI18N
        jLabel_cantidadActual.setText("Cantidad Actual:");

        jButton_ActualzarStock.setFont(new java.awt.Font("Ebrima", 0, 14)); // NOI18N
        jButton_ActualzarStock.setText("Actualizar Stock");

        jButton1.setFont(new java.awt.Font("Ebrima", 0, 14)); // NOI18N
        jButton1.setText("Nuevo Producto");

        javax.swing.GroupLayout jPanel_stockLayout = new javax.swing.GroupLayout(jPanel_stock);
        jPanel_stock.setLayout(jPanel_stockLayout);
        jPanel_stockLayout.setHorizontalGroup(
            jPanel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_stockLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_stockLayout.createSequentialGroup()
                        .addComponent(jLabel_nombreStock, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                        .addGap(268, 268, 268))
                    .addGroup(jPanel_stockLayout.createSequentialGroup()
                        .addGroup(jPanel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel_stockLayout.createSequentialGroup()
                                .addComponent(jButton_ActualzarStock)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1))
                            .addGroup(jPanel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSpinner1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel_cantidadActual, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSpinner2, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel_cantidadMinima, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
                            .addComponent(jTextField_nombreStock, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 743, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82))
        );
        jPanel_stockLayout.setVerticalGroup(
            jPanel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_stockLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel_stockLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel_nombreStock, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_nombreStock, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel_cantidadActual, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel_cantidadMinima, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addGroup(jPanel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_ActualzarStock, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(262, Short.MAX_VALUE))
        );

        jTabbedPane_menuPestañas.addTab("Stock", jPanel_stock);

        getContentPane().add(jTabbedPane_menuPestañas, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_Analisis_CargarOrden1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Analisis_CargarOrden1ActionPerformed
        jDialog_ListadoAnalisis.setVisible(true);
        jDialog_ListadoAnalisis.setLocationRelativeTo(jTextField_Apellido);
        jDialog_ListadoAnalisis.setLocationRelativeTo(datosAnalisis_CargarOrden5);
        jDialog_ListadoAnalisis.setSize(850, 500);

    }//GEN-LAST:event_jButton_Analisis_CargarOrden1ActionPerformed

    // metodo para cargar la tabla de analisis que deben ser seleccionados.
    public void cargarTablaAnalisis(ArrayList<Analisis> a) {
        modeloTablaAnalisis.setRowCount(0);
        int fila = 0;
        for (Analisis i : a) {
            modeloTablaAnalisis.addRow(new Object[7]);
            jTable_Analisis.setValueAt(i.getCodigo(), fila, 0);
            jTable_Analisis.setValueAt(i.getNombre(), fila, 1);
            jTable_Analisis.setValueAt(i.getIndicacionesPrevias(), fila, 2);
            jTable_Analisis.setValueAt(i.getCantidadUnidadesB(), fila, 3);
            jTable_Analisis.setValueAt(i.getConsentimiento(), fila, 4);
//            jTable_Analisis.setValueAt(i.getDescartable().getNombre(), fila, 5);
            jTable_Analisis.setValueAt(i.getValoresReferencia(), fila, 6);
            fila++;
        }
    }

    private void jTable2_TablaOrdenesPendientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2_TablaOrdenesPendientesMouseClicked
        Integer codigoOrden = Integer.parseInt(String.valueOf(modeloTablaOrdenesPendientes.getValueAt(jTable2_TablaOrdenesPendientes.getSelectedRow(), 0)));
        System.out.println("Recibi el codigo de la orden numero: " + codigoOrden);
        modeloTablaAnalisisResultados.setRowCount(0);
        cargarTablaAnalisisResultado(codigoOrden);

    }//GEN-LAST:event_jTable2_TablaOrdenesPendientesMouseClicked

    private void jButton2CargarResultadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2CargarResultadoActionPerformed
         // String nombreBio = bioquimicos.get(0).getNombre() + bioquimicos.get(0).getApellido();
         String nombreBio = (String) jComboBox_NombreBioquimicos.getSelectedItem();
        
        Manager_Ordenes mo = new Manager_Ordenes();
        Boolean control = true;
        ArrayList<Resultado> lista = null;
        try {
            lista = mo.recuperarResultadosDeTabla(modeloTablaAnalisisResultados, jTable4_cargarValoresAnalisis, nombreBio);
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Resultado r : lista) {
            if (r.getValorTomado().equalsIgnoreCase("Ingrese valor") || r.getValorTomado().equals("")) {
                control = false;
            } else if (control) {
                mo.actualizarResultado(r.getCodigoOrden(), r.getCodigoAnalisis(), r.getNombreAnalisis(), r.getValorTomado());
                
                 // Obtener el Bioquimico.
                 
                if(nombreBio == "Fede"){
                    mo.finalizarOrden(r.getCodigoOrden(), bioquimicos.get(0));
                }else if (nombreBio == "Noelia"){
                    mo.finalizarOrden(r.getCodigoOrden(), bioquimicos.get(1));
                }
                    
                   }
        }
        if (control == false) {
            JOptionPane.showMessageDialog(null, "No se pueden cargar resultados vacios o con campos invalidos");
        } else {
            JOptionPane.showMessageDialog(null, "La orden ha sido finalizada");
        }


    }//GEN-LAST:event_jButton2CargarResultadoActionPerformed
    public void cargarTablaAnalsisSeleccionados(ArrayList<Analisis> lista) {

        modeloTablaAnalisisSelecionados.setRowCount(0);

        int fila = 0;
        for (Analisis a : lista) {
            modeloTablaAnalisisSelecionados.addRow(new Object[2]);
            jTable_AnalisisSeleccionados.setValueAt(a.getCodigo(), fila, 0);
            jTable_AnalisisSeleccionados.setValueAt(a.getNombre(), fila, 1);
            System.out.println("El codigo de la tablita es: " + a.getCodigo());
            System.out.println("EL nombre de tablita es: " + a.getNombre());
            fila++;

        }

    }

    public Boolean ValidarCampos() {

        Boolean control = true;
        ManagerPaciente mp = new ManagerPaciente();
        ArrayList<Paciente> pacientes = mp.recuperarFilas();

        // Datos Ordenes
        String fechaIngreso = dameFecha(jDateChooserfechaIngreso.getDate());
        String nombreMedico = jTextField_Medico_CargarOrden.getText();

        // Datos Paciente
        String nombre = jTextField_Nombre.getText();
        String apellido = jTextField_Apellido.getText();
        int dni = 0;
        Long telefono = null;
        String fNacimiento; // se pasa la fecha a formato String
        int edad = 0;
        String sexo;
        Date fecha = jDateChooser_fechaNacimiento.getDate();

        fNacimiento = dameFecha(fecha);
        
        // control para que no pase la fecha actual.
        
        Calendar fecha_actual = new GregorianCalendar();
        int dia = fecha_actual.get(Calendar.DATE);
        int mes = fecha_actual.get(Calendar.MONTH);
        int anio = fecha_actual.get(Calendar.YEAR);
        
        int dia_ingreso_control = jDateChooserfechaIngreso.getCalendar().get(Calendar.DATE);
        int mes_ingreso_control = jDateChooserfechaIngreso.getCalendar().get(Calendar.MONTH);
        int anio_ingreso_control = jDateChooserfechaIngreso.getCalendar().get(Calendar.YEAR);       

        // Control fecha ingreso vacia
        if (control) {
            jDateChooserfechaIngreso.setBorder(new LineBorder(Color.gray));
            if (fechaIngreso == null) {
                jDateChooserfechaIngreso.setForeground(Color.red);
                jDateChooserfechaIngreso.setBorder(new LineBorder(Color.red));
                control = false;
            }
            if(dia_ingreso_control > dia || mes_ingreso_control >mes || anio_ingreso_control > anio){
                jDateChooserfechaIngreso.setBorder(new LineBorder(Color.red));
                control = false;
            }
            
        }

        // Control nombre del medico 
        if (control) {
            jTextField_Medico_CargarOrden.setBackground(Color.white);
            String regex = "^[a-zA-Z][a-zA-Z\\s]*$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(nombreMedico);
            if (!matcher.matches()) {
                jTextField_Medico_CargarOrden.setBackground(Color.red);
                control = false;
            }
            if (nombreMedico.equalsIgnoreCase("")) {
                jTextField_Medico_CargarOrden.setBackground(Color.red);
                control = false;
            }
        }

        // Control nombre
        if (control) {
            jTextField_Nombre.setBackground(Color.white);
            String regex = "^[a-zA-Z][a-zA-Z\\s]*$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(nombre);
            System.out.println(nombre);
            if (!matcher.matches()) {
                System.out.println("PIOLAZOO");
                jTextField_Nombre.setBackground(Color.red);
                control = false;
            }
            if (nombre.equalsIgnoreCase("ingrese el nombre")) {
                jTextField_Nombre.setBackground(Color.red);
                control = false;
            }

        }

        // Control apellido
        if (control) {
            jTextField_Apellido.setBackground(Color.white);
            String regex = "^[a-zA-Z][a-zA-Z\\s]*$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(apellido);
            if (!matcher.matches()) {
                jTextField_Apellido.setBackground(Color.red);
                control = false;
            }
            if (apellido.equalsIgnoreCase("ingrese el apellido")) {
                jTextField_Apellido.setBackground(Color.red);
                control = false;
            }
        }

        // Control Dni por tamaño 
        if (control) {
            jTextField_dni.setBackground(Color.white);
            try {
                dni = Integer.parseInt(jTextField_dni.getText());
            } catch (NumberFormatException | NullPointerException e) {
                jTextField_dni.setBackground(Color.MAGENTA);
                System.out.println("asdasdasd");
                control = false;
            }

            if (dni > 99999999 || dni < 1000000) {
                jTextField_dni.setBackground(Color.red);
                control = false;
            }

        }
        // control de dni en la base de datos
        if (control && flag == 2) {
            for (Paciente p : pacientes) {
                if (dni == p.getDni()) {
                    JOptionPane.showMessageDialog(null, "El paciente" + p.getNombre() + "con DNI" + p.getDni() + "ya se encuentra en la base de datos.\n Puede buscarlo en la lista de pacientes.");
                    control = false;
                }
            }

        }
        // Control edad por tamaño
        if (control) {
            jTextField_Edad.setBackground(Color.white);
            try {
                edad = Integer.parseInt(jTextField_Edad.getText());

            } catch (NumberFormatException e) {
                jTextField_Edad.setBackground(Color.MAGENTA);
            }
            if (edad > 120 || edad < 1) {
                jTextField_Edad.setBackground(Color.red);
            }
        }
        // Control Edad
        if (control) {
            jTextField_Edad.setBackground(Color.white);
            jDateChooser_fechaNacimiento.setForeground(Color.white);

            int año = Calendar.getInstance().get(Calendar.YEAR);
            try {
                int añoNacimiento = jDateChooser_fechaNacimiento.getCalendar().get(Calendar.YEAR);
                if (edad != (año-1) - añoNacimiento) {
                    jTextField_Edad.setBackground(Color.red);
                    jDateChooser_fechaNacimiento.setForeground(Color.red);
                    control = false;
                }
            } catch (NullPointerException e) {
                jDateChooser_fechaNacimiento.setForeground(Color.red);
            }

        }

        // control telefono 
        if (control) {
            try {
                String tel = jTextField_telefono.getText();
                telefono = Long.parseLong(jTextField_telefono.getText());
                jTextField_telefono.setBackground(Color.white);
                String regex = "^[0-9]*{8,20}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(tel);
                if (!matcher.matches()) {
                    jTextField_telefono.setBackground(Color.red);
                    control = false;
                }
            } catch (NumberFormatException e) {
                jTextField_telefono.setBackground(Color.red);

            }

        }

        // Control fecha nacimiento vacio
        if (control) {
            if (fNacimiento == null) {
                jDateChooser_fechaNacimiento.setForeground(Color.red);
                control = false;
            }
        }
        return control;

    }

    public void cargarTablaListadoPacientes(ArrayList<Paciente> lista) {
        modeloTablaListadoPacientes.setRowCount(0);
        int fila = 0;
        for (Paciente p : lista) {
            modeloTablaListadoPacientes.addRow(new Object[7]);
            jTable_ListadoPacientes.setValueAt(p.getNombre(), fila, 0);
            jTable_ListadoPacientes.setValueAt(p.getApellido(), fila, 1);
            jTable_ListadoPacientes.setValueAt(p.getDni(), fila, 2);
            jTable_ListadoPacientes.setValueAt(p.getTelefono(), fila, 3);
            jTable_ListadoPacientes.setValueAt(p.getFechaNacimiento(), fila, 4);
            jTable_ListadoPacientes.setValueAt(p.getEdad(), fila, 5);
            jTable_ListadoPacientes.setValueAt(p.getSexo(), fila, 6);

            fila++;
        }

    }

    private void jTable3_TablaPestanaOrdenesPendientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3_TablaPestanaOrdenesPendientesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable3_TablaPestanaOrdenesPendientesMouseClicked

    private void jTable4_cargarValoresAnalisisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4_cargarValoresAnalisisMouseClicked
        Integer codigoAnalisis = Integer.parseInt(String.valueOf(modeloTablaAnalisisResultados.getValueAt(jTable4_cargarValoresAnalisis.getSelectedRow(), 1)));
        ManagerAnalisis ma = new ManagerAnalisis();
        //Estructura auxiliar para acceder a analisis
        ArrayList<Analisis> datosAnalisis = new ArrayList<Analisis>();

        datosAnalisis = ma.recuperarFilas();
        String valorReferencia = "";
        for (Analisis a : datosAnalisis) {
            int buscando = a.getCodigo();
            if (buscando == codigoAnalisis) {
                valorReferencia = a.getValoresReferencia();
            }
        }

        jTextArea1_valoresDeReferencia.setText(valorReferencia);
        // jTextArea1_valoresDeReferencia.setText("Terminar metodo recuperarValorReferencia\n" + "en manager analisis. Controlar que no cargue repetidos");

    }//GEN-LAST:event_jTable4_cargarValoresAnalisisMouseClicked

    private void jTextFieldResultadosBuscarOrdenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldResultadosBuscarOrdenKeyPressed

        modeloTablaOrdenesPendientes.setRowCount(0);
        ArrayList<Orden> ordenes = new ArrayList<Orden>();
        Manager_Ordenes mo = new Manager_Ordenes();
        ordenes = mo.recuperarFilas();

        String buscado;
        buscado = jTextFieldResultadosBuscarOrden.getText();
        buscado = buscado.toUpperCase();
        int i;
        int fila = 0;

        for (Orden o : ordenes) {

            String buscando = o.getPaciente().getNombre();
            buscando = buscando.toUpperCase();
            System.out.println("El string buscando dentro del for es: " + buscando);

            if (buscando.contains(buscado)) {
                modeloTablaOrdenesPendientes.addRow(new Object[6]);
                jTable2_TablaOrdenesPendientes.setValueAt(o.getNumero(), fila, 0);
                jTable2_TablaOrdenesPendientes.setValueAt(o.getFechaDeIngreso(), fila, 1);
                jTable2_TablaOrdenesPendientes.setValueAt(o.getMedico(), fila, 2);
                jTable2_TablaOrdenesPendientes.setValueAt(o.getPaciente().getDni(), fila, 3);
                jTable2_TablaOrdenesPendientes.setValueAt(o.getPaciente().getNombre(), fila, 4);
                jTable2_TablaOrdenesPendientes.setValueAt(o.getObraSocial(), fila, 5);
                fila++;
            }

        }
        jTextArea1_valoresDeReferencia.setText("Estoy buscando a: " + buscado + "\n  \n Fede ponete a laburar!");

    }//GEN-LAST:event_jTextFieldResultadosBuscarOrdenKeyPressed

    private void jTextFieldResultadosBuscarOrdenPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTextFieldResultadosBuscarOrdenPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldResultadosBuscarOrdenPropertyChange

    private void jTextFieldResultadosBuscarOrdenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldResultadosBuscarOrdenMouseClicked
        jTextFieldResultadosBuscarOrden.setText("");
    }//GEN-LAST:event_jTextFieldResultadosBuscarOrdenMouseClicked

    private void jTextFieldResultadosBuscarOrdenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldResultadosBuscarOrdenMouseEntered

        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldResultadosBuscarOrdenMouseEntered

    private void jTabbedPane_menuPestañasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane_menuPestañasMouseClicked
        // TODO add you
        cargarTablaOrdenesPendientes();
        cargarTablaOrdenesTerminadas();
        jTextFieldResultadosBuscarOrden.setText("Ingrese el nombre del paciente a buscar");
        jTextArea1_valoresDeReferencia.setText("");
    }//GEN-LAST:event_jTabbedPane_menuPestañasMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        modeloTablaAnalisisResultados.setRowCount(0);
        jTextArea1_valoresDeReferencia.setText("");

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextFieldResultadosBuscarOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldResultadosBuscarOrdenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldResultadosBuscarOrdenActionPerformed

    private void jButton_imprimirResultadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_imprimirResultadoActionPerformed
        String texto = jTextArea_VistaPrevia.getText();
        Integer codigoOrden = Integer.parseInt(String.valueOf(modeloTablaOrdenesTerminadas.getValueAt(jTable2_TablaOrdenesTerminadas.getSelectedRow(), 0)));
        generarInforme(texto, codigoOrden);

    }//GEN-LAST:event_jButton_imprimirResultadoActionPerformed
    /**
     * Este metodo genera el archivo de texto que contiene el la orden que va a
     * ser impresa por el usuario.
     *
     * @param informe
     * @param codigo
     */
    public void generarInforme(String informe, int codigo) {
        File archivo = new File("ArchivosOrdenes/orden n° " + codigo + ".txt");
        String[] datos = informe.split("\n\n");
        String[] paciente = datos[0].split("\n");
        String[] analisis = datos[1].split("\n");
        int i;
        try {
            
            FileWriter w = new FileWriter(archivo);
            BufferedWriter bw = new BufferedWriter(w);
            PrintWriter wr = new PrintWriter(bw);
            
            for (i = 0; i < paciente.length; i++) {
                wr.println(paciente[i]);
                
            }
            wr.println("\n\n");
            for (i = 0; i < analisis.length; i++) {
                wr.println(analisis[i]);
                
            }

            wr.close();
            bw.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al escribir el fichero");
        }
        JOptionPane.showMessageDialog(null, "El informe se ha generado exitosamente.");

    }
    private void jTable2_TablaOrdenesTerminadasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2_TablaOrdenesTerminadasMouseClicked
        Integer codigoOrden = Integer.parseInt(String.valueOf(modeloTablaOrdenesTerminadas.getValueAt(jTable2_TablaOrdenesTerminadas.getSelectedRow(), 0)));
        Manager_Ordenes mo = new Manager_Ordenes();
        ArrayList<Resultado> lista = mo.recuperarFilasResultados(codigoOrden);
        Orden o = mo.recuperarOrden(codigoOrden);
        String impresion = "Orden n°:" + codigoOrden + "\n"
                + "Nombre y Apellido: " + o.getPaciente().getNombre() + " " + o.getPaciente().getApellido() + "\n"
                + "DNI:" + o.getPaciente().getDni() + "\n" + "Sexo :" + o.getPaciente().getSexo() + "\n" + "Edad: " + o.getPaciente().getEdad() + "\n"
                + "Bioquimico: " + o.getBioquimico().getNombre() + "\n" + "Medico: " + o.getMedico() + "\n\n";

        String analisis = "Analisis: \n";
        String todosAnalisis = null;
        String ultimo;

        for (Resultado r : lista) {
            if (r.getCodigoOrden() == codigoOrden) {
                analisis += (r.getNombreAnalisis() + ":" + "  " + r.getValorTomado() + "\n");
            }
        }
        ultimo = impresion.concat(analisis);
        jTextArea_VistaPrevia.setText(ultimo);


    }//GEN-LAST:event_jTable2_TablaOrdenesTerminadasMouseClicked

    private void jButton_ListadoPaciente_CargarOrden1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ListadoPaciente_CargarOrden1ActionPerformed
        flag = 2;
        jComboBox_ObraSocial.setModel(modeloComboBox);
        desbloquear();

        /*jTextField_Nombre.setForeground(Color.darkGray);
        jTextField_Apellido.setForeground(Color.darkGray);
        jTextField_dni.setForeground(Color.darkGray);
        jTextField_telefono.setForeground(Color.darkGray);
        jTextField_Edad.setForeground(Color.darkGray);
         */
        jDateChooser_fechaNacimiento.setDate(null);


    }//GEN-LAST:event_jButton_ListadoPaciente_CargarOrden1ActionPerformed

    private void jComboBox_ObraSocialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_ObraSocialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_ObraSocialActionPerformed

    private void jButton1CargarListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1CargarListaActionPerformed
        ArrayList<Analisis> datos = new ArrayList();
        datos = ma.recuperarAnalisisSeleccionados(modeloTablaAnalisisParaSeleccionar, jTableAnalasisParaSeleccionar);
        cargarTablaAnalsisSeleccionados(datos);
    }//GEN-LAST:event_jButton1CargarListaActionPerformed

    private void jButton_CargarOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CargarOrdenActionPerformed
        ManagerPaciente mp = new ManagerPaciente();
        Manager_Ordenes mo = new Manager_Ordenes();

        // Datos Ordenes
        int codigo = 0;
        String fechaIngreso = dameFecha(jDateChooserfechaIngreso.getDate());
        String nombreMedico = jTextField_Medico_CargarOrden.getText();

        // Datos Paciente
        String nombre;
        String apellido;
        int dni = 0;
        Long telefono = null;
        String fNacimiento; // se pasa la fecha a formato String
        int edad = 0;
        String sexo;
        Date fecha = jDateChooser_fechaNacimiento.getDate();

        if (jRadioButton_femenino.isSelected()) {
            sexo = "Femenino";
        } else {
            sexo = "Masculino";
        }

        if (flag == 2) { // flag = 2, se ingresa un nuevo paciente al sistema

            if (ValidarCampos()) {

                nombre = jTextField_Nombre.getText();
                apellido = jTextField_Apellido.getText();
                try {
                    dni = Integer.parseInt(jTextField_dni.getText());
                    telefono = Long.parseLong(jTextField_telefono.getText());
                    edad = Integer.parseInt(jTextField_Edad.getText());
                } catch (NumberFormatException e) {

                }
                if (jRadioButton_femenino.isSelected()) {
                    sexo = "Femenino";
                } else {
                    sexo = "Masculino";
                }
                fNacimiento = dameFecha(fecha);

                // recuparar analsiis de la lista.
                
                try {
                    
                    // Carga de los resultados pendientes.
                    
                    ArrayList<Analisis> listaCodigos = ma.recuperarCodigos(modeloTablaAnalisisSelecionados, jTable_AnalisisSeleccionados);
                    if (listaCodigos.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "NO SE PUEDEN CARGAR ORDENES SIN ANALISIS\n"
                                + "Seleccione los analisis que desee y pulse el boton 'Cargar Analisis'");
                    } else {

                        mp.cargarPaciente(nombre, apellido, dni, telefono, fNacimiento, edad, sexo);
                        
                        // carga la obra social.
                        
                        String obraSocial = (String) jComboBox_ObraSocial.getSelectedItem();
                        try {
                            if (obraSocial.equals("")) {
                                jComboBox_ObraSocial.setBackground(Color.red);
                            } else {
                                mp.cargarObraSocialPaciente(obraSocial, dni);
                                jComboBox_ObraSocial.setBackground(Color.getHSBColor(240, 90, 90));
                            }
                        } catch (Exception e) {
                            jComboBox_ObraSocial.setBorder(new LineBorder(Color.red));
                        }

                        codigo = mo.cargarOrden(fechaIngreso, nombreMedico, dni, nombre, obraSocial);
                        
                        for (Analisis a : listaCodigos) {
                            if (a.getConsentimiento() == true) {
                                JOptionPane.showMessageDialog(null, "Para la realizacion del ANALISIS " + a.getNombre() + "\nse requiere un consentimiento por parte del paciente.");
                            }
                            mo.cargarResultado(codigo, a.getCodigo(), a.getNombre(), "");

                        }
                        cargarTablaOrdenesPendientes();
                        JOptionPane.showMessageDialog(null, "Se ha cargado la orden exitosamente", "Enorabuena", JOptionPane.OK_OPTION);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "error al cargar en la base de datos");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "telefono invalido");
                }
                JOptionPane.showMessageDialog(null, "La Orden numero" + codigo + "ha sido cargada exitosamente");
            }
        } else {
            if (ValidarCampos()) {

                nombre = jTextField_Nombre.getText();
                try {
                    dni = Integer.parseInt(jTextField_dni.getText());
                } catch (NumberFormatException e) {

                }

                try {
                    String obraSocial = (String) jComboBox_ObraSocial.getSelectedItem();
                    try {
                        if (obraSocial.equals("")) {
                            jComboBox_ObraSocial.setBackground(Color.red);
                        } else {
                            jComboBox_ObraSocial.setBackground(Color.getHSBColor(204, 204, 255));
                        }
                    } catch (NullPointerException e) {
                        jComboBox_ObraSocial.setBorder(new LineBorder(Color.red));
                    }
                    ArrayList<Analisis> listaCodigos = ma.recuperarCodigos(modeloTablaAnalisisSelecionados, jTable_AnalisisSeleccionados);
                    if (listaCodigos.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "NO SE PUEDEN CARGAR ORDENES SIN ANALISIS\n"
                                + "Seleccione los analisis que desee y pulse el boton 'Cargar Analisis'");
                    } else {

                        codigo = mo.cargarOrden(fechaIngreso, nombreMedico, dni, nombre, obraSocial);
                        cargarTablaOrdenesPendientes();
                        for (Analisis a : listaCodigos) {
                            System.out.println(a.getConsentimiento());
                            if (a.getConsentimiento() == true) {
                                JOptionPane.showMessageDialog(null, "Para la realizacion del ANALISIS " + a.getNombre() + "\nse requiere un consentimiento por parte del paciente.");

                            }
                            mo.cargarResultado(codigo, a.getCodigo(), a.getNombre(), "");
                        }
                    }

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "telefono invalido");
                }
            }
        }
    }//GEN-LAST:event_jButton_CargarOrdenActionPerformed

    private void jRadioButton_masculinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_masculinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton_masculinoActionPerformed

    private void jTextField_dniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_dniActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_dniActionPerformed

    private void jTextField_ApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_ApellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_ApellidoActionPerformed

    private void jButton_ListadoPaciente_CargarOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ListadoPaciente_CargarOrdenActionPerformed
        jDialog_ListadoPacientes.setVisible(true);
        jDialog_ListadoPacientes.setLocation(450, 150);

        jDialog_ListadoPacientes.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        jDialog_ListadoPacientes.setSize(1000, 400);
        flag = 1;
        bloquear();
    }//GEN-LAST:event_jButton_ListadoPaciente_CargarOrdenActionPerformed

    private void jTextField_buscadorAnalisisFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_buscadorAnalisisFocusGained
        jTextField_buscadorAnalisis.setText("");

    }//GEN-LAST:event_jTextField_buscadorAnalisisFocusGained

    private void jTextField_buscadorAnalisisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_buscadorAnalisisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_buscadorAnalisisActionPerformed

    private void jTextField_buscadorAnalisisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_buscadorAnalisisKeyPressed
        ArrayList<Analisis> buscado = new ArrayList();
        String busqueda = jTextField_buscadorAnalisis.getText();
        busqueda = busqueda.toLowerCase();
        String aux;
        int auxCodigo;
        if (jComboBox_buscador.getSelectedIndex() == 0) {

            for (Analisis a : datosAnalisis) {
                aux = a.getNombre().toLowerCase();
                System.out.println(aux);
                System.out.println(busqueda);
                if (aux.contains(busqueda)) {
                    buscado.add(a);

                }
            }

        } else if (jComboBox_buscador.getSelectedIndex() == 1) {

            for (Analisis a : datosAnalisis) {
                auxCodigo = a.getCodigo();
                try {
                    if (auxCodigo == Integer.parseInt(busqueda)) {
                        buscado.add(a);

                    }
                } catch (NumberFormatException e) {

                }

            }

        }
        cargarTablaAnalisisParaSeleccionar(buscado);


    }//GEN-LAST:event_jTextField_buscadorAnalisisKeyPressed

    private void jComboBox_buscadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_buscadorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_buscadorActionPerformed

    private void jTable_AnalisisSeleccionadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_AnalisisSeleccionadosMouseClicked
        modeloTablaAnalisisSelecionados.removeRow(jTable_AnalisisSeleccionados.getSelectedRow());
        jTable_AnalisisSeleccionados.setModel(modeloTablaAnalisisSelecionados);

    }//GEN-LAST:event_jTable_AnalisisSeleccionadosMouseClicked

    private void jTextField_NombreFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_NombreFocusGained
        if (jTextField_Nombre.getText().equalsIgnoreCase("ingrese el nombre")) {
            jTextField_Nombre.setText("");
        }

    }//GEN-LAST:event_jTextField_NombreFocusGained

    private void jTextField_ApellidoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_ApellidoFocusGained
        if (jTextField_Apellido.getText().equalsIgnoreCase("ingrese el apellido")) {
            jTextField_Apellido.setText("");
        }

    }//GEN-LAST:event_jTextField_ApellidoFocusGained

    private void jTextField_dniFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_dniFocusGained
        if (jTextField_dni.getText().equalsIgnoreCase("ingrese el dni")) {

            jTextField_dni.setText("");
        }

    }//GEN-LAST:event_jTextField_dniFocusGained

    private void jTextField_telefonoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_telefonoFocusGained
        if (jTextField_telefono.getText().equalsIgnoreCase("ingrese el telefono")) {

            jTextField_telefono.setText("");
        }
    }//GEN-LAST:event_jTextField_telefonoFocusGained

    private void jTextField_EdadFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_EdadFocusGained
        if (jTextField_Edad.getText().equalsIgnoreCase("ingrese la edad")) {

            jTextField_Edad.setText("");
        }
    }//GEN-LAST:event_jTextField_EdadFocusGained

    private void jTextField_NombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_NombreKeyTyped

    }//GEN-LAST:event_jTextField_NombreKeyTyped

    private void jTextField_UsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_UsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_UsuarioActionPerformed

    private void jTextField_contraseñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_contraseñaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_contraseñaActionPerformed

    private void jButton_FemeninoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_FemeninoActionPerformed
        jDialog_Login.setVisible(true);
        jDialog_Login.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        jDialog_Login.setAlwaysOnTop(true);
        jDialog_Login.setSize(350, 300);

    }//GEN-LAST:event_jButton_FemeninoActionPerformed

    private void jTextField_buscadorAnalisisBotonKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_buscadorAnalisisBotonKeyTyped
        ArrayList<Analisis> buscado = new ArrayList();
        String busqueda = jTextField_buscadorAnalisisBoton.getText();
        busqueda = busqueda.toLowerCase();
        String aux;
        String auxCodigo = "";
        if (jComboBox_buscadorAnalisisBoton.getSelectedIndex() == 0) {

            for (Analisis a : datosAnalisis) {
                aux = a.getNombre().toLowerCase();
                // System.out.println(aux);
                //System.out.println(busqueda);
                if (aux.contains(busqueda)) {
                    buscado.add(a);

                }
            }

        } else if (jComboBox_buscadorAnalisisBoton.getSelectedIndex() == 1) {

            for (Analisis a : datosAnalisis) {
                auxCodigo += a.getCodigo();
                System.out.println(auxCodigo);
                try {
                    if (auxCodigo.contains((busqueda))) {
                        buscado.add(a);
                    }
                } catch (NumberFormatException e) {

                }

            }

        }
        cargarTablaAnalisis(buscado);
    }//GEN-LAST:event_jTextField_buscadorAnalisisBotonKeyTyped

    private void jTextField_buscadorAnalisisBotonFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_buscadorAnalisisBotonFocusGained
        String texto = jTextField_buscadorAnalisisBoton.getText();
        if (texto.equalsIgnoreCase("ingrese el analisis que desea buscar")) {
            jTextField_buscadorAnalisisBoton.setText("");
        }
    }//GEN-LAST:event_jTextField_buscadorAnalisisBotonFocusGained

    private void jTextField_codigoAnalisisNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_codigoAnalisisNuevoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_codigoAnalisisNuevoActionPerformed

    private void jCheckBox_consentimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_consentimientoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox_consentimientoActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jDialog_altaAnalisis.setVisible(true);
        jDialog_altaAnalisis.setSize(400, 500);


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        jDialog_altaAnalisis.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton_confirmarNuevoAnalisisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_confirmarNuevoAnalisisActionPerformed
        Integer codigo = (Integer.parseInt(jTextField_codigoAnalisisNuevo.getText()));
        String nombre = jTextField_nombreAnalisisNuevo.getText();
        String indicaciones = jEditorPane_valoresReferencia.getText();
        Integer unidadesB = (Integer.parseInt(jTextField_uBAnalisisNuevo.getText()));
        Boolean consentimiento = false;
        Descartable descartables = new Descartable(jTextField_nombreDescartable.getText(), (Integer.parseInt(jTextField_cantidadDescartable.getText())), 15);
        String valoresR = jEditorPane_indicacionesPrevias.getText();
        if (jCheckBox_consentimiento.isSelected()) {
            consentimiento = true;
        }

        ManagerAnalisis manager = new ManagerAnalisis();
        datosAnalisis.add(manager.cargarAnalisis(codigo, nombre, indicaciones, unidadesB, descartables, consentimiento, valoresR));
        cargarTablaAnalisis(datosAnalisis);
        cargarTablaAnalisisParaSeleccionar(datosAnalisis);


    }//GEN-LAST:event_jButton_confirmarNuevoAnalisisActionPerformed
    public void validarCamposAnalisis() {

    }
    private void jButton_abrirIndicacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_abrirIndicacionesActionPerformed
        jDialog_cargarTextosLargos.setVisible(true);
        jEditorPane_indicacionesPrevias.setFocusable(true);
        jDialog_cargarTextosLargos.setSize(900, 500);

    }//GEN-LAST:event_jButton_abrirIndicacionesActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        jDialog_cargarTextosLargos.dispose();
        jEditorPane_indicacionesPrevias.setFocusable(true);

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton_abrirValoresReferenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_abrirValoresReferenciaActionPerformed
        jDialog_cargarTextosLargos.setVisible(true);
        jDialog_cargarTextosLargos.setSize(900, 500);
        jEditorPane_valoresReferencia.setFocusable(true);

    }//GEN-LAST:event_jButton_abrirValoresReferenciaActionPerformed

    private void jButton_eliminarAnalisisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_eliminarAnalisisActionPerformed
        ManagerAnalisis ma = new ManagerAnalisis();
        // controlar si el analisis es borrable o no. 

        ma.deleteAnalisis(ma.seleccionarAnalisis(modeloTablaAnalisis, jTable_Analisis, 0));


    }//GEN-LAST:event_jButton_eliminarAnalisisActionPerformed

    private void jTextField_buscadorPacientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_buscadorPacientesKeyPressed
        ArrayList<Paciente> buscado = new ArrayList();
        String busqueda = jTextField_buscadorPacientes.getText();
        busqueda = busqueda.toLowerCase();
        String aux;

        for (Paciente p : datosPaciente) {
            aux = (p.getNombre() + p.getApellido() + p.getDni()).toLowerCase();
            if (aux.contains(busqueda)) {
                buscado.add(p);
            }
        }
        cargarTablaListadoPacientes(buscado);


    }//GEN-LAST:event_jTextField_buscadorPacientesKeyPressed

    private void jTextField_buscadorPacientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_buscadorPacientesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_buscadorPacientesActionPerformed

    private void jTextField_buscadorPacientesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_buscadorPacientesFocusGained
        if (jTextField_buscadorPacientes.getText().equalsIgnoreCase("Ingrese aqui lo que desea buscar...")) {
            jTextField_buscadorPacientes.setText("");
        }

    }//GEN-LAST:event_jTextField_buscadorPacientesFocusGained

    private void jTable_ListadoPacientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_ListadoPacientesMouseClicked

        ManagerPaciente mp = new ManagerPaciente();
        ArrayList<Obra_Social> obraSocial;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaDate = null;
        ComboBoxModel modelo_comboBox;
        String[] obra_social = new String[4];

        Paciente p = (mp.recupararPaciente(modeloTablaListadoPacientes, jTable_ListadoPacientes));
        jTextField_Nombre.setText(p.getNombre());
        jTextField_Apellido.setText(p.getApellido());
        jTextField_dni.setText((String.valueOf(p.getDni())));
        jTextField_telefono.setText((String.valueOf(p.getTelefono())));
        jTextField_Edad.setText(String.valueOf(p.getEdad()));

        try {
            fechaDate = formato.parse(p.getFechaNacimiento());
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "ERROR EN FORMATO DE FECHA");

        }

        jDateChooser_fechaNacimiento.setDate(fechaDate);
        if (p.getSexo().equals("Masculino")) {
            jRadioButton_masculino.setSelected(true);
        } else {
            jRadioButton_femenino.setSelected(true);
        }

        obraSocial = mp.recuperarObraSocial(p.getDni()); // Recupero las Obra Sociales del paciente con el DNI.
        
        int i;
        for (i = 0; i < obraSocial.size(); i++) { // Recorro el arreglo de obras sociales de dicho paciente.
            try {
                switch (obraSocial.get(i).getNombre()) {
                    case ("Swiss Medical"):
                        obra_social[i] = "Swiss Medical";
                        // jComboBox_ObraSocial.setSelectedIndex(0);
                        break;
                    case ("Femesa"):
                        // jComboBox_ObraSocial.setSelectedIndex(1);
                        obra_social[i] = "Femesa";
                        break;
                    case ("Osde"):
                        // jComboBox_ObraSocial.setSelectedIndex(2);
                        obra_social[i] = "Osde";
                        break;
                    case ("Sancor Salud"):
                        // jComboBox_ObraSocial.setSelectedIndex(3);
                        obra_social[i] = "Sancor Salud";
                        break;
                    case ("Particular"):
                        obra_social[i] = "Particular";
                        // jComboBox_ObraSocial.setSelectedIndex(4);
                        break;
                    default:
                        break;
                }
            } catch (NullPointerException e) {
                jComboBox_ObraSocial.setSelectedIndex(4);
            }
        }
        modelo_comboBox = new DefaultComboBoxModel(obra_social);
        jComboBox_ObraSocial.setModel(modelo_comboBox);
        jDialog_ListadoPacientes.dispose();

    }//GEN-LAST:event_jTable_ListadoPacientesMouseClicked

    private void jTableAnalasisParaSeleccionarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableAnalasisParaSeleccionarMouseClicked
        Analisis a = ma.seleccionarAnalisis(modeloTablaAnalisisParaSeleccionar, jTableAnalasisParaSeleccionar, 1);
        jTextArea_cargarOrden.setText(a.getValoresReferencia());
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableAnalasisParaSeleccionarMouseClicked

    private void jTextField_Medico_CargarOrdenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_Medico_CargarOrdenFocusGained
        if (jTextField_Nombre.getText().equalsIgnoreCase("ingrese el nombre del medico")) {
            jTextField_Nombre.setText("");
        }
    }//GEN-LAST:event_jTextField_Medico_CargarOrdenFocusGained

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Integer dni_paciente = Integer.parseInt(String.valueOf(modeloTablaPaciente.getValueAt(jTable_ListadoPacientes.getSelectedRow(), 2)));
        String obraSocial = String.valueOf((modeloTablaPaciente.getValueAt(jTable_ListadoPacientes.getSelectedRow(), 2)));
        ArrayList<Orden> lista = mo.readOrdenDePaciente(dni_paciente);
        if (lista.isEmpty()) {
            mp.deletePaciente(dni_paciente);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox_NombreBioquimicosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_NombreBioquimicosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_NombreBioquimicosActionPerformed
    public String tomarTexto(String texto) {
        return texto;
    }

    public void desbloquear() {
        // metodo para desbloquear los campos de texto de los datos del paciente.

        jTextField_Nombre.setEnabled(true);
        jTextField_Nombre.setText("ingrese el nombre");
        jTextField_Apellido.setEnabled(true);
        jTextField_Apellido.setText("ingrese el apellido");
        jTextField_dni.setEnabled(true);
        jTextField_dni.setText("ingrese el dni");
        jTextField_Edad.setEnabled(true);
        jTextField_Edad.setText("ingrese la edad");
        jTextField_telefono.setEnabled(true);
        jTextField_telefono.setText("ingrese el telefono");
        jDateChooser_fechaNacimiento.setEnabled(true);
        jRadioButton_femenino.setEnabled(true);
        jRadioButton_masculino.setEnabled(true);
        jComboBox_ObraSocial.setEnabled(true);

    }

    public String dameFecha(Date date) {
        String formato = jDateChooser_fechaNacimiento.getDateFormatString();
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        String fecha = null;
        try {
            fecha = String.valueOf(sdf.format(date));
        } catch (NullPointerException e) {
            jDateChooser_fechaNacimiento.setForeground(Color.red);

        }

        return fecha;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Medico_cargarOrden;
    private javax.swing.ButtonGroup buttonGroup_sexo;
    private javax.swing.JLabel datosAnalisis_CargarOrden1;
    private javax.swing.JLabel datosAnalisis_CargarOrden2;
    private javax.swing.JLabel datosAnalisis_CargarOrden3;
    private javax.swing.JLabel datosAnalisis_CargarOrden4;
    private javax.swing.JLabel datosAnalisis_CargarOrden5;
    private javax.swing.JLabel datosAnalisis_CargarOrden6;
    private javax.swing.JLabel datosAnalisis_CargarOrden7;
    private javax.swing.JLabel datosAnalisis_CargarOrden8;
    private javax.swing.JLabel datosAnalisis_CargarOrden9;
    private javax.swing.JLabel datosPaciente_CargarOrden;
    private javax.swing.JLabel fecha_CargarOrden;
    private javax.swing.JLabel fecha_CargarOrden1;
    private javax.swing.JLabel fecha_CargarOrden2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton1CargarLista;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton2CargarResultado;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButton_ActualzarStock;
    private javax.swing.JButton jButton_Analisis_CargarOrden1;
    private javax.swing.JButton jButton_CargarOrden;
    private javax.swing.JButton jButton_Femenino;
    private javax.swing.JButton jButton_ListadoPaciente_CargarOrden;
    private javax.swing.JButton jButton_ListadoPaciente_CargarOrden1;
    private javax.swing.JLabel jButton_ObraSocial_CargarOrden;
    private javax.swing.JButton jButton_abrirIndicaciones;
    private javax.swing.JButton jButton_abrirValoresReferencia;
    private javax.swing.JButton jButton_confirmarNuevoAnalisis;
    private javax.swing.JButton jButton_eliminarAnalisis;
    private javax.swing.JButton jButton_imprimirResultado;
    private javax.swing.JCheckBox jCheckBox_consentimiento;
    private javax.swing.JComboBox<String> jComboBox_NombreBioquimicos;
    private javax.swing.JComboBox<String> jComboBox_ObraSocial;
    private javax.swing.JComboBox<String> jComboBox_buscador;
    private javax.swing.JComboBox<String> jComboBox_buscadorAnalisisBoton;
    private com.toedter.calendar.JDateChooser jDateChooser_fechaNacimiento;
    private com.toedter.calendar.JDateChooser jDateChooserfechaIngreso;
    private javax.swing.JDialog jDialog_ListadoAnalisis;
    private javax.swing.JDialog jDialog_ListadoPacientes;
    private javax.swing.JDialog jDialog_Login;
    private javax.swing.JDialog jDialog_altaAnalisis;
    private javax.swing.JDialog jDialog_cargarTextosLargos;
    private javax.swing.JEditorPane jEditorPane_indicacionesPrevias;
    private javax.swing.JEditorPane jEditorPane_valoresReferencia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_cantidadActual;
    private javax.swing.JLabel jLabel_cantidadMinima;
    private javax.swing.JLabel jLabel_nombreStock;
    private javax.swing.JPanel jPanel2ListarOrdenesPendientes;
    private javax.swing.JPanel jPanel2_cargarResultados;
    private javax.swing.JPanel jPanel3ImprimirResultados;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel_CargarOrden;
    private javax.swing.JPanel jPanel_stock;
    private javax.swing.JRadioButton jRadioButton_femenino;
    private javax.swing.JRadioButton jRadioButton_masculino;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JTabbedPane jTabbedPane_menuPestañas;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2_TablaOrdenesPendientes;
    private javax.swing.JTable jTable2_TablaOrdenesTerminadas;
    private javax.swing.JTable jTable3_TablaPestanaOrdenesPendientes;
    private javax.swing.JTable jTable4_cargarValoresAnalisis;
    private javax.swing.JTable jTableAnalasisParaSeleccionar;
    private javax.swing.JTable jTable_Analisis;
    private javax.swing.JTable jTable_AnalisisSeleccionados;
    private javax.swing.JTable jTable_ListadoPacientes;
    private javax.swing.JTextArea jTextArea1_valoresDeReferencia;
    private javax.swing.JTextArea jTextArea_VistaPrevia;
    private javax.swing.JTextArea jTextArea_cargarOrden;
    private javax.swing.JTextField jTextFieldResultadosBuscarOrden;
    private javax.swing.JTextField jTextField_Apellido;
    private javax.swing.JTextField jTextField_Edad;
    private javax.swing.JTextField jTextField_Medico_CargarOrden;
    private javax.swing.JTextField jTextField_Nombre;
    private javax.swing.JTextField jTextField_Usuario;
    private javax.swing.JTextField jTextField_buscadorAnalisis;
    private javax.swing.JTextField jTextField_buscadorAnalisisBoton;
    private javax.swing.JTextField jTextField_buscadorPacientes;
    private javax.swing.JTextField jTextField_cantidadDescartable;
    private javax.swing.JTextField jTextField_codigoAnalisisNuevo;
    private javax.swing.JTextField jTextField_contraseña;
    private javax.swing.JTextField jTextField_dni;
    private javax.swing.JTextField jTextField_nombreAnalisisNuevo;
    private javax.swing.JTextField jTextField_nombreDescartable;
    private javax.swing.JTextField jTextField_nombreStock;
    private javax.swing.JTextField jTextField_telefono;
    private javax.swing.JTextField jTextField_uBAnalisisNuevo;
    // End of variables declaration//GEN-END:variables
}
