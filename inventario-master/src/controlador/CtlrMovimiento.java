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
    
    public CtlrMovimiento(Movimiento movimiento, MovimientoProducto movprod, ConsultasMovimiento cmovimiento, FrmActivos vmovimiento, FrmEntrada ventrada, FrmSalida vsalida) {
        this.movimiento = movimiento;
        this.movprod = movprod;
        this.productos = new ArrayList<>();
        this.cmovimiento = cmovimiento;
        this.vmovimiento = vmovimiento;
        this.ventrada = ventrada;
        this.vsalida = vsalida;
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
        this.modelotbpen.addColumn("ID");
        this.modelotbpen.addColumn("Cantidad");
        this.modelotbpsa.addColumn("ID");
        this.modelotbpsa.addColumn("Cantidad");
        this.modelol1s.addColumn("Código");
        this.modelol1s.addColumn("Nombre");
        this.modelol1s.addColumn("Cantidad");
        this.modelol2s.addColumn("Código");
        this.modelol2s.addColumn("Nombre");
        this.modelol2s.addColumn("Cantidad");
        this.vsalida.lstCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                agregarCliente();
            }
        });
        this.vmovimiento.btnSalida.addActionListener(this);
        this.vmovimiento.btnEntrada.addActionListener(this);
        this.ventrada.btnAceptarEntrada.addActionListener(this);
        this.vsalida.btnAceptarSalida.addActionListener(this);
        this.vsalida.btnRestarSalida.addActionListener(this);
        this.vsalida.btnSumarSalida.addActionListener(this);
        this.vsalida.lstCliente.setModel(modelocliente);
        this.vsalida.jtbP1Salida.setModel(modelol1s);
        this.vsalida.jtbP2Salida.setModel(modelol2s);
        this.vmovimiento.jtbEntradas.setModel(modelotben);
        this.vmovimiento.jtbProEntradas.setModel(modelotbpen);
        this.vmovimiento.jtbSalida.setModel(modelotbsa);
        this.vmovimiento.jtbProSalidas.setModel(modelotbpsa);
    }
    
    public void agregarCliente(){
        int index = vsalida.lstCliente.getSelectedIndex();
        ListHelper cliente = (ListHelper) modelocliente.getElementAt(index);
        movimiento.setUbicacion(cliente.getId());
        System.out.println(cliente.getId()); 
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
        }else{
            if (e.getSource() == vmovimiento.btnEntrada) {
                ventrada.setVisible(true);       
            }else{
                if (e.getSource() == vsalida.btnSumarSalida) {
                    try{
                        movprod.setCantidad(Integer.parseInt(vsalida.txtCantidadSalida.getText()));
                        productos.add(movprod);
                        System.out.println(movprod);
                    }catch(NumberFormatException ex){
                        System.out.println(ex);
                    }         
                }else{
                    if (e.getSource() == vsalida.btnAceptarSalida) {
                        try{
                            movimiento.setCodigo(vsalida.txtCodigoSalida.getText());
                            String fecha = vsalida.txtFechaSalida.getText();
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
                            movimiento.setFecha(date);
                            movimiento.setTipo("salida");
                            movimiento.setObservaciones(vsalida.txtObservacionesSalida.getText());
                            cmovimiento.insertar(movimiento);
                            
                        }catch(ParseException ex){
                            System.out.println(ex);
                        }
                    }else{
                        if (e.getSource() == vsalida.btnRestarSalida) {
                            
                        }
                    }
                }
            }
        }
    }
    
}
