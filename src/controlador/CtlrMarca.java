/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.ConsultasMarca;
import modelo.Marca;
import modelo.Producto;
import vista.FrmActivos;

/**
 *
 * @author Administrator
 */
public final class CtlrMarca implements ActionListener{
    
    private final Marca marca;
    private final ConsultasMarca cmarca;
    private final FrmActivos vmarca;
    private final DefaultTableModel modelomarca;
    private final DefaultTableModel modelomp;

    public CtlrMarca(Marca marca, ConsultasMarca cmarca, FrmActivos vmarca) {
        this.marca = marca;
        this.cmarca = cmarca;
        this.vmarca = vmarca;
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
            public void mouseClicked(java.awt.event.MouseEvent evt){
                getSelectedMarca();
                llenarTablaProductos();
            }
        });
        this.vmarca.btnEliminarMarca.addActionListener(this);
        this.vmarca.btnInsertarMarca.addActionListener(this);
        this.vmarca.btnModificarMarca.addActionListener(this);
        this.vmarca.btnBuscarMarca.addActionListener(this);
        this.vmarca.jtbMarca.setModel(modelomarca);
        this.vmarca.jtbMarcaProducto.setModel(modelomp);
    }
    public void limpiarTabla(){
        for (int i = 0; i < modelomarca.getRowCount(); i++) {
            modelomarca.removeRow(i);
        }
    }
    public void limpiarTablaProductos(){
        for (int i = 0; i < modelomp.getRowCount(); i++) {
            modelomp.removeRow(i);
        }
    }
    public void llenarTablaProductos(){
        limpiarTablaProductos();
        ArrayList<Producto> productos = cmarca.buscarProductos(marca);
        Object[] array= new Object[4];
        for (int i = 0; i < productos.size(); i++) {
            array[0] = productos.get(i).getCodigo();
            array[1] = productos.get(i).getNombre();
            array[2] = productos.get(i).getPeso();
            array[3] = productos.get(i).getStock();
            modelomp.addRow(array);
        }
    }
    public void llenarTabla(){
        limpiarTabla();
        ArrayList<Marca> marcas = cmarca.getMarcas();
        Object[] array = new Object[2];
        for (int i = 0; i < marcas.size(); i++) {
            array[0] = marcas.get(i).getId();
            array[1] = marcas.get(i).getNombre();
            modelomarca.addRow(array);
        }
    }
    public void getSelectedMarca(){
        int fila = vmarca.jtbMarca.getSelectedRow();
        System.out.println(fila);
        String id = vmarca.jtbMarca.getValueAt(fila,0).toString();
        String[] rs =cmarca.buscarElemento(id);
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
                vmarca.txtIDMarca.setText(marca.getId()+"");
                vmarca.txtNombreMarca.setText(marca.getNombre());
                vmarca.txtObservaciones.setText(marca.getObservaciones());
            }else{
                JOptionPane.showMessageDialog(null, "No se encontro registro");
                limpiar();
            }
            
        }else{
            if(e.getSource() == vmarca.btnEliminarMarca){
                marca.setId(Integer.parseInt(vmarca.txtIDMarca.getText()));
                if (cmarca.eliminar(marca)) {
                    JOptionPane.showMessageDialog(null, "Marca eliminada");
                    limpiar();
                    llenarTabla();
                }else{
                    JOptionPane.showMessageDialog(null, "Error al eliminar");
                    limpiar();
                    llenarTabla();
                }
            }else{
                if (e.getSource() == vmarca.btnInsertarMarca) {
                    marca.setId(Integer.parseInt(vmarca.txtIDMarca.getText()));
                    marca.setNombre(vmarca.txtNombreMarca.getText());
                    marca.setObservaciones(vmarca.txtObservaciones.getText());
                    if (cmarca.insertar(marca)) {
                        JOptionPane.showMessageDialog(null, "Marca insertada");
                        limpiar();
                        llenarTabla();
                    }else{
                        JOptionPane.showMessageDialog(null, "Error al insertar marca");
                        limpiar ();
                        llenarTabla();
                    }
                    
                }else{
                    if (e.getSource() == vmarca.btnModificarMarca) {
                        marca.setId(Integer.parseInt(vmarca.txtIDMarca.getText()));
                        marca.setNombre(vmarca.txtNombre.getText());
                        marca.setObservaciones(vmarca.txtObservaciones.getText());
                        
                        if (cmarca.modificar(marca)) {
                            JOptionPane.showMessageDialog(null, "Marca modificada");
                            limpiar();
                            llenarTabla();
                        }else{
                            JOptionPane.showMessageDialog(null, "Error al modificar marca");
                            limpiar();
                            llenarTabla();
                        }
                    }
                }
            }
            
        }
    }
    
    public void limpiar(){
        vmarca.txtIDMarca.setText("");
        vmarca.txtNombreMarca.setText("");
        vmarca.txtObservaciones.setText("");
    }

    
    
}
