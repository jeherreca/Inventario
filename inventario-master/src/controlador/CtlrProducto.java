/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import inventario.ComboBoxHelper;
import java.awt.event.ActionEvent;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import modelo.ConsultasProducto;
import modelo.Producto;
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
public final class CtlrProducto implements ActionListener {

    private final Producto producto;
    private final ConsultasProducto cproducto;
    private final FrmActivos vproducto;
    private final DefaultTableModel modelo;
    private final DefaultComboBoxModel cbxmodelo;
    private final CtlrBodega cbodega;

    public CtlrProducto(Producto producto, ConsultasProducto cproducto, FrmActivos vproducto, CtlrBodega cbodega) {
        this.producto = producto;
        this.cproducto = cproducto;
        this.vproducto = vproducto;
        this.cbodega = cbodega;
        this.modelo = new DefaultTableModel();
        this.cbxmodelo = new DefaultComboBoxModel();
        this.modelo.addColumn("Código");
        this.modelo.addColumn("Nombre");
        this.modelo.addColumn("Descripción");
        this.modelo.addColumn("Peso");
        this.modelo.addColumn("Marca");
        this.modelo.addColumn("Cantidad");
        llenarComboBox();
        llenarTabla();
        this.vproducto.jtbActivos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                getSelectedProducto();
            }
        });
        this.vproducto.btnEliminar.addActionListener(this);
        this.vproducto.btnBuscar.addActionListener(this);
        this.vproducto.btnInsertar.addActionListener(this);
        this.vproducto.btnModificar.addActionListener(this);
        this.vproducto.btnReporteProducto.addActionListener(this);
        this.vproducto.btnExcelActivo.addActionListener(this);
        this.vproducto.jtbActivos.setModel(modelo);
        this.vproducto.jtbActivos.setDefaultEditor(Object.class, null);
        this.vproducto.cbxMarca.setModel(cbxmodelo);
        this.vproducto.txtIDActivo.setVisible(false);
    }

    public void llenarTabla() {
        limpiarTabla();
        ArrayList<Producto> productos = cproducto.getProductos();
        Object[] array = new Object[6];
        for (int i = 0; i < productos.size(); i++) {
            array[0] = productos.get(i).getCodigo();
            array[1] = productos.get(i).getNombre();
            array[2] = productos.get(i).getDescripcion();
            array[3] = productos.get(i).getPeso();
            array[4] = cproducto.getNombreMarca(productos.get(i).getMarca());
            array[5] = productos.get(i).getStock();
            modelo.addRow(array);
        }
    }

    public void limpiarTabla() {
        int size = modelo.getRowCount();
        for (int i = 0; i < size; i++) {
            modelo.removeRow(size - 1 - i);
        }
    }

    public void getSelectedProducto() {
        int fila = vproducto.jtbActivos.getSelectedRow();
        String codigo = vproducto.jtbActivos.getValueAt(fila, 0).toString();
        String[] rs = cproducto.buscarElemento(codigo);
        vproducto.txtIDActivo.setText(rs[0]);
        vproducto.txtCodigoActivos.setText(rs[1]);
        vproducto.txtNombre.setText(rs[2]);
        vproducto.txtDescripcion.setText(rs[3]);
        vproducto.txtPeso.setText(rs[4]);
        vproducto.cbxMarca.getModel().setSelectedItem(new ComboBoxHelper(Integer.parseInt(rs[5]), cproducto.getNombreMarca(Integer.parseInt(rs[5]))));
        vproducto.txtCantidad.setText(rs[6]);
    }

    public void limpiar() {
        vproducto.txtIDActivo.setText("");
        vproducto.txtCodigoActivos.setText("");
        vproducto.txtNombre.setText("");
        vproducto.txtDescripcion.setText("");
        vproducto.txtPeso.setText("");
        vproducto.txtCantidad.setText("");
    }

    public void llenarComboBox() {
        cbxmodelo.removeAllElements();
        ArrayList<ComboBoxHelper> marcas = cproducto.getMarcas();
        for (int i = 0; i < marcas.size(); i++) {
            cbxmodelo.addElement(marcas.get(i));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vproducto.btnBuscar) {
            producto.setCodigo(vproducto.txtCodigoActivos.getText());
            if (cproducto.buscar(producto)) {
                vproducto.txtIDActivo.setText(producto.getId() + "");
                vproducto.txtCodigoActivos.setText(producto.getCodigo());
                vproducto.txtNombre.setText(producto.getNombre());
                vproducto.txtDescripcion.setText(producto.getDescripcion());
                vproducto.txtPeso.setText(producto.getPeso() + "");
                vproducto.cbxMarca.getModel().setSelectedItem(new ComboBoxHelper(producto.getMarca(), cproducto.getNombreMarca(producto.getMarca())));
                vproducto.txtCantidad.setText(producto.getStock() + "");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontro registro");
                limpiar();
            }

        } else if (e.getSource() == vproducto.btnEliminar) {

            producto.setId(Integer.parseInt(vproducto.txtIDActivo.getText()));
            if (cproducto.eliminar(producto)) {
                JOptionPane.showMessageDialog(null, "Producto eliminado");
                limpiar();
                llenarTabla();
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar");
                limpiar();
                llenarTabla();
            }
        } else if (e.getSource() == vproducto.btnInsertar) {

            producto.setCodigo(vproducto.txtCodigoActivos.getText());
            producto.setNombre(vproducto.txtNombre.getText());
            producto.setDescripcion(vproducto.txtDescripcion.getText());
            producto.setPeso(Double.parseDouble(vproducto.txtPeso.getText()));
            ComboBoxHelper marca = (ComboBoxHelper) vproducto.cbxMarca.getSelectedItem();
            producto.setMarca(marca.getId());
            producto.setStock(Integer.parseInt(vproducto.txtCantidad.getText()));
            if (cproducto.insertar(producto)) {
                cbodega.getCbodega().insertar(producto);
                cbodega.llenarTabla();
                JOptionPane.showMessageDialog(null, "Producto insertado");
                limpiar();
                llenarTabla();
            } else {
                JOptionPane.showMessageDialog(null, "Error al insertar producto");
                limpiar();
                llenarTabla();
            }

        } else if (e.getSource() == vproducto.btnModificar) {

            producto.setId(Integer.parseInt(vproducto.txtIDActivo.getText()));
            producto.setCodigo(vproducto.txtCodigoActivos.getText());
            producto.setNombre(vproducto.txtNombre.getText());
            producto.setDescripcion(vproducto.txtDescripcion.getText());
            producto.setPeso(Double.parseDouble(vproducto.txtPeso.getText()));
            ComboBoxHelper marca = (ComboBoxHelper) vproducto.cbxMarca.getSelectedItem();
            producto.setMarca(marca.getId());
            producto.setStock(Integer.parseInt(vproducto.txtCantidad.getText()));

            if (cproducto.modificar(producto)) {
                JOptionPane.showMessageDialog(null, "Producto modificado");
                limpiar();
                llenarTabla();
            } else {
                JOptionPane.showMessageDialog(null, "Error al modificar producto");
                limpiar();
                llenarTabla();
            }
        } else if (e.getSource() == vproducto.btnReporteProducto) {

            try {
                JasperReport reporte = null;
                String path = "src\\reporte\\ReporteActivos.jasper";

                reporte = (JasperReport) JRLoader.loadObjectFromFile(path);

                JasperPrint jprint = JasperFillManager.fillReport(reporte, null, cproducto.getConexion());

                JasperViewer view = new JasperViewer(jprint, false);

                view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

                view.setVisible(true);
            } catch (JRException ex) {
                Logger.getLogger(CtlrUbicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getSource() == vproducto.btnExcelActivo) {

            Workbook book = new XSSFWorkbook();
            Sheet sheet = book.createSheet("Productos");

            try {
                int imgIndex;
                try (InputStream is = new FileInputStream("src\\resources\\logoandamas.jpg")) {
                    byte[] bytes = IOUtils.toByteArray(is);
                    imgIndex = book.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
                }

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

                Row filaTitulo = sheet.createRow(1);
                Cell celdaTitulo = filaTitulo.createCell(1);
                celdaTitulo.setCellStyle(tituloEstilo);
                celdaTitulo.setCellValue("Lista de materiales");

                sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 3));

                String[] cabecera = {"Código", "Nombre", "Descripción", "Peso", "Cantidad", "Marca"};

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

                Connection con = cproducto.getConexion();
                PreparedStatement ps;
                ResultSet rs;

                int numFilaDatos = 5;

                CellStyle datosEstilo = book.createCellStyle();
                datosEstilo.setBorderBottom(BorderStyle.THIN);
                datosEstilo.setBorderLeft(BorderStyle.THIN);
                datosEstilo.setBorderTop(BorderStyle.THIN);
                datosEstilo.setBorderRight(BorderStyle.THIN);

                String sql = "SELECT codigo, activo.nombre, descripcion, peso, cantidad, marca.nombre as marca FROM activo JOIN marca ON activo.idmarca = marca.idmarca";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();

                int numCol = rs.getMetaData().getColumnCount();

                while (rs.next()) {
                    Row filaDatos = sheet.createRow(numFilaDatos);
                    for (int i = 0; i < numCol; i++) {
                        Cell celdaDatos = filaDatos.createCell(i);
                        celdaDatos.setCellStyle(datosEstilo);

                        if (i == 3) {
                            celdaDatos.setCellValue(rs.getDouble(i + 1));
                        } else {
                            if (i == 4) {
                                celdaDatos.setCellValue(rs.getInt(i + 1));
                            } else {
                                celdaDatos.setCellValue(rs.getString(i + 1));
                            }
                        }
                    }
                    numFilaDatos++;
                }

                sheet.autoSizeColumn(0);
                sheet.autoSizeColumn(1);
                sheet.autoSizeColumn(2);
                sheet.autoSizeColumn(3);
                sheet.autoSizeColumn(4);
                sheet.autoSizeColumn(5);

                sheet.setZoom(150);
                Date date = new Date();
                String strDateFormat = "yyyy-MM-dd";
                DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                String formattedDate = dateFormat.format(date);

                try (FileOutputStream fileOut = new FileOutputStream("ReporteInventario " + formattedDate + ".xlsx")) {
                    book.write(fileOut);
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(CtlrBodega.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | SQLException ex) {
                Logger.getLogger(CtlrBodega.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
