package Assember;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ReadInstruction {
    private static ReadInstruction instance;
    private Set<String> encounteredLabels = new HashSet<>();
    private String filePath;
    private List<Map<String, String>> mappedLines;
    private Map<String, Integer> labelToAddressMap;

    private ReadInstruction(String filePath) {
        this.filePath = filePath;
        this.mappedLines = new ArrayList<>();
        this.labelToAddressMap = new HashMap<>();
        mapFileIntoParts(); // Automatically map lines upon instantiation
    }

    public static ReadInstruction getInstance(String filePath) {
        if (instance == null) {
            instance = new ReadInstruction(filePath);
        }
        return instance;
    }

    private void mapFileIntoParts() {
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int linenumber = 0;
            while ((line = bufferedReader.readLine()) != null) {
                mapLineIntoParts(line, linenumber);
                linenumber++;
            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mapLineIntoParts(String line, Integer num) {
        String[] parts = splitLine(line);
        Map<String, String> lineMap = new HashMap<>();
        lineMap.put("Address", num.toString());
        if (parts.length >= 1) {
            String firstPart = parts[0].trim();
            if (!firstPart.isEmpty()) {
                if (isReservedWords(firstPart)) {
                    lineMap.put("instruction", firstPart);
                }
                if (encounteredLabels.contains(firstPart)) {
                    System.err.println("Duplicate label found: " + firstPart + " by line " + (num + 1));
                    System.exit(1);
                } else {
                    lineMap.put("Label", firstPart);
                    labelToAddressMap.put(firstPart, num);
                    encounteredLabels.add(firstPart);
                }
            }
        }
        if (parts.length >= 2) {
            if (isReservedWords(parts[0])){
                lineMap.put("field0",parts[1]);
            }
            lineMap.put("instruction", parts[1]);
            if (!isReservedWords(lineMap.get("instruction"))){
                System.err.println("Opcode not found: " + parts[1] + " by line " + (num + 1));
                System.exit(1);
            }
        }
        if (parts.length >= 3) {
            if (isReservedWords(parts[0])){
                lineMap.put("field1",parts[1]);
            }
            lineMap.put("field0", parts[2]);
        }
        if (parts.length >= 4) {
            if (isReservedWords(parts[0])){
                lineMap.put("field2",parts[1]);
            }
            lineMap.put("field1", parts[3]);
        }
        if (parts.length >= 5) {
            lineMap.put("field2", parts[4]);
        }
        if (parts.length >= 6) {
            lineMap.put("comments", parts[5]);
        }
        mappedLines.add(lineMap);
    }

    private boolean isReservedWords(String s) {
        String[] reserve = {"lw", "sw", "add", "nand", "beq", "jalr", "halt", "noop", ".fill"};
        return Arrays.asList(reserve).contains(s);
    }

    private String[] splitLine(String line) {
        String[] parts = line.split("\\s+", 6);
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        return parts;
    }

    public List<Map<String, String>> getMappedLines() {
        return mappedLines;
    }

    public void printMappedLines() {
        for (Map<String, String> lineMap : mappedLines) {
            System.out.println("Mapped Line:");
            for (Map.Entry<String, String> entry : lineMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println();
        }
    }

    public void clarifyInstruction() {
        for (Map<String, String> instructionSet : mappedLines) {
            String opcode = instructionSet.get("instruction").toLowerCase();
            switch (opcode) {
                case "add", "nand" -> RTypeInstruction(instructionSet);
                case "lw", "sw", "beq" -> ITypeInstruction(instructionSet);
                case "jalr" -> JTypeInstruction(instructionSet);
                case "halt", "noop" -> OTypeInstruction(instructionSet);
                case ".fill" -> getFill(instructionSet);
                default -> System.out.println("error");
            }
        }
    }

    public void RTypeInstruction(Map<String, String> instructionSet) {
        String value = instructionSet.get("instruction");
        String field0 = instructionSet.get("field0");
        String field1 = instructionSet.get("field1");
        String field2 = instructionSet.get("field2");

        int intValueField0 = Integer.parseInt(field0);
        int intValueField1 = Integer.parseInt(field1);
        int intValueField2 = Integer.parseInt(field2);

        String opcode = switch (value) {
            case "add" -> "000";
            case "nand" -> "001";
            default -> "";
        };

        String RTypeValue = opcode + String.format("%3s", Integer.toBinaryString(intValueField0)).replace(' ', '0')
                + String.format("%3s", Integer.toBinaryString(intValueField1)).replace(' ', '0')
                + String.format("%16s", Integer.toBinaryString(intValueField2)).replace(' ', '0');

//        printInstructionValue(RTypeValue);
        printValueToFile(RTypeValue, "output.txt", instructionSet);

    }

    public void ITypeInstruction(Map<String, String> instructionSet) {
        String value = instructionSet.get("instruction");
        String field0 = instructionSet.get("field0");
        String field1 = instructionSet.get("field1");
        String field2 = instructionSet.get("field2");

        int intValueField0 = Integer.parseInt(field0);
        int intValueField1 = Integer.parseInt(field1);
        int intValueField2;
        String binaryField2;
        if (isInteger(field2)) {
            intValueField2 = Integer.parseInt(field2);
            binaryField2 = (intValueField2 < 0) ? convertNegativeNumberToBinary(intValueField2, 16) :
                    String.format("%16s", Integer.toBinaryString(intValueField2)).replace(' ', '0');
        } else {
            intValueField2 = getAddressForLabel(field2);
            if (Objects.equals(value, "beq")) {
                intValueField2 = (intValueField2 - 1) - Integer.parseInt(instructionSet.get("Address"));
            }
            binaryField2 = convertNegativeNumberToBinary(intValueField2, 16);
        }

        String opcode = switch (value) {
            case "lw" -> "010";
            case "sw" -> "011";
            case "beq" -> "100";
            default -> "";
        };

        String ITypeValue = opcode + String.format("%3s", Integer.toBinaryString(intValueField0)).replace(' ', '0')
                + String.format("%3s", Integer.toBinaryString(intValueField1)).replace(' ', '0')
                + binaryField2;

//        printInstructionValue(ITypeValue);
        printValueToFile(ITypeValue, "output.txt", instructionSet);


    }


    public void JTypeInstruction(Map<String, String> instructionSet) {
        String value = instructionSet.get("instruction");
        String field0 = instructionSet.get("field0");
        String field1 = instructionSet.get("field1");

        int intValueField0 = Integer.parseInt(field0);
        int intValueField1 = Integer.parseInt(field1);

        String binaryField0 = String.format("%3s", Integer.toBinaryString(intValueField0)).replace(' ', '0');
        String binaryField1 = String.format("%3s", Integer.toBinaryString(intValueField1)).replace(' ', '0');
        String binaryField2 = String.format("%16s", Integer.toBinaryString(intValueField1)).replace(' ', '0');

        if ("jalr".equals(value)) {
            String opcodeJalr = "101";
            String JTypeJalrValue = opcodeJalr + binaryField0 + binaryField1 + binaryField2;
            //printInstructionValue(JTypeJalrValue);
            printValueToFile(JTypeJalrValue, "output.txt", instructionSet);
        }
    }

    public void OTypeInstruction(Map<String, String> instructionSet) {
        String value = instructionSet.get("instruction");
        String opcode = "";
        if ("halt".equals(value)) opcode = "110";
        else if ("noop".equals(value)) opcode = "111";
        String binaryField0 = "0".repeat(22);
        String OTypeValue = opcode + binaryField0;
        printValueToFile(OTypeValue, "output.txt", instructionSet);
    }

    public void getFill(Map<String, String> instructionSet) {
        String field0 = instructionSet.get("field0");
        int numericValue = isInteger(field0) ? Integer.parseInt(field0) : getAddressForLabel(field0);
        printValueToFile(String.valueOf(numericValue), "output.txt",instructionSet);
    }
    public static boolean isBinary(String input) {
        try {
            Integer.parseInt(input, 2);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }



    private void printValueToFile(String numericValue, String filePath,Map<String, String> instructionSet) {
        boolean append;
        if (Objects.equals(instructionSet.get("Address"),"0")) {
            append = false;
        } else append = true;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,append))) {
            int decimalValue;
            if (isBinary(numericValue)){
                decimalValue = Integer.parseInt(numericValue, 2);
            } else {
                decimalValue = Integer.parseInt(numericValue);
            }
            String textToWrite = String.valueOf(decimalValue);
            writer.write(textToWrite+ "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String convertNegativeNumberToBinary(int n, int bits) {
        int maxPositiveValue = (1 << (bits - 1)) - 1;
        if (n > maxPositiveValue || n < -maxPositiveValue - 1) {
            throw new IllegalArgumentException("Input value out of range for " + bits + "-bit two's complement");
        }
        if (n < 0) {
            n = (1 << bits) + n;
        }
        String binary = Integer.toBinaryString(n);
        while (binary.length() < bits) {
            binary = "0" + binary;
        }
        return binary;
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            if (Integer.parseInt(str) > 32767 || Integer.parseInt(str) < -32768) {
                System.err.println("Use offset more than 16 bits: " + Integer.parseInt(str));
                System.exit(1);
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Integer getAddressForLabel(String label) {
        Integer address = labelToAddressMap.get(label);
        if (address == null) {
            System.err.println("The label " + label + " is undefined" );
            System.exit(1);
        }
        if (address > 32767 || address < -32768) {
            System.err.println("Use offset more than 16 bits: " + address);
            System.exit(1);
        }
        return address;
    }

    public static void main(String[] args) {
        ReadInstruction Read = new ReadInstruction("assembly.txt");
//        Read.printMappedLines();
//        System.out.println(Read.getAddressForLabel("start"));
        Read.clarifyInstruction();
    }
}