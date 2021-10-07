package br.unisinos.teoria.computacao.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import br.unisinos.teoria.computacao.codificacoes.Hamming;
import br.unisinos.teoria.computacao.codificacoes.crc8.CRC8;
import br.unisinos.teoria.computacao.codificacoes.crc8.InvalidCRCException;

@Service
public class NoiseTreatmentService {
	
	public byte[] addNoiseTreatment(byte[] data) {
		ArrayList<Byte> resultBytes = new ArrayList<>();
		resultBytes.add(data[0]);
		resultBytes.add(data[1]);

		byte calculatedCrc = CRC8.calc(getDataForCrc(data));
		resultBytes.add(calculatedCrc);

		ArrayList<Byte> hammingResult = Hamming.encode(data);
		resultBytes.addAll(3, hammingResult);

		byte[] result = new byte[resultBytes.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = resultBytes.get(i);
		}

		return result;
	}

	public byte[] checkNoiseTreatment(byte[] data) throws InvalidCRCException {
		ArrayList<Byte> resultBytes = new ArrayList<>();
		resultBytes.add(data[0]);
		resultBytes.add(data[1]);

		byte calculatedCrc = CRC8.calc(getDataForCrc(data));
		if (calculatedCrc != data[2]) {
			throw new InvalidCRCException("O arquivo está corrompido!");
		}

		ArrayList<Byte> hammingResult = Hamming.decode(data);
		resultBytes.addAll(2, hammingResult);

		byte[] result = new byte[resultBytes.size()];

		for (int i = 0; i < result.length; i++) {
			result[i] = resultBytes.get(i);
		}

		return result;
	}

	private byte[] getDataForCrc(byte[] data) {
		byte[] dataForCrc = new byte[2];
		System.arraycopy(data, 0, dataForCrc, 0, 2);
		return dataForCrc;
	}
}
