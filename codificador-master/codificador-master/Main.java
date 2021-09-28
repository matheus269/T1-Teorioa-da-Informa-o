import static codificacoes.CodingType.Delta;
import static codificacoes.CodingType.EliasGamma;
import static codificacoes.CodingType.Fibonacci;
import static codificacoes.CodingType.Golomb;
import static codificacoes.CodingType.Unary;
import static codificacoes.CodingType.getValueByName;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import codificacoes.CodingType;
import codificacoes.Decoder;
import codificacoes.DeltaCodification;
import codificacoes.Encoder;
import codificacoes.EliasGammaCodification;
import codificacoes.FibonacciCodification;
import codificacoes.GolombCodification;
import codificacoes.UnaryCodification;

public class Main {

    public static void main(String[] args) {
        boolean rodando = true;

        while (rodando) {
            
            Object[] functions = {"Codificar", "Decodificar"};
            int op = JOptionPane.showOptionDialog(null, "Escolha a função desejada:", "Função",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, functions, functions[0]);

            if (op == -1) {
                Object[] options = {"Fechar", "Voltar"};
                int end = JOptionPane.showOptionDialog(null, "Tem certeza que deseja fechar ?", "Fechar",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                if (end == 0) {
                    rodando = false;
                    break;
                } else {
                    continue;
                }
            }

            

            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setCurrentDirectory(new java.io.File("./arquivos"));
            if (op == 1) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.cod", "cod");
                fileChooser.setFileFilter(filter);
                fileChooser.addChoosableFileFilter(filter);
            }
            File selectedFile = null;
            int retVal = fileChooser.showOpenDialog(null);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                if (op == 1) {
                    while (retVal == JFileChooser.APPROVE_OPTION && !fileChooser.getSelectedFile().getName().endsWith(".cod")) {
                        JOptionPane.showMessageDialog(null, "O arquivo "
                                        + fileChooser.getSelectedFile().getName() + " Selecione um arquivo .cod",
                                "Erro de compatibilidade", JOptionPane.ERROR_MESSAGE);
                        retVal = fileChooser.showOpenDialog(null);
                    }
                }
                selectedFile = fileChooser.getSelectedFile();
                JOptionPane.showMessageDialog(null, selectedFile.getName());
            }

            if (retVal == 1) {
                continue;
            }

            if (op == 1) {
                try {
                    Decoder decoder = null;
                    byte[] data = Files.readAllBytes(selectedFile.toPath());
                    switch (data[0]) {
                        case 0:
                            decoder = new GolombCodification(data[1]);
                            break;

                        case 1:
                            decoder = new EliasGammaCodification();
                            break;

                        case 2:
                            decoder = new FibonacciCodification();
                            break;

                        case 3:
                            decoder = new UnaryCodification();
                            break;

                        case 4:
                            decoder = new DeltaCodification();
                            break;

                        default:
                            break;
                    }

                    if (decoder != null) {
                        byte[] result = decoder.decode(data);
                        final String ext = ".decoded";
                        String filePath = selectedFile.getPath();
                        int extIndex = filePath.lastIndexOf(".");
                        String newPath = (extIndex > -1 ? filePath.substring(0, extIndex) : filePath) + ext;
                        Files.write(Paths.get(newPath), result);
                        JOptionPane.showMessageDialog(null, "Decodificação finailzada");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Object[] items = {Golomb.getName(), EliasGamma.getName(), Fibonacci.getName(), Unary.getName(), Delta.getName()};
                Object selectedValue = JOptionPane.showInputDialog(null, "Qual codificação deseja usar ?", "Opções",
                        JOptionPane.INFORMATION_MESSAGE, null, items, items[0]);

                if (selectedValue == null) {
                    continue;
                } else {
                    final CodingType selectedCodingType = getValueByName((String) selectedValue);
                    Encoder encoder = null;

                    switch (selectedCodingType) {
                        case Golomb:
                            boolean invalidDivisor = true;
                            String inputValue = null;

                            while (invalidDivisor) {
                                inputValue = JOptionPane.showInputDialog("Insira o valor do divisor: (Entre 1 e 255)");

                                if (inputValue == null) {
                                    break;
                                }

                                try {
                                    int divisor = Integer.parseInt(inputValue);
                                    encoder = new GolombCodification(divisor);
                                    if(divisor > 0 && divisor < 256) {
                                        invalidDivisor = false;
                                    }
                                } catch (Exception e) {
                                    // e.printStackTrace();
                                }
                            }

                            if (inputValue == null) {
                                continue;
                            }
                            break;
                        case EliasGamma:
                            encoder = new EliasGammaCodification();
                            break;
                        case Fibonacci:
                            encoder = new FibonacciCodification();
                            break;
                        case Unary:
                            encoder = new UnaryCodification();
                            break;
                        case Delta:
                            encoder = new DeltaCodification();
                            break;
                    }

                    try {
                        byte[] data = Files.readAllBytes(selectedFile.toPath());
                        byte[] result = encoder.encode(data);
                        final String ext = ".cod";
                        String filePath = selectedFile.getPath();
                        int extIndex = filePath.lastIndexOf(".");
                        String newPath = (extIndex > -1 ? filePath.substring(0, extIndex) : filePath) + ext;
                        Files.write(Paths.get(newPath), result);
                        JOptionPane.showMessageDialog(null, "Codificação finalizada");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.exit(0);
    }
}
