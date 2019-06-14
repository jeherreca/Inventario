/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import modelo.Bodega;
import modelo.ConsultasBodega;
import vista.FrmActivos;

/**
 *
 * @author Administrator
 */
public final class CtlrBodega {

    private ConsultasBodega cbodega;
    private final FrmActivos vbodega;
    private final DefaultTableModel modelo;

    public CtlrBodega(ConsultasBodega cbodega, FrmActivos vbodega) {
        this.cbodega = cbodega;
        this.vbodega = vbodega;
        this.modelo = new DefaultTableModel();
        this.modelo.addColumn("ID");
        this.modelo.addColumn("Cantidad");
        llenarTabla();
        this.vbodega.jtbBodega.setModel(modelo);
    }

    public ConsultasBodega getCbodega() {
        return cbodega;
    }

    public void setCbodega(ConsultasBodega cbodega) {
        this.cbodega = cbodega;
    }

    public void llenarTabla() {
        limpiarTablaBodega();
        ArrayList<Bodega> proveedores = cbodega.getBodegas();
        Object[] array = new Object[2];
        for (int i = 0; i < proveedores.size(); i++) {
            array[0] = proveedores.get(i).getIdproducto();
            array[1] = proveedores.get(i).getStock();
            modelo.addRow(array);
        }
    }

    public void limpiarTablaBodega() {
        int size = modelo.getRowCount();
        for (int i = 0; i < size; i++) {
            modelo.removeRow(size - 1 - i);
        }
    }
}
