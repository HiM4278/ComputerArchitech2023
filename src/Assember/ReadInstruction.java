package Assember;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadInstruction {
    private static ReadInstruction instance;
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
            if (isReservedWords(parts[0])) {
                lineMap.put("instruction", parts[0]);
            }
            lineMap.put("Label", parts[0]);
            labelToAddressMap.put(parts[0], num);
        }
        if (parts.length >= 2) {
            if (isReservedWords(parts[0])){
                lineMap.put("field0",parts[1]);
            }
            lineMap.put("instruction", parts[1]);
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

        String RTypeValue = opcode + formatBinary(intValueField0, 3)
                + formatBinary(intValueField1, 3)
                + formatBinary(intValueField2, 16);

        printInstructionValue(instructionSet, RTypeValue);
    }

    public void ITypeInstruction(Map<String, String> instructionSet) {
        String value = instructionSet.get("instruction");
        String field0 = instructionSet.get("field0");
        String field1 = instructionSet.get("field1");
        String field2 = instructionSet.get("field2");

        int intValueField0 = Integer.parseInt(field0);
        int intValueField1 = Integer.parseInt(field1);
        int intValueField2 = isInteger(field2) ? Integer.parseInt(field2) : getAddressForLabel(field2);

        if ("beq".equals(value)) intValueField2 -= Integer.parseInt(instructionSet.get("Address"));

        String opcode = switch (value) {
            case "lw" -> "010";
            case "sw" -> "011";
            case "beq" -> "100";
            default -> "";
        };

        String ITypeValue = opcode + formatBinary(intValueField0, 3)
                + formatBinary(intValueField1, 3)
                + formatBinary(intValueField2, 16);

        printInstructionValue(instructionSet, ITypeValue);
    }

    public void JTypeInstruction(Map<String, String> instructionSet) {
        String value = instructionSet.get("instruction");
        String field0 = instructionSet.get("field0");
        String field1 = instructionSet.get("field1");

        int intValueField0 = Integer.parseInt(field0);
        int intValueField1 = Integer.parseInt(field1);

        String binaryField0 = getBinaryString(intValueField0, 3);
        String binaryField1 = getBinaryString(intValueField1, 3);
        String binaryField2 = getBinaryString(intValueField1, 16);

        if ("jalr".equals(value)) {
            String opcodeJalr = "101";
            String JTypeJalrValue = opcodeJalr + binaryField0 + binaryField1 + binaryField2;
            printInstructionValue(instructionSet, JTypeJalrValue);
        }
    }

    public void OTypeInstruction(Map<String, String> instructionSet) {
        String value = instructionSet.get("instruction");
        String opcode = "";

        if ("halt".equals(value)) opcode = "110";
        else if ("noop".equals(value)) opcode = "111";

        String binaryField0 = "0".repeat(22);
        String OTypeValue = opcode + binaryField0;

        printInstructionValue(instructionSet, OTypeValue);
    }

    private String getBinaryString(int value, int width) {
        return String.format("%" + width + "s", Integer.toBinaryString(Math.abs(value)))
                .replace(' ', value < 0 ? '1' : '0');
    }

    private void printInstructionValue(Map<String, String> instructionSet, String binaryValue) {
        int decimalValue = Integer.parseInt(binaryValue, 2);
        System.out.println("(address " + instructionSet.get("Address") + "): " + decimalValue);
    }

    private String formatBinary(int value, int width) {
        String binary = Integer.toBinaryString(Math.abs(value));
        return String.format("%" + width + "s", binary).replace(' ', value < 0 ? '1' : '0');
    }

    public void getFill(Map<String, String> instructionSet) {
        String field0 = instructionSet.get("field0");

        int numericValue = isInteger(field0) ? Integer.parseInt(field0) : getAddressForLabel(field0);
        System.out.println("(address " + instructionSet.get("Address") + "): " + numericValue);
    }

    private boolean isReservedWords(String s) {
        String[] reserve = {"lw", "sw", "add", "nand", "beq", "jalr", "halt", "noop", ".fill"};
        return Arrays.asList(reserve).contains(s);
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Integer getAddressForLabel(String label) {
        return labelToAddressMap.get(label);
    }

    public static void main(String[] args) {
        ReadInstruction Read = new ReadInstruction("/Users/natxpss/Documents/ComputerArchitech2023/src/Assember/Assem.txt");
//        Read.printMappedLines();
//        System.out.println(Read.getAddressForLabel("start"));
        Read.clarifyInstruction();
    }
}