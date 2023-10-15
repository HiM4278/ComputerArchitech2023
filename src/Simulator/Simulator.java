package Simulator;
import Simulator.ALU.ADD;
import Simulator.ALU.ALU;
import Simulator.Control.MainControl;
import Simulator.Logic.AND;
import Simulator.Logic.MUX;
import Simulator.Logic.OR;
import Simulator.Memory.Memory;
import Simulator.Memory.MemoryReader;
import Simulator.Register.MainRegister;
import Simulator.Register.PC;

public class Simulator {
    private final int NUM_MEMORY = 65536;
    private final int NUM_REGS = 8;
    private final int MAX_LINE_LENGTH = 1000;

    private int pc;
    private int[] mem = new int[NUM_MEMORY];
    private int[] reg = new int[NUM_REGS];
    private int numMemory;

    // Wires
    private final Wire w_input_pc = new Wire();
    private final Wire w_output_pc = new Wire();
    private final Wire w_instruction = new Wire();
    private final Wire w_dataA = new Wire();
    private final Wire w_dataB = new Wire();
    private final Wire w_alu_result = new Wire();
    private final Wire w_alu_zero = new Wire();
    private final Wire w_data_from_mem = new Wire();
    private final Wire w_mux_dataToReg = new Wire();
    private final Wire w_mux_srcToALU = new Wire();
    private final Wire w_mux_destReg = new Wire();
    private final Wire w_add_pc_1 = new Wire();
    private final Wire w_add_pc_offSet = new Wire();
    private final Wire w_beq_or_jump = new Wire();
    private final Wire w_mux_pc_or_data = new Wire();
    private final Wire w_mux_jump_or_branch = new Wire();

    // P Wires
    private final Wire w_registerA = new Wire(w_instruction, 19, 21);
    private final Wire w_registerB = new Wire(w_instruction, 16, 18);
    private final Wire w_writeReg = new Wire(w_instruction, 0, 2);
    private final Wire w_immediate = new Wire(w_instruction, 0, 15, true);


    // Control Wires
    private final Wire control_branch = new Wire();
    private final Wire control_jump = new Wire();
    private final Wire control_memRead = new Wire();
    private final Wire control_memToReg = new Wire();
    private final Wire control_aluOp = new Wire();
    private final Wire control_memWrite = new Wire();
    private final Wire control_aluSrc = new Wire();
    private final Wire control_regWrite = new Wire();
    private final Wire control_regSelect = new Wire();
    private final Wire control_end = new Wire();
    private final Wire control_beq = new Wire();

    // Memory
    private final Memory memory = new Memory("input.txt");

    private final PC PC = new PC(
            w_input_pc,
            control_end,
            w_output_pc
    );

    private final MemoryReader instrMemory = new MemoryReader(
            memory,
            w_output_pc,
            w_instruction
    );

    private final MainControl control = new MainControl(
            w_instruction,
            control_branch,
            control_jump,
            control_memRead,
            control_memToReg,
            control_aluOp,
            control_memWrite,
            control_aluSrc,
            control_regWrite,
            control_regSelect,
            control_end
    );

    private final MUX mux_destReg = new MUX(
            control_regSelect,
            new Wire[]{w_registerB, w_writeReg},
            w_mux_destReg
    );

    private final MainRegister register = new MainRegister(
            w_registerA,
            w_registerB,
            w_mux_destReg,
            w_mux_pc_or_data,
            control_regWrite,
            w_dataA,
            w_dataB
    );

    private final MUX mux_srcToALU = new MUX(
            control_aluSrc,
            new Wire[]{w_dataB, w_immediate},
            w_mux_srcToALU
    );

    private final ALU alu = new ALU(
            w_dataA,
            w_mux_srcToALU,
            control_aluOp,
            w_alu_result,
            w_alu_zero
    );

    private final MemoryReader dataMemory = new MemoryReader(
            memory,
            w_alu_result,
            w_dataB,
            control_memWrite,
            control_memRead,
            w_data_from_mem
    );

    private final MUX mux_dataToReg = new MUX(
            control_memToReg,
            new Wire[]{w_alu_result, w_data_from_mem},
            w_mux_dataToReg
    );

    private final ADD add_pc_1 = new ADD(w_output_pc, new Wire(1), w_add_pc_1);
    private final ADD add_pc_offSet = new ADD(w_add_pc_1, w_immediate, w_add_pc_offSet);

    private final AND and_branch_zero = new AND(control_branch, w_alu_zero, control_beq);
    private final OR or_beq_jump = new OR(control_beq, control_jump, w_beq_or_jump);
    private final MUX mux_newPC = new MUX(
            w_beq_or_jump,
            new Wire[]{w_add_pc_1, w_mux_jump_or_branch},
            w_input_pc
    );

    private final MUX mux_jump_or_branch = new MUX(
            control_jump,
            new Wire[]{w_add_pc_offSet, w_dataA},
            w_mux_jump_or_branch
    );

    private final MUX mux_pc_or_data = new MUX(
            control_jump,
            new Wire[]{w_mux_dataToReg, w_add_pc_1},
            w_mux_pc_or_data
    );

    public static void printBIN(int n){
        System.out.print(n);
        System.out.println(" : 0b" + String.format("%32s", Integer.toBinaryString(n)).replaceAll(" ", "0"));
    }

    private void printState()
    {
        int i;
        System.out.print("\n@@@\nstate:\n");
        System.out.printf("\tpc %d\n", pc);
        System.out.print("\tmemory:\n");
        for (i=0; i < numMemory; i++) {
            System.out.printf("\t\tmem[ %d ] %d\n", i, mem[i]);
        }
        System.out.print("\tregisters:\n");
        for (i=0; i < NUM_REGS; i++) {
            System.out.printf("\t\treg[ %d ] %d\n", i, reg[i]);
        }
        System.out.print("end state\n");
    }

    private void updateVariable(){
        mem = memory.toArrayList();
        reg = register.toArrayList();
        pc = PC.data;
        numMemory = memory.numMemory;
    }


    public void run(){
        int instrCount = 0;
        memory.prettyPrint();
        for(int i = 1; i <= 500; i++){

            updateVariable();
            printState();

            // Fetch Instruction
            instrMemory.execute();

            // Instruction Decode and Control Generation
            control.execute();
            register.execute();

            // Execute (ALU)
            mux_srcToALU.execute();
            alu.execute();
            and_branch_zero.execute();
            or_beq_jump.execute();
            add_pc_1.execute();
            add_pc_offSet.execute();
            mux_jump_or_branch.execute();
            mux_newPC.execute();

            // Memory access
            dataMemory.execute();

            // Register write back
            mux_destReg.execute();
            mux_dataToReg.execute();
            mux_pc_or_data.execute();
            register.write();

            PC.execute();
            instrCount++;
            if(PC.isEnd()) {
                System.out.println("machine halted");
                System.out.println("total of " + instrCount + " instructions executed");
                System.out.println("final state of machine:");
                updateVariable();
                printState();
                break;
            }
            if(pc >= numMemory) {
                System.err.println("machine might infinite loop");
                break;
            }
        }
    }
}
