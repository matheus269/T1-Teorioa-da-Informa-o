package br.unisinos.teoria.computacao.codificacoes;

import java.util.ArrayList;

public class DeltaCodification implements Encoder, Decoder {

    @Override
    public byte[] decode(byte[] data) {
        
        return null;
    }

    @Override
    public byte[] encode(byte [] data) {

        ArrayList<Byte> resultBytes = new ArrayList<>();
        addHeaderValues(resultBytes);

       

        byte[] result = new byte[resultBytes.size()];

        for(int i = 0; i < result.length; i++) {
            result[i] = resultBytes.get(i);
        }
        return result;
    }

    private void addHeaderValues(ArrayList<Byte> resultBytes){
        resultBytes.add((byte) 4);
        resultBytes.add((byte) 0);
    }
}
