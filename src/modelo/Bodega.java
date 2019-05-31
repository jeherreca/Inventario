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
public class Bodega {
    private int idproducto;
    private int stock;

    public Bodega(int idproducto, int stock) {
        this.idproducto = idproducto;
        this.stock = stock;
    }
    
    public Bodega(){
        
    }
    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
    
}
