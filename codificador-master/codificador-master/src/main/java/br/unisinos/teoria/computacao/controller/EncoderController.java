package br.unisinos.teoria.computacao.controller;

import br.unisinos.teoria.computacao.coders.TipoCodificacao;
import br.unisinos.teoria.computacao.representation.CodersRequest;
import br.unisinos.teoria.computacao.representation.CodersResponse;
import br.unisinos.teoria.computacao.service.EncodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class EncoderController {

    public static final String SUFIX_ARCHIVE_ENCODE = ".cod";
    public static final String SUFIX_ARCHIVE_DECODE = ".dec";
    @Autowired
    EncodeService encodeService;


    @PostMapping(value = "/encoder/{tipoEncode}")
    public HttpEntity<byte[]> encode(@PathVariable("tipoEncode") Integer tipoEncode, @RequestParam("file") MultipartFile file, @RequestParam("divisor")  Integer divisor) throws IOException {

        TipoCodificacao tipoCodificacao = TipoCodificacao.parse(tipoEncode);
        if(TipoCodificacao.G.equals(tipoCodificacao)){
            if(divisor == null){
                throw new RuntimeException();
            }
        }

        byte[] data = file.getBytes();
        byte[] response =  encodeService.encodar(data,divisor,tipoCodificacao);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=" + file.getOriginalFilename() + SUFIX_ARCHIVE_DECODE);
        return new HttpEntity<>(response,headers);

    }


    @PostMapping(value = "/decoder")
    public HttpEntity<byte[]> decode(@RequestParam("file") MultipartFile file) throws IOException {
                byte[] data = file.getBytes();
        byte[] response =  encodeService.decode(data);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=" + file.getOriginalFilename() + SUFIX_ARCHIVE_DECODE);
        return new HttpEntity<>(response,headers);

    }
}
