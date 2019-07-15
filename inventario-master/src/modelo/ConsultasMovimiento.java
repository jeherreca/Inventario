/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import inventario.ComboBoxHelper;
import inventario.ListHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class ConsultasMovimiento extends Conexion {

    public Connection getConexion() {
        return getConnection();
    }

    public ArrayList<ComboBoxHelper> getComboBoxClientes() {
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;

        String sql = "SELECT idubicaciones, nombre FROM ubicaciones";
        ArrayList<ComboBoxHelper> clientes = new ArrayList<>();
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                clientes.add(new ComboBoxHelper(rs.getInt("idubicaciones"), rs.getString("nombre")));
            }

            return clientes;
        } catch (SQLException e) {
            System.out.println(e);
            return clientes;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public boolean getMovimiento(String id, Movimiento movimiento) {
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;

        String sql = "SELECT identrada, fecha, codigo, idubicacion, observaciones FROM movimiento WHERE codigo=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                movimiento.setId(rs.getInt("identrada"));
                movimiento.setCodigo(rs.getString("codigo"));
                movimiento.setFecha(rs.getDate("fecha"));
                movimiento.setObservaciones(rs.getString("observaciones"));
                movimiento.setUbicacion(rs.getInt("idubicacion"));
            }

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

    public String getUbicacion(int id) {
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;

        String sql = "SELECT nombre FROM ubicaciones WHERE idubicaciones=?";

        String nombre = "";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                nombre = rs.getString("nombre");
            }

            return nombre;
        } catch (SQLException e) {
            System.out.println(e);
            return nombre;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public boolean verificar(int id, int idcliente) {
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;

        String sql = "SELECT * FROM ubicacion_productos WHERE idproductos=? AND idubicacion=?";
        boolean r = false;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, idcliente);
            rs = ps.executeQuery();

            if (rs.next()) {
                r = true;
            }
            return r;
        } catch (SQLException e) {
            System.out.println(e);
            return r;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public ArrayList<Object[]> getSalidasProd(String codigo) {
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;

        String sql = "SELECT activo.codigo, activo.nombre, movimiento_productos.cantidad FROM inventario.activo INNER JOIN inventario.movimiento_productos ON activo.idproductos = movimiento_productos.idproducto WHERE movimiento_productos.idmovimiento=?";

        ArrayList<Object[]> salprod = new ArrayList<>();

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, getIDByCode(codigo));

            rs = ps.executeQuery();

            while (rs.next()) {
                Object[] movprod = {rs.getString("codigo"), rs.getString("nombre"), rs.getInt("cantidad")};
                salprod.add(movprod);
            }

            return salprod;
        } catch (SQLException e) {
            System.out.println(e);
            return salprod;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public ArrayList<Producto> getProductosByID(int id) {
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;

        ArrayList<Producto> prods = new ArrayList<>();

        String sql = "SELECT idproductos, cantidad FROM ubicacion_productos WHERE idubicacion=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                Producto p = getProducto(rs.getInt("idproductos"));
                p.setStock(rs.getInt("cantidad"));
                prods.add(p);
            }

            return prods;
        } catch (SQLException e) {
            System.out.println(e);
            return prods;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public boolean sumarUbicacionProducto(int cantidad, int id, int idcliente) {
        PreparedStatement ps;
        Connection con = getConnection();

        String sql = "UPDATE ubicacion_productos SET cantidad = cantidad+? WHERE idproductos=? AND idubicacion=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, cantidad);
            ps.setInt(2, id);
            ps.setInt(3, idcliente);

            ps.execute();

            return true;
        } catch (SQLException e) {
            System.out.println("Error al sumar ubicacion producto: " + e);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public boolean insertarUbicacionProducto(int idcliente, int cantidad, int idproducto) {
        PreparedStatement ps;
        Connection con = getConnection();

        String sql = "INSERT INTO ubicacion_productos (idubicacion, idproductos, cantidad) VALUES (?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, idcliente);
            ps.setInt(2, idproducto);
            ps.setInt(3, cantidad);

            ps.execute();

            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar ubicacion_producto: " + e);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public int getIDByCode(String codigo) {
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;

        String sql = "SELECT identrada FROM movimiento WHERE codigo=?";

        int id = 0;

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, codigo);
            rs = ps.executeQuery();

            if (rs.next()) {
                id = rs.getInt("identrada");
            }
            System.out.println("El id del producto es: "+id);
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

    public boolean sumarBodega(int cantidad, int id) {
        PreparedStatement ps;
        Connection con = getConnection();

        String sql = "UPDATE bodega SET cantidad = cantidad+? WHERE idproducto=?";
        System.out.println("cantidad a sumar: "+cantidad);
        System.out.println("producto a sumar: "+id);
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, cantidad);
            ps.setInt(2, id);

            ps.execute();
            System.out.println("sumado");
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

    public ArrayList<Movimiento> getMovimientos(String tipo) {
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;

        String sql = "SELECT * FROM movimiento WHERE tipo=?";

        ArrayList<Movimiento> salidas = new ArrayList<>();

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, tipo);
            rs = ps.executeQuery();

            while (rs.next()) {
                Movimiento mov = new Movimiento();
                mov.setId(rs.getInt("identrada"));
                mov.setFecha(rs.getDate("fecha"));
                mov.setCodigo(rs.getString("codigo"));
                mov.setUbicacion(rs.getInt("idubicacion"));
                mov.setObservaciones(rs.getString("observaciones"));
                mov.setTipo("salida");

                salidas.add(mov);
            }
            return salidas;
        } catch (SQLException e) {
            System.out.println(e);
            return salidas;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }

    }

    public boolean insertarMovimientoProducto(MovimientoProducto movprod) {
        PreparedStatement ps;
        Connection con = getConnection();

        String sql = "INSERT INTO movimiento_productos (idmovimiento, idproducto, cantidad) VALUES (?,?,?)";

        try {
            ps = con.prepareStatement(sql);

            ps.setInt(1, movprod.getMovimiento());
            ps.setInt(2, movprod.getActivo());
            ps.setInt(3, movprod.getCantidad());

            ps.execute();

            System.out.println("Movimiento producto guardado");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al guardar Movimiento producto");
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

    public boolean insertar(Movimiento mov) {
        PreparedStatement ps;
        Connection con = getConnection();

        String sql = "INSERT INTO movimiento (fecha, codigo, idubicacion, tipo, observaciones) VALUES (?,?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(mov.getFecha().getTime()));
            ps.setString(2, mov.getCodigo());
            ps.setInt(3, mov.getUbicacion());
            ps.setString(4, mov.getTipo());
            ps.setString(5, mov.getObservaciones());

            ps.execute();

            System.out.println("Movimiento guardado");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al guardar movimiento");
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

    public ArrayList<ListHelper> getClientes() {
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;

        String sql = "SELECT idubicaciones, nombre FROM ubicaciones";
        ArrayList<ListHelper> clientes = new ArrayList<>();

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                clientes.add(new ListHelper(rs.getInt("idubicaciones"), rs.getString("nombre")));
            }

            return clientes;
        } catch (SQLException e) {
            System.out.println(e);
            return clientes;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public boolean restarCliente(int cantidad, int idcliente, int id) {
        PreparedStatement ps;
        Connection con = getConnection();

        String sql = "UPDATE ubicacion_productos SET cantidad=cantidad-? WHERE idproductos=? AND idubicacion=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, cantidad);
            ps.setInt(2, id);
            ps.setInt(3, idcliente);

            ps.execute();

            System.out.println("Ubicación modificada");
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

    public boolean restarBodega(int cantidad, int id) {
        PreparedStatement ps;
        Connection con = getConnection();

        String sql = "UPDATE bodega SET cantidad=cantidad-? WHERE idproducto=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, cantidad);
            ps.setInt(2, id);

            ps.execute();

            System.out.println("Bodega modificada");
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

    public ArrayList<Producto> getProductosBodega() {
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;

        String sql = "SELECT * FROM bodega ";

        ArrayList<Producto> prod = new ArrayList<>();

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                prod.add(getProducto(rs.getInt("idproducto")));
                prod.get(prod.size() - 1).setStock(rs.getInt("cantidad"));
            }
            return prod;
        } catch (SQLException e) {

            System.out.println(e);
            return prod;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public boolean updateMovimiento(Movimiento mov, int movimiento) {
        PreparedStatement ps;
        Connection con = getConnection();

        String sql = "UPDATE movimiento SET fecha=?, idubicacion=?, observaciones=? WHERE identrada=? ";
        try {
            ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(mov.getFecha().getTime()));
            ps.setInt(2, mov.getUbicacion());
            ps.setString(3, mov.getObservaciones());
            ps.setInt(4, movimiento);
            ps.execute();

            System.out.println("Entrada modificada");

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

    public boolean updateMovProd(int cantidad, int id) {
        PreparedStatement ps;
        Connection con = getConnection();

        String sql = "UPDATE movimiento_productos SET cantidad =? WHERE idproducto =?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, cantidad);
            ps.setInt(2, id);

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

    public Producto getProductoByCodigo(String codigo) {
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;

        String sql = "SELECT * FROM activo WHERE codigo = ?";

        Producto prod = new Producto();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, codigo);

            rs = ps.executeQuery();

            if (rs.next()) {
                prod = new Producto(rs.getInt("idproductos"), rs.getString("codigo"), rs.getString("nombre"), rs.getString("descripcion"), rs.getDouble("peso"), rs.getInt("idmarca"), rs.getInt("cantidad"));
            }
            return prod;
        } catch (SQLException e) {
            System.out.println(e);
            return prod;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public Producto getProducto(int id) {
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;

        String sql = "SELECT * FROM activo WHERE idproductos = ?";

        Producto prod = new Producto();
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                prod = new Producto(rs.getInt("idproductos"), rs.getString("codigo"), rs.getString("nombre"), rs.getString("descripcion"), rs.getDouble("peso"), rs.getInt("idmarca"), rs.getInt("cantidad"));
            }
            return prod;
        } catch (SQLException e) {
            System.out.println(e);
            return prod;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public boolean borrarMovimientos(String codigo) {
        PreparedStatement ps;
        Connection con = getConnection();

        String sql = "DELETE FROM movimiento_productos WHERE idmovimiento=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, getIDByCode(codigo));
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

    public int getIdClienteByName(String name) {
        PreparedStatement ps;
        Connection con = getConnection();
        ResultSet rs;

        String sql = "SELECT idubicaciones FROM ubicaciones WHERE nombre=?";

        int id = 0;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, name);

            rs = ps.executeQuery();

            if (rs.next()) {
                id = rs.getInt("idubicaciones");
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

}
