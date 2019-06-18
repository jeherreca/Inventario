/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import modelo.ConsultasMarca;
import modelo.Marca;
import modelo.Producto;
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
public final class CtlrMarca implements ActionListener {

    private final Marca marca;
    private final ConsultasMarca cmarca;
    private final CtlrProducto ctlrproducto;
    private final FrmActivos vmarca;
    private final DefaultTableModel modelomarca;
    private final DefaultTableModel modelomp;

    public CtlrMarca(Marca marca, ConsultasMarca cmarca, FrmActivos vmarca, CtlrProducto ctlrproducto) {
        this.marca = marca;
        this.cmarca = cmarca;
        this.vmarca = vmarca;
        this.ctlrproducto = ctlrproducto;
        this.modelomarca = new DefaultTableModel();
        this.modelomp = new DefaultTableModel();
        this.modelomarca.addColumn("ID");
        this.modelomarca.addColumn("Nombre");
        this.modelomp.addColumn("Código");
        this.modelomp.addColumn("Nombre");
        this.modelomp.addColumn("Peso");
        this.modelomp.addColumn("Cantidad");
        llenarTabla();
        this.vmarca.jtbMarca.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                getSelectedMarca();
                llenarTablaProductos();
                changeLabels();
            }
        });
        this.vmarca.btnEliminarMarca.addActionListener(this);
        this.vmarca.btnInsertarMarca.addActionListener(this);
        this.vmarca.btnModificarMarca.addActionListener(this);
        this.vmarca.btnBuscarMarca.addActionListener(this);
        this.vmarca.btnReporteMarca.addActionListener(this);
        this.vmarca.jtbMarca.setModel(modelomarca);
        this.vmarca.jtbMarca.setDefaultEditor(Object.class, null);
        this.vmarca.jtbMarcaProducto.setModel(modelomp);
        this.vmarca.jtbMarcaProducto.setDefaultEditor(Object.class, null);
    }

    public void limpiarTabla() {
        int size = modelomarca.getRowCount();
        for (int i = 0; i < size; i++) {
            modelomarca.removeRow(size - 1 - i);
        }
    }

    public void limpiarTablaProductos() {
        int size = modelomp.getRowCount();
        for (int i = 0; i < size; i++) {
            modelomp.removeRow(size - 1 - i);
        }
    }

    public void llenarTablaProductos() {
        limpiarTablaProductos();
        ArrayList<Producto> productos = cmarca.buscarProductos(marca);
        Object[] array = new Object[4];
        for (int i = 0; i < productos.size(); i++) {
            array[0] = productos.get(i).getCodigo();
            array[1] = productos.get(i).getNombre();
            array[2] = productos.get(i).getPeso();
            array[3] = productos.get(i).getStock();
            modelomp.addRow(array);
        }
    }

    public void changeLabels() {
        int fila = vmarca.jtbMarca.getSelectedRow();
        String id = vmarca.jtbMarca.getValueAt(fila, 0).toString();
        vmarca.lblPesoMarca.setText(cmarca.getSumPeso(Integer.parseInt(id)) + "");
        vmarca.lblCantidadMarca.setText(cmarca.getSum(Integer.parseInt(id), "cantidad") + "");
    }

    public void llenarTabla() {
        limpiarTabla();
        ArrayList<Marca> marcas = cmarca.getMarcas();
        Object[] array = new Object[2];
        for (int i = 0; i < marcas.size(); i++) {
            array[0] = marcas.get(i).getId();
            array[1] = marcas.get(i).getNombre();
            modelomarca.addRow(array);
        }
    }

    public void getSelectedMarca() {
        int fila = vmarca.jtbMarca.getSelectedRow();
        String id = vmarca.jtbMarca.getValueAt(fila, 0).toString();
        String[] rs = cmarca.buscarElemento(id);
        marca.setId(Integer.parseInt(rs[0]));
        vmarca.txtIDMarca.setText(rs[0]);
        marca.setNombre(rs[1]);
        vmarca.txtNombreMarca.setText(rs[1]);
        marca.setObservaciones(rs[2]);
        vmarca.txtObservaciones.setText(rs[2]);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vmarca.btnBuscarMarca) {
            marca.setId(Integer.parseInt(vmarca.txtIDMarca.getText()));
            if (cmarca.buscar(marca)) {
                vmarca.txtIDMarca.setText(marca.getId() + "");
                vmarca.txtNombreMarca.setText(marca.getNombre());
                vmarca.txtObservaciones.setText(marca.getObservaciones());
            } else {
                JOptionPane.showMessageDialog(null, "No se encontro registro");
                limpiar();
            }

        } else {
            if (e.getSource() == vmarca.btnEliminarMarca) {
                marca.setId(Integer.parseInt(vmarca.txtIDMarca.getText()));
                if (cmarca.eliminar(marca)) {
                    JOptionPane.showMessageDialog(null, "Marca eliminada");
                    ctlrproducto.llenarComboBox();
                    limpiar();
                    llenarTabla();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar");
                    limpiar();
                    llenarTabla();
                }
            } else {
                if (e.getSource() == vmarca.btnInsertarMarca) {
                    marca.setNombre(vmarca.txtNombreMarca.getText());
                    marca.setObservaciones(vmarca.txtObservaciones.getText());
                    if (cmarca.insertar(marca)) {
                        JOptionPane.showMessageDialog(null, "Marca insertada");
                        limpiar();
                        llenarTabla();
                        ctlrproducto.llenarComboBox();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al insertar marca");
                        limpiar();
                        llenarTabla();
                    }

                } else {
                    if (e.getSource() == vmarca.btnModificarMarca) {
                        marca.setId(Integer.parseInt(vmarca.txtIDMarca.getText()));
                        marca.setNombre(vmarca.txtNombreMarca.getText());
                        marca.setObservaciones(vmarca.txtObservaciones.getText());

                        if (cmarca.modificar(marca)) {
                            JOptionPane.showMessageDialog(null, "Marca modificada");
                            limpiar();
                            llenarTabla();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al modificar marca");
                            limpiar();
                            llenarTabla();
                        }
                    } else {
                        if (e.getSource() == vmarca.btnReporteMarca ) {
                            try{
                                JasperReport reporte = null;
                                String path = "src\\reporte\\ReporteMarca.jasper";
                                
                                Map parametros = new HashMap();
                                int fila = vmarca.jtbMarca.getSelectedRow();
                                
                                parametros.put("id", vmarca.jtbMarca.getValueAt(fila, 0) );
                                
                                reporte = (JasperReport) JRLoader.loadObjectFromFile(path);
                                
                                JasperPrint jprint = JasperFillManager.fillReport(reporte, parametros, cmarca.getConexion());
                                
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
        vmarca.txtIDMarca.setText("");
        vmarca.txtNombreMarca.setText("");
        vmarca.txtObservaciones.setText("");
    }

}
