package Simulator.Control;

import Simulator.Wire;

public class MainControl implements Control{

    private Wire wireInput;
    private Wire wireBranch;
    private Wire wireMemRead;
    private Wire wireMemReg;
    private Wire wireALUOp;
    private Wire wireMemWrite;
    private Wire wireALUSrc;
    private Wire wireRegWrite;
    private Wire wireJump;

    public MainControl(Wire wireInput, Wire wireBranch, Wire wireMemRead, Wire wireMemReg, Wire wireALUOp, Wire wireMemWrite, Wire wireALUSrc, Wire wireRegWrite, Wire wireJump){
        this.wireInput = wireInput;
        this.wireBranch = wireBranch;
        this.wireMemRead = wireMemRead;
        this.wireMemReg = wireMemReg;
        this.wireALUOp = wireALUOp;
        this.wireMemWrite = wireMemWrite;
        this.wireALUSrc = wireALUSrc;
        this.wireRegWrite = wireRegWrite;
        this.wireJump = wireRegWrite;
    }
    @Override
    public void SetControl() {

    }
}
