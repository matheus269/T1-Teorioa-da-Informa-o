package br.unisinos.teoria.computacao.codificacoes;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import static br.unisinos.teoria.computacao.binary.BinaryUtil.alterarBinario;
import static br.unisinos.teoria.computacao.binary.BinaryUtil.toByteArray;

public class DeltaCodification implements Encoder, Decoder {

    private static final int NUMERO_BITS_BUFFER = 8;

    @Override
    public byte[] decode(byte[] data) {
        ArrayList<Byte> resultBytes = new ArrayList<>();
        resultBytes.add(data[2]);
        int maxSize = data[1] +1 +1 ;

        List<BitSet> bites = new ArrayList<>();
        for(int i=3;i<data.length;i++){
            byte bit = data[i];
            BitSet bite = BitSet.valueOf(new byte[] { bit });
            bites.add(bite);
        }
        List<BitSet> listTamBits = new ArrayList<>();
        int j=0;
        int index = 0;
        while(j<bites.size()) {
            BitSet bitSet = new BitSet(7);
            try {
                for (int i = 0; i < maxSize; i++) {
                    if (index == 8) {
                        index = 0;
                        j++;
                    }
                    bitSet.set(i, bites.get(j).get(index++));
                }
            }catch (Exception e){
                System.out.println("Final arquivo");
            }
            listTamBits.add(bitSet);
        }

        int deltaAtual = data[2];
        for(int bitSetCurrent=0; bitSetCurrent < listTamBits.size(); bitSetCurrent++) {
            BitSet bitSet = listTamBits.get(bitSetCurrent);
            boolean isChange = bitSet.get(0);
            boolean isBigger = bitSet.get(1);

            String bitComplete = "";
            for(int k = 2;k < maxSize;k++){
                bitComplete+= bitSet.get(k) ? "1" : "0";
            }
            int delta = Integer.parseInt(bitComplete,2)+1;
            if(isChange) {
                if(!isBigger){
                    deltaAtual -=  delta;
                }else{
                    deltaAtual +=  delta;
                }
            }
            resultBytes.add((byte) Math.abs(deltaAtual));
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
        int biggestDelta = 0;

        ArrayList<Byte> deltas = new ArrayList<>();
        //descobre o delta de cada byte
        for(int i = 0; i < data.length-1; i++){
            delta = (byte) (data[i+1] - data[i]);
            deltas.add(delta);

            if (Math.abs(delta) > Math.abs(biggestDelta)){
                biggestDelta = Math.abs(delta);
            }
        }

        int byteLength = 0;
        int index=8;
        String biggestDeltaBinary = Integer.toBinaryString(biggestDelta);
        List<BitSet> lista = new ArrayList<>();
        BitSet bite = BitSet.valueOf(new byte[]{data[0]});
        lista.add(bite);
        //tentativa de calcular o codeword de cada salto
        BitSet bitSet = new BitSet(8);
        for(int i = 0; i < deltas.size(); i++){
            byte deltaPrevious = (i-1)>0? deltas.get(i-1): 0;
            boolean isChange = deltas.get(i) != 0;
            alterarBinario(lista,index++,isChange);
            if(isChange) {
                boolean isbigger =   deltas.get(i) > deltaPrevious;
                alterarBinario(lista,index++,isbigger);
                String binary = StringUtils.leftPad(Integer.toBinaryString(Math.abs(deltas.get(i))-1),biggestDeltaBinary.length(),'0');
                for(int j = 0; j< binary.length();j++) {
                    alterarBinario(lista, index++,binary.charAt(j) == '1');
                }
            }
        }
        byte[] cabecalho = HeaderValues(biggestDeltaBinary.length());
        byte[] resultado = toByteArray(lista);
        return mergeCabecalhoResultado(cabecalho,resultado);
    }

    private byte[] mergeCabecalhoResultado(byte[] cabecalho, byte[] resultado) {
        byte[] retorno = new byte[cabecalho.length+ resultado.length];
        retorno[0] = cabecalho[0];
        retorno[1] = cabecalho[1];
        for (int i=2;i < retorno.length;i++){
            retorno[i] = resultado[i-2];
        }
        return retorno;
    }

    private byte[] HeaderValues(int bigger){
        byte[] cabecalho = new byte[2];
        cabecalho[0] = (byte)4;
        cabecalho[1] = (byte)bigger;
        return cabecalho;
    }
}
