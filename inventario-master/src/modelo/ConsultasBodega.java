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
public class ConsultasBodega extends Conexion {
    public Connection getConexion(){
        return getConnection();
    }
    public boolean insertar(Producto pro) {
        PreparedStatement ps;
        Connection con = getConnection();
        String sql = "INSERT INTO bodega (idproducto, cantidad) VALUES (?,?) ";
        int id = buscarIDProducto(pro.getCodigo());
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, pro.getStock());

            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public int buscarIDProducto(String codigo) {
        PreparedStatement ps;
        Connection con = getConnection();
        String sql = "SELECT idproductos FROM activo WHERE codigo =?";
        ResultSet rs;
        int id = 0;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, codigo);

            rs = ps.executeQuery();

            if (rs.next()) {

                id = rs.getInt("idproductos");
            }
            return id;
        } catch (SQLException e) {
            System.out.println(e);
            return id;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public ArrayList<Object[]> getBodegas() {
        PreparedStatement ps;
        Connection con = getConnection();
        String sql = "SELECT activo.codigo, activo.nombre, activo.descripcion, bodega.cantidad FROM activo INNER JOIN bodega ON activo.idproductos = bodega.idproducto";
        ResultSet rs;
        ArrayList<Object[]> productos = new ArrayList<>();

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Object[] bodega = {rs.getString("codigo"), rs.getString("nombre"), rs.getString("descripcion"), rs.getInt("cantidad")};
                productos.add(bodega);
            }

            return productos;
        } catch (SQLException e) {
            System.out.println(e);
            return productos;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }
}
