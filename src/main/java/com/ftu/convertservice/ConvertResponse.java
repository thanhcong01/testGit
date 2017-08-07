
package com.ftu.convertservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "convertResponse", propOrder = {
    "_return"
})
public class ConvertResponse {

    @XmlElement(name = "return")
    protected PdfDocxFile _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link PdfDocxFile }
     *     
     */
    public PdfDocxFile getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link PdfDocxFile }
     *     
     */
    public void setReturn(PdfDocxFile value) {
        this._return = value;
    }

}
