/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import modelo.ConsultasProveedor;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Producto;
import modelo.Proveedor;
import vista.FrmActivos;

/**
 *
 * @author Administrator
 */
public final class CtlrProveedor implements ActionListener{
    
    private final Proveedor proveedor;
    private final ConsultasProveedor cproveedor;
    private final FrmActivos vproveedor;
    private final DefaultTableModel modelo;

    public CtlrProveedor(Proveedor proveedor, ConsultasProveedor cproveedor, FrmActivos vproveedor) {
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
        llenarTabla();
        this.vproveedor.jtbProveedores.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                getSelectedProveedor();
            }
        });
        this.vproveedor.btnBuscarProveedor.addActionListener(this);
        this.vproveedor.btnInsertarProveedor.addActionListener(this);
        this.vproveedor.btnEliminarProveedor.addActionListener(this);
        this.vproveedor.btnModificarProveedor.addActionListener(this);
        this.vproveedor.jtbProveedores.setModel(modelo);
    }
 public void llenarTabla(){
        limpiarTabla();
        ArrayList<Proveedor> proveedores = cproveedor.getProveedores();
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
    public void limpiarTabla(){
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
        }
    }
    
    public void getSelectedProveedor(){
        int fila = vproveedor.jtbProveedores.getSelectedRow();
        System.out.println(fila);
        String codigo = vproveedor.jtbProveedores.getValueAt(fila,0).toString();
        String[] rs = cproveedor.buscarElemento(codigo);
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
                vproveedor.txtIDProveedor.setText(proveedor.getId()+"");
                vproveedor.txtNombreProveedor.setText(proveedor.getNombre());
                vproveedor.txtContacto.setText(proveedor.getTelefono());
                vproveedor.txtDireccion.setText(proveedor.getDireccion());
                vproveedor.txtCiudad.setText(proveedor.getCiudad()+"");
                vproveedor.txtIdentificacion.setText(proveedor.getIdentificacion()+"");
            }else{
                JOptionPane.showMessageDialog(null, "No se encontro registro");
                limpiar();
            }
            
        }else{
            if(e.getSource() == vproveedor.btnEliminarProveedor){
                proveedor.setId(Integer.parseInt(vproveedor.txtIDProveedor.getText()));
                if (cproveedor.eliminar(proveedor)) {
                    JOptionPane.showMessageDialog(null, "Proveedor eliminado");
                    limpiar();
                    llenarTabla();
                }else{
                    JOptionPane.showMessageDialog(null, "Error al eliminar");
                    limpiar();
                    llenarTabla();
                }
            }else{
                if (e.getSource() == vproveedor.btnInsertarProveedor) {
                    proveedor.setNombre(vproveedor.txtNombreProveedor.getText());
                    proveedor.setTelefono(vproveedor.txtContacto.getText());
                    proveedor.setDireccion(vproveedor.txtDireccion.getText());
                    proveedor.setCiudad(vproveedor.txtCiudad.getText());
                    proveedor.setIdentificacion(vproveedor.txtIdentificacion.getText());
                    if (cproveedor.insertar(proveedor)) {
                        JOptionPane.showMessageDialog(null, "Proveedor insertado");
                        limpiar();
                        llenarTabla();
                    }else{
                        JOptionPane.showMessageDialog(null, "Error al insertar proveedor");
                        limpiar ();
                        llenarTabla();
                    }
                    
                }else{
                    if (e.getSource() == vproveedor.btnModificar) {
                        proveedor.setId(Integer.parseInt(vproveedor.txtIDProveedor.getText()));
                        proveedor.setNombre(vproveedor.txtNombreProveedor.getText());
                        proveedor.setTelefono(vproveedor.txtContacto.getText());
                        proveedor.setDireccion(vproveedor.txtDireccion.getText());
                        proveedor.setCiudad(vproveedor.txtCiudad.getText());
                        proveedor.setIdentificacion(vproveedor.txtIdentificacion.getText());
                        
                        if (cproveedor.modificar(proveedor)) {
                            JOptionPane.showMessageDialog(null, "Producto modificado");
                            limpiar();
                            llenarTabla();
                        }else{
                            JOptionPane.showMessageDialog(null, "Error al modificar producto");
                            limpiar();
                            llenarTabla();
                        }
                    }
                }
            }
            
        }
    }
    
    public void limpiar(){
        vproveedor.txtIDProveedor.setText("");
        vproveedor.txtNombreProveedor.setText("");
        vproveedor.txtContacto.setText("");
        vproveedor.txtDireccion.setText("");
        vproveedor.txtCiudad.setText("");
        vproveedor.txtIdentificacion.setText("");
    }
    
    
    
}
