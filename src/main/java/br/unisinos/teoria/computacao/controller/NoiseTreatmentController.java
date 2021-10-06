package br.unisinos.teoria.computacao.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	public ResponseEntity<String> encode(@RequestHeader("file") String filePath) {
		try {
			byte[] data = Files.readAllBytes(Paths.get(filePath));
			byte[] result = service.addNoiseTreatment(data);

			int extIndex = filePath.lastIndexOf(".");
			String newPath = (extIndex > -1 ? filePath.substring(0, extIndex) : filePath) + SUFIX_ARCHIVE_ENCODE;
			Files.write(Paths.get(newPath), result);
			
			return ResponseEntity.ok("Arquivo codificado disponível em: " + newPath);
			
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Caminho do arquivo inválido");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno");
		}
	}

	@PostMapping(value = "/decoder")
	public ResponseEntity<String> decode(@RequestHeader("file") String filePath) throws InvalidCRC {
		try {
			byte[] data = Files.readAllBytes(Paths.get(filePath));
			byte[] result = service.checkNoiseTreatment(data);

			int extIndex = filePath.lastIndexOf(".");
			String newPath = (extIndex > -1 ? filePath.substring(0, extIndex) : filePath) + SUFIX_ARCHIVE_DECODE;
			Files.write(Paths.get(newPath), result);
			
			return ResponseEntity.ok("Arquivo decodificado disponível em: " + newPath);
			
		} catch (InvalidCRC e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Caminho do arquivo inválido");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno");
		}
	}
}
