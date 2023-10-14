package Simulator.Register;
import Simulator.Wire;
public class PC implements Register{

    public int data; // address in RAM of current instruction
    private final Wire wireInput;
    private final Wire wireOutput;
    private final Wire wc_end;

    /**
     * Constructor of PC
     * @param wireInput [input] when the PC execute the data of this wire will be
     *                  assigned to the PC's data (address in RAM of current instruction)
     * @param wc_end [control] when the PC execute if this wire is 1 the PC will not update
     * @param wireOutput [output] when the PC execute update follow the PC's data
     */
    public PC(Wire wireInput, Wire wc_end, Wire wireOutput){
        this.wireInput = wireInput;
        this.wc_end = wc_end;
        this.wireOutput = wireOutput;
        this.data = 0;
        execute();
    }

    /**
     * Update PC follow the data of wire input
     */
    @Override
    public void execute() {
        data = wireInput.get();
        wireOutput.set(data);
    }

    /**
     * Check PC end or not
     * @return 1 if current PC point halt instruction; otherwise 0
     */
    public boolean isEnd(){
        return wc_end.get() == 0b1;
    }
}

