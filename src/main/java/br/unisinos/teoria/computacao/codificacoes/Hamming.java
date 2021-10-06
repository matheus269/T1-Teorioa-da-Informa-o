package br.unisinos.teoria.computacao.codificacoes;

import java.util.ArrayList;
import java.util.BitSet;

public class Hamming {

	public static ArrayList<Byte> encode(byte[] data) {
		ArrayList<Byte> resultBytes = new ArrayList<>();
		BitSet codeword = new BitSet();
		int bitPosition = 0;

		loopEncode(data, resultBytes, codeword, bitPosition);

		return resultBytes;
	}

	private static void loopEncode(byte[] data, ArrayList<Byte> resultBytes, BitSet codeword, int bitPosition) {
		// para cada bit, exceto o cabeçalho
		for (int index = 2; index < data.length; index++) {
			BitSet bits = BitSet.valueOf(new long[] { data[index] });

			// até 4 bits, vai registrando os bits no codeword
			for (int i = 7; i >= 0; i--) {
				if (bitPosition == 4) {
					// calcula o hamming
					resultBytes.add(calcHamming(codeword));
					// salva o resultado no resultBytes, e recomeça
					bitPosition = 0;
					codeword.clear();
				}
				if (bits.get(i)) {
					codeword.set(bitPosition);
				}
				bitPosition++;
			}
			// calcula apenas os 4 ultimos
			if (index == data.length - 1) {
				codeword.clear();
				for (int i = 3; i >= 0; i--) {
					if (bits.get(i)) {
						codeword.set(3 - i);
					}
				}
				resultBytes.add(calcHamming(codeword));
			}
		}
	}

	public static ArrayList<Byte> decode(byte[] data) {
		ArrayList<Byte> resultBytes = new ArrayList<>();
		BitSet codeword = new BitSet();
		int bitPosition = 0;
		BitSet bitsIniciais, bitsFinais, bitsDecoded = BitSet.valueOf(new long[] { 0 });

		loopDecode(data, resultBytes, codeword, bitPosition, bitsDecoded);

		return resultBytes;
	}

	private static void loopDecode(byte[] data, ArrayList<Byte> resultBytes, BitSet codeword, int bitPosition, BitSet bitsDecoded) {
		BitSet bitsFinais;
		BitSet bitsIniciais;
		// para cada bit, exceto o cabeçalho
		for (int index = 3; index < data.length; index++) {
			BitSet bits = BitSet.valueOf(new long[] { data[index] });

			// até 4 bits, vai registrando os bits no codeword
			for (int i = 7; i >= 0; i--) {
				if (bits.get(i)) {
					codeword.set(bitPosition);
				}
				bitPosition++;
			}

			if (bitPosition == 8) {
				// salva o resultado no resultBytes, e recomeça
				if (index % 2 == 1) { // 4 bits iniciais
					bitsIniciais = verifyHamming(codeword);
					for (int j = 0; j < 4; j++) {
						if (bitsIniciais.get(j)) {
							bitsDecoded.set(j);
						}
					}

					bitsIniciais.clear();
				} else { // 4 bits finais
					bitsFinais = verifyHamming(codeword);
					for (int j = 0; j < 4; j++) {
						if (bitsFinais.get(j)) {
							bitsDecoded.set(j + 4);
						}
					}

					bitsDecoded = reverseBitsOfBitset(bitsDecoded);
					byte decoded = !bitsDecoded.isEmpty() ? bitsDecoded.toByteArray()[0] : 0;

					resultBytes.add(decoded);
					bitsFinais.clear();
					bitsDecoded.clear();
				}
				bitPosition = 0;
				codeword.clear();
			}
		}
	}

	private static BitSet verifyHamming(BitSet codeword) {
		BitSet result = BitSet.valueOf(new long[] { 0 });
		// calcula o hamming para o codeword, detecta erros
		for (int i = 0; i < 4; i++) {
			if (codeword.get(i)) {
				result.set(i);
			}
		}
		//1 codigo hamming
		setHammingCode(result, 0, 2, 1, 4);
		//2 codigo hamming
		setHammingCode(result, 1, 2, 3, 5);
		//3 codigo hamming
		setHammingCode(result, 0, 2, 3, 6);

		if (result.get(4) != codeword.get(4) && result.get(5) != codeword.get(5) && result.get(6) != codeword.get(6)) {
			result.flip(2);
			System.out.println("Ruído corrigido no 3o bit!");
		}

		else if (result.get(4) != codeword.get(4) && result.get(6) != codeword.get(6)) {
			result.flip(0);
			System.out.println("Ruído corrigido no 1o bit!");
		}

		else if (result.get(4) != codeword.get(4) && result.get(5) != codeword.get(5)) {
			result.flip(1);
			System.out.println("Ruído corrigido no 2o bit!");
		}

		else if (result.get(5) != codeword.get(5) && result.get(6) != codeword.get(6)) {
			result.flip(3);
			System.out.println("Ruído corrigido no 4o bit!");
		}

		return result;
	}

	private static byte calcHamming(BitSet codeword) {
		// 1st hamming code
		setHammingCode(codeword, 0, 2, 1, 4);

		// 2nd hamming code
		setHammingCode(codeword, 1, 2, 3, 5);

		// 3rd hamming code
		setHammingCode(codeword, 0, 2, 3, 6);

		codeword = reverseBitsOfBitset(codeword);

		byte coded = !codeword.isEmpty() ? codeword.toByteArray()[0] : 0;
		return coded;
	}

	private static void setHammingCode(BitSet codeword, int index1, int index2, int index3, int indexToSet) {
		int count = 0;
		if (codeword.get(index1))
			count++;
		if (codeword.get(index2))
			count++;
		if (codeword.get(index3))
			count++;
		if (count % 2 != 0) {
			codeword.set(indexToSet);
		}
	}

	private static BitSet reverseBitsOfBitset(BitSet inByte) {
		BitSet result = BitSet.valueOf(new long[] { 0 });

		for (int i = 0; i < 8; i++) {
			if (inByte.get(i)) {
				result.set(7 - i);
			}
		}

		return result;
	}

}
