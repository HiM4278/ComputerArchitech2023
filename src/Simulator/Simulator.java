package Simulator;
import Simulator.ALU.MainALU;
import Simulator.Control.MainControl;
import Simulator.Logic.Multiplexer;
import Simulator.Memory.Memory;
import Simulator.Memory.MemoryReader;
import Simulator.Register.MainRegister;
import Simulator.Register.PC;

import java.lang.invoke.MutableCallSite;

public class Simulator {
    // Memory
    private final Memory memory = new Memory("output.txt");

    // Wire for PC
    private final Wire w_clock = new Wire();
    private final Wire w_input_pc = new Wire();

    private final PC pc = new PC(w_input_pc, w_clock);

    // Instruction Memory
    private final MemoryReader instrMemory = new MemoryReader(memory, pc.w_output());
    private final Wire w_instruction = instrMemory.w_output_data();

    // Control
    private final MainControl control = new MainControl(w_instruction);

    // Register
    private final Wire w_regWriteData = new Wire();
    private final MainRegister register = new MainRegister(w_instruction, w_regWriteData, control.getRegWrite());

    private final Wire w_imm = new Wire();

    private final Wire alu_src = new Wire();

    private final Multiplexer mux_alu_src = new Multiplexer(control.getALUSrc(), new Wire[]{register.w_regB(), w_imm}, alu_src);

    private final MainALU alu = new MainALU(register.w_regA(), alu_src, control.getALUOp());

    private final MemoryReader dataMemory = new MemoryReader(memory, alu.w_result(), register.w_regB(), control.getMemWrite(), control.getMemRead());

    private final Multiplexer mux_reg_write = new Multiplexer(control.getMemToReg(), new Wire[]{alu.w_result(), dataMemory.w_output_data()}, w_regWriteData);

    private void printBIN(int n){
        System.out.print(n);
        System.out.println(" : 0b" + String.format("%32s", Integer.toBinaryString(n)).replaceAll(" ", "0"));
    }

    public void run(){
        int clock = 0;
        for(int i = 1; i <= 10; i++){
            clock = clock ^ 1;
            w_input_pc.set(pc.w_output().get()+1);
            w_imm.set(w_instruction.getRangeData(0,15));
            printBIN(w_instruction.get());
            printBIN(w_regWriteData.get());
            printBIN(control.getALUSrc().get());
            register.write();
            w_clock.set(clock);
            System.out.println("PC: " + pc.data);
            register.prettyPrint();
        }
    }
}
