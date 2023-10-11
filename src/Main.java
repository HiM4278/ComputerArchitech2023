import Assember.ReadInstruction;
import Simulator.ALU.ALU;
import Simulator.ALU.MainALU;
import Simulator.Memory.MainMemory;
import Simulator.Register.MainRegister;
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
        
//        Wire w_alu_operand1 = new Wire(455),
//                w_alu_operand2 = new Wire(5),
//                w_alu_result = new Wire(),
//                w_alu_zero = new Wire(),
//                w_ALUControl = new Wire(0b0110);
//
//        ALU alu = new MainALU(
//                w_alu_operand1,
//                w_alu_operand2,
//                w_alu_result,
//                w_alu_zero,
//                w_ALUControl
//        );

//        w_alu_operand1.setData(455);
//        printBIN(w_alu_result.getData());

        /*Wire w_reg_in_select = new Wire(8716319),
                w_reg_out_readA = new Wire(),
                w_reg_out_readB = new Wire(),
                w_reg_write_data = new Wire(),
                control_write = new Wire();

        MainRegister register = new MainRegister(
                w_reg_in_select,
                w_reg_write_data,
                control_write,
                w_reg_out_readA,
                w_reg_out_readB
        );

        printBIN(w_reg_out_readA.getData());
        control_write.setData(0b1);
        w_reg_write_data.setData(455);
        printBIN(w_reg_out_readB.getData());*/


        Wire wMem_in_address = new Wire(1),
                wMem_in_writeData = new Wire(),
                controlMem_write = new Wire(),
                controlMem_read = new Wire(),
                wMem_out_readData = new Wire();

        MainMemory memory = new MainMemory(
                wMem_in_address,
                wMem_in_writeData,
                controlMem_write,
                controlMem_read,
                wMem_out_readData
                );

        printBIN(wMem_out_readData.getData());
        controlMem_write.setData(1);
        wMem_in_writeData.setData(658);
        controlMem_write.setData(0);
        controlMem_read.setData(1);
        printBIN(wMem_out_readData.getData());


    }

}
