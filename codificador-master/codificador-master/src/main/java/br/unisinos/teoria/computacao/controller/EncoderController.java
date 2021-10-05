package br.unisinos.teoria.computacao.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import br.unisinos.teoria.computacao.coders.TipoCodificacao;
import br.unisinos.teoria.computacao.service.EncodeService;

@RestController
public class EncoderController {

	public static final String SUFIX_ARCHIVE_ENCODE = ".cod";
	public static final String SUFIX_ARCHIVE_DECODE = ".dec";
	@Autowired
	EncodeService encodeService;

	@PostMapping(value = "/encoder/{tipoEncode}")
	public ResponseEntity<String> encode(
			@PathVariable("tipoEncode") Integer tipoEncode,
			@RequestHeader("file") String filePath, 
			@RequestHeader("divisor") Integer divisor) throws IOException {

		TipoCodificacao tipoCodificacao = TipoCodificacao.parse(tipoEncode);
		if (TipoCodificacao.G.equals(tipoCodificacao)) {
			if (divisor == null) {
				throw new RuntimeException();
			}
		}

		byte[] data =  Files.readAllBytes(Paths.get(filePath));
		byte[] response = encodeService.encodar(data, divisor, tipoCodificacao);
		 
		int extIndex = filePath.lastIndexOf(".");
		String newPath = (extIndex > -1 ? filePath.substring(0, extIndex) : filePath) + SUFIX_ARCHIVE_ENCODE;
		Files.write(Paths.get(newPath), response);
		
		return ResponseEntity.ok("Arquivo codificado disponível em: " + newPath);

	}

	@PostMapping(value = "/decoder")
	public ResponseEntity<String> decode(@RequestHeader("file") String filePath) throws IOException {
		byte[] data = Files.readAllBytes(Paths.get(filePath));
		byte[] response = encodeService.decode(data);
		
		int extIndex = filePath.lastIndexOf(".");
		String newPath = (extIndex > -1 ? filePath.substring(0, extIndex) : filePath) + SUFIX_ARCHIVE_DECODE;
		Files.write(Paths.get(newPath), response);
		return ResponseEntity.ok("Arquivo decodificado disponível em: " + newPath);
	}
}
