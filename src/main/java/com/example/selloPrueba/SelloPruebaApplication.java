package com.example.selloPrueba;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import com.example.selloPrueba.Controller.CadenaOriginalController;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SelloPruebaApplication {
	public static void main(String[] args) throws TransformerException, IOException{
		SpringApplication.run(SelloPruebaApplication.class, args);
		
		CadenaOriginalController cadenaOriginalController = new CadenaOriginalController();
		cadenaOriginalController.getCadena();
	}
}
