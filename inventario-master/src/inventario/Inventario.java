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
import vista.FrmSalida;

/**
 *
 * @author Administrator
 */
public class Inventario {

    public static void main(String[] args) {
        
        Producto producto = new Producto();
        ConsultasProducto cproducto = new ConsultasProducto();
        FrmActivos vproducto = new FrmActivos();
        FrmEntrada ventrada = new FrmEntrada();
        FrmSalida vsalida = new FrmSalida();

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
        CtlrMovimiento cltrmov = new CtlrMovimiento(mov, movprod, cmov, vproducto, ventrada, vsalida, cbodega);
        vproducto.setVisible(true);
        
    }
}
