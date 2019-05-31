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
public class ConsultasUbicacion extends Conexion {
    
    public ArrayList<UbicacionProducto> buscarProductos(Ubicacion ubi){
        PreparedStatement ps;
        Connection con=getConnection();
        ArrayList<UbicacionProducto> productos = new ArrayList<>();
        String sql = "SELECT * FROM ubicacion_productos WHERE idubicacion=?";
        ResultSet rs;
        
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, ubi.getId());
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                UbicacionProducto pro  = new UbicacionProducto(rs.getInt("idubicacion"), rs.getInt("idproductos"), rs.getInt("cantidad"));
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
    public ArrayList<Ubicacion> getUbicacion(){
        PreparedStatement ps;
        ResultSet rs;
        Connection con = getConnection();
        
        String sql = "SELECT * FROM ubicaciones";
        ArrayList<Ubicacion> ubicaciones = new ArrayList<>();
        
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                Ubicacion ubicacion = new Ubicacion(rs.getInt("idubicaciones"), rs.getString("nombre"), rs.getString("contacto"), rs.getString("direccion"), rs.getString("ciudad"), rs.getString("identificacion"));
                ubicaciones.add(ubicacion);
            }
            return ubicaciones;
            
            
        }catch(SQLException e){
            System.out.println(e);
            return ubicaciones;
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                System.out.println(e);
            }
        }
    }
    public boolean insertar(Ubicacion ubicaciones){
        PreparedStatement ps;
        Connection con = getConnection();
        
        String sql = "INSERT INTO ubicaciones (nombre, contacto, direccion, ciudad, identificacion) VALUES (?,?,?,?,?)";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, ubicaciones.getNombre());
            ps.setString(2, ubicaciones.getTelefono());
            ps.setString(3, ubicaciones.getDireccion());
            ps.setString(4, ubicaciones.getCiudad());
            ps.setString(5, ubicaciones.getIdentificacion());
            
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
    public boolean eliminar(Ubicacion pro){
        PreparedStatement ps;
        Connection con = getConnection();
        
        String sql = "DELETE FROM ubicaciones WHERE idubicaciones=?";
        
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
    public boolean modificar(Ubicacion pro){
        PreparedStatement ps;
        Connection con = getConnection();
        
        String sql = "UPDATE ubicaciones SET nombre=?, contacto=?, direccion=?, ciudad=?, identificacion=? WHERE idubicaciones=?";
        
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
            ps = conn.prepareStatement("SELECT idubicaciones, nombre, contacto, direccion, ciudad, identificacion FROM ubicaciones WHERE idubicaciones=?");
            ps.setString(1, id);
            rs= ps.executeQuery();
            if (rs.next()) {
                resultados[0]=rs.getInt("idubicaciones")+"";
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
    public boolean buscar(Ubicacion pro){
        PreparedStatement ps;
        ResultSet rs;
        Connection con = getConnection();
        
        String sql = "SELECT * FROM ubicaciones WHERE idubicaciones=?";
        
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, pro.getId());
            
            rs= ps.executeQuery();
            if(rs.next()){
                pro.setId(rs.getInt("idubicaciones"));
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
