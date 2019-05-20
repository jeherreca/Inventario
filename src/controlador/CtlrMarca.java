/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import javax.swing.table.DefaultTableModel;
import modelo.ConsultasMarca;
import modelo.Marca;
import vista.FrmActivos;

/**
 *
 * @author Administrator
 */
public class CtlrMarca {
    
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
    }
    
    
    
    
}
