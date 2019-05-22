/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import modelo.ConsultasMarca;
import modelo.Marca;
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
                //Actualizar tabla
            }
        });
        this.vmarca.btnEliminarMarca.addActionListener(this);
        this.vmarca.btnInsertarMarca.addActionListener(this);
        this.vmarca.btnModificarMarca.addActionListener(this);
        this.vmarca.jtbMarca.setModel(modelomarca);
        this.vmarca.jtbMarcaProducto.setModel(modelomp);
    }
    public void limpiarTabla(){
        for (int i = 0; i < modelomarca.getRowCount(); i++) {
            modelomarca.removeRow(i);
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
    @Override
    public void actionPerformed(ActionEvent e) {
    }
    
    
    
    
}
