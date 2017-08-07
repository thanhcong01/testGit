
package com.ftu.convertservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.ftu.convertservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Convert_QNAME = new QName("http://service.convert.viettel.com/", "convert");
    private final static QName _ConvertResponse_QNAME = new QName("http://service.convert.viettel.com/", "convertResponse");
    private final static QName _PdfDocxFile_QNAME = new QName("http://service.convert.viettel.com/", "pdfDocxFile");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.ftu.convertservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PdfDocxFile }
     * 
     */
    public PdfDocxFile createPdfDocxFile() {
        return new PdfDocxFile();
    }

    /**
     * Create an instance of {@link Convert }
     * 
     */
    public Convert createConvert() {
        return new Convert();
    }

    /**
     * Create an instance of {@link ConvertResponse }
     * 
     */
    public ConvertResponse createConvertResponse() {
        return new ConvertResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Convert }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.convert.viettel.com/", name = "convert")
    public JAXBElement<Convert> createConvert(Convert value) {
        return new JAXBElement<Convert>(_Convert_QNAME, Convert.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.convert.viettel.com/", name = "convertResponse")
    public JAXBElement<ConvertResponse> createConvertResponse(ConvertResponse value) {
        return new JAXBElement<ConvertResponse>(_ConvertResponse_QNAME, ConvertResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PdfDocxFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.convert.viettel.com/", name = "pdfDocxFile")
    public JAXBElement<PdfDocxFile> createPdfDocxFile(PdfDocxFile value) {
        return new JAXBElement<PdfDocxFile>(_PdfDocxFile_QNAME, PdfDocxFile.class, null, value);
    }

}
