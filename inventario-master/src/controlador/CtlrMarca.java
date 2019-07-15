/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import modelo.ConsultasMarca;
import modelo.Marca;
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
public final class CtlrMarca implements ActionListener {

    private final Marca marca;
    private final ConsultasMarca cmarca;
    private final CtlrProducto ctlrproducto;
    private final FrmActivos vmarca;
    private final DefaultTableModel modelomarca;
    private final DefaultTableModel modelomp;

    public CtlrMarca(Marca marca, ConsultasMarca cmarca, FrmActivos vmarca, CtlrProducto ctlrproducto) {
        this.marca = marca;
        this.cmarca = cmarca;
        this.vmarca = vmarca;
        this.ctlrproducto = ctlrproducto;
        this.modelomarca = new DefaultTableModel();
        this.modelomp = new DefaultTableModel();
        this.modelomarca.addColumn("ID");
        this.modelomarca.addColumn("Nombre");
        this.modelomp.addColumn("Código");
        this.modelomp.addColumn("Nombre");
        this.modelomp.addColumn("Peso");
        this.modelomp.addColumn("Cantidad");
        llenarTabla();
        this.vmarca.jtbMarca.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                getSelectedMarca();
                llenarTablaProductos();
                changeLabels();
            }
        });
        this.vmarca.btnEliminarMarca.addActionListener(this);
        this.vmarca.btnInsertarMarca.addActionListener(this);
        this.vmarca.btnModificarMarca.addActionListener(this);
        this.vmarca.btnBuscarMarca.addActionListener(this);
        this.vmarca.btnReporteMarca.addActionListener(this);
        this.vmarca.btnExcelMarca.addActionListener(this);
        this.vmarca.jtbMarca.setModel(modelomarca);
        this.vmarca.jtbMarca.setDefaultEditor(Object.class, null);
        this.vmarca.jtbMarcaProducto.setModel(modelomp);
        this.vmarca.jtbMarcaProducto.setDefaultEditor(Object.class, null);
    }

    public void limpiarTabla() {
        int size = modelomarca.getRowCount();
        for (int i = 0; i < size; i++) {
            modelomarca.removeRow(size - 1 - i);
        }
    }

    public void limpiarTablaProductos() {
        int size = modelomp.getRowCount();
        for (int i = 0; i < size; i++) {
            modelomp.removeRow(size - 1 - i);
        }
    }

    public void llenarTablaProductos() {
        limpiarTablaProductos();
        ArrayList<Producto> productos = cmarca.buscarProductos(marca);
        Object[] array = new Object[4];
        for (int i = 0; i < productos.size(); i++) {
            array[0] = productos.get(i).getCodigo();
            array[1] = productos.get(i).getNombre();
            array[2] = productos.get(i).getPeso();
            array[3] = productos.get(i).getStock();
            modelomp.addRow(array);
        }
    }

    public void changeLabels() {
        int fila = vmarca.jtbMarca.getSelectedRow();
        String id = vmarca.jtbMarca.getValueAt(fila, 0).toString();
        vmarca.lblPesoMarca.setText(cmarca.getSumPeso(Integer.parseInt(id)) + "");
        vmarca.lblCantidadMarca.setText(cmarca.getSum(Integer.parseInt(id), "cantidad") + "");
    }

    public void llenarTabla() {
        limpiarTabla();
        ArrayList<Marca> marcas = cmarca.getMarcas();
        Object[] array = new Object[2];
        for (int i = 0; i < marcas.size(); i++) {
            array[0] = marcas.get(i).getId();
            array[1] = marcas.get(i).getNombre();
            modelomarca.addRow(array);
        }
    }

    public void getSelectedMarca() {
        int fila = vmarca.jtbMarca.getSelectedRow();
        String id = vmarca.jtbMarca.getValueAt(fila, 0).toString();
        String[] rs = cmarca.buscarElemento(id);
        marca.setId(Integer.parseInt(rs[0]));
        vmarca.txtIDMarca.setText(rs[0]);
        marca.setNombre(rs[1]);
        vmarca.txtNombreMarca.setText(rs[1]);
        marca.setObservaciones(rs[2]);
        vmarca.txtObservaciones.setText(rs[2]);
    }

    public void limpiar() {
        vmarca.txtIDMarca.setText("");
        vmarca.txtNombreMarca.setText("");
        vmarca.txtObservaciones.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //**Boton buscar**
        if (e.getSource() == vmarca.btnBuscarMarca) {
            marca.setId(Integer.parseInt(vmarca.txtIDMarca.getText()));
            if (cmarca.buscar(marca)) {
                vmarca.txtIDMarca.setText(marca.getId() + "");
                vmarca.txtNombreMarca.setText(marca.getNombre());
                vmarca.txtObservaciones.setText(marca.getObservaciones());
            } else {
                JOptionPane.showMessageDialog(null, "No se encontro registro");
                limpiar();
            }
            //**Boton eliminar**
        } else if (e.getSource() == vmarca.btnEliminarMarca) {

            marca.setId(Integer.parseInt(vmarca.txtIDMarca.getText()));
            if (cmarca.eliminar(marca)) {
                JOptionPane.showMessageDialog(null, "Marca eliminada");
                ctlrproducto.llenarComboBox();
                limpiar();
                llenarTabla();
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar");
                limpiar();
                llenarTabla();
            }
            //**Boton insertar**
        } else if (e.getSource() == vmarca.btnInsertarMarca) {

            marca.setNombre(vmarca.txtNombreMarca.getText());
            marca.setObservaciones(vmarca.txtObservaciones.getText());
            if (cmarca.insertar(marca)) {
                JOptionPane.showMessageDialog(null, "Marca insertada");
                limpiar();
                llenarTabla();
                ctlrproducto.llenarComboBox();
            } else {
                JOptionPane.showMessageDialog(null, "Error al insertar marca");
                limpiar();
                llenarTabla();
            }
            //**Boton modificar**
        } else if (e.getSource() == vmarca.btnModificarMarca) {

            marca.setId(Integer.parseInt(vmarca.txtIDMarca.getText()));
            marca.setNombre(vmarca.txtNombreMarca.getText());
            marca.setObservaciones(vmarca.txtObservaciones.getText());

            if (cmarca.modificar(marca)) {
                JOptionPane.showMessageDialog(null, "Marca modificada");
                limpiar();
                llenarTabla();
            } else {
                JOptionPane.showMessageDialog(null, "Error al modificar marca");
                limpiar();
                llenarTabla();
            }
            //**Boton reporte**
        } else if (e.getSource() == vmarca.btnReporteMarca) {

            try {
                JasperReport reporte = null;
                String path = "src\\reporte\\ReporteMarca.jasper";

                Map parametros = new HashMap();
                int fila = vmarca.jtbMarca.getSelectedRow();

                parametros.put("id", vmarca.jtbMarca.getValueAt(fila, 0));

                reporte = (JasperReport) JRLoader.loadObjectFromFile(path);

                JasperPrint jprint = JasperFillManager.fillReport(reporte, parametros, cmarca.getConexion());

                JasperViewer view = new JasperViewer(jprint, false);

                view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

                view.setVisible(true);
            } catch (JRException ex) {
                Logger.getLogger(CtlrUbicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
            //**Boton excel**
        } else if (e.getSource() == vmarca.btnExcelMarca) {

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

                String[] cabecera = {"Código", "Nombre", "Descripción", "Peso", "Cantidad"};

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

                Connection con = cmarca.getConexion();
                PreparedStatement ps;
                ResultSet rs;

                int numFilaDatos = 5;

                CellStyle datosEstilo = book.createCellStyle();
                datosEstilo.setBorderBottom(BorderStyle.THIN);
                datosEstilo.setBorderLeft(BorderStyle.THIN);
                datosEstilo.setBorderTop(BorderStyle.THIN);
                datosEstilo.setBorderRight(BorderStyle.THIN);

                String sql = "SELECT codigo, nombre, descripcion, peso, cantidad FROM inventario.activo WHERE idmarca = ?";

                ps = con.prepareStatement(sql);
                int fila = vmarca.jtbMarca.getSelectedRow();
                ps.setInt(1, (int) vmarca.jtbMarca.getValueAt(fila, 0));
                rs = ps.executeQuery();

                int numCol = rs.getMetaData().getColumnCount();
                String nombreCliente = (String) vmarca.jtbMarca.getValueAt(fila, 1);
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

                Row filaTitulo = sheet.createRow(1);
                Cell celdaTitulo = filaTitulo.createCell(1);
                celdaTitulo.setCellStyle(tituloEstilo);
                celdaTitulo.setCellValue("Lista de materiales de la marca: " + nombreCliente);

                sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 3));

                sheet.autoSizeColumn(0);
                sheet.autoSizeColumn(1);
                sheet.autoSizeColumn(2);
                sheet.autoSizeColumn(3);
                sheet.autoSizeColumn(4);

                sheet.setZoom(150);
                Date date = new Date();
                String strDateFormat = "yyyy-MM-dd";
                DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                String formattedDate = dateFormat.format(date);

                FileOutputStream fileOut = new FileOutputStream("excel\\Reporte" + nombreCliente + " " + formattedDate + ".xlsx");
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
