/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import inventario.ListHelper;
import inventario.ListHelperProducto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import modelo.ConsultasMovimiento;
import modelo.Movimiento;
import modelo.MovimientoProducto;
import vista.FrmActivos;
import vista.FrmEntrada;
import vista.FrmSalida;

/**
 *
 * @author Administrator
 */
public class CtlrMovimiento implements ActionListener{
    private final Movimiento movimiento;
    private final MovimientoProducto movprod;
    private final ArrayList<MovimientoProducto> productos;
    private final ConsultasMovimiento cmovimiento;
    private final FrmActivos vmovimiento;
    private final FrmEntrada ventrada;
    private final FrmSalida vsalida;
    private final DefaultListModel modelol1e;
    private final DefaultListModel modelol2e;
    private final DefaultListModel modelol1s;
    private final DefaultListModel modelol2s;
    private final DefaultListModel modelocliente;
    private final DefaultTableModel modelotben;
    private final DefaultTableModel modelotbsa;
    private final DefaultTableModel modelotbpen;
    private final DefaultTableModel modelotbpsa;
    
    public CtlrMovimiento(Movimiento movimiento, MovimientoProducto movprod, ConsultasMovimiento cmovimiento, FrmActivos vmovimiento, FrmEntrada ventrada, FrmSalida vsalida) {
        this.movimiento = movimiento;
        this.movprod = movprod;
        this.productos = new ArrayList<>();
        this.cmovimiento = cmovimiento;
        this.vmovimiento = vmovimiento;
        this.ventrada = ventrada;
        this.vsalida = vsalida;
        this.modelol1e = new DefaultListModel();
        this.modelol2e = new DefaultListModel();
        this.modelol1s = new DefaultListModel();
        this.modelol2s = new DefaultListModel();
        this.modelocliente = new DefaultListModel();
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
        this.modelotbpen.addColumn("ID");
        this.modelotbpen.addColumn("Cantidad");
        this.modelotbpsa.addColumn("ID");
        this.modelotbpsa.addColumn("Cantidad");
        this.vsalida.lstCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                agregarCliente();
            }
        });
        this.vsalida.lstP1Salida.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                agregarProductoLista();
            }
        });
        this.vmovimiento.btnSalida.addActionListener(this);
        this.vmovimiento.btnEntrada.addActionListener(this);
        this.ventrada.btnAceptarEntrada.addActionListener(this);
        this.ventrada.btnRestarEntrada.addActionListener(this);
        this.ventrada.btnSumarEntrada.addActionListener(this);
        this.vsalida.btnAceptarSalida.addActionListener(this);
        this.vsalida.btnRestarSalida.addActionListener(this);
        this.vsalida.btnSumarSalida.addActionListener(this);
        this.ventrada.lstP1Entrada.setModel(modelol1e);
        this.ventrada.lstP2Entrada.setModel(modelol2e);
        this.vsalida.lstP1Salida.setModel(modelol1s);
        this.vsalida.lstP2Salida.setModel(modelol2s);
        this.vsalida.lstCliente.setModel(modelocliente);
        this.vmovimiento.jtbEntradas.setModel(modelotben);
        this.vmovimiento.jtbProEntradas.setModel(modelotbpen);
        this.vmovimiento.jtbSalida.setModel(modelotbsa);
        this.vmovimiento.jtbProSalidas.setModel(modelotbpsa);
    }
    
    public void agregarProductoLista(){
        int index = vsalida.lstP1Salida.getSelectedIndex();
        ListHelperProducto cliente = (ListHelperProducto) modelol1s.getElementAt(index);
        movprod.setActivo(cliente.getId());
    }
    
    public void agregarCliente(){
        int index = vsalida.lstCliente.getSelectedIndex();
        ListHelper cliente = (ListHelper) modelocliente.getElementAt(index);
        
        movimiento.setUbicacion(cliente.getId());
        System.out.println(cliente.getId());
        
    }
    public void llenarListaBodega(){
        modelol1s.removeAllElements();
        ArrayList<ListHelperProducto> bodega = cmovimiento.getBodega();
        for (int i = 0; i < bodega.size(); i++) {
            modelol1s.addElement(bodega.get(i));
        }
    }
    public void llenarListaSalida(){
        
    }
    public void llenarListaClientes(){
        modelocliente.removeAllElements();
        ArrayList<ListHelper> clientes = cmovimiento.getClientes();
        for (int i = 0; i < clientes.size(); i++) {
            modelocliente.addElement(clientes.get(i));
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vmovimiento.btnSalida) {
            vsalida.setVisible(true);
            llenarListaClientes();
            llenarListaBodega();
        }else{
            if (e.getSource() == vmovimiento.btnEntrada) {
                ventrada.setVisible(true);
                
            }else{
                if (e.getSource() == vsalida.btnSumarSalida) {
                    movprod.setCantidad(Integer.parseInt(vsalida.txtCantidadSalida.getText()));
                    
                    productos.add(movprod);
                    
                    System.out.println(movprod);
                }
            }
        }
    }
    
}
