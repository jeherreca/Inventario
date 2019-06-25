/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import modelo.ConsultasUbicacion;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import modelo.Ubicacion;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vista.FrmActivos;

/**
 *
 * @author Administrator
 */
public final class CtlrUbicacion implements ActionListener {

    private final Ubicacion proveedor;
    private final ConsultasUbicacion cproveedor;
    private final FrmActivos vproveedor;
    private final DefaultTableModel modelo;
    private final DefaultTableModel modeloc;

    public CtlrUbicacion(Ubicacion proveedor, ConsultasUbicacion cproveedor, FrmActivos vproveedor) {
        this.proveedor = proveedor;
        this.cproveedor = cproveedor;
        this.vproveedor = vproveedor;
        this.modelo = new DefaultTableModel();
        this.modelo.addColumn("ID");
        this.modelo.addColumn("Nombre");
        this.modelo.addColumn("Teléfono");
        this.modelo.addColumn("Dirección");
        this.modelo.addColumn("Ciudad");
        this.modelo.addColumn("Identificación");
        this.modeloc = new DefaultTableModel();
        this.modeloc.addColumn("Código");
        this.modeloc.addColumn("Nombre");
        this.modeloc.addColumn("Peso");
        this.modeloc.addColumn("Cantidad");
        llenarTabla();
        this.vproveedor.jtbProveedores.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                getSelectedProveedor();
                llenarTablaProductos();
                changeLabels();
            }
        });
        this.vproveedor.btnReporteCliente.addActionListener(this);
        this.vproveedor.btnBuscarProveedor.addActionListener(this);
        this.vproveedor.btnInsertarProveedor.addActionListener(this);
        this.vproveedor.btnEliminarProveedor.addActionListener(this);
        this.vproveedor.btnModificarProveedor.addActionListener(this);
        this.vproveedor.btnExcelCliente.addActionListener(this);
        this.vproveedor.jtbProveedores.setModel(modelo);
        this.vproveedor.jtbProveedores.setDefaultEditor(Object.class, null);
        this.vproveedor.jtbUbicacionProducto.setModel(modeloc);
        this.vproveedor.jtbUbicacionProducto.setDefaultEditor(Object.class, null);
    }

    public void changeLabels() {
        vproveedor.lblCantidadCliente.setText(cproveedor.getSum(proveedor.getId()) + "");
        vproveedor.lblPesoCliente.setText(cproveedor.getSumPeso(proveedor.getId()) + "");
    }

    public void llenarTablaProductos() {
        limpiarTablaProductos();
        ArrayList<Object[]> productos = cproveedor.buscarProductos(proveedor);
        for (int i = 0; i < productos.size(); i++) {
            modeloc.addRow(productos.get(i));
        }
    }

    public void llenarTabla() {
        limpiarTabla();
        ArrayList<Ubicacion> proveedores = cproveedor.getUbicacion();
        Object[] array = new Object[6];
        for (int i = 0; i < proveedores.size(); i++) {
            array[0] = proveedores.get(i).getId();
            array[1] = proveedores.get(i).getNombre();
            array[2] = proveedores.get(i).getTelefono();
            array[3] = proveedores.get(i).getDireccion();
            array[4] = proveedores.get(i).getCiudad();
            array[5] = proveedores.get(i).getIdentificacion();
            modelo.addRow(array);
        }
    }

    public void limpiarTablaProductos() {
        int size = modeloc.getRowCount();
        for (int i = 0; i < size; i++) {
            modeloc.removeRow(size - 1 - i);
        }
    }

    public void limpiarTabla() {
        int size = modelo.getRowCount();
        for (int i = 0; i < size; i++) {
            modelo.removeRow(size - 1 - i);
        }
    }

    public void getSelectedProveedor() {
        int fila = vproveedor.jtbProveedores.getSelectedRow();
        System.out.println(fila);
        String codigo = vproveedor.jtbProveedores.getValueAt(fila, 0).toString();
        String[] rs = cproveedor.buscarElemento(codigo);
        proveedor.setId(Integer.parseInt(rs[0]));
        vproveedor.txtIDProveedor.setText(rs[0]);
        vproveedor.txtNombreProveedor.setText(rs[1]);
        vproveedor.txtContacto.setText(rs[2]);
        vproveedor.txtDireccion.setText(rs[3]);
        vproveedor.txtCiudad.setText(rs[4]);
        vproveedor.txtIdentificacion.setText(rs[5]);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vproveedor.btnBuscarProveedor) {
            proveedor.setId(Integer.parseInt(vproveedor.txtIDProveedor.getText()));
            if (cproveedor.buscar(proveedor)) {
                vproveedor.txtIDProveedor.setText(proveedor.getId() + "");
                vproveedor.txtNombreProveedor.setText(proveedor.getNombre());
                vproveedor.txtContacto.setText(proveedor.getTelefono());
                vproveedor.txtDireccion.setText(proveedor.getDireccion());
                vproveedor.txtCiudad.setText(proveedor.getCiudad() + "");
                vproveedor.txtIdentificacion.setText(proveedor.getIdentificacion() + "");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontro registro");
                limpiar();
            }
        } else {
            if (e.getSource() == vproveedor.btnEliminarProveedor) {
                proveedor.setId(Integer.parseInt(vproveedor.txtIDProveedor.getText()));
                if (cproveedor.eliminar(proveedor)) {
                    JOptionPane.showMessageDialog(null, "Cliente eliminado");
                    limpiar();
                    llenarTabla();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar cliente");
                    limpiar();
                    llenarTabla();
                }
            } else {
                if (e.getSource() == vproveedor.btnInsertarProveedor) {
                    proveedor.setNombre(vproveedor.txtNombreProveedor.getText());
                    proveedor.setTelefono(vproveedor.txtContacto.getText());
                    proveedor.setDireccion(vproveedor.txtDireccion.getText());
                    proveedor.setCiudad(vproveedor.txtCiudad.getText());
                    proveedor.setIdentificacion(vproveedor.txtIdentificacion.getText());
                    if (cproveedor.insertar(proveedor)) {
                        JOptionPane.showMessageDialog(null, "Cliente insertado");
                        limpiar();
                        llenarTabla();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al insertar cliente");
                        limpiar();
                        llenarTabla();
                    }
                } else {
                    if (e.getSource() == vproveedor.btnModificarProveedor) {
                        proveedor.setId(Integer.parseInt(vproveedor.txtIDProveedor.getText()));
                        proveedor.setNombre(vproveedor.txtNombreProveedor.getText());
                        proveedor.setTelefono(vproveedor.txtContacto.getText());
                        proveedor.setDireccion(vproveedor.txtDireccion.getText());
                        proveedor.setCiudad(vproveedor.txtCiudad.getText());
                        proveedor.setIdentificacion(vproveedor.txtIdentificacion.getText());

                        if (cproveedor.modificar(proveedor)) {
                            JOptionPane.showMessageDialog(null, "Cliente modificado");
                            limpiar();
                            llenarTabla();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al modificar cliente");
                            limpiar();
                            llenarTabla();
                        }
                    }else{
                        if (e.getSource() == vproveedor.btnReporteCliente) {
                            try{
                                JasperReport reporte = null;
                                String path = "src\\reporte\\ReporteCliente.jasper";
                                
                                Map parametros = new HashMap();
                                int fila = vproveedor.jtbProveedores.getSelectedRow();
                                
                                parametros.put("id", vproveedor.jtbProveedores.getValueAt(fila, 0) );
                                
                                reporte = (JasperReport) JRLoader.loadObjectFromFile(path);
                                
                                JasperPrint jprint = JasperFillManager.fillReport(reporte, parametros, cproveedor.getConexion());
                                
                                JasperViewer view = new JasperViewer (jprint, false);
                                
                                view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                                
                                view.setVisible(true);
                            } catch (JRException ex) {
                                Logger.getLogger(CtlrUbicacion.class.getName()).log(Level.SEVERE, null, ex);
                            } 
                        } else {
                            if (e.getSource() == vproveedor.btnExcelCliente) {
                                Workbook book = new XSSFWorkbook();
                                Sheet sheet = book.createSheet("Productos");

                                try {
                                    InputStream is = new FileInputStream("src\\resources\\logoandamas.jpg");    
                                    byte[] bytes = IOUtils.toByteArray(is);
                                    int imgIndex = book.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
                                    is.close();

                                    CreationHelper help = book.getCreationHelper();
                                    Drawing draw = sheet.createDrawingPatriarch();

                                    ClientAnchor anchor = help.createClientAnchor();
                                    anchor.setCol1(0);
                                    anchor.setRow1(0);
                                    Picture pict = draw.createPicture(anchor, imgIndex);
                                    pict.resize(1, 3);

                                    CellStyle tituloEstilo = book.createCellStyle();
                                    tituloEstilo.setAlignment(HorizontalAlignment.CENTER);
                                    tituloEstilo.setVerticalAlignment(VerticalAlignment.CENTER);
                                    Font fuenteTitulo = book.createFont();
                                    fuenteTitulo.setFontName("Arial");
                                    fuenteTitulo.setBold(true);
                                    fuenteTitulo.setFontHeightInPoints((short) 14);
                                    tituloEstilo.setFont(fuenteTitulo);


                                    String[] cabecera = {"Código", "Nombre", "Descripción", "Cantidad"};

                                    CellStyle headerStyle = book.createCellStyle();
                                    headerStyle.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
                                    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                                    headerStyle.setBorderBottom(BorderStyle.THIN);
                                    headerStyle.setBorderBottom(BorderStyle.THIN);
                                    headerStyle.setBorderBottom(BorderStyle.THIN);

                                    Font fuente = book.createFont();
                                    fuente.setFontName("Arial");
                                    fuente.setBold(true);
                                    fuente.setFontHeightInPoints((short) 14);
                                    fuente.setColor(IndexedColors.WHITE.getIndex());
                                    fuente.setFontHeightInPoints((short) 12);
                                    headerStyle.setFont(fuente);

                                    Row filaEncabezados = sheet.createRow(4);

                                    for (int i = 0; i < cabecera.length; i++) {
                                        Cell celdaEncabezado = filaEncabezados.createCell(i);
                                        celdaEncabezado.setCellStyle(headerStyle);
                                        celdaEncabezado.setCellValue(cabecera[i]);
                                    }

                                    Connection con = cproveedor.getConexion();
                                    PreparedStatement ps;
                                    ResultSet rs;

                                    int numFilaDatos = 5;

                                    CellStyle datosEstilo = book.createCellStyle();
                                    datosEstilo.setBorderBottom(BorderStyle.THIN);
                                    datosEstilo.setBorderLeft(BorderStyle.THIN);
                                    datosEstilo.setBorderTop(BorderStyle.THIN);
                                    datosEstilo.setBorderRight(BorderStyle.THIN);

                                    String sql = "SELECT activo.codigo, activo.nombre, activo.descripcion, ubicacion_productos.cantidad, ubicaciones.nombre as cliente FROM (( inventario.activo INNER JOIN inventario.ubicacion_productos ON activo.idproductos = ubicacion_productos.idproductos) INNER JOIN inventario.ubicaciones ON ubicacion_productos.idubicacion = ubicaciones.idubicaciones ) WHERE ubicacion_productos.idubicacion = ?";
                                    
                                    
                                    ps = con.prepareStatement(sql);
                                    int fila = vproveedor.jtbProveedores.getSelectedRow();
                                    ps.setInt(1, (int) vproveedor.jtbProveedores.getValueAt(fila, 0));
                                    rs = ps.executeQuery();

                                    int numCol = rs.getMetaData().getColumnCount() - 1;
                                    String nombreCliente = "";
                                    while (rs.next()) {
                                        Row filaDatos = sheet.createRow(numFilaDatos);
                                        for (int i = 0; i < numCol; i++) {
                                            Cell celdaDatos = filaDatos.createCell(i);
                                            celdaDatos.setCellStyle(datosEstilo);
                                            nombreCliente = rs.getString("cliente");
                                            if (i == 3) {
                                                celdaDatos.setCellValue(rs.getInt(i+1));
                                            }else{
                                                celdaDatos.setCellValue(rs.getString(i+1));
                                            }
                                        }
                                        numFilaDatos++;              
                                    }
                                    
                                                                        
                                    Row filaTitulo = sheet.createRow(1);
                                    Cell celdaTitulo = filaTitulo.createCell(1);
                                    celdaTitulo.setCellStyle(tituloEstilo);
                                    celdaTitulo.setCellValue("Lista de materiales en "+nombreCliente);

                                    sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 2));
                                    
                                    sheet.autoSizeColumn(0);
                                    sheet.autoSizeColumn(1);
                                    sheet.autoSizeColumn(2);
                                    sheet.autoSizeColumn(3);

                                    sheet.setZoom(150);
                                    Date date = new Date();
                                    String strDateFormat = "yyyy-MM-dd";
                                    DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                                    String formattedDate= dateFormat.format(date);

                                    FileOutputStream fileOut = new FileOutputStream("Reporte"+nombreCliente+" "+formattedDate+".xlsx");
                                    book.write(fileOut);
                                    fileOut.close();

                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(CtlrBodega.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException | SQLException ex) {
                                    Logger.getLogger(CtlrBodega.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void limpiar() {
        vproveedor.txtIDProveedor.setText("");
        vproveedor.txtNombreProveedor.setText("");
        vproveedor.txtContacto.setText("");
        vproveedor.txtDireccion.setText("");
        vproveedor.txtCiudad.setText("");
        vproveedor.txtIdentificacion.setText("");
    }
}
