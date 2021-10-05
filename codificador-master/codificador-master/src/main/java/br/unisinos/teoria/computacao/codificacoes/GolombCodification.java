package br.unisinos.teoria.computacao.codificacoes;

import java.util.ArrayList;
import java.util.BitSet;

public class GolombCodification implements Encoder, Decoder {

    public static final int MAX_BIT_SIZE = 7;
    public static final int SHIFT_ONE = 1;
    public static final int ASCII = 256;
    final int divisor;
    final int suffixSize;
    int bitePosition = 0;
    byte resultByte = 0;

    public GolombCodification(int divisor) {
        this.divisor = divisor;
        this.suffixSize = logTwo(divisor);
    }

    @Override
    public byte[] encode(byte [] data) {
        ArrayList<Byte> resultBytes = new ArrayList<>();

        int value;
        int rest;
        int valToShift;
        int aux;

        addHeaderValues(resultBytes);

        interateByteConvert(data, resultBytes);

        //add residual bits of non complete byte
        if (bitePosition > 0) {
            resultBytes.add(resultByte);
        }

        byte[] result = new byte[resultBytes.size()];

        for (int i = 0; i < result.length; i++) {
            result[i] = resultBytes.get(i);
        }

        return result;
    }

    private void interateByteConvert(byte[] data, ArrayList<Byte> resultBytes) {
        int value;
        int aux;
        int rest;
        int valToShift;
        for(byte b : data) {
            if(b<0){
                aux=ASCII+b;
            } else{
                aux=b;
            }

            value = aux / divisor;
            rest = aux - (value * divisor);

            //interate the
            for(int i = 0; i < value; i++) {
                if (bitePosition >= 8) {
                    resultBytes.add(resultByte);
                    resultByte = 0;
                    bitePosition = 0;
                }
                bitePosition++;
            }

            if (bitePosition >= 8) {
                //byte is complete, add to array and start over
                resultBytes.add(resultByte);
                resultByte = 0;
                bitePosition = 0;
            }

            //resultByte add stopbit (1)
            valToShift = MAX_BIT_SIZE- bitePosition;
            resultByte = (byte) (resultByte | (SHIFT_ONE<<valToShift));
            bitePosition++;

            //add rest in binary
            BitSet bitsOfRest = BitSet.valueOf(new long[] { rest });
            for(int i = suffixSize-1; i >= 0; i--){
                if (bitePosition >= 8) {
                    //byte is complete, add to array and start over
                    resultBytes.add(resultByte);
                    resultByte = 0;
                    bitePosition = 0;
                }

                if(bitsOfRest.get(i)) {
                    valToShift = MAX_BIT_SIZE- bitePosition;
                    resultByte = (byte) (resultByte | (SHIFT_ONE<<valToShift));
                }

                bitePosition++;
            }
        }
    }

    @Override
    public byte[] decode(byte[] data) {
        ArrayList<Byte> decoded = new ArrayList<>();
        BitSet byteSuffix = new BitSet();
        boolean binaryArea = false;
        int countPrefix = 0;
        int suffixSizeHelp = suffixSize;

        interateByteToDecode(data, decoded, byteSuffix, binaryArea, countPrefix, suffixSizeHelp);

        byte[] decodedBytes = new byte[decoded.size()];
        for (int i = 0; i < decodedBytes.length; i++) {
            decodedBytes[i] = decoded.get(i);
        }

        return decodedBytes;
    }

    private void interateByteToDecode(byte[] data, ArrayList<Byte> decoded, BitSet byteSuffix, boolean binaryArea, int countPrefix, int suffixSizeHelp) {
        int rest;
        int result;
        int value;
        for(int index = 2; index < data.length; index++) {
            BitSet bits = BitSet.valueOf(new long[] { data[index] });
            for(int i = 7; i >= 0; i--){
                if(!binaryArea) {
                    if(!bits.get(i)){
                        countPrefix++;
                    } else {
                        binaryArea = true;
                    }
                } else {
                    if(bits.get(i)) {
                        byteSuffix.set(suffixSizeHelp - 1);
                    }
                    suffixSizeHelp--;
                    if(suffixSizeHelp <= 0) {
                        value = countPrefix * divisor;
                        rest = !byteSuffix.isEmpty() ? byteSuffix.toByteArray()[0] : 0;
                        result = value + rest;
                        decoded.add((byte)result);
                        countPrefix = 0;
                        binaryArea = false;
                        byteSuffix.clear();
                        suffixSizeHelp = suffixSize;
                    }
                }
            }
        }
    }


    private int logTwo(int value){
        return (int) (Math.log(value) / Math.log(2) );
    }

    private void addHeaderValues(ArrayList<Byte> resultBytes){
        resultBytes.add((byte) 0);
        resultBytes.add((byte) divisor);
    }
}
