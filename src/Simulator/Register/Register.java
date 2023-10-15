package Simulator.Register;

import Simulator.Hardware;
import Simulator.Wire;

/**
 * The "Register" class represents a set of eight registers.
 */
public class Register implements Hardware {
    private final int[] reg = new int[8];
    private final Wire w_registerA;
    private final Wire w_registerB;
    private final Wire w_writeReg;
    private final Wire w_writeData;
    private final Wire wc_regWrite;
    private final Wire w_readDataA;
    private final Wire w_readDataB;

    /**
     * Constructs a Register component with the ability to read from and write to eight registers.
     *
     * @param w_registerA  [input] Wire representing a 3-bit position for register A, used to choose one of the eight registers.
     *                     When executed, assigns w_readDataA.data to reg[regA].
     * @param w_registerB  [input] Wire representing a 3-bit position for register B, used to choose one of the eight registers.
     *                     When executed, assigns w_readDataB.data to reg[regB].
     * @param w_writeReg   [input] Wire representing a 3-bit position for writing to a register, choosing one of the eight registers.
     *                     When executed, assigns reg[writeReg] to w_writeData.
     * @param w_writeData  [input] Wire representing a 3-bit position for WriteData. When executed, w_writeData is used in the register when the control RegWrite is true.
     * @param wc_regWrite  [input] Control wire for writing to registers.
     * @param w_readDataA  [output] Wire used to send data from register A to ALU.
     * @param w_readDataB  [output] Wire used to send data from register B to ALU.
     */
    public Register(
            Wire w_registerA,
            Wire w_registerB,
            Wire w_writeReg,
            Wire w_writeData,
            Wire wc_regWrite,
            Wire w_readDataA,
            Wire w_readDataB
    ){
        this.w_registerA = w_registerA;
        this.w_registerB = w_registerB;
        this.w_writeReg = w_writeReg;
        this.w_writeData = w_writeData;
        this.wc_regWrite = wc_regWrite;
        this.w_readDataA = w_readDataA;
        this.w_readDataB = w_readDataB;
        execute();
    }

    /**
     * Retrieve the registers as an array.
     * @return The array representing the registers.
     */
    public int[] toArrayList(){
        return reg;
    }

    /**
     * Execute the read operation and set the data to output wires.
     */
    @Override
    public void execute() {
        w_readDataA.set(this.reg[w_registerA.get()]); // set the value for ReadDataA
        w_readDataB.set(this.reg[w_registerB.get()]); // set the value for ReadDataB
    }

    /**
     * Write data to the registers if the control wire RegWrite is set.
     */
    public void write(){
        if(wc_regWrite.get() == 0b1){
            if(w_writeReg.get() != 0) reg[w_writeReg.get()] = w_writeData.get();
        }
    }

    /**
     * Print the contents of the registers for debugging purposes.
     */
    public void prettyPrint(){
        for(int i = 0; i < 8; i++) {
            System.out.println(reg[i]);
        }
        System.out.println("-----------------------------------------");
    }
}
