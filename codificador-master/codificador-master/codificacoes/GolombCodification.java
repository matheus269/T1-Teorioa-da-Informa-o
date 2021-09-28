package codificacoes;

import codificacoes.Decoder;
import codificacoes.Encoder;

import java.util.ArrayList;
import java.util.BitSet;

public class GolombCodification implements Encoder, Decoder {

    final int divisor;
    final int suffixSize;

    public GolombCodification(int divisor) {
        this.divisor = divisor;
        this.suffixSize = calculateLog2(divisor);
    }

    @Override
    public byte[] decode(byte[] data) {
        ArrayList<Byte> decoded = new ArrayList<>();
      

      
        byte[] decodedBytes = new byte[decoded.size()];
        for (int i = 0; i < decodedBytes.length; i++) {
            decodedBytes[i] = decoded.get(i);
        }

        return decodedBytes;
    }

    @Override
    public byte[] encode(byte [] data) {
        ArrayList<Byte> resultBytes = new ArrayList<>();
       

        addHeaderValues(resultBytes);

        

        byte[] result = new byte[resultBytes.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = resultBytes.get(i);
        }

        return result;
    }

    private int calculateLog2(int value){
        return (int) (Math.log(value) / Math.log(2) + 1e-10);
    }

    private void addHeaderValues(ArrayList<Byte> resultBytes){
        resultBytes.add((byte) 0);
        resultBytes.add((byte) divisor);
    }
}
