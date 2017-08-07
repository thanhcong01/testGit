/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ftu.exportpdf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author E5420
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pdfDocxFile", propOrder = {
    "filename",
    "content",
    "convertType"
})
public class PdfDocxFile {

    protected String filename;
    protected byte[] content;
    protected String convertType;

    /**
     * Gets the value of the filename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the value of the filename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilename(String value) {
        this.filename = value;
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setContent(byte[] value) {
        this.content = value;
    }

    /**
     * Gets the value of the convertType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConvertType() {
        return convertType;
    }

    /**
     * Sets the value of the convertType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConvertType(String value) {
        this.convertType = value;
    }

}
