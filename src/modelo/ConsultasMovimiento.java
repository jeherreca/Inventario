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
public class ConsultasMovimiento extends Conexion{
    public boolean insertar(Movimiento mov){
        PreparedStatement ps;
        Connection con = getConnection();
        
        String sql = "INSERT INTO movimiento (fecha, codigo, idubicacion, tipo, observaciones) VALUES (?,?,?,?,?)";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(mov.getFecha().getTime()));
            ps.setString(2, mov.getCodigo());
            ps.setInt(3, mov.getUbicacion());
            ps.setString(4, mov.getTipo());
            ps.setString(5, mov.getTipo());
            
            ps.execute();
            
            System.out.println("Movimiento guardado");
            return true;
        }catch(SQLException e){
            System.out.println("Error al guardar movimiento");
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
    public ArrayList<String> getClientes(){
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;
        
        String sql = "SELECT nombre FROM ubicaciones";
        ArrayList<String> clientes = new ArrayList<>();
        
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                clientes.add(rs.getString("nombre"));
            }
            
            return clientes;
        }catch(SQLException e){
            System.out.println(e);
            return clientes;
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                System.out.println(e);
            }
        }
        
    }
    
    public ArrayList<String> getBodega (){
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;
        
        String sql = "SELECT * FROM bodega";
        ArrayList<String> bodega = new ArrayList<>();
        
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                
                bodega.add(rs.getInt("idproducto")+"");
            }
            
            return bodega;
        }catch(SQLException e){
            System.out.println(e);
            return bodega;
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                System.out.println(e);
            }
        }
    }
}
