package Simulator.Control;

import Simulator.Wire;

public class MainControl implements Control{

    private final Wire wireInput;
    private final Wire wc_Jump;
    private final Wire wc_Branch;
    private final Wire wc_MemRead;
    private final Wire wc_MemToReg;
    private final Wire wc_ALUOp;
    private final Wire wc_MemWrite;
    private final Wire wc_ALUSrc;
    private final Wire wc_RegWrite;

    public MainControl(Wire wireInput, Wire wc_Jump, Wire wc_Branch, Wire wc_MemRead, Wire wc_MemToReg, Wire wc_ALUOp, Wire wc_MemWrite, Wire wc_ALUSrc, Wire wc_RegWrite){
        this.wireInput = wireInput;
        this.wc_Jump = wc_Jump;
        this.wc_Branch = wc_Branch;
        this.wc_MemRead = wc_MemRead;
        this.wc_MemToReg = wc_MemToReg;
        this.wc_ALUOp = wc_ALUOp;
        this.wc_MemWrite = wc_MemWrite;
        this.wc_ALUSrc = wc_ALUSrc;
        this.wc_RegWrite = wc_RegWrite;
        subWrite();
        execute();
    }

    public void subWrite(){
        this.wireInput.subscribe(this);
    }


    @Override
    public void execute() {
        this.wc_Jump.setData(0b0);
        this.wc_Branch.setData(0b0);
        this.wc_MemRead.setData(0b0);
        this.wc_MemToReg.setData(0b0);
        this.wc_ALUOp.setData(0b0);
        this.wc_MemWrite.setData(0b0);
        this.wc_ALUSrc.setData(0b0);
        this.wc_RegWrite.setData(0b0);

        int control = wireInput.getRangeData(22,24);
        //add
        if(control == 0b000){
            this.wc_ALUOp.setData(0b0010);
            this.wc_RegWrite.setData(0b1);
        }
        //nand
        if(control == 0b001){
            this.wc_ALUOp.setData(0b1100);
            this.wc_RegWrite.setData(0b1);
        }
        //I-type lw
        if (control == 0b010){
            this.wc_MemRead.setData(0b1);
            this.wc_MemToReg.setData(0b1);
            this.wc_ALUOp.setData(0b0010);
            this.wc_ALUSrc.setData(0b1);
            this.wc_RegWrite.setData(0b1);
        }
        //sw
        if (control == 0b011){
            this.wc_ALUOp.setData(0b0010);
            this.wc_MemWrite.setData(0b1);
            this.wc_ALUSrc.setData(0b1);
            this.wc_RegWrite.setData(0b1);
        }
        // beq
        if(control == 0b100){
            this.wc_Branch.setData(0b1);
            this.wc_ALUOp.setData(0b0110);
        }
        //J-type
        if(control == 0b101){
            this.wc_Jump.setData(0b1);
            this.wc_RegWrite.setData(0b1);
        }

//        switch (control) {
//            case 0b000, 0b001, 0b010, 0b101 -> this.wc_RegWrite.setData(0b1);
////            case 0b010
//            case 0b101 -> this.wc_Jump.setData(0b1);
//        }

//        if (control == 0b101){
//            this.wc_Jump.setData(0b1);
//        }
//
//        if (control == 0b100){
//            this.wc_Branch.setData(0b1);
//        }
//
//        if (control == 0b010){
//            this.wc_MemRead.setData(0b1);
//        }
//
//        if (control == 0b010){
//            this.wc_MemToReg.setData(0b1);
//        }
//
//        if(control == 0b000 | control == 0b001 | control == 0b010 | control == 0b101 ){
//            this.wc_RegWrite.setData(0b1);
//        }
//
//        if (control == 0b101){
//            this.wc_RegWrite.setData(0b1);
//        }

        

    }
}
