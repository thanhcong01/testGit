/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.exportpdf;

import com.ftu.inventory.bo.StockTransactionDetail;
import com.ftu.inventory.bo.StockTransactionSerial;
import com.ftu.java.bo.SerialA;
import com.ftu.utils.AnalysisSerial;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.xml.bind.JAXBException;
import org.docx4j.convert.out.pdf.PdfConversion;
import org.docx4j.convert.out.pdf.viaXSLFO.PdfSettings;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author E5420
 */
public class ExportInventory {

    private String demo = "/WEB-INF/template/demo.docx";
    private String demo2 = "/WEB-INF/template/";
    public StreamedContent export(List<StockTransactionDetail> model) throws Exception {
        String filePath = null;
        InputStream f = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(demo);

        /* replace template doc by data of model */
        WordprocessingMLPackage wmp = WordprocessingMLPackage
                .load(f);
        HashMap map = new HashMap();
        List<SerialA> lsDT = new ArrayList<>();
        for (StockTransactionDetail g : model) {
            g.setGoodsStatusName(g.getGoodsStatusName());
            Long k = new Long(g.getLsSerial().size());
            g.setQuantity(k);
            List<StockTransactionSerial> lsI = g.getLsSerial();
            AnalysisSerial as = new AnalysisSerial(lsI, null);
            lsDT.addAll(as.analysis());
        }
        WordExportUtils wordExportUtils = new WordExportUtils();
        wordExportUtils.replaceTable(wmp, 0, model);
        wordExportUtils.replaceTable(wmp, 1, lsDT);
        wordExportUtils.replacePlaceholder(wmp, map);

        // convert word to PDF
        com.ftu.convertservice.PdfDocxFile pdfDocxFile = wordExportUtils.writePDFToStream(wmp, false);

        // write pdf to file
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
        String expFolder = resourceBundle.getString("expVETC");
        if (!new File(expFolder).exists()) {
            FileUtil.mkdirs(expFolder);
        }
        String name = "printGoods"
                + (new Date()).getTime() + ".pdf";
        filePath = expFolder + name;
        OutputStream outputStream = new FileOutputStream(filePath);
        outputStream.write(pdfDocxFile.getContent());
        outputStream.flush();
        outputStream.close();
//        AttachsDAO attDAO = new AttachsDAO();
//        Attachs att1 = attDAO.getLastByObjectId(model.getEquipmentProfileId());
//        Attachs att = new Attachs();
//        if (att1 != null) {
//            attDAO.delete(att1);
//        }
//        att.setAttachName(name);
//        att.setAttachPath(filePath);
//        att.setObjectId(model.getEquipmentProfileId());
//        att.setIsDeleted(0L);
//        attDAO.saveOrUpdate(att);
        return new DefaultStreamedContent(new FileInputStream(filePath), "application/pdf");
    }

    public StreamedContent importPrint(List<StockTransactionDetail> model,String code,String shop,String staff,String date, String tranTypeName) throws Docx4JException, JAXBException, FileNotFoundException {
        InputStream f = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(demo);

        /* replace template doc by data of model */
        WordprocessingMLPackage wmp = WordprocessingMLPackage
                .load(f);
        HashMap map = new HashMap();
 //       List<SerialA> lsDT = new ArrayList<>();
//        for (StockTransactionDetail g : model) {
//            g.setGoodsStatusName(g.getGoodsStatusName());
//            Long k = new Long(g.getLsSerial().size());
//            g.setQuantity(k);
//            List<StockTransactionSerial> lsI = g.getLsSerial();
//            AnalysisSerial as = new AnalysisSerial(lsI, null);
//            lsDT.addAll(as.analysis());
//        }
        WordExportUtils wordExportUtils = new WordExportUtils();
        map.put("importName",tranTypeName );
        map.put("code",code );
        map.put("shop",shop );
        map.put("staff",staff );
        map.put("date",date );
        wordExportUtils.replaceTable(wmp, 1, model);
       // wordExportUtils.replaceTable(wmp, 1, lsDT);
        wordExportUtils.replacePlaceholder(wmp, map);

        ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
        String expFolder = resourceBundle.getString("expVETC");
        if (!new File(expFolder).exists()) {
            FileUtil.mkdirs(expFolder);
        }
        String name = "printGoods"
                + (new Date()).getTime() + ".pdf";
        String filePath = expFolder + name;
        PdfSettings pdfSettings = new PdfSettings();
        OutputStream outputStream = new FileOutputStream(filePath);
        PdfConversion converter = new org.docx4j.convert.out.pdf.viaXSLFO.Conversion(
                wmp);
        converter.output(outputStream, pdfSettings);
        return new DefaultStreamedContent(new FileInputStream(filePath), "application/pdf");
    }

    public void importExp(List<StockTransactionDetail> model) throws Docx4JException, JAXBException, FileNotFoundException {
        InputStream f = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(demo);

        /* replace template doc by data of model */
        WordprocessingMLPackage wmp = WordprocessingMLPackage
                .load(f);
        HashMap map = new HashMap();
        List<SerialA> lsDT = new ArrayList<>();
        for (StockTransactionDetail g : model) {
            g.setGoodsStatusName(g.getGoodsStatusName());
            Long k = new Long(g.getLsSerial().size());
            g.setQuantity(k);
            List<StockTransactionSerial> lsI = g.getLsSerial();
            AnalysisSerial as = new AnalysisSerial(lsI, null);
            lsDT.addAll(as.analysis());
        }
        WordExportUtils wordExportUtils = new WordExportUtils();
        wordExportUtils.replaceTable(wmp, 0, model);
        wordExportUtils.replaceTable(wmp, 1, lsDT);
        wordExportUtils.replacePlaceholder(wmp, map);

        String name = "printGoods"
                + (new Date()).getTime() + ".docx";
        String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(demo2) + name;
//        PdfSettings pdfSettings = new PdfSettings();
        OutputStream outputStream = new FileOutputStream(filePath);
//        PdfConversion converter = new org.docx4j.convert.out.pdf.viaXSLFO.Conversion(
//                wmp);
//        converter.output(outputStream, pdfSettings);
        wmp.save(outputStream);
    }

}
