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
public class ConsultasProveedor extends Conexion {
    
    public ArrayList<Proveedor> getProveedores(){
        PreparedStatement ps;
        ResultSet rs;
        Connection con = getConnection();
        
        String sql = "SELECT * FROM proveedores";
        ArrayList<Proveedor> proveedores = new ArrayList<>();
        
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                Proveedor proveedor = new Proveedor(rs.getInt("idproveedores"), rs.getString("nombre"), rs.getString("contacto"), rs.getString("direccion"), rs.getString("ciudad"), rs.getString("identificacion"));
                proveedores.add(proveedor);
            }
            return proveedores;
            
            
        }catch(SQLException e){
            System.out.println(e);
            return proveedores;
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                System.out.println(e);
            }
        }
    }
    public boolean insertar(Proveedor proveedores){
        PreparedStatement ps;
        Connection con = getConnection();
        
        String sql = "INSERT INTO proveedores (nombre, contacto, direccion, ciudad, identificacion) VALUES (?,?,?,?,?)";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, proveedores.getNombre());
            ps.setString(2, proveedores.getTelefono());
            ps.setString(3, proveedores.getDireccion());
            ps.setString(4, proveedores.getCiudad());
            ps.setString(5, proveedores.getIdentificacion());
            
            ps.execute();
            
            System.out.println("Proveedor guardado");
            return true;
        }catch(SQLException e){
            System.out.println("Error al guardar proveedor");
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
    public boolean eliminar(Proveedor pro){
        PreparedStatement ps;
        Connection con = getConnection();
        
        String sql = "DELETE FROM proveedores WHERE idproveedores=?";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, pro.getId());
            
            ps.execute();
            
            System.out.println("Proveedor eliminado");
            return true;
        }catch(SQLException e){
            System.out.println("Error al eliminar el proveedor");
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
    public boolean modificar(Proveedor pro){
        PreparedStatement ps;
        Connection con = getConnection();
        
        String sql = "UPDATE proveedores SET nombre=?, contacto=?, direccion=?, ciudad=?, identificacion=? WHERE idproveedores=?";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, pro.getNombre());
            ps.setString(2, pro.getTelefono());
            ps.setString(3, pro.getDireccion());
            ps.setString(4, pro.getCiudad());
            ps.setString(5, pro.getIdentificacion());
            ps.setInt(6, pro.getId());
            
            ps.execute();
            
            System.out.println("Proveedor modificado");
            return true;
        }catch(SQLException e){
            System.out.println("Error al modificar el proveedor");
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
    public String[] buscarElemento(String id){
        String[] resultados = new String[6];
        PreparedStatement ps;
        ResultSet rs;
        Connection conn = getConnection();   
        try{       
            ps = conn.prepareStatement("SELECT idproveedores, nombre, contacto, direccion, ciudad, identificacion FROM proveedores WHERE idproveedores=?");
            ps.setString(1, id);
            rs= ps.executeQuery();
            if (rs.next()) {
                resultados[0]=rs.getInt("idproveedores")+"";
                resultados[1]=rs.getString("nombre")+"";
                resultados[2]=rs.getString("contacto")+"";
                resultados[3]=rs.getString("direccion")+"";
                resultados[4]=rs.getString("ciudad")+"";
                resultados[5]=rs.getString("identificacion")+"";
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
    public boolean buscar(Proveedor pro){
        PreparedStatement ps;
        ResultSet rs;
        Connection con = getConnection();
        
        String sql = "SELECT * FROM proveedores WHERE idproveedores=?";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, pro.getId());
            
            rs= ps.executeQuery();
            if(rs.next()){
                pro.setId(rs.getInt("idproveedores"));
                pro.setNombre(rs.getString("nombre"));
                pro.setTelefono(rs.getString("contacto"));
                pro.setDireccion(rs.getString("direccion"));
                pro.setCiudad(rs.getString("ciudad"));
                pro.setIdentificacion(rs.getString("identificacion"));
                
                return true;
            }
            System.out.println("Proveedor no encontrado");
            return false;
        }catch(SQLException e){
            System.out.println("Error al buscar el proveedor");
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
