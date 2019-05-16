/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.ConsultasProducto;
import modelo.Producto;
import vista.FrmActivos;

/**
 *
 * @author Administrator
 */
public class CtlrProducto implements ActionListener {
    private Producto producto;
    private ConsultasProducto cproducto ;
    private FrmActivos vproducto ;
    private DefaultTableModel modelo;
    
    public CtlrProducto(Producto producto, ConsultasProducto cproducto, FrmActivos vproducto) {
        this.producto = producto;
        this.cproducto = cproducto;
        this.vproducto = vproducto;
        this.modelo = new DefaultTableModel();
        this.vproducto.jtbActivos.setModel(modelo);
        this.vproducto.btnEliminar.addActionListener(this);
        this.vproducto.btnBuscar.addActionListener(this);
        this.vproducto.btnInsertar.addActionListener(this);
        this.vproducto.btnModificar.addActionListener(this);
    }
    
    public void llenarTabla(Producto[] productos){
        productos =
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
                }else{
                    JOptionPane.showMessageDialog(null, "Error al eliminar");
                    limpiar();
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
                        JOptionPane.showMessageDialog(null, "Producto insertado");
                        limpiar();
                    }else{
                        JOptionPane.showMessageDialog(null, "Error al insertar producto");
                        limpiar ();
                    }
                    
                }else{
                    if (e.getSource() == vproducto.btnModificar) {
                        producto.setId(Integer.parseInt(vproducto.txtIDActivo.getText()));
                        producto.setCodigo(vproducto.txtCodigoActivos.getText());
                        producto.setNombre(vproducto.txtNombre.getText());
                        producto.setDescripcion(vproducto.txtDescripcion.getText());
                        producto.setMarca(Integer.parseInt(String.valueOf(vproducto.cbxMarca.getSelectedItem())));
                        producto.setStock(Integer.parseInt(vproducto.txtCantidad.getText()));
                        
                        if (cproducto.modificar(producto)) {
                            JOptionPane.showMessageDialog(null, "Producto modificado");
                            limpiar();
                        }else{
                            JOptionPane.showMessageDialog(null, "Error al modificar producto");
                            limpiar();
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
    
    
}
