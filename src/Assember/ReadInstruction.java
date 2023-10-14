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

    /**
     * This method aims to process a list of instruction sets and classify them based on their opcode.
     * input : MappedLines: A list of instruction sets, where each set is represented as
     * a Map<String, String> containing keys like "instruction," "field0," "field1," etc.
     * output : The method does not have a direct output. Instead, it delegates the processing
     * of different instruction types to specific methods
     */
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

    /**
     * This method processes R-type instructions, converts their fields to binary,
     * and outputs the resulting binary instruction to a file.
     * @param instructionSet Map<String, String> containing keys
     * output : The method does not return a value. Instead, it writes the binary R-type
     * instruction to an output file ("output.txt") using the printValueToFile method.
     */
    public void RTypeInstruction(Map<String, String> instructionSet) {
        // Retrieve instruction fields from the instructionSet map.
        String value = instructionSet.get("instruction");
        String field0 = instructionSet.get("field0");
        String field1 = instructionSet.get("field1");
        String field2 = instructionSet.get("field2");

        // Parse instruction fields to integer values.
        int intValueField0 = Integer.parseInt(field0);
        int intValueField1 = Integer.parseInt(field1);
        int intValueField2 = Integer.parseInt(field2);

        String opcode = switch (value) {
            case "add" -> "000";
            case "nand" -> "001";
            default -> "";
        };

        // Convert integer fields to binary strings and format them accordingly.
        // Combine the opcode and binary fields to form the R-type instruction in binary format.
        String RTypeValue = opcode + String.format("%3s", Integer.toBinaryString(intValueField0)).replace(' ', '0')
                + String.format("%3s", Integer.toBinaryString(intValueField1)).replace(' ', '0')
                + String.format("%16s", Integer.toBinaryString(intValueField2)).replace(' ', '0');

        printValueToFile(RTypeValue, "output.txt", instructionSet);
    }

    /**
     * This method processes I-type instructions, converts their fields to binary,
     * and outputs the resulting binary instruction to a file.
     * @param instructionSet Map<String, String> containing keys
     * output : The method does not return a value. Instead, it writes the binary I-type
     * instruction to an output file ("output.txt") using the printValueToFile method.
     */
    public void ITypeInstruction(Map<String, String> instructionSet) {
        // Retrieve instruction fields from the instructionSet map.
        String value = instructionSet.get("instruction");
        String field0 = instructionSet.get("field0");
        String field1 = instructionSet.get("field1");
        String field2 = instructionSet.get("field2");

        // Parse instruction fields to integer values.
        int intValueField0 = Integer.parseInt(field0);
        int intValueField1 = Integer.parseInt(field1);
        int intValueField2;
        String binaryField2;

        // Determine the binary representation of field2 based on its type (integer or label).
        if (isInteger(field2)) {
            intValueField2 = Integer.parseInt(field2);
            // If field2 is negative, convert it to a 16-bit two's complement binary number.
            binaryField2 = (intValueField2 < 0) ? convertNegativeNumberToBinary(intValueField2, 16) :
                    String.format("%16s", Integer.toBinaryString(intValueField2)).replace(' ', '0');
        } else {
            // If field2 is a label, get its address and calculate the offset if it's a "beq" instruction.
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

        // Convert integer fields to binary strings and format them accordingly.
        // Combine the opcode and binary fields to form the I-type instruction in binary format.
        String ITypeValue = opcode + String.format("%3s", Integer.toBinaryString(intValueField0)).replace(' ', '0')
                + String.format("%3s", Integer.toBinaryString(intValueField1)).replace(' ', '0')
                + binaryField2;
        printValueToFile(ITypeValue, "output.txt", instructionSet);
    }

    /**
     * This method processes J-type instructions, specifically "jalr," and converts
     * their fields to binary. It then outputs the resulting binary instruction to a file.
     * @param instructionSet Map<String, String> containing keys
     * output : The method does not return a value. Instead, it writes the binary J-type
     * instruction to an output file ("output.txt") using the printValueToFile method
     */
    public void JTypeInstruction(Map<String, String> instructionSet) {
        // Retrieve instruction fields from the instructionSet map.
        String value = instructionSet.get("instruction");
        String field0 = instructionSet.get("field0");
        String field1 = instructionSet.get("field1");

        // Parse instruction fields to integer values.
        int intValueField0 = Integer.parseInt(field0);
        int intValueField1 = Integer.parseInt(field1);

        // Convert integer fields to formatted 3-bit binary strings.
        String binaryField0 = String.format("%3s", Integer.toBinaryString(intValueField0)).replace(' ', '0');
        String binaryField1 = String.format("%3s", Integer.toBinaryString(intValueField1)).replace(' ', '0');
        // Create a 16-bit binary representation of field1, used for "jalr" instructions.
        String binaryField2 = String.format("%16s", Integer.toBinaryString(intValueField1)).replace(' ', '0');

        if ("jalr".equals(value)) {
            String opcodeJalr = "101";
            // Combine opcode and binary fields to form the J-type instruction in binary format.
            String JTypeJalrValue = opcodeJalr + binaryField0 + binaryField1 + binaryField2;
            printValueToFile(JTypeJalrValue, "output.txt", instructionSet);
        }
    }

    /**
     * This method processes O-type instructions ("halt" and "noop") and converts them to
     * binary format. It then outputs the resulting binary instruction to a file.
     * @param instructionSet Map<String, String> containing keys
     * outputs: The method does not return a value. Instead, it writes the binary O-type instruction
     * to an output file ("output.txt") using the printValueToFile method.
     */
    public void OTypeInstruction(Map<String, String> instructionSet) {
        // Retrieve the instruction from the instructionSet map.
        String value = instructionSet.get("instruction");
        String opcode = "";
        if ("halt".equals(value)) opcode = "110";
        else if ("noop".equals(value)) opcode = "111";
        // Create a 22-bit binary representation of zeros for O-type instructions.
        String binaryField0 = "0".repeat(22);
        // Combine opcode and binary fields to form the O-type instruction in binary format.
        String OTypeValue = opcode + binaryField0;
        printValueToFile(OTypeValue, "output.txt", instructionSet);
    }

    /**
     *This method processes ".fill" instructions and converts the specified value (either an integer or a label)
     * to binary format. It then outputs the resulting binary instruction to a file.
     * @param instructionSet Map<String, String> containing keys
     * outputs: The method does not return a value. Instead, it writes the binary ".fill"
     * instruction to an output file ("output.txt") using the printValueToFile method.
     */
    public void getFill(Map<String, String> instructionSet) {
        // Retrieve the value from the instructionSet map.
        String field0 = instructionSet.get("field0");
        // Parse the value to an integer.
        int numericValue = isInteger(field0) ? Integer.parseInt(field0) : getAddressForLabel(field0);
        printValueToFile(String.valueOf(numericValue), "output.txt",instructionSet);
    }

    /**
     *This method writes a numeric value to a file specified by filePath.
     * If it's not the first instruction, the value is appended to the file.
     * @param numericValue Binary numbers come from Typeinstruction from the Instruction.
     * @param filePath File to store machine code values.
     * @param instructionSet Map<String, String> containing keys
     * output : The method does not return a value. Instead, it writes the numeric
     * value followed by a newline character to the specified output file.
     */
    private void printValueToFile(String numericValue, String filePath,Map<String, String> instructionSet) {
        boolean append;
        // Determine whether to append to the file or create a new file.
        if (Objects.equals(instructionSet.get("Address"),"0")) {
            append = false;
        } else append = true;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,append))) {
            int decimalValue;
            if (Objects.equals(instructionSet.get("instruction"),".fill")){
                decimalValue = Integer.parseInt(numericValue);
            } else {
                decimalValue = Integer.parseInt(numericValue, 2);
            }
            // Write the decimal value followed by a newline to the file.
            String textToWrite = String.valueOf(decimalValue);
            writer.write(textToWrite+ "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *This method converts a negative decimal number to its binary representation
     * using two's complement representation.
     * @param n Values in fields
     * @param bits Number of bits in the field
     * @return The binary representation of the negative decimal number n as a string.
     */
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

    /**
     *This method checks if a given string represents a valid integer within the range of -32768 to 32767.
     * @param str
     * @return true if the input string represents a valid integer within
     * the specified range (-32768 to 32767), and false otherwise.
     */
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

    /**
     * This method retrieves the address associated with a given label and
     * ensures that the address is within the valid range of -32768 to 32767.
     * @param label That is mapped from an instruction that is a character
     * @return the address corresponding to the given label if it is defined and
     *within the valid range. Otherwise, it prints an error message and exits the program.
     */
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
        ReadInstruction Read = new ReadInstruction("/Users/natxpss/Documents/ComputerArchitech2023/src/Assember/Assembler.txt");
//        Read.printMappedLines();
//        System.out.println(Read.getAddressForLabel("start"));
        Read.clarifyInstruction();
    }
}