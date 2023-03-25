package com.example.selloPrueba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import com.example.selloPrueba.Controller.CadenaOriginalController;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SelloPruebaApplication {
	public static void main(String[] args){
		SpringApplication.run(SelloPruebaApplication.class, args);
		
		CadenaOriginalController cadenaOriginalController = new CadenaOriginalController();
		cadenaOriginalController.getSello();
	}
}
