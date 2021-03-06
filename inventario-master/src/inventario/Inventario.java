/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventario;

import controlador.CtlrBodega;
import controlador.CtlrMarca;
import controlador.CtlrMovimiento;
import controlador.CtlrProducto;
import controlador.CtlrUbicacion;
import javax.swing.JOptionPane;
import modelo.ConsultasBodega;
import modelo.ConsultasMarca;
import modelo.ConsultasMovimiento;
import modelo.ConsultasProducto;
import modelo.ConsultasUbicacion;
import modelo.Marca;
import modelo.Movimiento;
import modelo.MovimientoProducto;
import modelo.Producto;
import modelo.Ubicacion;
import vista.FrmActivos;
import vista.FrmEntrada;
import vista.FrmModificarEntrada;
import vista.FrmModificarSalida;
import vista.FrmSalida;

/**
 *
 * @author Administrator
 */
public class Inventario {

    public static void main(String[] args) {
        try{      
            Producto producto = new Producto();
            ConsultasProducto cproducto = new ConsultasProducto();
            FrmActivos vproducto = new FrmActivos();
            FrmEntrada ventrada = new FrmEntrada();
            FrmSalida vsalida = new FrmSalida();
            FrmModificarSalida vmodsalida = new FrmModificarSalida();
            FrmModificarEntrada vmodentrada = new FrmModificarEntrada();
            Marca marca = new Marca();
            ConsultasMarca cmarca = new ConsultasMarca();

            Ubicacion ubicacion = new Ubicacion();
            ConsultasUbicacion cubicacion = new ConsultasUbicacion();

            ConsultasBodega conBodega = new ConsultasBodega();
            CtlrBodega cbodega = new CtlrBodega(conBodega, vproducto);

            Movimiento mov = new Movimiento();
            MovimientoProducto movprod = new MovimientoProducto();
            ConsultasMovimiento cmov = new ConsultasMovimiento();

            CtlrProducto ctlrprod = new CtlrProducto(producto, cproducto, vproducto, cbodega);
            CtlrMarca ctlrmarca = new CtlrMarca(marca, cmarca, vproducto, ctlrprod);
            CtlrUbicacion ctlrubic = new CtlrUbicacion(ubicacion, cubicacion, vproducto);
            CtlrMovimiento cltrmov = new CtlrMovimiento(mov, movprod, cmov, vproducto, ventrada, vsalida, vmodsalida, vmodentrada, cbodega);
            vproducto.setVisible(true);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha conectado con la base de datos, Inicie WAMP Server");
        }           
    }
}
