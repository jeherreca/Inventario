/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.ConsultasBodega;
import modelo.ConsultasProducto;
import modelo.Producto;
import vista.FrmActivos;

/**
 *
 * @author Administrator
 */
public final class CtlrProducto implements ActionListener {
    private final Producto producto;
    private final ConsultasProducto cproducto ;
    private final FrmActivos vproducto ;
    private final DefaultTableModel modelo;
    private final DefaultComboBoxModel cbxmodelo;
    private final CtlrBodega cbodega;
    
    public CtlrProducto(Producto producto, ConsultasProducto cproducto, FrmActivos vproducto, CtlrBodega cbodega) {
        this.producto = producto;
        this.cproducto = cproducto;
        this.vproducto = vproducto;
        this.cbodega = cbodega;
        this.modelo = new DefaultTableModel();
        this.cbxmodelo = new DefaultComboBoxModel();
        this.modelo.addColumn("Código");
        this.modelo.addColumn("Nombre");
        this.modelo.addColumn("Descripción");
        this.modelo.addColumn("Peso");
        this.modelo.addColumn("Marca");
        this.modelo.addColumn("Cantidad");
        llenarComboBox();
        llenarTabla();
        this.vproducto.jtbActivos.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                getSelectedProducto();
            }
        });        
        this.vproducto.btnEliminar.addActionListener(this);
        this.vproducto.btnBuscar.addActionListener(this);
        this.vproducto.btnInsertar.addActionListener(this);
        this.vproducto.btnModificar.addActionListener(this);
        this.vproducto.jtbActivos.setModel(modelo);
        this.vproducto.cbxMarca.setModel(cbxmodelo);
        this.vproducto.txtIDActivo.setVisible(false);
    }
    
    public void llenarTabla(){
        limpiarTabla();
        ArrayList<Producto> productos = cproducto.getProductos();
        Object[] array = new Object[6];
        for (int i = 0; i < productos.size(); i++) {
            array[0] = productos.get(i).getCodigo();
            array[1] = productos.get(i).getNombre();
            array[2] = productos.get(i).getDescripcion();
            array[3] = productos.get(i).getPeso();
            array[4] = productos.get(i).getMarca();
            array[5] = productos.get(i).getStock();
            modelo.addRow(array);
        }
    }
    public void limpiarTabla(){
        int size = modelo.getRowCount();
        for (int i = 0; i < size; i++) {
            modelo.removeRow(size - 1 - i);
        }
    }
    
    public void getSelectedProducto(){
        int fila = vproducto.jtbActivos.getSelectedRow();
        String codigo = vproducto.jtbActivos.getValueAt(fila,0).toString();
        String[] rs =cproducto.buscarElemento(codigo);
            vproducto.txtIDActivo.setText(rs[0]);
            vproducto.txtCodigoActivos.setText(rs[1]);
            vproducto.txtNombre.setText(rs[2]);
            vproducto.txtDescripcion.setText(rs[3]);
            vproducto.txtPeso.setText(rs[4]);
            vproducto.cbxMarca.setSelectedItem(rs[5]);
            vproducto.txtCantidad.setText(rs[6]);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vproducto.btnBuscar) {
            producto.setCodigo(vproducto.txtCodigoActivos.getText());
            if (cproducto.buscar(producto)) {
                vproducto.txtIDActivo.setText(producto.getId()+"");
                vproducto.txtCodigoActivos.setText(producto.getCodigo());
                vproducto.txtNombre.setText(producto.getNombre());
                vproducto.txtDescripcion.setText(producto.getDescripcion());
                vproducto.txtPeso.setText(producto.getPeso()+"");
                vproducto.txtCantidad.setText(producto.getStock()+"");
            }else{
                JOptionPane.showMessageDialog(null, "No se encontro registro");
                limpiar();
            }
            
        }else{
            if(e.getSource() == vproducto.btnEliminar){
                producto.setId(Integer.parseInt(vproducto.txtIDActivo.getText()));
                if (cproducto.eliminar(producto)) {
                    JOptionPane.showMessageDialog(null, "Producto eliminado");
                    limpiar();
                    llenarTabla();
                }else{
                    JOptionPane.showMessageDialog(null, "Error al eliminar");
                    limpiar();
                    llenarTabla();
                }
            }else{
                if (e.getSource() == vproducto.btnInsertar) {
                    producto.setCodigo(vproducto.txtCodigoActivos.getText());
                    producto.setNombre(vproducto.txtNombre.getText());
                    producto.setDescripcion(vproducto.txtDescripcion.getText());
                    producto.setPeso(Double.parseDouble(vproducto.txtPeso.getText()));
                    producto.setMarca(Integer.parseInt(String.valueOf(vproducto.cbxMarca.getSelectedItem())));
                    producto.setStock(Integer.parseInt(vproducto.txtCantidad.getText()));
                    if (cproducto.insertar(producto)) {
                        cbodega.getCbodega().insertar(producto);
                        cbodega.llenarTabla();
                        JOptionPane.showMessageDialog(null, "Producto insertado");
                        limpiar();
                        llenarTabla();
                    }else{
                        JOptionPane.showMessageDialog(null, "Error al insertar producto");
                        limpiar ();
                        llenarTabla();
                    }
                    
                }else{
                    if (e.getSource() == vproducto.btnModificar) {
                        producto.setId(Integer.parseInt(vproducto.txtIDActivo.getText()));
                        producto.setCodigo(vproducto.txtCodigoActivos.getText());
                        producto.setNombre(vproducto.txtNombre.getText());
                        producto.setDescripcion(vproducto.txtDescripcion.getText());
                        producto.setPeso(Double.parseDouble(vproducto.txtPeso.getText()));
                        producto.setMarca(Integer.parseInt(String.valueOf(vproducto.cbxMarca.getSelectedItem())));
                        producto.setStock(Integer.parseInt(vproducto.txtCantidad.getText()));
                        
                        if (cproducto.modificar(producto)) {
                            
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
        vproducto.txtIDActivo.setText("");
        vproducto.txtCodigoActivos.setText("");
        vproducto.txtNombre.setText("");
        vproducto.txtDescripcion.setText("");
        vproducto.txtPeso.setText("");
        vproducto.txtCantidad.setText("");
    }

    private void llenarComboBox() {
        ArrayList<Integer> marcas = cproducto.getMarcas();
        for (int i = 0; i < marcas.size(); i++) {
            cbxmodelo.addElement(marcas.get(i));
        }
    }
    
    
}
