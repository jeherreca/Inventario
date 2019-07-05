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
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import modelo.ConsultasBodega;
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
public final class CtlrBodega implements ActionListener {

    private ConsultasBodega cbodega;
    private final FrmActivos vbodega;
    private final DefaultTableModel modelo;

    public CtlrBodega(ConsultasBodega cbodega, FrmActivos vbodega) {
        this.cbodega = cbodega;
        this.vbodega = vbodega;
        this.modelo = new DefaultTableModel();
        this.modelo.addColumn("Código");
        this.modelo.addColumn("Nombre");
        this.modelo.addColumn("Descripción");
        this.modelo.addColumn("Cantidad");
        this.vbodega.jtbBodega.setDefaultEditor(Object.class, null);
        llenarTabla();
        this.vbodega.jtbBodega.setModel(modelo);
        this.vbodega.btnReporteBodega.addActionListener(this);
        this.vbodega.btnExcelBodega.addActionListener(this);
    }

    public ConsultasBodega getCbodega() {
        return cbodega;
    }

    public void setCbodega(ConsultasBodega cbodega) {
        this.cbodega = cbodega;
    }

    public void llenarTabla() {
        limpiarTablaBodega();
        ArrayList<Object[]> proveedores = cbodega.getBodegas();
        for (int i = 0; i < proveedores.size(); i++) {
            modelo.addRow(proveedores.get(i));
        }
    }

    public void limpiarTablaBodega() {
        int size = modelo.getRowCount();
        for (int i = 0; i < size; i++) {
            modelo.removeRow(size - 1 - i);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //**Boton reporte**
        if (e.getSource() == vbodega.btnReporteBodega) {
            try {
                JasperReport reporte = null;
                String path = "src\\reporte\\ReporteBodega.jasper";

                reporte = (JasperReport) JRLoader.loadObjectFromFile(path);

                JasperPrint jprint = JasperFillManager.fillReport(reporte, null, cbodega.getConexion());

                JasperViewer view = new JasperViewer(jprint, false);

                view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

                view.setVisible(true);
            } catch (JRException ex) {
                Logger.getLogger(CtlrUbicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
            //**Boton excel**
        } else if (e.getSource() == vbodega.btnExcelBodega) {

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

                Row filaTitulo = sheet.createRow(1);
                Cell celdaTitulo = filaTitulo.createCell(1);
                celdaTitulo.setCellStyle(tituloEstilo);
                celdaTitulo.setCellValue("Lista de materiales en bodega");

                sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 3));

                String[] cabecera = {"Código", "Nombre", "Descripción", "Cantidad", "Marca"};

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

                Connection con = cbodega.getConexion();
                PreparedStatement ps;
                ResultSet rs;

                int numFilaDatos = 5;

                CellStyle datosEstilo = book.createCellStyle();
                datosEstilo.setBorderBottom(BorderStyle.THIN);
                datosEstilo.setBorderLeft(BorderStyle.THIN);
                datosEstilo.setBorderTop(BorderStyle.THIN);
                datosEstilo.setBorderRight(BorderStyle.THIN);

                String sql = "SELECT activo.codigo, activo.nombre, activo.descripcion, bodega.cantidad, marca.nombre AS marca FROM ((inventario.bodega JOIN inventario.activo ON bodega.idproducto = activo.idproductos) JOIN inventario.marca ON activo.idmarca = marca.idmarca )";
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
                            celdaDatos.setCellValue(rs.getString(i + 1));
                        }
                    }
                    numFilaDatos++;
                }

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

                FileOutputStream fileOut = new FileOutputStream("ReporteBodega " + formattedDate + ".xlsx");
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
