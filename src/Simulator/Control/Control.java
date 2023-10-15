package Simulator.Control;

import Simulator.Hardware;
import Simulator.Wire;

/**
 * Control is a class representing the main control unit in a simulated computer architecture.
 * It takes input signals from an instruction and produces control signals for various components
 * in the processor.
 */
public class Control implements Hardware {

    // Input Wire representing the instruction.
    private final Wire w_instruction;

    // Output Wire representing control signals.
    private final Wire wc_Jump;
    private final Wire wc_Branch;
    private final Wire wc_MemRead;
    private final Wire wc_MemToReg;
    private final Wire wc_ALUOp;
    private final Wire wc_MemWrite;
    private final Wire wc_ALUSrc;
    private final Wire wc_RegWrite;
    private final Wire wc_RegSelect;
    private final Wire wc_End;

    /**
     * Constructs a MainControl unit with the specified input and control wires.
     * @param w_instruction [input] Wire representing the instruction.
     * @param wc_Jump [output] Wire representing the jump control signal.
     * @param wc_Branch [output] Wire representing the branch control signal.
     * @param wc_MemRead [output] Wire representing the memory read control signal.
     * @param wc_MemToReg [output] Wire representing the memory-to-register control signal.
     * @param wc_ALUOp [output] Wire representing the ALU operation control signal.
     * @param wc_MemWrite [output] Wire representing the memory write control signal.
     * @param wc_ALUSrc [output] Wire representing the ALU source control signal.
     * @param wc_RegWrite [output] Wire representing the register write control signal.
     * @param wc_RegSelect [output] Wire representing the register-write select regB or rt control signal.
     * @param wc_End [output] Wire representing the end control signal.
     */
    public Control(
            Wire w_instruction,
            Wire wc_Jump,
            Wire wc_Branch,
            Wire wc_MemRead,
            Wire wc_MemToReg,
            Wire wc_ALUOp,
            Wire wc_MemWrite,
            Wire wc_ALUSrc,
            Wire wc_RegWrite,
            Wire wc_RegSelect,
            Wire wc_End
    ){
        this.w_instruction = w_instruction;
        this.wc_Jump = wc_Jump;
        this.wc_Branch = wc_Branch;
        this.wc_MemRead = wc_MemRead;
        this.wc_MemToReg = wc_MemToReg;
        this.wc_ALUOp = wc_ALUOp;
        this.wc_MemWrite = wc_MemWrite;
        this.wc_ALUSrc = wc_ALUSrc;
        this.wc_RegWrite = wc_RegWrite;
        this.wc_RegSelect = wc_RegSelect;
        this.wc_End = wc_End;
    }

    /**
     * Generate control signals based on the instruction and set the appropriate output wires.
     */
    @Override
    public void execute() {
        this.wc_Jump.set(0b0);
        this.wc_Branch.set(0b0);
        this.wc_MemRead.set(0b0);
        this.wc_MemToReg.set(0b0);
        this.wc_ALUOp.set(0b0000);
        this.wc_MemWrite.set(0b0);
        this.wc_ALUSrc.set(0b0);
        this.wc_RegWrite.set(0b0);
        this.wc_RegSelect.set(0b0);
        this.wc_End.set(0b0);

        // Determine control signals based on the binary code of the instruction.
        int opcode = w_instruction.getRangeData(22, 24);

        // Set control signals based on the specific opcode.
        if(opcode==0b000) { // add
            this.wc_ALUOp.set(0b0010);
            this.wc_RegWrite.set(0b1);
            this.wc_RegSelect.set(0b1);
        }else if(opcode==0b001) { // nand
            this.wc_ALUOp.set(0b0000);
            this.wc_RegWrite.set(0b1);
            this.wc_RegSelect.set(0b1);
        }else if(opcode==0b010) { // lw
            this.wc_MemRead.set(0b1);
            this.wc_MemToReg.set(0b1);
            this.wc_ALUOp.set(0b0010);
            this.wc_ALUSrc.set(0b1);
            this.wc_RegWrite.set(0b1);
        }else if(opcode==0b011) { // sw
            this.wc_ALUOp.set(0b0010);
            this.wc_MemWrite.set(0b1);
            this.wc_ALUSrc.set(0b1);
        }else if(opcode==0b100) { // beq
            this.wc_Branch.set(0b1);
            this.wc_ALUOp.set(0b0110);
        }else if(opcode==0b101) { // jalr
            this.wc_Jump.set(0b1);
            this.wc_RegWrite.set(0b1);
        }else if(opcode==0b110) { // halt
            this.wc_End.set(0b1);
        }
    }
}
