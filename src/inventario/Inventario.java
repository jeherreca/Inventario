/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventario;

import controlador.CtlrMarca;
import controlador.CtlrProducto;
import controlador.CtlrProveedor;
import modelo.ConsultasMarca;
import modelo.ConsultasProducto;
import modelo.ConsultasProveedor;
import modelo.Marca;
import modelo.Producto;
import modelo.Proveedor;
import vista.FrmActivos;

/**
 *
 * @author Administrator
 */
public class Inventario {
    public static void main(String[] args) {
        
        Producto producto = new Producto();
        ConsultasProducto cproducto = new ConsultasProducto();
        FrmActivos vproducto = new FrmActivos();
        
        Marca marca = new Marca();
        ConsultasMarca cmarca = new ConsultasMarca();
        
        Proveedor proveedor = new Proveedor();
        ConsultasProveedor cproveedor = new ConsultasProveedor();
        
        CtlrMarca ctlrmarca = new CtlrMarca(marca, cmarca, vproducto);
        CtlrProducto ctlrprod = new CtlrProducto(producto, cproducto, vproducto);
        CtlrProveedor ctlrprov = new CtlrProveedor(proveedor, cproveedor, vproducto);
        
        vproducto.setVisible(true);
    }
}
