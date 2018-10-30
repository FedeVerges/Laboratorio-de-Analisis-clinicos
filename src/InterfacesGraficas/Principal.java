/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacesGraficas;

import Clases.Analisis;
import Clases.Orden;
import Clases.Resultado;
import Manager.ManagerAnalisis;
import Manager.ManagerPaciente;
import Manager.Manager_Ordenes;
import java.awt.Color;
import javax.swing.JCheckBox;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.CheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.omg.CORBA.MARSHAL;

/**
 *
 * @author fede_
 */
public class Principal extends javax.swing.JFrame {

    private DefaultTableModel modeloTablaAnalisis;
    private DefaultTableModel modeloTablaAnalisisParaSeleccionar;
    private DefaultTableModel modeloTablaOrdenesPendientes;
    private DefaultTableModel modeloTablaAnalisisResultados;
    private DefaultTableModel modeloTablaAnalisisSelecionados;
    private ManagerAnalisis ma = new ManagerAnalisis();
    private DefaultTableModel modeloTablaPaciente;
    private Manager_Ordenes managerOrdenes = new Manager_Ordenes();

    //private static int fila2=0;
    /**
     * Creates new form Principal
     */
    public Principal() {
        //Tabla emergente del boton
        modeloTablaAnalisis = new DefaultTableModel(null, ma.recuperarColumnas());
        //CONTROLAR ESTO!!!!!

        initComponents();

        //Tablas pestaña cargar resultados:
        modeloTablaOrdenesPendientes = (DefaultTableModel) jTable2_TablaOrdenesPendientes.getModel();
        jTable2_TablaOrdenesPendientes.setModel(modeloTablaOrdenesPendientes);

        // Tabla Pacientes
        modeloTablaPaciente = (DefaultTableModel) jTable_pacientes.getModel();
        jTable_pacientes.setModel(modeloTablaPaciente);

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
        jTable_Analisis.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        jTable2_TablaOrdenesPendientes.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        cargarTablaAnalisis();
        cargarTablaAnalisisParaSeleccionar();
        cargarTablaOrdenesPendientes();
    }

    // Metodo para cargar la tabla de los analisis que pueden ser seleccionados.
    public void cargarTablaAnalisisParaSeleccionar() {
        ManagerAnalisis ma = new ManagerAnalisis();
        ArrayList<Analisis> datos = ma.recuperarFilas();
        int fila = 0;
        for (Analisis i : datos) {
            modeloTablaAnalisisParaSeleccionar.addRow(new Object[8]);
            jTableAnalasisParaSeleccionar.setValueAt(false, fila, 0);
            jTableAnalasisParaSeleccionar.setValueAt(i.getCodigo(), fila, 1);
            jTableAnalasisParaSeleccionar.setValueAt(i.getNombre(), fila, 2);
            jTableAnalasisParaSeleccionar.setValueAt(i.getIndicacionesPrevias(), fila, 3);
            jTableAnalasisParaSeleccionar.setValueAt(i.getCantidadUnidadesB(), fila, 4);
            jTableAnalasisParaSeleccionar.setValueAt(i.getConsentimiento(), fila, 5);
            jTableAnalasisParaSeleccionar.setValueAt(i.getCostoDescartables(), fila, 6);
            jTableAnalasisParaSeleccionar.setValueAt(i.getValoresReferencia(), fila, 7);

            fila++;
        }
    }

    /*
    
    Metodo para cargar la tabla de analisis seleccionados con mouse
    
     /*
    public void listarAnalisisSeleccionados(int codigo, String nombre, String indicacionesPrevias, int cantidadUnidadesB, String consentimiento, int costoDescartables, String valoresReferencia) {

        jTableAnalisis1.setValueAt(codigo,0,0);
        jTableAnalisis1.setValueAt(nombre,0,1);
        jTableAnalisis1.setValueAt(nombre,fila2,2);
        jTableAnalisis1.setValueAt(nombre,fila2,3);
        jTableAnalisis1.setValueAt(nombre,fila2,4);
        jTableAnalisis1.setValueAt(nombre,fila2,5);
        jTableAnalisis1.setValueAt(nombre,fila2,6);
        ArrayList<Analisis> datos = ma.añadirAnalisisEnTabla();
         
        // ArrayList<Analisis> datos = ma.recuperarAnalisisSeleccionados(codigo, nombre, indicacionesPrevias, cantidadUnidadesB, consentimiento, costoDescartables, valoresReferencia);
    }
     */
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
    Metodo para cargar la tabla de ordenes pendientes.
     */
    public void cargarTablaOrdenesPendientes() {
        Manager_Ordenes mo = new Manager_Ordenes();
        ArrayList<Orden> datos = mo.recuperarFilas();

        int fila = 0;
        for (Orden i : datos) {
            modeloTablaOrdenesPendientes.addRow(new Object[6]);
            jTable2_TablaOrdenesPendientes.setValueAt(i.getNumero(), fila, 0);
            jTable2_TablaOrdenesPendientes.setValueAt(i.getFechaDeIngreso(), fila, 1);
            jTable2_TablaOrdenesPendientes.setValueAt(i.getMedico(), fila, 2);
            jTable2_TablaOrdenesPendientes.setValueAt(i.getDniPaciente(), fila, 3);
            jTable2_TablaOrdenesPendientes.setValueAt(i.getNombrePaciente(), fila, 4);
            jTable2_TablaOrdenesPendientes.setValueAt(i.getObraSocial(), fila, 5);

            fila++;
        }
    }

    public void cargarTablaAnalisisResultado(Integer codigoOrden) {
        Manager_Ordenes mo = new Manager_Ordenes();

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
        jFrame_ListadoAnalisis = new javax.swing.JFrame();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_Analisis = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jDialog_ListadoPacientes = new javax.swing.JDialog();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable_pacientes = new javax.swing.JTable();
        jTabbedPane_menuPestañas = new javax.swing.JTabbedPane();
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
        jPanel3 = new javax.swing.JPanel();
        jPanel_menuPestañas = new javax.swing.JPanel();
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
        jDateChooser_fechaNacimiento = new com.toedter.calendar.JDateChooser();
        jDateChooserfechaIngreso = new com.toedter.calendar.JDateChooser();
        jButton_ObraSocial_CargarOrden = new javax.swing.JLabel();
        jComboBox_ObraSocial = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable_AnalisisSeleccionados = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jButton_Femenino = new javax.swing.JButton();
        jButton_Analisis_CargarOrden1 = new javax.swing.JButton();

        jFrame_ListadoAnalisis.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jFrame_ListadoAnalisis.setTitle("Listado de Analisis");
        jFrame_ListadoAnalisis.setAlwaysOnTop(true);
        jFrame_ListadoAnalisis.setFocusableWindowState(false);

        jTable_Analisis.setBackground(new java.awt.Color(204, 204, 204));
        jTable_Analisis.setModel(modeloTablaAnalisis);
        jTable_Analisis.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable_Analisis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_AnalisisMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable_Analisis);

        jLabel2.setFont(new java.awt.Font("Microsoft JhengHei", 0, 18)); // NOI18N
        jLabel2.setText("Analisis");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(563, 595, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jFrame_ListadoAnalisis.getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jTable_pacientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
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
        jScrollPane8.setViewportView(jTable_pacientes);

        javax.swing.GroupLayout jDialog_ListadoPacientesLayout = new javax.swing.GroupLayout(jDialog_ListadoPacientes.getContentPane());
        jDialog_ListadoPacientes.getContentPane().setLayout(jDialog_ListadoPacientesLayout);
        jDialog_ListadoPacientesLayout.setHorizontalGroup(
            jDialog_ListadoPacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_ListadoPacientesLayout.createSequentialGroup()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDialog_ListadoPacientesLayout.setVerticalGroup(
            jDialog_ListadoPacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocation(new java.awt.Point(150, 0));

        jTable2_TablaOrdenesPendientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numero", "Fecha de Ingreso", "Medico", "DNI Paciente", "Paciente", "Obra Social"
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
        jTable2_TablaOrdenesPendientes.getTableHeader().setReorderingAllowed(false);
        jTable2_TablaOrdenesPendientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2_TablaOrdenesPendientesMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable2_TablaOrdenesPendientes);

        jTextArea1_valoresDeReferencia.setColumns(20);
        jTextArea1_valoresDeReferencia.setRows(5);
        jScrollPane5.setViewportView(jTextArea1_valoresDeReferencia);

        datosAnalisis_CargarOrden2.setFont(new java.awt.Font("Ebrima", 3, 18)); // NOI18N
        datosAnalisis_CargarOrden2.setText("Seleccione una orden sin terminar de la lista:");

        datosAnalisis_CargarOrden3.setFont(new java.awt.Font("Ebrima", 3, 18)); // NOI18N
        datosAnalisis_CargarOrden3.setText("Valores de referencia:");

        datosAnalisis_CargarOrden4.setFont(new java.awt.Font("Ebrima", 3, 18)); // NOI18N
        datosAnalisis_CargarOrden4.setText("Cargue los valores :");

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
        jScrollPane6.setViewportView(jTable4_cargarValoresAnalisis);

        jButton2CargarResultado.setText("Cargar resultado");
        jButton2CargarResultado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2CargarResultadoActionPerformed(evt);
            }
        });

        jButton3.setText("Limpiar tabla");

        javax.swing.GroupLayout jPanel2_cargarResultadosLayout = new javax.swing.GroupLayout(jPanel2_cargarResultados);
        jPanel2_cargarResultados.setLayout(jPanel2_cargarResultadosLayout);
        jPanel2_cargarResultadosLayout.setHorizontalGroup(
            jPanel2_cargarResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2_cargarResultadosLayout.createSequentialGroup()
                .addGroup(jPanel2_cargarResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2_cargarResultadosLayout.createSequentialGroup()
                        .addGroup(jPanel2_cargarResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2_cargarResultadosLayout.createSequentialGroup()
                                .addComponent(datosAnalisis_CargarOrden4, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(49, 49, 49)
                                .addComponent(jButton2CargarResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 742, Short.MAX_VALUE)
                            .addComponent(jScrollPane6))
                        .addGap(37, 37, 37))
                    .addGroup(jPanel2_cargarResultadosLayout.createSequentialGroup()
                        .addComponent(datosAnalisis_CargarOrden2, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2_cargarResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(datosAnalisis_CargarOrden3, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64))
        );
        jPanel2_cargarResultadosLayout.setVerticalGroup(
            jPanel2_cargarResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2_cargarResultadosLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2_cargarResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(datosAnalisis_CargarOrden2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datosAnalisis_CargarOrden3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2_cargarResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2_cargarResultadosLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2_cargarResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(datosAnalisis_CargarOrden4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2CargarResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addComponent(jScrollPane5))
                .addContainerGap(88, Short.MAX_VALUE))
        );

        jTabbedPane_menuPestañas.addTab("Cargar Resultados", jPanel2_cargarResultados);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1148, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 512, Short.MAX_VALUE)
        );

        jTabbedPane_menuPestañas.addTab("Imprimir Resultados", jPanel3);

        jLabel1.setFont(new java.awt.Font("Ebrima", 3, 18)); // NOI18N
        jLabel1.setText("Ingrese los datos de la Orden");

        fecha_CargarOrden.setFont(new java.awt.Font("Ebrima", 2, 14)); // NOI18N
        fecha_CargarOrden.setText("Fecha");

        Medico_cargarOrden.setFont(new java.awt.Font("Ebrima", 2, 14)); // NOI18N
        Medico_cargarOrden.setText("Medico");

        jTextField_Medico_CargarOrden.setText("nombremedico");

        datosPaciente_CargarOrden.setFont(new java.awt.Font("Ebrima", 3, 18)); // NOI18N
        datosPaciente_CargarOrden.setText("Datos Paciente");

        jButton_ListadoPaciente_CargarOrden.setBackground(java.awt.Color.darkGray);
        jButton_ListadoPaciente_CargarOrden.setFont(new java.awt.Font("Ebrima", 2, 14)); // NOI18N
        jButton_ListadoPaciente_CargarOrden.setText("Listado de Pacientes");
        jButton_ListadoPaciente_CargarOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ListadoPaciente_CargarOrdenActionPerformed(evt);
            }
        });

        jTextField_Nombre.setText("Nombre");

        jTextField_Apellido.setText("Apellido");

        jTextField_dni.setText("DNI");

        jTextField_telefono.setText("Telefono");

        fecha_CargarOrden1.setFont(new java.awt.Font("Ebrima", 2, 14)); // NOI18N
        fecha_CargarOrden1.setText("Fecha de Nacimieto");

        fecha_CargarOrden2.setFont(new java.awt.Font("Ebrima", 2, 14)); // NOI18N
        fecha_CargarOrden2.setText("Sexo");

        datosAnalisis_CargarOrden1.setFont(new java.awt.Font("Ebrima", 3, 18)); // NOI18N
        datosAnalisis_CargarOrden1.setText("Análisis Seleccionados");

        buttonGroup_sexo.add(jRadioButton_masculino);
        jRadioButton_masculino.setFont(new java.awt.Font("Ebrima", 0, 11)); // NOI18N
        jRadioButton_masculino.setText("Masculino");

        buttonGroup_sexo.add(jRadioButton_femenino);
        jRadioButton_femenino.setFont(new java.awt.Font("Ebrima", 0, 11)); // NOI18N
        jRadioButton_femenino.setText("Femenino");

        jButton_CargarOrden.setBackground(java.awt.Color.darkGray);
        jButton_CargarOrden.setFont(new java.awt.Font("Ebrima", 2, 14)); // NOI18N
        jButton_CargarOrden.setText("Cargar Orden");
        jButton_CargarOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CargarOrdenActionPerformed(evt);
            }
        });

        jTextArea_cargarOrden.setColumns(20);
        jTextArea_cargarOrden.setRows(5);
        jScrollPane4.setViewportView(jTextArea_cargarOrden);

        jTextField_Edad.setText("Edad");

        datosAnalisis_CargarOrden5.setFont(new java.awt.Font("Ebrima", 3, 18)); // NOI18N
        datosAnalisis_CargarOrden5.setText("Listado Análisis");

        jButton1CargarLista.setText("Cargar Lista");
        jButton1CargarLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1CargarListaActionPerformed(evt);
            }
        });

        jTableAnalasisParaSeleccionar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Seleccione", "Codigo", "Nombre", "indicaciones ", "Unidades Bioquimicas", "Consentimiento", "Descartables", "Valores Referencia"
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
        jScrollPane1.setViewportView(jTableAnalasisParaSeleccionar);

        jButton_ObraSocial_CargarOrden.setFont(new java.awt.Font("Ebrima", 2, 14)); // NOI18N
        jButton_ObraSocial_CargarOrden.setText("Obra Social");

        jComboBox_ObraSocial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Swiss Medical", "Femesa", "Osde", "Sancor Salud" }));
        jComboBox_ObraSocial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_ObraSocialActionPerformed(evt);
            }
        });

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
        jScrollPane9.setViewportView(jTable_AnalisisSeleccionados);

        javax.swing.GroupLayout jPanel_menuPestañasLayout = new javax.swing.GroupLayout(jPanel_menuPestañas);
        jPanel_menuPestañas.setLayout(jPanel_menuPestañasLayout);
        jPanel_menuPestañasLayout.setHorizontalGroup(
            jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                        .addGroup(jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Medico_cargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fecha_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField_Medico_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateChooserfechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_dni, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Edad, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                        .addComponent(fecha_CargarOrden1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jDateChooser_fechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                        .addComponent(fecha_CargarOrden2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jRadioButton_masculino)
                        .addGap(0, 0, 0)
                        .addComponent(jRadioButton_femenino))
                    .addComponent(jSeparator1)
                    .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                        .addComponent(datosPaciente_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_ListadoPaciente_CargarOrden))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_menuPestañasLayout.createSequentialGroup()
                        .addComponent(jButton_ObraSocial_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox_ObraSocial, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(47, 47, 47)
                .addGroup(jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                        .addGroup(jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                                .addComponent(datosAnalisis_CargarOrden5, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(88, 88, 88))
                            .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton1CargarLista))
                            .addComponent(jScrollPane4)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE)
                    .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                        .addComponent(datosAnalisis_CargarOrden1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(538, 538, 538))
                    .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel_menuPestañasLayout.setVerticalGroup(
            jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGroup(jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(fecha_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_menuPestañasLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooserfechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Medico_cargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jTextField_Medico_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                        .addGroup(jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(datosPaciente_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_ListadoPaciente_CargarOrden))
                        .addGap(7, 7, 7)
                        .addComponent(jTextField_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jTextField_Apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jTextField_dni, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                                .addComponent(jTextField_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField_Edad, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fecha_CargarOrden1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jDateChooser_fechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fecha_CargarOrden2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jRadioButton_masculino)
                                    .addComponent(jRadioButton_femenino))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton_ObraSocial_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox_ObraSocial, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                        .addGroup(jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(datosAnalisis_CargarOrden5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1CargarLista))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(datosAnalisis_CargarOrden1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel_menuPestañasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4))
                            .addGroup(jPanel_menuPestañasLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addComponent(jButton_CargarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane_menuPestañas.addTab("Cargar Orden", jPanel_menuPestañas);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jButton_Femenino.setText("Iniciar Sesion");
        jPanel4.add(jButton_Femenino, java.awt.BorderLayout.LINE_END);

        jButton_Analisis_CargarOrden1.setBackground(java.awt.Color.darkGray);
        jButton_Analisis_CargarOrden1.setFont(new java.awt.Font("Ebrima", 2, 14)); // NOI18N
        jButton_Analisis_CargarOrden1.setText("Análisis");
        jButton_Analisis_CargarOrden1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Analisis_CargarOrden1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton_Analisis_CargarOrden1, java.awt.BorderLayout.LINE_START);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane_menuPestañas))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane_menuPestañas))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_Analisis_CargarOrden1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Analisis_CargarOrden1ActionPerformed
        jFrame_ListadoAnalisis.setVisible(true);
        jFrame_ListadoAnalisis.setLocationRelativeTo(jTextField_Apellido);
        jFrame_ListadoAnalisis.setSize(800, 450);
    }//GEN-LAST:event_jButton_Analisis_CargarOrden1ActionPerformed

    // metodo para cargar la tabla de analisis que deben ser seleccionados.
    public void cargarTablaAnalisis() {
        ManagerAnalisis ma = new ManagerAnalisis();
        ArrayList<Analisis> datos = ma.recuperarFilas();
        int fila = 0;
        for (Analisis i : datos) {
            modeloTablaAnalisis.addRow(new Object[6]);
            jTable_Analisis.setValueAt(i.getCodigo(), fila, 0);
            jTable_Analisis.setValueAt(i.getNombre(), fila, 1);
            jTable_Analisis.setValueAt(i.getIndicacionesPrevias(), fila, 2);
            jTable_Analisis.setValueAt(i.getCantidadUnidadesB(), fila, 3);
            jTable_Analisis.setValueAt(i.getConsentimiento(), fila, 4);
            jTable_Analisis.setValueAt(i.getCostoDescartables(), fila, 5);
            fila++;
        }
    }

    private void jTable2_TablaOrdenesPendientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2_TablaOrdenesPendientesMouseClicked
        Integer codigoOrden = Integer.parseInt(String.valueOf(modeloTablaOrdenesPendientes.getValueAt(jTable2_TablaOrdenesPendientes.getSelectedRow(), 0)));
        System.out.println("Recibi el codigo de la orden numero: " + codigoOrden);
        cargarTablaAnalisisResultado(codigoOrden);

    }//GEN-LAST:event_jTable2_TablaOrdenesPendientesMouseClicked

    private void jTable_AnalisisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_AnalisisMouseClicked
        Iterator i;

        Integer codigoAnalisis = Integer.parseInt(String.valueOf(modeloTablaAnalisis.getValueAt(jTable_Analisis.getSelectedRow(), 0)));
        String nombre = String.valueOf(modeloTablaAnalisis.getValueAt(jTable_Analisis.getSelectedRow(), 1));
        String indicaciones = String.valueOf(modeloTablaAnalisis.getValueAt(jTable_Analisis.getSelectedRow(), 2));
        Integer cantUB = Integer.parseInt(String.valueOf(modeloTablaAnalisis.getValueAt(jTable_Analisis.getSelectedRow(), 3)));
        String consentimiento = String.valueOf(modeloTablaAnalisis.getValueAt(jTable_Analisis.getSelectedRow(), 4));
        Integer descartables = Integer.parseInt(String.valueOf(modeloTablaAnalisis.getValueAt(jTable_Analisis.getSelectedRow(), 5)));
        String valoresReferencia = String.valueOf(modeloTablaAnalisis.getValueAt(jTable_Analisis.getSelectedRow(), 6));
        Analisis a = new Analisis(codigoAnalisis, nombre, indicaciones, cantUB, consentimiento, descartables, valoresReferencia);

        ManagerAnalisis ma = new ManagerAnalisis();
        //  ma.recuperarAnalisisSeleccionados(codigoAnalisis, nombre, indicaciones, cantUB, consentimiento, descartables,valoresReferencia);


    }//GEN-LAST:event_jTable_AnalisisMouseClicked

    private void jButton2CargarResultadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2CargarResultadoActionPerformed
        Manager_Ordenes mo = new Manager_Ordenes();
        try {
            mo.recuperarResultadosDeTabla(modeloTablaAnalisisResultados, jTable4_cargarValoresAnalisis);
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton2CargarResultadoActionPerformed

    private void jButton1CargarListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1CargarListaActionPerformed
        ArrayList<Analisis> datos = new ArrayList();
        datos = ma.recuperarAnalisisSeleccionados(modeloTablaAnalisisParaSeleccionar, jTableAnalasisParaSeleccionar);
        cargarTablaAnalsisSeleccionados(datos);
    }//GEN-LAST:event_jButton1CargarListaActionPerformed
    public void cargarTablaAnalsisSeleccionados(ArrayList<Analisis> lista) {
        int i;
        for (i = 0; i < jTable_AnalisisSeleccionados.getRowCount(); i++) {
            modeloTablaAnalisisSelecionados.removeRow(i);
        }

        int fila = 0;
        for (Analisis a : lista) {
            modeloTablaAnalisisSelecionados.addRow(new Object[2]);
            jTable_AnalisisSeleccionados.setValueAt(a.getCodigo(), fila, 0);
            jTable_AnalisisSeleccionados.setValueAt(a.getNombre(), fila, 1);

            fila++;

        }

    }

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

        // if controles ( controles de campos invalidos y tambien que no existan repetidos.)
        // realizar control de dni repetidos en tu bd y no cargar valores que ya existan.
        nombre = jTextField_Nombre.getText();
        apellido = jTextField_Apellido.getText();
        try{
        dni = Integer.parseInt(jTextField_dni.getText());
        telefono = Long.parseLong(jTextField_telefono.getText());
        edad = Integer.parseInt(jTextField_Edad.getText());
         }catch (NumberFormatException e){
            jTextField_dni.setBackground(Color.red);
        }
        if (jRadioButton_femenino.isSelected()) {
            sexo = "Femenino";
        } else {
            sexo = "Masculino";
        }
        fNacimiento = dameFecha(fecha);

        if (ValidarCampos(fechaIngreso,nombreMedico, nombre, apellido, dni, telefono, edad,fNacimiento)) {

            // recuparar analsiis de la lista.
            try {
                mp.cargarPaciente(nombre, apellido, dni, telefono, fNacimiento, edad, sexo);
                String obraSocial = (String) jComboBox_ObraSocial.getSelectedItem();
                if (obraSocial.equals("")) {
                } else {
                    mp.cargarObraSocialPaciente(obraSocial, dni);
                }
                codigo = mo.cargarOrden(fechaIngreso, nombreMedico, dni, nombre, obraSocial);

                // carga de los resultados pendientes.
                ArrayList<Analisis> listaCodigos = ma.recuperarCodigos(modeloTablaAnalisisSelecionados, jTable_AnalisisSeleccionados);
                for (Analisis a : listaCodigos) {
                    Resultado r = new Resultado(codigo, a.getCodigo(), a.getNombre());
                    mo.cargarResultado(r);

                }

                /*
            if(listaCodigos.isEmpty()){
            JOptionPane.showMessageDialog(null, "No ha cargado ningun analisis en la lista.");
        }
                 */
            } catch (SQLException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "error al cargar en la base de datos");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "telefono invalido");
            }
            JOptionPane.showMessageDialog(null, "alta con Exito");
        }

    }//GEN-LAST:event_jButton_CargarOrdenActionPerformed
    public Boolean ValidarCampos(String fechaIngreso,String medico, String nombre, String apellido, int dni, Long telefono, int edad, String fechaNacimiento) {

        Boolean control = true;
        ManagerPaciente mp = new ManagerPaciente();
        
        // Control fecha ingreso vacia
        
        if(control){
            if(fechaIngreso == null){
                jDateChooserfechaIngreso.setForeground(Color.red);
                control = false;
            }
        }

        // Control nombre del medico 
        if (control) {
            jTextField_Medico_CargarOrden.setBackground(Color.white);
            String regex = "^[a-zA-Z][a-zA-Z\\s]*$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(medico);
            if (!matcher.matches()) {
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
            if (!matcher.matches()) {
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
        }

        // Control Dni por tamaño 
        if (control) {
            jTextField_dni.setBackground(Color.white);
            if (dni > 99999999 || dni < 1000000) {
                jTextField_dni.setBackground(Color.red);
                control = false;
            }
        }
        // Control edad por tamaño
        if (control) {
            jTextField_Edad.setBackground(Color.white);
            if (edad > 120 || edad < 1) {
                jTextField_Edad.setBackground(Color.red);
            }
        }
        // Control Edad
        if (control) {
            jTextField_Edad.setBackground(Color.white);
            jDateChooser_fechaNacimiento.setForeground(Color.white);

            int año = Calendar.getInstance().get(Calendar.YEAR);
            int añoNacimiento = jDateChooser_fechaNacimiento.getCalendar().get(Calendar.YEAR);
            if (edad != año - añoNacimiento) {
                jTextField_Edad.setBackground(Color.red);
                jDateChooser_fechaNacimiento.setForeground(Color.red);
                control = false;
            }
        }
        
        // Control fecha nacimiento vacio
        
        if(control){
            if(fechaNacimiento == null){
                jDateChooser_fechaNacimiento.setForeground( Color.red);
                control = false;
            }
        }
        return control;

    }


    private void jButton_ListadoPaciente_CargarOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ListadoPaciente_CargarOrdenActionPerformed

    }//GEN-LAST:event_jButton_ListadoPaciente_CargarOrdenActionPerformed

    private void jComboBox_ObraSocialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_ObraSocialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_ObraSocialActionPerformed
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
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JLabel datosPaciente_CargarOrden;
    private javax.swing.JLabel fecha_CargarOrden;
    private javax.swing.JLabel fecha_CargarOrden1;
    private javax.swing.JLabel fecha_CargarOrden2;
    private javax.swing.JButton jButton1CargarLista;
    private javax.swing.JButton jButton2CargarResultado;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton_Analisis_CargarOrden1;
    private javax.swing.JButton jButton_CargarOrden;
    private javax.swing.JButton jButton_Femenino;
    private javax.swing.JButton jButton_ListadoPaciente_CargarOrden;
    private javax.swing.JLabel jButton_ObraSocial_CargarOrden;
    private javax.swing.JComboBox<String> jComboBox_ObraSocial;
    private com.toedter.calendar.JDateChooser jDateChooser_fechaNacimiento;
    private com.toedter.calendar.JDateChooser jDateChooserfechaIngreso;
    private javax.swing.JDialog jDialog_ListadoPacientes;
    private javax.swing.JFrame jFrame_ListadoAnalisis;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2_cargarResultados;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel_menuPestañas;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JRadioButton jRadioButton_femenino;
    private javax.swing.JRadioButton jRadioButton_masculino;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane_menuPestañas;
    private javax.swing.JTable jTable2_TablaOrdenesPendientes;
    private javax.swing.JTable jTable4_cargarValoresAnalisis;
    private javax.swing.JTable jTableAnalasisParaSeleccionar;
    private javax.swing.JTable jTable_Analisis;
    private javax.swing.JTable jTable_AnalisisSeleccionados;
    private javax.swing.JTable jTable_pacientes;
    private javax.swing.JTextArea jTextArea1_valoresDeReferencia;
    private javax.swing.JTextArea jTextArea_cargarOrden;
    private javax.swing.JTextField jTextField_Apellido;
    private javax.swing.JTextField jTextField_Edad;
    private javax.swing.JTextField jTextField_Medico_CargarOrden;
    private javax.swing.JTextField jTextField_Nombre;
    private javax.swing.JTextField jTextField_dni;
    private javax.swing.JTextField jTextField_telefono;
    // End of variables declaration//GEN-END:variables
}
