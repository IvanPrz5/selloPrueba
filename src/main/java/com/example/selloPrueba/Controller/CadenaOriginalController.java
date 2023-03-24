package com.example.selloPrueba.Controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.swing.text.Document;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.ssl.PKCS8Key;
import org.xml.sax.SAXException;

public class CadenaOriginalController {

	private final String xsltPath = "C:/Users/Propietario/Desktop/sdk-java18/resources/XSLT/cadenaoriginal_3_3.xslt";
	private final String cfdiPath = "C:/Users/Propietario/Desktop/sdk-java18/resources/CFDI33_Validacion_Servicio/cfdi33_1.xml";
	private final String key = "C:/Users/Propietario/Desktop/sdk-java18/resources/CertificadosDePrueba/CSD_EKU9003173C9.key";
	private final String passKey = "12345678a"; 

	public String sellarCfdi() throws GeneralSecurityException, TransformerException, IOException{
		PKCS8Key pkcs8 = new PKCS8Key(Base64.getDecoder().decode(key), passKey.toCharArray());
		KeyFactory privateKeyFactory = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec pkcs8Encoded = new PKCS8EncodedKeySpec(pkcs8.getDecryptedBytes());
		PrivateKey privateKey = privateKeyFactory.generatePrivate(pkcs8Encoded);
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.update(getCadena().getBytes());
		String firma = new String(Base64.getEncoder().encode(signature.sign()));
		System.out.println("Firma digital del CFDI:" + firma);
		return null;
	}

    public String getCadena() throws TransformerException, IOException {
        File xslt = new File(xsltPath);
		StreamSource sourceXsl = new StreamSource(xslt);
		File cfdi = new File(cfdiPath);
		StreamSource sourceXml = new StreamSource(cfdi);
		
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer(sourceXsl);
		System.out.println("Cadena Orignal" +  sourceXml.toString());
		transformer.transform(sourceXml, new StreamResult(System.out));
		// transformer.transform(sourceXml, new StreamResult(System.out));
		return null;
	}
}
