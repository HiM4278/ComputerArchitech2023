package Simulator.Control;

import Simulator.Wire;

public class MainControl implements Control{
    private final Wire w_instruction;
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

    public MainControl(
            Wire w_instruction,
            Wire wc_branch, Wire wc_jump,
            Wire wc_memRead,
            Wire wc_memToReg,
            Wire wc_aluOp,
            Wire wc_memWrite,
            Wire wc_aluSrc,
            Wire wc_regWrite,
            Wire wc_regSelect, Wire wc_end){
        this.w_instruction = w_instruction;
        this.wc_Jump = wc_jump;
        this.wc_Branch = wc_branch;
        this.wc_MemRead = wc_memRead;
        this.wc_MemToReg = wc_memToReg;
        this.wc_ALUOp = wc_aluOp;
        this.wc_MemWrite = wc_memWrite;
        this.wc_ALUSrc = wc_aluSrc;
        this.wc_RegWrite = wc_regWrite;
        this.wc_RegSelect = wc_regSelect;
        this.wc_End = wc_end;
        subWrite();
        execute();
    }

    public void subWrite(){
        this.w_instruction.subscribe(this);
    }

    @Override
    public void execute() {
        this.wc_Jump.set(0b0);
        this.wc_Branch.set(0b0);
        this.wc_MemRead.set(0b0);
        this.wc_MemToReg.set(0b0);
        this.wc_ALUOp.set(0b0);
        this.wc_MemWrite.set(0b0);
        this.wc_ALUSrc.set(0b0);
        this.wc_RegWrite.set(0b0);
        this.wc_RegSelect.set(0b0);
        this.wc_End.set(0b0);

        int control = w_instruction.getRangeData(22, 24);

        // add
        if(control==0b000) {
            this.wc_ALUOp.set(0b0010);
            this.wc_RegWrite.set(0b1);
            this.wc_RegSelect.set(0b1);
        }
        // nand
        if(control==0b001) {
            this.wc_ALUOp.set(0b1100);
            this.wc_RegWrite.set(0b1);
            this.wc_RegSelect.set(0b1);
        }
        // lw
        if(control==0b010) {
            this.wc_MemRead.set(0b1);
            this.wc_MemToReg.set(0b1);
            this.wc_ALUOp.set(0b0010);
            this.wc_ALUSrc.set(0b1);
            this.wc_RegWrite.set(0b1);
        }
        // sw
        if(control==0b011) {
            this.wc_ALUOp.set(0b0010);
            this.wc_MemWrite.set(0b1);
            this.wc_ALUSrc.set(0b1);
        }
        // beq
        if(control==0b100) {
            this.wc_Branch.set(0b1);
            this.wc_ALUOp.set(0b0110);
        }
        // J-type
        if(control==0b101) {
            this.wc_Jump.set(0b1);
            this.wc_RegWrite.set(0b1);
        }
        // halt
        if(control==0b110) {
            this.wc_End.set(0b1);
        }
    }
}
