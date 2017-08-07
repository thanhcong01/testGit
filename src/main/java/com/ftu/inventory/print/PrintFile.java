/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.print;

import java.io.InputStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.PrinterName;

/**
 *
 * @author E5420
 */
public class PrintFile {

    public void printf(InputStream in) throws PrintException {
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        AttributeSet attributeSet = new HashAttributeSet();
        attributeSet.add(new PrinterName("NPI8DA48A", null));
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();

        DocPrintJob job = service.createPrintJob();
        Doc doc = new SimpleDoc(in, flavor, null);
        PrintJobWatcher watcher = new PrintJobWatcher(job);
        job.print(doc, null);
        watcher.waitForDone();
    }
}
