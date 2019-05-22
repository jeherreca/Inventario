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
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class ConsultasMarca extends Conexion {
    
    public boolean insertar(Marca marca){
        PreparedStatement ps;
        Connection con = getConnection();
        
        String sql = "INSERT INTO marca (nombre, observaciones) VALUES (?,?)";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, marca.getNombre());
            ps.setString(2, marca.getObservaciones());
            
            ps.execute();
            
            System.out.println("Marca guardada");
            return true;
        }catch(SQLException e){
            System.out.println("Error al guardar marca");
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
    public boolean eliminar(Marca marca){
        PreparedStatement ps;
        Connection con = getConnection();
        
        String sql = "DELETE FROM marca WHERE idmarca=?";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, marca.getId());
            
            ps.execute();
            
            System.out.println("Marca eliminada");
            return true;
        }catch(SQLException e){
            System.out.println("Error al eliminar la marca");
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
    public boolean modificar(Marca marca){
        PreparedStatement ps;
        Connection con = getConnection();
        
        String sql = "UPDATE marca SET nombre=?, observaciones=? WHERE idmarca=?";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, marca.getNombre());
            ps.setString(2, marca.getObservaciones());
            ps.setInt(3, marca.getId());
            
            ps.execute();
            
            System.out.println("Marca modificada");
            return true;
        }catch(SQLException e){
            System.out.println("Error al modificar la marca");
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
    
    public boolean buscar (Marca marca){
        PreparedStatement ps;
        Connection con = getConnection();
        String sql = "SELECT * FROM marca WHERE idmarca=?";
        ResultSet rs;
        
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, marca.getId());
            rs = ps.executeQuery();
            
            if (rs.next()) {
                marca.setId(rs.getInt("idmarca"));
                marca.setNombre(rs.getString("nombre"));
                marca.setObservaciones(rs.getString("observaciones"));
                
                return true;
            }
            System.out.println("Marca no encontrada");
            return false;
        }catch(SQLException e){
            System.out.println("Error al buscar marca");
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
    
    public ArrayList<Producto> buscarProductos(Marca marca){
        PreparedStatement ps;
        Connection con=getConnection();
        ArrayList<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE idmarca=?";
        ResultSet rs;
        
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, marca.getId());
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                Producto pro  = new Producto(rs.getInt("idproductos"), rs.getString("codigo"), rs.getString("nombre"), rs.getString("descripcion"), rs.getDouble("peso"), marca.getId(), rs.getInt("cantidad"));
                productos.add(pro);
            }
            return productos;
        }catch(SQLException e){
            System.out.println(e);
            return productos;
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                System.out.println(e);
            }
        }
    }
    public ArrayList<Marca> getMarcas(){
        PreparedStatement ps;
        ResultSet rs;
        Connection con = getConnection();
        
        String sql = "SELECT * FROM marca";
        ArrayList<Marca> marcas = new ArrayList<>();
        
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                Marca marca = new Marca(rs.getInt("idmarca"), rs.getString("nombre"), rs.getString("observaciones"));
                marcas.add(marca);
            }
            return marcas;
            
            
        }catch(SQLException e){
            System.out.println(e);
            return marcas;
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                System.out.println(e);
            }
        }
    }
}
