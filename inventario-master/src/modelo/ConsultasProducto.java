/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import inventario.ComboBoxHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class ConsultasProducto extends Conexion{
    
    public ArrayList<ComboBoxHelper> getMarcas(){
        PreparedStatement ps;
        ResultSet rs;
        Connection con = getConnection();
        
        String sql = "SELECT idmarca, nombre FROM marca";
        ArrayList<ComboBoxHelper> marcas = new ArrayList<>();
        
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                marcas.add(new ComboBoxHelper(rs.getInt("idmarca"), rs.getString("nombre")));
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
    public ArrayList<Producto> getProductos(){
        PreparedStatement ps;
        ResultSet rs;
        Connection con = getConnection();
        
        String sql = "SELECT * FROM activo";
        ArrayList<Producto> productos = new ArrayList<>();
        
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                Producto prod = new Producto(rs.getInt("idproductos"), rs.getString("codigo"), rs.getString("nombre"), rs.getString("descripcion"), rs.getDouble("peso"), rs.getInt("idmarca"), rs.getInt("cantidad"));
                productos.add(prod);
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
    public String getNombreMarca (int id){
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;
        String nombre = "";
        String sql = "SELECT nombre FROM marca WHERE idmarca=?";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if(rs.next()){
                nombre = rs.getString("nombre");
            }
            return nombre;
        }catch(SQLException e){
            System.out.println(e);
            return nombre;
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                System.out.println(e);
            }
        }
    }
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
        
        String sql = "UPDATE activo SET codigo=?, nombre=?, descripcion=?, peso=?, idmarca=?, cantidad=? WHERE idproductos=?";
        
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
    
    public String[] buscarElemento(String codigo){
        String[] resultados = new String[7];
        PreparedStatement ps;
        ResultSet rs;
        Connection conn = getConnection();   
        try{       
            ps = conn.prepareStatement("SELECT idproductos, codigo, nombre, descripcion, peso, idmarca, cantidad FROM activo WHERE codigo=?");
            ps.setString(1, codigo);
            rs= ps.executeQuery();
            if (rs.next()) {
                resultados[0]=rs.getInt("idproductos")+"";
                resultados[1]=rs.getString("codigo")+"";
                resultados[2]=rs.getString("nombre")+"";
                resultados[3]=rs.getString("descripcion")+"";
                resultados[4]=rs.getDouble("peso")+"";
                resultados[5]=rs.getInt("idmarca")+"";
                resultados[6]=rs.getInt("cantidad")+"";
            }
            return resultados;
        }catch(SQLException e){
            System.out.println(e);
            return resultados;
        }finally{
            try{
                conn.close();
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
