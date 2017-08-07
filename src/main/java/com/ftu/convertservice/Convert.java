
package com.ftu.convertservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "convert", propOrder = {
    "input"
})
public class Convert {

    protected PdfDocxFile input;

    /**
     * Gets the value of the input property.
     * 
     * @return
     *     possible object is
     *     {@link PdfDocxFile }
     *     
     */
    public PdfDocxFile getInput() {
        return input;
    }

    /**
     * Sets the value of the input property.
     * 
     * @param value
     *     allowed object is
     *     {@link PdfDocxFile }
     *     
     */
    public void setInput(PdfDocxFile value) {
        this.input = value;
    }

}
