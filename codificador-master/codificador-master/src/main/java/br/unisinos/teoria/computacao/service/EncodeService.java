package br.unisinos.teoria.computacao.service;

import br.unisinos.teoria.computacao.coders.TipoCodificacao;
import br.unisinos.teoria.computacao.codificacoes.*;
import br.unisinos.teoria.computacao.representation.CodersResponse;
import org.springframework.stereotype.Service;

@Service
public class EncodeService {


    public byte[] encodar(byte[] data, Integer divisor, TipoCodificacao tipoCodificacao) {
        byte[] coders = data;
        Encoder encoder = null;
        switch (tipoCodificacao) {
            case G:
                            encoder = new GolombCodification(divisor);
                            break;
            case E:
                            encoder = new EliasGammaCodification();
                            break;
            case  F:
                            encoder = new FibonacciCodification();
                            break;
            case U:
                            encoder = new UnaryCodification();
                            break;
            case D:
                            encoder = new DeltaCodification();
                            break;
            }
            byte[] result = encoder.encode(coders);
            return result;
    }

    public byte[] decode(byte[] data) {
        byte[] coders = data;
        Decoder decoder = null;

        TipoCodificacao tipoCodificacao = TipoCodificacao.parse(data[0]);
        int divisor = data[1];
        switch (tipoCodificacao) {
            case G:
                decoder = new GolombCodification(divisor);
                break;
            case E:
                decoder = new EliasGammaCodification();
                break;
            case  F:
                decoder = new FibonacciCodification();
                break;
            case U:
                decoder = new UnaryCodification();
                break;
            case D:
                decoder = new DeltaCodification();
                break;
        }
        byte[] result = decoder.decode(coders);
        return result;

    }
}
