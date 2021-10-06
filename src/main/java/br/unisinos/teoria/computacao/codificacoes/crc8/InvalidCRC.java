package br.unisinos.teoria.computacao.codificacoes.crc8;

public class InvalidCRC extends Exception{

    public InvalidCRC(String msg){
        super(msg);
    }
}