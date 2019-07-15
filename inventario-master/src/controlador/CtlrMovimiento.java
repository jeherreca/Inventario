package controlador;

import inventario.ComboBoxHelper;
import inventario.ListHelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import modelo.ConsultasMovimiento;
import modelo.Movimiento;
import modelo.MovimientoProducto;
import modelo.Producto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import vista.FrmActivos;
import vista.FrmEntrada;
import vista.FrmModificarEntrada;
import vista.FrmModificarSalida;
import vista.FrmSalida;

/**
 *
 * @author Administrator
 */
public final class CtlrMovimiento implements ActionListener {

    private String codes;
    private String codee;
    private final Movimiento movimiento;
    private int idcliente1;
    private int idcliente2;
    private final ConsultasMovimiento cmovimiento;
    private final FrmActivos vmovimiento;
    private final FrmEntrada ventrada;
    private final FrmSalida vsalida;
    private final FrmModificarSalida vmodsalida;
    private final FrmModificarEntrada vmodentrada;
    private final CtlrBodega ctlrbodega;
    private final DefaultTableModel modelol1e;
    private final DefaultTableModel modelol2e;
    private final DefaultTableModel modelol1s;
    private final DefaultTableModel modelol2s;
    private final DefaultListModel modelocliente;
    private final DefaultListModel modeloclientes;
    private final DefaultTableModel modelotben;
    private final DefaultTableModel modelotbsa;
    private final DefaultTableModel modelotbpen;
    private final DefaultTableModel modelotbpsa;
    private final DefaultTableModel modelomods;
    private final DefaultComboBoxModel cbxmodelo;
    private final DefaultTableModel modelolmods;
    private final DefaultTableModel modelomode;
    private final DefaultTableModel modelolmode;
    private final DefaultComboBoxModel cbxmodelo2;

    public CtlrMovimiento(Movimiento movimiento, MovimientoProducto movprod, ConsultasMovimiento cmovimiento, FrmActivos vmovimiento, FrmEntrada ventrada, FrmSalida vsalida, FrmModificarSalida vmodsalida, FrmModificarEntrada vmodentrada, CtlrBodega ctlrbodega) {
        this.movimiento = movimiento;
        this.cmovimiento = cmovimiento;
        this.vmovimiento = vmovimiento;
        this.ventrada = ventrada;
        this.vsalida = vsalida;
        this.vmodsalida = vmodsalida;
        this.vmodentrada = vmodentrada;
        this.ctlrbodega = ctlrbodega;
        this.modelol1e = new DefaultTableModel();
        this.modelol2e = new DefaultTableModel();
        this.modelol1s = new DefaultTableModel();
        this.modelol2s = new DefaultTableModel();
        this.modelocliente = new DefaultListModel();
        this.modeloclientes = new DefaultListModel();
        this.modelotben = new DefaultTableModel();
        this.modelotbsa = new DefaultTableModel();
        this.modelotbpen = new DefaultTableModel();
        this.modelotbpsa = new DefaultTableModel();
        this.modelomods = new DefaultTableModel();
        this.cbxmodelo = new DefaultComboBoxModel();
        this.modelolmods = new DefaultTableModel();
        this.modelolmode = new DefaultTableModel();
        this.modelomode = new DefaultTableModel();
        this.cbxmodelo2 = new DefaultComboBoxModel();
        this.modelotben.addColumn("Código");
        this.modelotben.addColumn("Fecha");
        this.modelotben.addColumn("Ubicación");
        this.modelotben.addColumn("Observaciones");
        this.modelotbsa.addColumn("Código");
        this.modelotbsa.addColumn("Fecha");
        this.modelotbsa.addColumn("Ubicación");
        this.modelotbsa.addColumn("Observaciones");
        this.modelotbpen.addColumn("Código");
        this.modelotbpen.addColumn("Nombre");
        this.modelotbpen.addColumn("Cantidad");
        this.modelotbpsa.addColumn("Código");
        this.modelotbpsa.addColumn("Nombre");
        this.modelotbpsa.addColumn("Cantidad");
        this.modelol1s.addColumn("Código");
        this.modelol1s.addColumn("Nombre");
        this.modelol1s.addColumn("Cantidad");
        this.modelol2s.addColumn("Código");
        this.modelol2s.addColumn("Nombre");
        this.modelol2s.addColumn("Cantidad");
        this.modelol1e.addColumn("Código");
        this.modelol1e.addColumn("Nombre");
        this.modelol1e.addColumn("Cantidad");
        this.modelol2e.addColumn("Código");
        this.modelol2e.addColumn("Nombre");
        this.modelol2e.addColumn("Cantidad");
        this.modelolmods.addColumn("Código");
        this.modelolmods.addColumn("Cantidad");
        this.modelomods.addColumn("Código");
        this.modelomods.addColumn("Cantidad");
        this.modelolmode.addColumn("Código");
        this.modelolmode.addColumn("Cantidad");
        this.modelomode.addColumn("Código");
        this.modelomode.addColumn("Cantidad");
        llenarTablaSalidas();
        llenarTablaEntradas();
        this.vsalida.lstCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                agregarCliente();
                limpiarSalidas(modelol2s);
                llenarTablaBodega();
            }
        });
        this.vmovimiento.jtbSalida.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                llenarTablaSalidasProd();
            }
        });
        this.vmovimiento.jtbEntradas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                llenarTablaEntradasProd();
            }
        });
        this.ventrada.lstCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                llenarTablaCliente();
                limpiarSalidas(modelol2e);
            }
        });
        this.vmovimiento.btnSalida.addActionListener(this);
        this.vmovimiento.btnEntrada.addActionListener(this);
        this.ventrada.btnAceptarEntrada.addActionListener(this);
        this.vsalida.btnAceptarSalida.addActionListener(this);
        this.vsalida.btnRestarSalida.addActionListener(this);
        this.vsalida.btnSumarSalida.addActionListener(this);
        this.ventrada.btnSumarEntrada.addActionListener(this);
        this.ventrada.btnRestarEntrada.addActionListener(this);
        this.vmovimiento.btnReporteEntrada.addActionListener(this);
        this.vmovimiento.btnReporteSalida.addActionListener(this);
        this.vmovimiento.btnModificarSalida.addActionListener(this);
        this.vmovimiento.btnModificarEntrada.addActionListener(this);
        this.vmodsalida.btnAceptarMod.addActionListener(this);
        this.vmodsalida.btnRemoverModSalida.addActionListener(this);
        this.vmodsalida.btnSumarProducto.addActionListener(this);
        this.vmodentrada.btnAceptarMod.addActionListener(this);
        this.vmodentrada.btnRemoverModEntrada.addActionListener(this);
        this.vmodentrada.btnSumarProducto.addActionListener(this);
        this.vsalida.lstCliente.setModel(modelocliente);
        this.vmodsalida.jtbBodega.setModel(modelolmods);
        this.vsalida.jtbP1Salida.setModel(modelol1s);
        this.vsalida.jtbP1Salida.setDefaultEditor(Object.class, null);
        this.vsalida.jtbP2Salida.setModel(modelol2s);
        this.vsalida.jtbP2Salida.setDefaultEditor(Object.class, null);
        this.vmovimiento.jtbEntradas.setModel(modelotben);
        this.vmovimiento.jtbEntradas.setDefaultEditor(Object.class, null);
        this.vmovimiento.jtbProEntradas.setModel(modelotbpen);
        this.vmovimiento.jtbProEntradas.setDefaultEditor(Object.class, null);
        this.vmovimiento.jtbSalida.setModel(modelotbsa);
        this.vmovimiento.jtbSalida.setDefaultEditor(Object.class, null);
        this.vmovimiento.jtbProSalidas.setModel(modelotbpsa);
        this.vmovimiento.jtbProSalidas.setDefaultEditor(Object.class, null);
        this.ventrada.lstCliente.setModel(modeloclientes);
        this.ventrada.jtbP1Entrada.setModel(modelol1e);
        this.ventrada.jtbP1Entrada.setDefaultEditor(Object.class, null);
        this.ventrada.jtbP2Entrada.setModel(modelol2e);
        this.ventrada.jtbP2Entrada.setDefaultEditor(Object.class, null);
        this.vmodsalida.jtbModProductosS.setModel(modelomods);
        this.vmodsalida.cbxModClienteS.setModel(cbxmodelo);
        this.vmodentrada.cbxModClienteE.setModel(cbxmodelo2);
        this.vmodentrada.jtbClientes.setModel(modelolmode);
        this.vmodentrada.jtbModProductosE.setModel(modelomode);
        
    }

    public void llenarTablaCliente() {
        limpiarSalidas(modelol1e);
        int index = ventrada.lstCliente.getSelectedIndex();
        ListHelper cliente = (ListHelper) modeloclientes.getElementAt(index);
        idcliente2 = cliente.getId();

        ArrayList<Producto> prod = cmovimiento.getProductosByID(idcliente2);
        Object[] array = new Object[3];
        for (int i = 0; i < prod.size(); i++) {
            array[0] = prod.get(i).getCodigo();
            array[1] = prod.get(i).getNombre();
            array[2] = prod.get(i).getStock();

            modelol1e.addRow(array);
        }
    }

    public void limpiarSalidas(DefaultTableModel modelo) {
        int size = modelo.getRowCount();
        for (int i = 0; i < size; i++) {
            modelo.removeRow(size - 1 - i);
        }
    }

    public void llenarTablaSalidasProd() {
        limpiarSalidas(modelotbpsa);
        String codigo = vmovimiento.jtbSalida.getValueAt(vmovimiento.jtbSalida.getSelectedRow(), 0).toString();
        ArrayList<Object[]> movprods = cmovimiento.getSalidasProd(codigo);
        for (int i = 0; i < movprods.size(); i++) {

            modelotbpsa.addRow(movprods.get(i));
        }
    }

    public void llenarTablaEntradasProd() {
        limpiarSalidas(modelotbpen);
        String codigo = vmovimiento.jtbEntradas.getValueAt(vmovimiento.jtbEntradas.getSelectedRow(), 0).toString();
        ArrayList<Object[]> movprods = cmovimiento.getSalidasProd(codigo);
        for (int i = 0; i < movprods.size(); i++) {
            modelotbpen.addRow(movprods.get(i));
        }
    }

    public void agregarCliente() {
        int index = vsalida.lstCliente.getSelectedIndex();
        ListHelper cliente = (ListHelper) modelocliente.getElementAt(index);
        idcliente1 = cliente.getId();
        System.out.println(idcliente1);
    }

    public void llenarListaClientes(DefaultListModel modelo) {
        modelo.removeAllElements();
        ArrayList<ListHelper> clientes = cmovimiento.getClientes();

        for (int i = 0; i < clientes.size(); i++) {
            modelo.addElement(clientes.get(i));
        }
    }

    public void llenarTablaSalidas() {
        limpiarSalidas(modelotbsa);
        ArrayList<Movimiento> prods = cmovimiento.getMovimientos("salida");
        Object[] array = new Object[4];

        for (int i = 0; i < prods.size(); i++) {
            array[0] = prods.get(i).getCodigo();
            array[1] = prods.get(i).getFecha();
            array[2] = cmovimiento.getUbicacion(prods.get(i).getUbicacion());
            array[3] = prods.get(i).getObservaciones();
            modelotbsa.addRow(array);
        }
    }

    public void llenarTablaEntradas() {
        limpiarSalidas(modelotben);
        ArrayList<Movimiento> prods = cmovimiento.getMovimientos("entrada");
        Object[] array = new Object[4];

        for (int i = 0; i < prods.size(); i++) {
            array[0] = prods.get(i).getCodigo();
            array[1] = prods.get(i).getFecha();
            array[2] = cmovimiento.getUbicacion(prods.get(i).getUbicacion());
            array[3] = prods.get(i).getObservaciones();
            modelotben.addRow(array);
        }
    }

    public void llenarComboBox(DefaultComboBoxModel cbxmodelo) {
        cbxmodelo.removeAllElements();
        ArrayList<ComboBoxHelper> clientes = cmovimiento.getComboBoxClientes();
        for (int i = 0; i < clientes.size(); i++) {
            cbxmodelo.addElement(clientes.get(i));
        }
    }
    
    public void llenarTablaBodega() {
        limpiarSalidas(modelol1s);
        ArrayList<Producto> prods = cmovimiento.getProductosBodega();
        Object[] array = new Object[3];

        for (int i = 0; i < prods.size(); i++) {
            array[0] = prods.get(i).getCodigo();
            array[1] = prods.get(i).getNombre();
            array[2] = prods.get(i).getStock();
            modelol1s.addRow(array);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vmovimiento.btnSalida) {
            vsalida.setVisible(true);
            llenarListaClientes(modelocliente);
            llenarTablaBodega();
            limpiarSalidas(modelol2s);

        } else if (e.getSource() == vmovimiento.btnEntrada) {

            ventrada.setVisible(true);
            llenarListaClientes(modeloclientes);
            limpiarSalidas(modelol1e);
            limpiarSalidas(modelol2e);

        } else if (e.getSource() == vsalida.btnSumarSalida) {

            try {
                int fila = vsalida.jtbP1Salida.getSelectedRow();
                if (Integer.parseInt(vsalida.jtbP1Salida.getValueAt(fila, 2).toString()) >= Integer.parseInt(vsalida.txtCantidadSalida.getText())) {
                    int sw = 0;
                    String codigo = vsalida.jtbP1Salida.getValueAt(fila, 0).toString();
                    for (int i = 0; i < modelol2s.getRowCount(); i++) {
                        int cantidad = Integer.parseInt(vsalida.jtbP2Salida.getValueAt(i, 2).toString());
                        if (codigo.equals(vsalida.jtbP2Salida.getValueAt(i, 0).toString())) {
                            modelol2s.setValueAt(cantidad + Integer.parseInt(vsalida.txtCantidadSalida.getText()), i, 2);
                            modelol1s.setValueAt(Integer.parseInt(vsalida.jtbP1Salida.getValueAt(fila, 2).toString()) - Integer.parseInt(vsalida.txtCantidadSalida.getText()), fila, 2);
                            sw = 1;
                            break;
                        }
                    }
                    if (sw == 0) {                    
                        Producto p = cmovimiento.getProductoByCodigo(codigo);
                        Object[] array = new Object[3];
                        array[0] = p.getCodigo();
                        array[1] = p.getNombre();
                        array[2] = vsalida.txtCantidadSalida.getText();
                        modelol1s.setValueAt(Integer.parseInt(vsalida.jtbP1Salida.getValueAt(fila, 2).toString()) - Integer.parseInt(array[2].toString()), fila, 2);
                        modelol2s.addRow(array);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Cantidad no disponible");
                }
            } catch (NumberFormatException ex) {
                System.out.println(ex);
            }

        } else if (e.getSource() == vsalida.btnAceptarSalida) {

            try {
                movimiento.setCodigo(vsalida.txtCodigoSalida.getText());
                String fecha = vsalida.txtFechaSalida.getText();
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
                movimiento.setFecha(date);
                movimiento.setTipo("salida");
                movimiento.setObservaciones(vsalida.txtObservacionesSalida.getText());
                movimiento.setUbicacion(idcliente1);
                if (cmovimiento.insertar(movimiento)) {
                    int idmov = cmovimiento.getIDByCode(vsalida.txtCodigoSalida.getText());
                    for (int i = 0; i < modelol2s.getRowCount(); i++) {
                        MovimientoProducto movprod = new MovimientoProducto();
                        movprod.setActivo(cmovimiento.getProductoByCodigo(modelol2s.getValueAt(i, 0).toString()).getId());
                        movprod.setMovimiento(idmov);
                        movprod.setCantidad(Integer.parseInt(modelol2s.getValueAt(i, 2).toString()));
                        int cantidad = movprod.getCantidad();
                        cmovimiento.restarBodega(cantidad, cmovimiento.getProductoByCodigo(modelol2s.getValueAt(i, 0).toString()).getId());
                        cmovimiento.insertarMovimientoProducto(movprod);
                        if (cmovimiento.verificar(movprod.getActivo(), idcliente1)) {
                            cmovimiento.sumarUbicacionProducto(cantidad, movprod.getActivo(), idcliente1);
                        } else {
                            cmovimiento.insertarUbicacionProducto(idcliente1, cantidad, movprod.getActivo());
                        }
                    }
                    ctlrbodega.llenarTabla();
                    llenarTablaSalidas();
                    llenarTablaBodega();
                    limpiarSalidas(modelol2s);
                    JOptionPane.showMessageDialog(null, "Salida agregada");
                } else {
                    JOptionPane.showMessageDialog(null, "Error, Intente nuevamente");
                }
            } catch (ParseException ex) {
                System.out.println(ex);
            }

        } else if (e.getSource() == vsalida.btnRestarSalida) {

            int row = vsalida.jtbP2Salida.getSelectedRow();
            int cantidad = Integer.parseInt(vsalida.jtbP2Salida.getValueAt(row, 2).toString());
            String c = vsalida.jtbP2Salida.getValueAt(row, 0).toString();
            for (int i = 0; i < modelol1s.getRowCount(); i++) {
                if (c.equals(vsalida.jtbP1Salida.getValueAt(i, 0).toString())) {
                    int cantidadBodega = (int) vsalida.jtbP1Salida.getValueAt(i, 2);
                    modelol1s.setValueAt(cantidadBodega + cantidad, i, 2);
                }
            }
            modelol2s.removeRow(row);
        } else if (e.getSource() == ventrada.btnSumarEntrada) {

            try {
                int fila = ventrada.jtbP1Entrada.getSelectedRow();
                if (Integer.parseInt(ventrada.jtbP1Entrada.getValueAt(fila, 2).toString()) >= Integer.parseInt(ventrada.txtCantidadEntrada.getText())) {
                    String codigo = ventrada.jtbP1Entrada.getValueAt(fila, 0).toString();
                    int sw=0;
                    for (int i = 0; i < modelol2e.getRowCount(); i++) {
                        int cantidad = Integer.parseInt(modelol2e.getValueAt(i, 2).toString());
                        if (codigo.equals(ventrada.jtbP2Entrada.getValueAt(i, 0).toString())) {
                            modelol2e.setValueAt(cantidad + Integer.parseInt(ventrada.txtCantidadEntrada.getText()), i, 2);
                            modelol1e.setValueAt(Integer.parseInt(ventrada.jtbP1Entrada.getValueAt(fila, 2).toString()) - Integer.parseInt(ventrada.txtCantidadEntrada.getText()), fila, 2);
                            sw=1;
                            break;
                        }
                    }
                    if (sw==0) {
                        Producto p = cmovimiento.getProductoByCodigo(codigo);

                        Object[] array = new Object[3];
                        array[0] = p.getCodigo();
                        array[1] = p.getNombre();
                        array[2] = ventrada.txtCantidadEntrada.getText();
                        modelol1e.setValueAt(Integer.parseInt(ventrada.jtbP1Entrada.getValueAt(fila, 2).toString()) - Integer.parseInt(array[2].toString()), fila, 2);
                        modelol2e.addRow(array);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Cantidad no disponible");
                }
            } catch (NumberFormatException ex) {
                System.out.println(ex);
            }

        } else if (e.getSource() == ventrada.btnAceptarEntrada) {

            try {
                movimiento.setCodigo(ventrada.txtCodigoEntrada.getText());
                String fecha = ventrada.txtFechaEntrada.getText();
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
                movimiento.setFecha(date);
                movimiento.setTipo("entrada");
                movimiento.setObservaciones(ventrada.txtObservacionesEntrada.getText());
                movimiento.setUbicacion(idcliente2);
                cmovimiento.insertar(movimiento);
                int idmov = cmovimiento.getIDByCode(ventrada.txtCodigoEntrada.getText());
                for (int i = 0; i < modelol2e.getRowCount(); i++) {
                    MovimientoProducto movprod = new MovimientoProducto();
                    movprod.setActivo(cmovimiento.getProductoByCodigo(modelol2e.getValueAt(i, 0).toString()).getId());
                    movprod.setMovimiento(idmov);
                    movprod.setCantidad(Integer.parseInt(modelol2e.getValueAt(i, 2).toString()));
                    int cantidad = movprod.getCantidad();
                    cmovimiento.restarCliente(cantidad, idcliente2, movprod.getActivo());
                    cmovimiento.insertarMovimientoProducto(movprod);
                    cmovimiento.sumarBodega(cantidad, movprod.getActivo());
                }
                llenarTablaEntradas();
                limpiarSalidas(modelol1e);
                limpiarSalidas(modelol2e);
                JOptionPane.showMessageDialog(null, "Entrada agregada");
            } catch (ParseException ex) {
                System.out.println(ex);
            }

        } else if (e.getSource() == ventrada.btnRestarEntrada) {

            int row = ventrada.jtbP2Entrada.getSelectedRow();
            int cantidad = Integer.parseInt(ventrada.jtbP2Entrada.getValueAt(row, 2).toString());
            String c = ventrada.jtbP2Entrada.getValueAt(row, 0).toString();
            for (int i = 0; i < modelol1e.getRowCount(); i++) {
                if (c.equals(modelol1e.getValueAt(i, 0))) {
                    int cantidadBodega = (int) ventrada.jtbP1Entrada.getValueAt(i, 2);
                    modelol1e.setValueAt(cantidadBodega+cantidad, i, 2);
                }
            }
            modelol2e.removeRow(row);
        } else if (e.getSource() == vmovimiento.btnReporteEntrada) {

            try {
                JasperReport reporte = null;
                String path = "src\\reporte\\ReporteEntradas.jasper";
                Map parametros = new HashMap();
                int fila = vmovimiento.jtbEntradas.getSelectedRow();
                parametros.put("codigo", vmovimiento.jtbEntradas.getValueAt(fila, 0));
                reporte = (JasperReport) JRLoader.loadObjectFromFile(path);
                JasperPrint jprint = JasperFillManager.fillReport(reporte, parametros, cmovimiento.getConexion());
                JasperViewer view = new JasperViewer(jprint, false);
                view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                view.setVisible(true);
            } catch (JRException ex) {
                Logger.getLogger(CtlrUbicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getSource() == vmovimiento.btnReporteSalida) {

            try {
                JasperReport reporte = null;
                String path = "src\\reporte\\ReporteSalidas.jasper";
                Map parametros = new HashMap();
                int fila = vmovimiento.jtbSalida.getSelectedRow();
                parametros.put("codigo", vmovimiento.jtbSalida.getValueAt(fila, 0));
                reporte = (JasperReport) JRLoader.loadObjectFromFile(path);
                JasperPrint jprint = JasperFillManager.fillReport(reporte, parametros, cmovimiento.getConexion());
                JasperViewer view = new JasperViewer(jprint, false);
                view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                view.setVisible(true);
            } catch (JRException ex) {
                Logger.getLogger(CtlrUbicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getSource() == vmovimiento.btnModificarSalida) {
            
            int row = vmovimiento.jtbSalida.getSelectedRow();
            limpiarSalidas(modelolmods);
            if (row != (-1)) {
                codes = vmovimiento.jtbSalida.getValueAt(row, 0).toString();
                cmovimiento.getMovimiento(codes, movimiento);
                vmodsalida.setVisible(true);
                llenarComboBox(cbxmodelo);
                vmodsalida.txtModCodigoS.setText(movimiento.getCodigo());
                vmodsalida.txtModFechaS.setText(movimiento.getFecha() + "");
                vmodsalida.txtModObservacionesS.setText(movimiento.getObservaciones());
                limpiarSalidas(modelomods);
                vmodsalida.cbxModClienteS.getModel().setSelectedItem(new ComboBoxHelper(cmovimiento.getIdClienteByName(vmovimiento.jtbSalida.getValueAt(row, 2).toString()), vmovimiento.jtbSalida.getValueAt(row, 2).toString()));
                for (int i = 0; i < vmovimiento.jtbProSalidas.getRowCount(); i++) {
                    modelomods.addRow(new Object[]{vmovimiento.jtbProSalidas.getValueAt(i, 0), vmovimiento.jtbProSalidas.getValueAt(i, 2)});
                }
                ArrayList<Producto> bodega = cmovimiento.getProductosBodega();
                for (int i = 0; i < bodega.size(); i++) {
                    modelolmods.addRow(new Object[]{bodega.get(i).getCodigo(), bodega.get(i).getStock()});
                }
            } else {
                JOptionPane.showMessageDialog(null, "No selecciono ningún movimiento");
            }
        } else if (e.getSource() == vmodsalida.btnAceptarMod) {

            for (int i = 0; i < modelotbpsa.getRowCount(); i++) {                
                cmovimiento.sumarBodega(Integer.parseInt(vmovimiento.jtbProSalidas.getValueAt(i, 2).toString()), cmovimiento.getProductoByCodigo(vmovimiento.jtbProSalidas.getValueAt(i, 0).toString()).getId());
                cmovimiento.restarCliente(Integer.parseInt(vmovimiento.jtbProSalidas.getValueAt(i, 2).toString()), cmovimiento.getIdClienteByName(vmovimiento.jtbSalida.getValueAt(vmovimiento.jtbSalida.getSelectedRow(), 2).toString()), cmovimiento.getProductoByCodigo(vmovimiento.jtbProSalidas.getValueAt(i, 0).toString()).getId());
            }
            cmovimiento.borrarMovimientos(codes);
            String fecha = vmodsalida.txtModFechaS.getText();
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
            } catch (ParseException ex) {
                System.out.println(ex);
            }
            movimiento.setFecha(date);
            movimiento.setObservaciones(vmodsalida.txtModObservacionesS.getText());
            ComboBoxHelper ub = (ComboBoxHelper) vmodsalida.cbxModClienteS.getSelectedItem();
            movimiento.setUbicacion(ub.getId());
            if (cmovimiento.updateMovimiento(movimiento, cmovimiento.getIDByCode(vmodsalida.txtModCodigoS.getText()))) {
                int idmov = cmovimiento.getIDByCode(vmodsalida.txtModCodigoS.getText());
                for (int i = 0; i < vmodsalida.jtbModProductosS.getRowCount(); i++) {
                    MovimientoProducto movprod = new MovimientoProducto();
                    movprod.setActivo(cmovimiento.getProductoByCodigo(modelomods.getValueAt(i, 0).toString()).getId());
                    movprod.setMovimiento(idmov);
                    movprod.setCantidad(Integer.parseInt(modelomods.getValueAt(i, 1).toString()));
                    int cantidad = movprod.getCantidad();
                    cmovimiento.restarBodega(cantidad, cmovimiento.getProductoByCodigo(modelomods.getValueAt(i, 0).toString()).getId());
                    cmovimiento.insertarMovimientoProducto(movprod);
                    
                    if (cmovimiento.verificar(movprod.getActivo(), movimiento.getUbicacion())) {
                        
                        cmovimiento.sumarUbicacionProducto(cantidad, movprod.getActivo(), movimiento.getUbicacion());
                    } else {
                        cmovimiento.insertarUbicacionProducto(movimiento.getUbicacion(), cantidad, movprod.getActivo());
                    }
                }
            }
        } else if (e.getSource() == vmodsalida.btnRemoverModSalida) {
            int row = vmodsalida.jtbModProductosS.getSelectedRow();
            int cantidad = Integer.parseInt(vmodsalida.jtbModProductosS.getValueAt(row, 1).toString());
            String c = vmodsalida.jtbModProductosS.getValueAt(row, 0).toString();
            for (int i = 0; i < modelolmods.getRowCount(); i++) {
                if (c.equals(vmodsalida.jtbBodega.getValueAt(i, 0).toString())) {
                    int cantidadBodega = Integer.parseInt(vmodsalida.jtbBodega.getValueAt(i,1).toString());
                    modelolmods.setValueAt(cantidad + cantidadBodega, i, 1);
                }
            }
            modelomods.removeRow(row);
        } else if (e.getSource() == vmodsalida.btnSumarProducto){
            int fila = vmodsalida.jtbBodega.getSelectedRow();
            if (Integer.parseInt(modelolmods.getValueAt(fila, 1).toString()) >= Integer.parseInt(vmodsalida.txtModCantidad.getText())) {
                int cantidad = Integer.parseInt(vmodsalida.txtModCantidad.getText());
                int sw = 0;
                String c = modelolmods.getValueAt(fila, 0).toString();
                for (int i = 0; i < modelomods.getRowCount(); i++) {
                    if (c.equals(modelomods.getValueAt(i, 0).toString())) {
                        modelomods.setValueAt(cantidad + Integer.parseInt(modelomods.getValueAt(i, 1).toString()),i,1);
                        modelolmods.setValueAt(Integer.parseInt(modelolmods.getValueAt(fila, 1).toString()) - cantidad, fila, 1);
                        sw=1;
                        break;
                    }
                }
                if (sw == 0) {
                    Producto p = cmovimiento.getProductoByCodigo(c);

                    Object[] array = new Object[3];
                    array[0] = p.getCodigo();
                    array[1] = vmodsalida.txtModCantidad.getText();
                    modelolmods.setValueAt(Integer.parseInt(modelolmods.getValueAt(fila, 1).toString()) - cantidad, fila, 1);
                    modelomods.addRow(array);
                }
            }else{
                JOptionPane.showMessageDialog(null, "Cantidad no disponible");
            }
        } else if (e.getSource() == vmovimiento.btnModificarEntrada) {
            int row = vmovimiento.jtbEntradas.getSelectedRow();
            limpiarSalidas(modelolmode);
            if (row != (-1)) {
                codee = vmovimiento.jtbEntradas.getValueAt(row, 0).toString();
                cmovimiento.getMovimiento(codee, movimiento);
                vmodentrada.setVisible(true);
                llenarComboBox(cbxmodelo2);
                vmodentrada.txtModCodigoE.setText(movimiento.getCodigo());
                vmodentrada.txtModFechaE.setText(movimiento.getFecha() + "");
                vmodentrada.txtModObservacionesE.setText(movimiento.getObservaciones());
                limpiarSalidas(modelomode);
                vmodentrada.cbxModClienteE.getModel().setSelectedItem(new ComboBoxHelper(cmovimiento.getIdClienteByName(vmovimiento.jtbEntradas.getValueAt(row, 2).toString()), vmovimiento.jtbEntradas.getValueAt(row, 2).toString()));
                for (int i = 0; i < vmovimiento.jtbProEntradas.getRowCount(); i++) {
                    modelomode.addRow(new Object[]{vmovimiento.jtbProEntradas.getValueAt(i, 0), vmovimiento.jtbProEntradas.getValueAt(i, 2)});
                }
                ArrayList<Producto> cliente = cmovimiento.getProductosByID(movimiento.getUbicacion());
                for (int i = 0; i < cliente.size(); i++) {
                    modelolmode.addRow(new Object[]{cliente.get(i).getCodigo(), cliente.get(i).getStock()});
                }
            }
        } else if(e.getSource() == vmodentrada.btnRemoverModEntrada){
            int row = vmodentrada.jtbModProductosE.getSelectedRow();
            int cantidad = Integer.parseInt(modelomode.getValueAt(row, 1).toString());
            String c = modelomode.getValueAt(row, 0).toString();
            for (int i = 0; i < modelolmode.getRowCount(); i++) {
                if (c.equals(modelolmode.getValueAt(i, 0).toString())) {
                    int cantidadCliente = Integer.parseInt(modelolmode.getValueAt(i, 1).toString());
                    modelolmode.setValueAt(cantidadCliente+cantidad, i, 1);
                }
            }
            modelomode.removeRow(row);
        } else if(e.getSource() == vmodentrada.btnSumarProducto){
            int fila = vmodentrada.jtbClientes.getSelectedRow();
            if (Integer.parseInt(modelolmode.getValueAt(fila, 1).toString()) >= Integer.parseInt(vmodentrada.txtModCantidad.getText())) {
                int cantidad = Integer.parseInt(vmodentrada.txtModCantidad.getText());
                int sw = 0;
                String c = modelolmode.getValueAt(fila, 0).toString();
                for (int i = 0; i < modelomode.getRowCount(); i++) {
                    if (c.equals(modelomode.getValueAt(i, 0).toString())) {
                        modelomode.setValueAt(cantidad+Integer.parseInt(modelomode.getValueAt(i, 1).toString()), i, 1);
                        modelolmode.setValueAt(Integer.parseInt(modelolmode.getValueAt(fila, 1).toString()) - cantidad, fila, 1);
                        sw=1;
                        break;
                    }
                }
                if (sw == 0) {
                    Producto p = cmovimiento.getProductoByCodigo(c);

                    Object[] array = new Object[3];
                    array[0] = p.getCodigo();
                    array[1] = vmodentrada.txtModCantidad.getText();
                    modelolmode.setValueAt(Integer.parseInt(modelolmode.getValueAt(fila, 1).toString()) - cantidad, fila, 1);
                    modelomode.addRow(array);                    
                }
            }else{
                JOptionPane.showMessageDialog(null, "Cantidad no disponible");
            }
        } else if (e.getSource() == vmodentrada.btnAceptarMod){
            for (int i = 0; i < vmovimiento.jtbProEntradas.getRowCount(); i++) {
                cmovimiento.restarBodega(Integer.parseInt(vmovimiento.jtbProEntradas.getValueAt(i, 2).toString()), cmovimiento.getProductoByCodigo(vmovimiento.jtbProEntradas.getValueAt(i, 0).toString()).getId());
                cmovimiento.sumarUbicacionProducto(Integer.parseInt(vmovimiento.jtbProEntradas.getValueAt(i, 2).toString()),cmovimiento.getProductoByCodigo(vmovimiento.jtbProEntradas.getValueAt(i, 0).toString()).getId(),cmovimiento.getIdClienteByName(vmovimiento.jtbEntradas.getValueAt(vmovimiento.jtbEntradas.getSelectedRow(), 2).toString()) );
            }
            cmovimiento.borrarMovimientos(codee);
            try {
                String fecha = vmodentrada.txtModFechaE.getText();
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
                movimiento.setFecha(date);
                movimiento.setTipo("entrada");
                movimiento.setObservaciones(vmodentrada.txtModObservacionesE.getText());
                ComboBoxHelper ub = (ComboBoxHelper) vmodentrada.cbxModClienteE.getSelectedItem();
                movimiento.setUbicacion(ub.getId());
                cmovimiento.updateMovimiento(movimiento, cmovimiento.getIDByCode(vmodentrada.txtModCodigoE.getText()));
                int idmov = cmovimiento.getIDByCode(vmodentrada.txtModCodigoE.getText());
                for (int i = 0; i < modelomode.getRowCount(); i++) {
                    MovimientoProducto movprod = new MovimientoProducto();
                    movprod.setActivo(cmovimiento.getProductoByCodigo(modelomode.getValueAt(i, 0).toString()).getId());
                    movprod.setMovimiento(idmov);
                    movprod.setCantidad(Integer.parseInt(modelomode.getValueAt(i, 1).toString()));
                    int cantidad = movprod.getCantidad();
                    cmovimiento.restarCliente(cantidad, ub.getId(), movprod.getActivo());
                    cmovimiento.insertarMovimientoProducto(movprod);
                    cmovimiento.sumarBodega(cantidad, movprod.getActivo());
                }
                llenarTablaEntradas();
                limpiarSalidas(modelomode);
                limpiarSalidas(modelolmode);
                JOptionPane.showMessageDialog(null, "Entrada agregada");
            } catch (ParseException ex) {
                System.out.println(ex);
            }
        }
    }
}