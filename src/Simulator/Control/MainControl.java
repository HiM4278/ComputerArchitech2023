package Simulator.Control;

import Simulator.Wire;

public class MainControl implements Control{

    private final Wire w_instruction;
    private final Wire wc_Jump = new Wire();
    private final Wire wc_Branch = new Wire();
    private final Wire wc_MemRead = new Wire();
    private final Wire wc_MemToReg = new Wire();
    private final Wire wc_ALUOp = new Wire();
    private final Wire wc_MemWrite = new Wire();
    private final Wire wc_ALUSrc = new Wire();
    private final Wire wc_RegWrite = new Wire();

    public MainControl(Wire w_instruction){
        this.w_instruction = w_instruction;
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

        int control = w_instruction.getRangeData(22, 24);
        // add
        if(control==0b000) {
            this.wc_ALUOp.set(0b0010);
            this.wc_RegWrite.set(0b1);
        }
        // nand
        if(control==0b001) {
            this.wc_ALUOp.set(0b1100);
            this.wc_RegWrite.set(0b1);
        }
        // I-type lw
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
            this.wc_RegWrite.set(0b1);
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
    }

    public Wire getJump(){
        return wc_Jump;
    }

    public Wire getBranch() {
        return wc_Branch;
    }

    public Wire getMemRead() {
        return wc_MemRead;
    }

    public Wire getMemToReg() {
        return wc_MemToReg;
    }

    public Wire getMemWrite() {
        return wc_MemWrite;
    }

    public Wire getRegWrite() {
        return wc_RegWrite;
    }

    public Wire getALUOp() {
        return wc_ALUOp;
    }

    public Wire getALUSrc() {
        return wc_ALUSrc;
    }
}
