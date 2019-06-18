/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

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
import vista.FrmSalida;

/**
 *
 * @author Administrator
 */
public final class CtlrMovimiento implements ActionListener {

    private final Movimiento movimiento;
    private int idcliente1;
    private int idcliente2;
    private final ArrayList<MovimientoProducto> productos;
    private final ConsultasMovimiento cmovimiento;
    private final FrmActivos vmovimiento;
    private final FrmEntrada ventrada;
    private final FrmSalida vsalida;
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

    public CtlrMovimiento(Movimiento movimiento, MovimientoProducto movprod, ConsultasMovimiento cmovimiento, FrmActivos vmovimiento, FrmEntrada ventrada, FrmSalida vsalida, CtlrBodega ctlrbodega) {
        this.movimiento = movimiento;
        this.productos = new ArrayList<>();
        this.cmovimiento = cmovimiento;
        this.vmovimiento = vmovimiento;
        this.ventrada = ventrada;
        this.vsalida = vsalida;
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
        llenarTablaSalidas();
        llenarTablaEntradas();
        this.vsalida.lstCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                agregarCliente();
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
        this.vsalida.lstCliente.setModel(modelocliente);
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
    }

    public void llenarTablaCliente() {
        limpiarClientes();
        int index = ventrada.lstCliente.getSelectedIndex();
        ListHelper cliente = (ListHelper) modeloclientes.getElementAt(index);
        idcliente2 = cliente.getId();
        System.out.println(idcliente2);

        ArrayList<Producto> prod = cmovimiento.getProductosByID(idcliente2);
        Object[] array = new Object[3];
        for (int i = 0; i < prod.size(); i++) {
            array[0] = prod.get(i).getCodigo();
            array[1] = prod.get(i).getNombre();
            array[2] = prod.get(i).getStock();

            modelol1e.addRow(array);
        }
    }

    public void limpiarClientes() {
        int size = modelol1e.getRowCount();
        for (int i = 0; i < size; i++) {
            modelol1e.removeRow(size - 1 - i);
        }
    }

    public void limpiarProdSalidas(DefaultTableModel modelo) {
        int size = modelo.getRowCount();
        for (int i = 0; i < size; i++) {
            modelo.removeRow(size - 1 - i);
        }
    }

    public void limpiarSalidasProd(DefaultTableModel modelo) {
        int size = modelo.getRowCount();
        for (int i = 0; i < size; i++) {
            modelo.removeRow(size - 1 - i);
        }
    }

    public void limpiarSalidas(DefaultTableModel modelo) {
        int size = modelo.getRowCount();
        for (int i = 0; i < size; i++) {
            modelotbsa.removeRow(size - 1 - i);
        }
    }

    public void llenarTablaSalidasProd() {
        limpiarSalidasProd(modelotbpsa);
        String codigo = vmovimiento.jtbSalida.getValueAt(vmovimiento.jtbSalida.getSelectedRow(), 0).toString();
        ArrayList<Object[]> movprods = cmovimiento.getSalidasProd(codigo);
        for (int i = 0; i < movprods.size(); i++) {

            modelotbpsa.addRow(movprods.get(i));
        }
    }

    public void llenarTablaEntradasProd() {
        limpiarSalidasProd(modelotbpen);
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

    public void llenarTablaBodega() {
        limpiarTablaBodega();
        ArrayList<Producto> prods = cmovimiento.getProductosBodega();
        Object[] array = new Object[3];

        for (int i = 0; i < prods.size(); i++) {
            array[0] = prods.get(i).getCodigo();
            array[1] = prods.get(i).getNombre();
            array[2] = prods.get(i).getStock();
            modelol1s.addRow(array);
        }
    }

    public void limpiarTablaBodega() {
        int size = modelol1s.getRowCount();
        for (int i = 0; i < size; i++) {
            modelol1s.removeRow(size - 1 - i);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vmovimiento.btnSalida) {
            vsalida.setVisible(true);
            llenarListaClientes(modelocliente);
            llenarTablaBodega();
            limpiarProdSalidas(modelol2s);
        } else {
            if (e.getSource() == vmovimiento.btnEntrada) {
                ventrada.setVisible(true);
                llenarListaClientes(modeloclientes);
                limpiarClientes();
                limpiarProdSalidas(modelol2e);

            } else {
                if (e.getSource() == vsalida.btnSumarSalida) {
                    try {
                        int fila = vsalida.jtbP1Salida.getSelectedRow();
                        if (Integer.parseInt(vsalida.jtbP1Salida.getValueAt(fila, 2).toString()) >= Integer.parseInt(vsalida.txtCantidadSalida.getText())) {
                            String codigo = vsalida.jtbP1Salida.getValueAt(fila, 0).toString();
                            for (int i = 0; i < modelol2s.getRowCount(); i++) {
                                if (codigo.equals(vsalida.jtbP2Salida.getValueAt(i, 0).toString())) {
                                    modelol2s.removeRow(i);
                                    break;
                                }
                            }
                            Producto p = cmovimiento.getProductoByCodigo(codigo);

                            Object[] array = new Object[3];
                            array[0] = p.getCodigo();
                            array[1] = p.getNombre();
                            array[2] = vsalida.txtCantidadSalida.getText();

                            modelol2s.addRow(array);
                        } else {
                            JOptionPane.showMessageDialog(null, "Cantidad no disponible");
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println(ex);
                    }
                } else {
                    if (e.getSource() == vsalida.btnAceptarSalida) {
                        try {
                            movimiento.setCodigo(vsalida.txtCodigoSalida.getText());
                            String fecha = vsalida.txtFechaSalida.getText();
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
                            movimiento.setFecha(date);
                            movimiento.setTipo("salida");
                            movimiento.setObservaciones(vsalida.txtObservacionesSalida.getText());
                            movimiento.setUbicacion(idcliente1);
                            if(cmovimiento.insertar(movimiento)){
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
                                limpiarProdSalidas(modelol2s);
                                JOptionPane.showMessageDialog(null, "Salida agregada");
                            }else{
                                JOptionPane.showMessageDialog(null, "Error, Intente nuevamente");
                            }
                        } catch (ParseException ex) {
                            System.out.println(ex);
                        }
                    } else {
                        if (e.getSource() == vsalida.btnRestarSalida) {
                            int row = vsalida.jtbP2Salida.getSelectedRow();
                            modelol2s.removeRow(row);
                        } else {
                            if (e.getSource() == ventrada.btnSumarEntrada) {
                                try {
                                    int fila = ventrada.jtbP1Entrada.getSelectedRow();
                                    if (Integer.parseInt(ventrada.jtbP1Entrada.getValueAt(fila, 2).toString()) >= Integer.parseInt(ventrada.txtCantidadEntrada.getText())) {
                                        String codigo = ventrada.jtbP1Entrada.getValueAt(fila, 0).toString();
                                        for (int i = 0; i < modelol2e.getRowCount(); i++) {
                                            if (codigo.equals(ventrada.jtbP2Entrada.getValueAt(i, 0).toString())) {
                                                modelol2e.removeRow(i);
                                                break;
                                            }
                                        }
                                        Producto p = cmovimiento.getProductoByCodigo(codigo);

                                        Object[] array = new Object[3];
                                        array[0] = p.getCodigo();
                                        array[1] = p.getNombre();
                                        array[2] = ventrada.txtCantidadEntrada.getText();

                                        modelol2e.addRow(array);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Cantidad no disponible");
                                    }
                                } catch (NumberFormatException ex) {
                                    System.out.println(ex);
                                }
                            } else {
                                if (e.getSource() == ventrada.btnAceptarEntrada) {
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
                                        limpiarClientes();
                                        limpiarProdSalidas(modelol2e);
                                        JOptionPane.showMessageDialog(null, "Entrada agregada");
                                    } catch (ParseException ex) {
                                        System.out.println(ex);
                                    }
                                } else {
                                    if (e.getSource() == ventrada.btnRestarEntrada) {
                                        int row = ventrada.jtbP2Entrada.getSelectedRow();
                                        modelol2e.removeRow(row);
                                    } else {
                                        if (e.getSource() == vmovimiento.btnReporteEntrada) {
                                            try{
                                                JasperReport reporte = null;
                                                String path = "src\\reporte\\ReporteEntradas.jasper";

                                                Map parametros = new HashMap();
                                                int fila = vmovimiento.jtbEntradas.getSelectedRow();

                                                parametros.put("codigo", vmovimiento.jtbEntradas.getValueAt(fila, 0) );

                                                reporte = (JasperReport) JRLoader.loadObjectFromFile(path);

                                                JasperPrint jprint = JasperFillManager.fillReport(reporte, parametros, cmovimiento.getConexion());

                                                JasperViewer view = new JasperViewer (jprint, false);

                                                view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

                                                view.setVisible(true);
                                            } catch (JRException ex) {
                                                Logger.getLogger(CtlrUbicacion.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
