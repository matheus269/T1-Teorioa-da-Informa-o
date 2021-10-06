package br.unisinos.teoria.computacao.binary;

import java.util.BitSet;
import java.util.List;

public class BinaryUtil {


    public static void alterarBinario(List<BitSet> bytes,int position){
        int posicaoArray = position / 8;
        if(posicaoArray>= bytes.size()){
            BitSet novoByte = new BitSet(8);
            novoByte.set(position % 8);
        }else{
            BitSet biteSet = bytes.get(posicaoArray);
            biteSet.flip(position % 8);
        }
    }

    public static void alterarBinario(List<BitSet> bytes,int position,boolean valor){
        int posicaoArray = position / 8;
        if(posicaoArray >= bytes.size()){
            BitSet novoByte = new BitSet(8);
            novoByte.set(position % 8,valor);
            bytes.add(novoByte);
        }else{
            BitSet biteSet = bytes.get(posicaoArray);
            biteSet.set(position % 8,valor);
        }
    }
    public static void alterarBinario(BitSet bytes,int position,boolean valor){
        bytes.set(position,valor);
    }

    public static byte[] toByteArray(List<BitSet> list) {
        byte[] retorno = new byte[list.size()];
        for(int i = 0;i< list.size();i++){
            BitSet item = list.get(i);
            byte bit = item.length() > 0 ? item.toByteArray()[0] : 0;
            retorno[i] = bit;
        }
        return retorno;
    }

}
