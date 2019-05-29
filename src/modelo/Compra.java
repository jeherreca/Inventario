/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.Date;

/**
 *
 * @author Administrator
 */
public class Compra {
    private int id;
    private int proveedor;
    private Date fecha;
    private Double precio;
    private String codigo;

    public Compra(int id, int proveedor, Date fecha, Double precio, String codigo) {
        this.id = id;
        this.proveedor = proveedor;
        this.fecha = fecha;
        this.precio = precio;
        this.codigo = codigo;
    }
    
   public Compra(){
       
   }
   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProveedor() {
        return proveedor;
    }

    public void setProveedor(int proveedor) {
        this.proveedor = proveedor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    
}
