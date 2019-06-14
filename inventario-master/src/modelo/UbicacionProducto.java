/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Administrator
 */
public class UbicacionProducto {

    int idubicacion;
    int idproducto;
    int cantidad;

    public UbicacionProducto(int idubicacion, int idproducto, int cantidad) {
        this.idubicacion = idubicacion;
        this.idproducto = idproducto;
        this.cantidad = cantidad;
    }

    public int getIdubicacion() {
        return idubicacion;
    }

    public void setIdubicacion(int idubicacion) {
        this.idubicacion = idubicacion;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

}
