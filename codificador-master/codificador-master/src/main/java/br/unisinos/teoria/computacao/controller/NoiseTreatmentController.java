package br.unisinos.teoria.computacao.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.unisinos.teoria.computacao.codificacoes.crc8.InvalidCRC;
import br.unisinos.teoria.computacao.service.NoiseTreatmentService;

@RestController
@RequestMapping("/noise-treatment")
public class NoiseTreatmentController {
	
	public static final String SUFIX_ARCHIVE_ENCODE = ".ecc";
	public static final String SUFIX_ARCHIVE_DECODE = ".cod";

	@Autowired	
	private NoiseTreatmentService service;
	
	@PostMapping(value = "/encoder")
	public ResponseEntity<String> encode(@RequestHeader("file") String filePath) throws IOException{
		byte[] data =  Files.readAllBytes(Paths.get(filePath));
        byte[] result = service.addNoiseTreatment(data);
                
        int extIndex = filePath.lastIndexOf(".");
        String newPath = (extIndex > -1 ? filePath.substring(0, extIndex) : filePath) + SUFIX_ARCHIVE_ENCODE;
        Files.write(Paths.get(newPath), result);
		return ResponseEntity.ok("Arquivo codificado disponível em: " + newPath);
	}
	
	@PostMapping(value = "/decoder")
	public ResponseEntity<String> decode(@RequestHeader("file") String filePath) throws IOException, InvalidCRC{
		byte[] data =  Files.readAllBytes(Paths.get(filePath));
        byte[] result = service.checkNoiseTreatment(data);
        
        int extIndex = filePath.lastIndexOf(".");
        String newPath = (extIndex > -1 ? filePath.substring(0, extIndex) : filePath) + SUFIX_ARCHIVE_DECODE;
        Files.write(Paths.get(newPath), result);
		return ResponseEntity.ok("Arquivo decodificado disponível em: " + newPath);
	}
}
