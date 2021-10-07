package br.unisinos.teoria.computacao.codificacoes.crc8;

public class InvalidCRCException extends Exception{

    public InvalidCRCException(String msg){
        super(msg);
    }
}