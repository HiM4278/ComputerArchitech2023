package Assember;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadInstruction {
    private static ReadInstruction instance;
    private String filePath;
    private List<Map<String, String>> mappedLines;

    private ReadInstruction(String filePath) {
        this.filePath = filePath;
        this.mappedLines = new ArrayList<>();
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
            lineMap.put("Label", parts[0]);
        }
        if (parts.length >= 2) {
            lineMap.put("instruction", parts[1]);
        }
        if (parts.length >= 3) {
            lineMap.put("field0", parts[2]);
        }
        if (parts.length >= 4) {
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
        String[] parts = line.split(" +", 6);
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

//    public static String CheckSignAndExtend(String field) {
//        if (field.startsWith("-")) {
//            return "11" + field.substring(1);
//        } else {
//            return "00" + field;
//        }
//    }

//    public void clarifyInstruction() {
//        String clarify = "";
//        for (Map<String, String> instruction_set : mappedLines) {
//            String value = instruction_set.get("instruction");
//            String field0 = instruction_set.get("field0");
//            String field1 = instruction_set.get("field1");
//            String field2 = instruction_set.get("field2");
//
//            //int to binary and extend sign-bit
//            String binaryField0 = String.format("%3s", Integer.toBinaryString(Integer.parseInt(instruction_set.get("field0")))).replaceAll(" ", "0");
//            String binaryField1 = String.format("%3s", Integer.toBinaryString(Integer.parseInt(instruction_set.get("field1")))).replaceAll(" ", "0");
//            String binaryField2 = String.format("%16s", Integer.toBinaryString(Integer.parseInt(instruction_set.get("field2")))).replaceAll(" ", "0");
//            System.out.println(binaryField0);
//            System.out.println(binaryField1);
//            System.out.println(binaryField2);
//
//
//            //Match instruction and opcode
//            switch (value) {
//                case "add":
//                    String opcode_and = "000";
//                    String RTypeAndValue = opcode_and + binaryField0 + binaryField1 + binaryField2;
//                    System.out.println(RTypeAndValue);
//                    break;
//                case "nand":
//                    String opcode_nand = "001";
//                    String RTypeNandValue = opcode_nand + binaryField0 + binaryField1 + binaryField2;
//                    System.out.println(RTypeNandValue);
//                    break;
//                case "lw":
//                    String opcode_lw = "010";
//                    String ITypeLwValue = opcode_lw + binaryField0 + binaryField1 + binaryField2;
//                    System.out.println(ITypeLwValue);
//                    break;
//                case "sw":
//                    String opcode_sw = "011";
//                    String ITypeSwValue = opcode_sw + binaryField0 + binaryField1 + binaryField2;
//                    System.out.println(ITypeSwValue);
//                    break;
//                case "beq":
//                    String opcode_beq = "100";
//                    String ITypeBeqValue = opcode_beq + binaryField0 + binaryField1 + binaryField2;
//                    System.out.println(ITypeBeqValue);
//                    break;
//                case "jalr":
//                    String opcode_jalr = "101";
//                    String JTypeJalrValue = opcode_jalr + binaryField0 + binaryField1 + binaryField2;
//                    System.out.println(JTypeJalrValue);
//                    break;
//                case "halt":
//                    String opcode_halt = "110";
//                    String OTypeHaltValue = opcode_halt + binaryField0 + binaryField1 + binaryField2;
//                    System.out.println(OTypeHaltValue);
//                    break;
//                case "loop":
//                    String opcode_loop = "111";
//                    String OTypeLoopValue = opcode_loop + binaryField0 + binaryField1 + binaryField2;
//                    System.out.println(OTypeLoopValue);
//                    break;
//                default:
//                    System.out.println("error");
//            }
//        }
//    }

    public void RTypeInstruction() {
        for (Map<String, String> instruction_set : mappedLines) {

                String value = instruction_set.get("instruction");
                String field0 = instruction_set.get("field0");
                String field1 = instruction_set.get("field1");
                String field2 = instruction_set.get("field2");

                int intValueField0 = Integer.parseInt(field0);
                int intValueField1 = Integer.parseInt(field1);
                int intValueField2 = Integer.parseInt(field2);

                String binaryField0 = "";
                String binaryField1 = "";
                String binaryField2 = "";

                if (intValueField0 >= 0) {
                    // Positive integer
                    binaryField0 = String.format("%3s", Integer.toBinaryString(intValueField0)).replaceAll(" ", "0");
                }
                if (intValueField0 < 0){
                    // Negative integer
                    binaryField0 = String.format("%3s", Integer.toBinaryString(-intValueField0)).replaceAll(" ", "1");
                }

                if (intValueField1 >= 0) {
                    binaryField1 = String.format("%3s", Integer.toBinaryString(intValueField1)).replaceAll(" ", "0");
                }
                if (intValueField0 < 0){
                    binaryField1 = String.format("%3s", Integer.toBinaryString(-intValueField1)).replaceAll(" ", "1");
                }

                if (intValueField2 >= 0) {
                    binaryField2 = String.format("%16s", Integer.toBinaryString(intValueField2)).replaceAll(" ", "0");
                }
                if (intValueField0 < 0){
                    binaryField2 = String.format("%16s", Integer.toBinaryString(-intValueField2)).replaceAll(" ", "1");
                }

                if (Objects.equals(value, "add")) {
                    String opcode_and = "000";
                    String RTypeAndValue = opcode_and + binaryField0 + binaryField1 + binaryField2;
                    System.out.println(RTypeAndValue);
                }
                if (Objects.equals(value, "nand")) {
                    String opcode_nand = "001";
                    String RTypeNandValue = opcode_nand + binaryField0 + binaryField1 + binaryField2;
                    System.out.println(RTypeNandValue);
                }
        }
    }

    public void ITypeInstruction() { //remain check label
        for (Map<String, String> instruction_set : mappedLines) {
            String value = instruction_set.get("instruction");
            String field0 = instruction_set.get("field0");
            String field1 = instruction_set.get("field1");
            String field2 = instruction_set.get("field2");

            int intValueField0 = Integer.parseInt(field0);
            int intValueField1 = Integer.parseInt(field1);
            int intValueField2 = Integer.parseInt(field2);

            String binaryField0 = "";
            String binaryField1 = "";
            String binaryField2 = "";

            if (intValueField0 >= 0) {
                // Positive integer
                binaryField0 = String.format("%3s", Integer.toBinaryString(intValueField0)).replaceAll(" ", "0");
            }
            if (intValueField0 < 0){
                // Negative integer
                binaryField0 = String.format("%3s", Integer.toBinaryString(-intValueField0)).replaceAll(" ", "1");
            }

            if (intValueField1 >= 0) {
                binaryField1 = String.format("%3s", Integer.toBinaryString(intValueField1)).replaceAll(" ", "0");
            }
            if (intValueField0 < 0){
                binaryField1 = String.format("%3s", Integer.toBinaryString(-intValueField1)).replaceAll(" ", "1");
            }

            if (intValueField2 >= 0) {
                binaryField2 = String.format("%16s", Integer.toBinaryString(intValueField2)).replaceAll(" ", "0");
            }
            if (intValueField0 < 0){
                binaryField2 = String.format("%16s", Integer.toBinaryString(-intValueField2)).replaceAll(" ", "1");
            }

            if (Objects.equals(value, "lw")) {
                String opcode_lw = "010";
                String RTypeAndValue = opcode_lw + binaryField0 + binaryField1 + binaryField2;
                System.out.println(RTypeAndValue);
            }
            if (Objects.equals(value, "sw")) {
                String opcode_sw = "011";
                String RTypeNandValue = opcode_sw + binaryField0 + binaryField1 + binaryField2;
                System.out.println(RTypeNandValue);
            }
            if (Objects.equals(value, "beq")) {
                String opcode_beq = "100";
                String ITypeBeqValue = opcode_beq + binaryField0 + binaryField1 + binaryField2;
                System.out.println(ITypeBeqValue);
            }
        }
    }

    public void JTypeInstruction() {
        for (Map<String, String> instruction_set : mappedLines) {
            String value = instruction_set.get("instruction");
            String field0 = instruction_set.get("field0");
            String field1 = instruction_set.get("field1");
            String field2 = instruction_set.get("field2");

            int intValueField0 = Integer.parseInt(field0);
            int intValueField1 = Integer.parseInt(field1);
            int intValueField2 = Integer.parseInt(field2);

            String binaryField0 = "";
            String binaryField1 = "";
            //String binaryField2;

            if (intValueField0 >= 0) {
                // Positive integer
                binaryField0 = String.format("%3s", Integer.toBinaryString(intValueField0)).replaceAll(" ", "0");
            }
            if (intValueField0 < 0){
                // Negative integer
                binaryField0 = String.format("%3s", Integer.toBinaryString(-intValueField0)).replaceAll(" ", "1");
            }

            if (intValueField1 >= 0) {
                binaryField1 = String.format("%3s", Integer.toBinaryString(intValueField1)).replaceAll(" ", "0");
            }
            if (intValueField0 < 0){
                binaryField1 = String.format("%3s", Integer.toBinaryString(-intValueField1)).replaceAll(" ", "1");
            }

            //binaryField2 = String.format("%16s", Integer.toBinaryString(intValueField2)).replaceAll(" ", "0");

            if (Objects.equals(value, "jalr")) {
                String opcode_jalr = "101";
//                String JTypeJalrValue = opcode_jalr + binaryField0 + binaryField1 + binaryField2;
                String JTypeJalrValue = opcode_jalr + binaryField0 + binaryField1 + "0000000000000000";
                System.out.println(JTypeJalrValue);
            }
        }
    }

        public static void main (String[]args){
            ReadInstruction Read = new ReadInstruction("/Users/natxpss/Documents/Project ComArch/src/Assember/instruction");
//        Read.printMappedLines();
//        Read.clarifyInstruction();
          Read.RTypeInstruction();
        }
    }
