/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Administrator
 */
public class ConsultasProducto extends Conexion{
    
    public boolean insertar(Producto pro){
        PreparedStatement ps;
        Connection con = getConnection();
        
        String sql = "INSERT INTO activo (codigo, nombre, descripcion, peso, idmarca, cantidad) VALUES (?,?,?,?,?,?)";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getNombre());
            ps.setString(3, pro.getDescripcion());
            ps.setDouble(4, pro.getPeso());
            ps.setInt(5, pro.getMarca());
            ps.setInt(6, pro.getStock());
            
            ps.execute();
            
            System.out.println("Producto guardado");
            return true;
        }catch(SQLException e){
            System.out.println("Error al guardar producto");
            System.out.println(e);
            return false;
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                System.out.println(e);
            }
            
        }
    }
    public boolean eliminar(Producto pro){
        PreparedStatement ps;
        Connection con = getConnection();
        
        String sql = "DELETE FROM activo WHERE idproductos=?";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, pro.getId());
            
            ps.execute();
            
            System.out.println("Producto eliminado");
            return true;
        }catch(SQLException e){
            System.out.println("Error al eliminar el producto");
            System.out.println(e);
            return false;
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                System.out.println(e);
            }  
        }
    }
    public boolean modificar(Producto pro){
        PreparedStatement ps;
        Connection con = getConnection();
        
        String sql = "UPDATE producto SET codigo=?, nombre=?, descripcion=?, peso=?, idmarca=?, cantidad=? WHERE idproductos=?";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getNombre());
            ps.setString(3, pro.getDescripcion());
            ps.setDouble(4, pro.getPeso());
            ps.setInt(5, pro.getMarca());
            ps.setInt(6, pro.getStock());
            ps.setInt(7, pro.getId());
            
            ps.execute();
            
            System.out.println("Producto modificado");
            return true;
        }catch(SQLException e){
            System.out.println("Error al modificar el producto");
            System.out.println(e);
            return false;
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                System.out.println(e);
            }
        }
    }
    
    public boolean buscar(Producto pro){
        PreparedStatement ps;
        ResultSet rs;
        Connection con = getConnection();
        
        String sql = "SELECT * FROM activo WHERE codigo=?";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, pro.getCodigo());
            
            rs= ps.executeQuery();
            if(rs.next()){
                pro.setId(rs.getInt("idproductos"));
                pro.setCodigo(rs.getString("codigo"));
                pro.setNombre(rs.getString("nombre"));
                pro.setDescripcion(rs.getString("descripcion"));
                pro.setPeso(rs.getDouble("peso"));
                pro.setMarca(rs.getInt("idmarca"));
                pro.setStock(rs.getInt("cantidad"));
                
                return true;
            }
            System.out.println("Producto no encontrado");
            return false;
        }catch(SQLException e){
            System.out.println("Error al buscar el producto");
            System.out.println(e);
            return false;
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                System.out.println(e);
            }
        }
    }
}
