package Simulator.Register;
import Simulator.Hardware;
import Simulator.Wire;

/**
 * The "PC" (Program Counter) class represents a component that keeps track of
 * the address in RAM of the current instruction.
 */
public class PC implements Hardware {

    public int data; // The address in RAM of the current instruction.
    private final Wire wireInput;
    private final Wire wireOutput;
    private final Wire wc_end;

    /**
     * Constructs a Program Counter (PC) component.
     *
     * @param wireInput  [input] When the PC executes, the data of this wire will be assigned
     *                   to the PC's data (the address in RAM of the current instruction).
     * @param wc_end     [control] When the PC executes, if this wire is set to 1, the PC will not update.
     * @param wireOutput [output] When the PC executes, it updates based on the PC's data,
     *                   and this wire carries the updated value.
     */
    public PC(Wire wireInput, Wire wc_end, Wire wireOutput){
        this.wireInput = wireInput;
        this.wc_end = wc_end;
        this.wireOutput = wireOutput;
        this.data = 0;
        execute();
    }

    /**
     * Update the PC based on the data of the wire input.
     */
    @Override
    public void execute() {
        data = wireInput.get();
        wireOutput.set(data);
    }

    /**
     * Check if the PC has reached the end by examining the control signal.
     * @return true (1) if the current PC points to a halt instruction; otherwise, false (0).
     */
    public boolean isEnd(){
        return wc_end.get() == 0b1;
    }
}

