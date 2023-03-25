package com.example.selloPrueba.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.ssl.PKCS8Key;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CadenaOriginalController {

	private final String xsltPath = "C:/Users/Propietario/Desktop/sdk-java18/resources/XSLT/cadenaoriginal_3_3.xslt";
	private final String cfdiPath = "C:/Users/Propietario/Desktop/sdk-java18/resources/CertificadosDePrueba/prueba.xml";
	private final String cer = "C:/Users/Propietario/Desktop/sdk-java18/resources/CertificadosDePrueba/CSD_EKU9003173C9.cer";
	private final String key = "C:/Users/Propietario/Desktop/sdk-java18/resources/CertificadosDePrueba/CSD_EKU9003173C9.key";
	private final String passKey = "12345678a";

	private final String pathSave = "C:/Users/Propietario/Desktop/sdk-java18/resources/XSLT/modificado.xml";

	public String getSello() {
		try {
			/* Sello */
			PKCS8Key pkcs8 = new PKCS8Key(Files.readAllBytes(Paths.get(key)), passKey.toCharArray());
			java.security.PrivateKey pk = pkcs8.getPrivateKey();

			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initSign(pk);
			signature.update(getCadena().getBytes("UTF-8"));

			String selloCfdi = new String(Base64.getEncoder().encode(signature.sign()));
			System.out.println("Sello:      " + selloCfdi);

			/* Cer a Base64 */
			byte[] fileContent = FileUtils.readFileToByteArray(new File(cer));
			String cerBase64 = Base64.getEncoder().encodeToString(fileContent);
			
			/* Número de Certificado */
			FileInputStream is = new FileInputStream(cer);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate certificado=(X509Certificate)cf.generateCertificate(is);
			byte[] byteArray= certificado.getSerialNumber().toByteArray(); //obtengo no. de serie
			String NoCertificado = new String(byteArray);
			System.out.println("No. Certificado:     " + NoCertificado);

			/* Modifica el xml original y lo guarda */
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
			Document document = documentBuilder.parse(cfdiPath);

			document.getDocumentElement().normalize();
			document.getDocumentElement().setAttribute("Sello", selloCfdi);
			document.getDocumentElement().setAttribute("Certificado", cerBase64);
			document.getDocumentElement().setAttribute("NoCertificado", NoCertificado);
			// document.getDocumentElement().setAttribute("Fecha", "2023-03-24T18:54:00");

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(pathSave));
			
			transformer.transform(source, result);
			return null;
		
		} catch (IOException e) {
			return getStackError(e);
		} catch (GeneralSecurityException e) {
			return getStackError(e);
		} catch (TransformerException e) {
			return getStackError(e);
		} catch (SAXException e) {
			return getStackError(e);
		} catch (ParserConfigurationException e) {
			return getStackError(e);
		}
	}

	public String getCadena() throws TransformerException, IOException {
		File xslt = new File(xsltPath);
		StreamSource sourceXsl = new StreamSource(xslt);
		File cfdi = new File(cfdiPath);
		StreamSource sourceXml = new StreamSource(cfdi);

		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer(sourceXsl);
		StringWriter out = new StringWriter();

		transformer.transform(sourceXml, new StreamResult(out));
		String cadenaOriginal = out.toString();

		System.out.println("Cadena Orignal: " + cadenaOriginal);
		return cadenaOriginal;
	}

	public static String getStackError(Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}