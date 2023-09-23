package Simulator.Control;
import Simulator.Wire;
public class ALUControl implements Control{
    private Wire wireALUOp;
    private Wire WireOutput;

    public ALUControl(Wire wireALUOp){
        this.wireALUOp = wireALUOp;
    }
    @Override
    public void SetControl() {

    }
}
