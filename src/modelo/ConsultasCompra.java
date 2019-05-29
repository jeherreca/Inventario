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
public class ConsultasCompra extends Conexion {
    
    public ArrayList<Compra> getCompras(){
        PreparedStatement ps;
        ResultSet rs;
        Connection con = getConnection();
        
        String sql = "SELECT * FROM compra";
        ArrayList<Compra> compras = new ArrayList<>();
        
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                Compra com = new Compra(rs.getInt("idcompra"), rs.getInt("idproveedor"), rs.getDate("fecha"), rs.getDouble("valor"), rs.getString("codigo"));
                compras.add(com);
            }
            return compras;
            
            
        }catch(SQLException e){
            System.out.println(e);
            return compras;
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                System.out.println(e);
            }
        }
    }
    public boolean insertar(Compra com){
        PreparedStatement ps;
        Connection con = getConnection();
        
        String sql = "INSERT INTO compra (idcompra, idproveedor, fecha, valor, codigo) VALUES (?,?,?,?,?)";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, com.getId());
            ps.setInt(2, com.getProveedor());
            ps.setDate(3, new java.sql.Date(com.getFecha().getTime()));
            ps.setDouble(4, com.getPrecio());
            ps.setString(5, com.getCodigo());
            
            ps.execute();
            
                System.out.println("Compra guardado");
            return true;
        }catch(SQLException e){
            System.out.println("Error al guardar compra");
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
    public boolean eliminar(Compra com){
        PreparedStatement ps;
        Connection con = getConnection();
        
        String sql = "DELETE FROM compra WHERE idcompra=?";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, com.getId());
            
            ps.execute();
            
            System.out.println("Compra eliminada");
            return true;
        }catch(SQLException e){
            System.out.println("Error al eliminar la compra");
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
    public boolean modificar(Compra com){
        PreparedStatement ps;
        Connection con = getConnection();
        
        String sql = "UPDATE compra SET idproveedor=?, fecha=?, valor=?, codigo=? WHERE idcompra=?";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, com.getProveedor());
            ps.setDate(2, new java.sql.Date(com.getFecha().getTime()));
            ps.setDouble(3, com.getPrecio());
            ps.setString(4, com.getCodigo());
            ps.setInt(5, com.getId());
            
            ps.execute();
            
            System.out.println("Compra modificada");
            return true;
        }catch(SQLException e){
            System.out.println("Error al modificar la compra");
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
    public String[] buscarElemento(int id){
        String[] resultados = new String[5];
        PreparedStatement ps;
        ResultSet rs;
        Connection conn = getConnection();   
        try{       
            ps = conn.prepareStatement("SELECT idcompra, idproveedor, fecha, valor, codigo FROM compra WHERE idcompra=?");
            ps.setInt(1, id);
            rs= ps.executeQuery();
            if (rs.next()) {
                resultados[0]=rs.getInt("idcompra")+"";
                resultados[1]=rs.getInt("idproveedor")+"";
                resultados[2]=rs.getDate("fecha")+"";
                resultados[3]=rs.getDouble("precio")+"";
                resultados[4]=rs.getString("codigo")+"";
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
    public boolean buscar(Compra com){
        PreparedStatement ps;
        ResultSet rs;
        Connection con = getConnection();
        
        String sql = "SELECT * FROM compra WHERE idcompra=?";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, com.getId());
            
            rs= ps.executeQuery();
            if(rs.next()){
                com.setId(rs.getInt("idproductos"));
                com.setCodigo(rs.getString("codigo"));
                com.setPrecio(rs.getDouble("valor"));
                com.setFecha(rs.getDate("fecha"));
                com.setProveedor(rs.getInt("idproveedor"));
                
                return true;
            }
            System.out.println("Compra no encontrada");
            return false;
        }catch(SQLException e){
            System.out.println("Error al buscar la compra");
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
