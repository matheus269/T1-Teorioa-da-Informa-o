package br.unisinos.teoria.computacao.codificacoes;

import java.util.ArrayList;

public class FibonacciCodification implements Encoder, Decoder {

    private final int zeroValue = 235;

    @Override
    public byte[] decode(byte[] data) {
        ArrayList<Integer> decoded = new ArrayList<>();
            

        byte[] decodedBytes = new byte[decoded.size()];
        for (int i = 0; i < decodedBytes.length; i++) {
            int ascii = decoded.get(i);
            decodedBytes[i] = (byte)ascii;
        }

        return decodedBytes;
    }

    @Override
    public byte[] encode(byte[] data) {
        ArrayList<Byte> resultBytes = new ArrayList<>();
        

        byte[] result = new byte[resultBytes.size() + 2];

        addHeaderValues(result);

        for (int i = 2; i < result.length; i++) {
            result[i] = resultBytes.get(i - 2);
        }

        return result;
    }

   

    private void addHeaderValues(byte[] result){
        result[0] = (byte) CodingType.Fibonacci.getIdentifier();
        result[1] = (byte) 0;
    }

}
