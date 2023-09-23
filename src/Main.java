import Assember.ReadInstruction;
import Simulator.ALU.ALU;
import Simulator.ALU.MainALU;
import Simulator.Wire;

public class Main {

    private static void printBIN(int n){
        System.out.print(n);
        System.out.println(" : 0b" + String.format("%32s", Integer.toBinaryString(n)).replaceAll(" ", "0"));
    }

    public static void main(String[] args) {
//        ReadInstruction Read = new ReadInstruction("/Users/natxpss/Documents/ComputerArchitech2023/src/Assember/Assem");
//        Read.printMappedLines();
//        System.out.println(Read.getAddressForLabel("five"));
//        Read.clarifyInstruction();
        
        Wire w_alu_operand1 = new Wire(455),
                w_alu_operand2 = new Wire(5),
                w_alu_result = new Wire(),
                w_alu_zero = new Wire(),
                w_ALUControl = new Wire(0b0110);
        
        ALU alu = new MainALU(
                w_alu_operand1,
                w_alu_operand2,
                w_alu_result,
                w_alu_zero,
                w_ALUControl
        );

        alu.execute();
        printBIN(w_alu_result.getData());
    }

}
