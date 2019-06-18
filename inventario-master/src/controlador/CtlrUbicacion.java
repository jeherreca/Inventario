/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import modelo.ConsultasUbicacion;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import modelo.Ubicacion;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import vista.FrmActivos;

/**
 *
 * @author Administrator
 */
public final class CtlrUbicacion implements ActionListener {

    private final Ubicacion proveedor;
    private final ConsultasUbicacion cproveedor;
    private final FrmActivos vproveedor;
    private final DefaultTableModel modelo;
    private final DefaultTableModel modeloc;

    public CtlrUbicacion(Ubicacion proveedor, ConsultasUbicacion cproveedor, FrmActivos vproveedor) {
        this.proveedor = proveedor;
        this.cproveedor = cproveedor;
        this.vproveedor = vproveedor;
        this.modelo = new DefaultTableModel();
        this.modelo.addColumn("ID");
        this.modelo.addColumn("Nombre");
        this.modelo.addColumn("Teléfono");
        this.modelo.addColumn("Dirección");
        this.modelo.addColumn("Ciudad");
        this.modelo.addColumn("Identificación");
        this.modeloc = new DefaultTableModel();
        this.modeloc.addColumn("Código");
        this.modeloc.addColumn("Nombre");
        this.modeloc.addColumn("Peso");
        this.modeloc.addColumn("Cantidad");
        llenarTabla();
        this.vproveedor.jtbProveedores.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                getSelectedProveedor();
                llenarTablaProductos();
                changeLabels();
            }
        });
        this.vproveedor.btnReporteCliente.addActionListener(this);
        this.vproveedor.btnBuscarProveedor.addActionListener(this);
        this.vproveedor.btnInsertarProveedor.addActionListener(this);
        this.vproveedor.btnEliminarProveedor.addActionListener(this);
        this.vproveedor.btnModificarProveedor.addActionListener(this);
        this.vproveedor.jtbProveedores.setModel(modelo);
        this.vproveedor.jtbProveedores.setDefaultEditor(Object.class, null);
        this.vproveedor.jtbUbicacionProducto.setModel(modeloc);
        this.vproveedor.jtbUbicacionProducto.setDefaultEditor(Object.class, null);
    }

    public void changeLabels() {
        vproveedor.lblCantidadCliente.setText(cproveedor.getSum(proveedor.getId()) + "");
        vproveedor.lblPesoCliente.setText(cproveedor.getSumPeso(proveedor.getId()) + "");
    }

    public void llenarTablaProductos() {
        limpiarTablaProductos();
        ArrayList<Object[]> productos = cproveedor.buscarProductos(proveedor);
        for (int i = 0; i < productos.size(); i++) {
            modeloc.addRow(productos.get(i));
        }
    }

    public void llenarTabla() {
        limpiarTabla();
        ArrayList<Ubicacion> proveedores = cproveedor.getUbicacion();
        Object[] array = new Object[6];
        for (int i = 0; i < proveedores.size(); i++) {
            array[0] = proveedores.get(i).getId();
            array[1] = proveedores.get(i).getNombre();
            array[2] = proveedores.get(i).getTelefono();
            array[3] = proveedores.get(i).getDireccion();
            array[4] = proveedores.get(i).getCiudad();
            array[5] = proveedores.get(i).getIdentificacion();
            modelo.addRow(array);
        }
    }

    public void limpiarTablaProductos() {
        int size = modeloc.getRowCount();
        for (int i = 0; i < size; i++) {
            modeloc.removeRow(size - 1 - i);
        }
    }

    public void limpiarTabla() {
        int size = modelo.getRowCount();
        for (int i = 0; i < size; i++) {
            modelo.removeRow(size - 1 - i);
        }
    }

    public void getSelectedProveedor() {
        int fila = vproveedor.jtbProveedores.getSelectedRow();
        System.out.println(fila);
        String codigo = vproveedor.jtbProveedores.getValueAt(fila, 0).toString();
        String[] rs = cproveedor.buscarElemento(codigo);
        proveedor.setId(Integer.parseInt(rs[0]));
        vproveedor.txtIDProveedor.setText(rs[0]);
        vproveedor.txtNombreProveedor.setText(rs[1]);
        vproveedor.txtContacto.setText(rs[2]);
        vproveedor.txtDireccion.setText(rs[3]);
        vproveedor.txtCiudad.setText(rs[4]);
        vproveedor.txtIdentificacion.setText(rs[5]);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vproveedor.btnBuscarProveedor) {
            proveedor.setId(Integer.parseInt(vproveedor.txtIDProveedor.getText()));
            if (cproveedor.buscar(proveedor)) {
                vproveedor.txtIDProveedor.setText(proveedor.getId() + "");
                vproveedor.txtNombreProveedor.setText(proveedor.getNombre());
                vproveedor.txtContacto.setText(proveedor.getTelefono());
                vproveedor.txtDireccion.setText(proveedor.getDireccion());
                vproveedor.txtCiudad.setText(proveedor.getCiudad() + "");
                vproveedor.txtIdentificacion.setText(proveedor.getIdentificacion() + "");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontro registro");
                limpiar();
            }
        } else {
            if (e.getSource() == vproveedor.btnEliminarProveedor) {
                proveedor.setId(Integer.parseInt(vproveedor.txtIDProveedor.getText()));
                if (cproveedor.eliminar(proveedor)) {
                    JOptionPane.showMessageDialog(null, "Cliente eliminado");
                    limpiar();
                    llenarTabla();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar cliente");
                    limpiar();
                    llenarTabla();
                }
            } else {
                if (e.getSource() == vproveedor.btnInsertarProveedor) {
                    proveedor.setNombre(vproveedor.txtNombreProveedor.getText());
                    proveedor.setTelefono(vproveedor.txtContacto.getText());
                    proveedor.setDireccion(vproveedor.txtDireccion.getText());
                    proveedor.setCiudad(vproveedor.txtCiudad.getText());
                    proveedor.setIdentificacion(vproveedor.txtIdentificacion.getText());
                    if (cproveedor.insertar(proveedor)) {
                        JOptionPane.showMessageDialog(null, "Cliente insertado");
                        limpiar();
                        llenarTabla();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al insertar cliente");
                        limpiar();
                        llenarTabla();
                    }
                } else {
                    if (e.getSource() == vproveedor.btnModificarProveedor) {
                        proveedor.setId(Integer.parseInt(vproveedor.txtIDProveedor.getText()));
                        proveedor.setNombre(vproveedor.txtNombreProveedor.getText());
                        proveedor.setTelefono(vproveedor.txtContacto.getText());
                        proveedor.setDireccion(vproveedor.txtDireccion.getText());
                        proveedor.setCiudad(vproveedor.txtCiudad.getText());
                        proveedor.setIdentificacion(vproveedor.txtIdentificacion.getText());

                        if (cproveedor.modificar(proveedor)) {
                            JOptionPane.showMessageDialog(null, "Cliente modificado");
                            limpiar();
                            llenarTabla();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al modificar cliente");
                            limpiar();
                            llenarTabla();
                        }
                    }else{
                        if (e.getSource() == vproveedor.btnReporteCliente) {
                            try{
                                JasperReport reporte = null;
                                String path = "src\\reporte\\ReporteCliente.jasper";
                                
                                Map parametros = new HashMap();
                                int fila = vproveedor.jtbProveedores.getSelectedRow();
                                
                                parametros.put("id", vproveedor.jtbProveedores.getValueAt(fila, 0) );
                                
                                reporte = (JasperReport) JRLoader.loadObjectFromFile(path);
                                
                                JasperPrint jprint = JasperFillManager.fillReport(reporte, parametros, cproveedor.getConexion());
                                
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

    public void limpiar() {
        vproveedor.txtIDProveedor.setText("");
        vproveedor.txtNombreProveedor.setText("");
        vproveedor.txtContacto.setText("");
        vproveedor.txtDireccion.setText("");
        vproveedor.txtCiudad.setText("");
        vproveedor.txtIdentificacion.setText("");
    }
}
