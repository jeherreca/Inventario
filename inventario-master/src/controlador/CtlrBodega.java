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
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import modelo.ConsultasBodega;
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
public final class CtlrBodega implements ActionListener {

    private ConsultasBodega cbodega;
    private final FrmActivos vbodega;
    private final DefaultTableModel modelo;

    public CtlrBodega(ConsultasBodega cbodega, FrmActivos vbodega) {
        this.cbodega = cbodega;
        this.vbodega = vbodega;
        this.modelo = new DefaultTableModel();
        this.modelo.addColumn("Código");
        this.modelo.addColumn("Nombre");
        this.modelo.addColumn("Descripción");
        this.modelo.addColumn("Cantidad");
        this.vbodega.jtbBodega.setDefaultEditor(Object.class, null);
        llenarTabla();
        this.vbodega.jtbBodega.setModel(modelo);
        this.vbodega.btnReporteBodega.addActionListener(this);
    }

    public ConsultasBodega getCbodega() {
        return cbodega;
    }

    public void setCbodega(ConsultasBodega cbodega) {
        this.cbodega = cbodega;
    }

    public void llenarTabla() {
        limpiarTablaBodega();
        ArrayList<Object[]> proveedores = cbodega.getBodegas();
        for (int i = 0; i < proveedores.size(); i++) {
            modelo.addRow(proveedores.get(i));
        }
    }

    public void limpiarTablaBodega() {
        int size = modelo.getRowCount();
        for (int i = 0; i < size; i++) {
            modelo.removeRow(size - 1 - i);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vbodega.btnReporteBodega) {
            try{
                                JasperReport reporte = null;
                                String path = "src\\reporte\\ReporteBodega.jasper";
                                
                                reporte = (JasperReport) JRLoader.loadObjectFromFile(path);
                                
                                JasperPrint jprint = JasperFillManager.fillReport(reporte, null, cbodega.getConexion());
                                
                                JasperViewer view = new JasperViewer (jprint, false);
                                
                                view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                                
                                view.setVisible(true);
                            } catch (JRException ex) {
                                Logger.getLogger(CtlrUbicacion.class.getName()).log(Level.SEVERE, null, ex);
                            }
        }
    }
}
