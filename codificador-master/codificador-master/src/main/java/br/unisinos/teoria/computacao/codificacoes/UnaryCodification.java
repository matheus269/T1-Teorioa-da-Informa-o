package br.unisinos.teoria.computacao.codificacoes;

import java.util.ArrayList;
import java.util.List;

public class UnaryCodification implements Encoder, Decoder {

    @Override
    public byte[] decode(byte[] bytes) {
    	List<Byte> asciiBytes = new ArrayList<>();
		int bitCount = 0;

		//começa em 2 pra pular os dois bytes do header
		for (int i = 2; i < bytes.length; i++) {
			for (int j = 7; j >= 0; j--) {
				
				if (getBit(bytes[i], j) == 1) {
					bitCount++;
				} else {
					//se ja é zero é pq veio dois zeros seguidos, ou seja, terminou os codewords
					if(bitCount == 0)
						continue;
					
					byte newAsciiByte = (byte) bitCount;
					asciiBytes.add(newAsciiByte);
					bitCount = 0;
				}
			}

		}
		return listToArray(asciiBytes);
    }

    @Override
    public byte[] encode(byte [] bytes) {
    	List<String> codewords = generateCodewords(bytes);
		List<Byte> bufferBytes = new ArrayList<>();

		addHeaderValues(bufferBytes);

		byte b = 0;
		int bitPosition = 7;

		for (String code : codewords) {
			for (int i = 0; i < code.length(); i++) {
				char c = code.charAt(i);

				if (c == '1') {
					b = addBitInBytePosition(b, bitPosition);
				}
				bitPosition--;

				// completou o byte, adiciona no buffer e cria um novo
				if (bitPosition == -1) {
					bufferBytes.add(b);
					b = 0;
					bitPosition = 7;
				}
			}
		}
		if (b != 0)
			bufferBytes.add(b);

		return listToArray(bufferBytes);
    }

	private List<String> generateCodewords(byte[] bytes) {
		List<String> codewords = new ArrayList<>();
		for (byte b : bytes) {
			String codeword = createStreamWithOnes(b) + "0";
			codewords.add(codeword);
		}
		return codewords;
	}
    
	private String createStreamWithOnes(int quantityOfOnes) {
		// howManyZeros 3 -> Return 111
		// howManyZeros 7 -> Return 1111111
		return new String(new char[quantityOfOnes]).replace("\0", "1");
	}

	private byte addBitInBytePosition(byte b, int position) {
		return (byte) (b | (1 << position));
	}

	private byte[] listToArray(List<Byte> bytes) {
		byte[] array = new byte[bytes.size()];
		for (int i = 0; i < bytes.size(); i++) {
			array[i] = bytes.get(i);
		}
		return array;
	}

	private void addHeaderValues(List<Byte> bufferBytes) {
		// 3 é o código do unário
		bufferBytes.add((byte) 3);
		// 0 pois não precisa de divisor
		bufferBytes.add((byte) 0);
	}

	public byte getBit(byte b, int position) {
		return (byte) ((b >> position) & 1);
	}
}