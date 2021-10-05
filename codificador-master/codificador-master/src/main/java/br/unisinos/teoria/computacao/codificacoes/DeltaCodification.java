package br.unisinos.teoria.computacao.codificacoes;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;

public class DeltaCodification implements Encoder, Decoder {

    @Override
    public byte[] decode(byte[] data) {
        ArrayList<Byte> resultBytes = new ArrayList<>();
        resultBytes.add(data[3]);
        int last;
        byte current = data[3];

        for(int index = 3; index < data.length; index++) {
            BitSet bitSet = BitSet.valueOf(new byte[]{data[index]});
            boolean isChange = bitSet.get(0);
            boolean isBigger = bitSet.get(1);

            String bitComplete = "";
            for(int j = 2;j < 8;j++){
                bitComplete+= bitSet.get(j) ? "1" : "0";
            }
            int delta = Integer.parseInt(bitComplete,2);
            if(isChange) {
                if(isBigger){
                    current -=  delta;
                }else{
                    current +=  delta;
                }
            }
            resultBytes.add(current);
        }
        byte[] bytesArray = mapperByteArray(resultBytes);
        return bytesArray;
    }

    private byte[] mapperByteArray(ArrayList<Byte> resultBytes) {
        byte[] bytesArray = new byte[resultBytes.size()];
        for (int i = 0; i < resultBytes.size(); i++) {
            bytesArray[i] = resultBytes.get(i);
        }
        return bytesArray;
    }

    @Override
    public byte[] encode(byte [] data) {

        ArrayList<Byte> resultBytes = new ArrayList<>();
        byte delta = 0;
        int largestDelta = 0;
        addHeaderValues(resultBytes);

        ArrayList<Byte> deltas = new ArrayList<>();

        //descobre o delta de cada byte
        for(int i = 0; i < data.length-1; i++){
            delta = (byte) (data[i+1] - data[i]);
            deltas.add(delta);

            if (Math.abs(delta) > Math.abs(largestDelta)) largestDelta = delta;

            if (i == 0) resultBytes.add(data[i]);
        }

        int byteLength = 0;

        //tentativa de calcular o codeword de cada salto
        for(int i = 0; i < deltas.size()-1; i++){
            if (deltas.get(i) != deltas.get(i+1)){
                byteLength = Integer.toBinaryString(deltas.get(i)).length();
                delta = (byte) (deltas.get(i) | (1<<byteLength));
            }
            resultBytes.add((byte) (delta));
        }

        byte[] result = mapperByteArray(resultBytes);
        return result;
    }


    private void addHeaderValues(ArrayList<Byte> resultBytes){
        resultBytes.add((byte) 4);
        resultBytes.add((byte) 0);
    }
}
