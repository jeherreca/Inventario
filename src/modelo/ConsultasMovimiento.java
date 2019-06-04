/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import inventario.ListHelper;
import inventario.ListHelperProducto;
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
    public ArrayList<ListHelper> getClientes(){
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;
        
        String sql = "SELECT idubicaciones, nombre FROM ubicaciones";
        ArrayList<ListHelper> clientes = new ArrayList<>();
        
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                clientes.add(new ListHelper (rs.getInt("idubicaciones"), rs.getString("nombre")));
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
    public String[] getNombreCodigoProducto(int id) {
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;
        
        String[] respuestas = new String[2];
        String sql = "SELECT codigo, nombre FROM activo WHERE idproductos=?";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if(rs.next()){
                respuestas[0] = rs.getString("codigo");
                respuestas[1] = rs.getString("nombre");
            }
            
            return respuestas;
        }catch(SQLException e){
            System.out.println(e);
            return respuestas;
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                System.out.println(e);
            }
        }          
    }
    
    public ArrayList<ListHelperProducto> getBodega (){
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;
        
        String sql = "SELECT * FROM bodega";
        ArrayList<ListHelperProducto> bodega = new ArrayList<>();
        
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                String[] respuestas = getNombreCodigoProducto(rs.getInt("idproducto"));
                bodega.add(new ListHelperProducto(rs.getInt("idproducto"), rs.getInt("stock"), respuestas[1], respuestas[0]));
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
