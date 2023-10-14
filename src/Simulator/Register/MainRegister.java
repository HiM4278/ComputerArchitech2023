package Simulator.Register;

import Simulator.Hardware;
import Simulator.Wire;

public class MainRegister implements Hardware {
    private final int[] reg = new int[8];
    private final Wire w_registerA;
    private final Wire w_registerB;
    private final Wire w_writeReg;
    private final Wire w_writeData;
    private final Wire wc_regWrite;
    private final Wire w_readDataA;
    private final Wire w_readDataB;

    /**
     * Constructor of MainRegister
     * @param w_registerA  [input] represent 3 bits of register's pos (regA),
     *                     that used choose 1 of 8 register,
     *                     when register execute assign w_readDataA.data to reg[regA]
     * @param w_registerB  [input] represent 3 bits of register's pos (regB),
     *                     that used choose 1 of 8 register,
     *                     when register execute assign w_readDataB.data to reg[regB]
     * @param w_writeReg   [input] represent 3 bits of register's pos (writeReg),
     *                     that used choose 1 of 8 register,
     *                     when register execute assign  reg[writeReg] to w_writeData
     * @param w_writeData  [input] represent 3 bits of register's pos (WriteData),
     *                     when register execute w_writeData used in register when the control RegWrite is true
     * @param wc_regWrite  [input] control wire that write register
     * @param w_readDataA  [output] send registerA to ALU
     * @param w_readDataB  [output] send registerB to ALU
     */
    public MainRegister(
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
     *
     * @return register
     */
    public int[] toArrayList(){
        return reg;
    }

    /**
     * Set value of data to hardware
     */
    @Override
    public void execute() {
        w_readDataA.set(this.reg[w_registerA.get()]); // set the value for ReadDataA
        w_readDataB.set(this.reg[w_registerB.get()]); // set the value for ReadDataB
    }

    /**
     * Get a data for write on register
     */
    public void write(){
        if(wc_regWrite.get() == 0b1){
            if(w_writeReg.get() != 0) reg[w_writeReg.get()] = w_writeData.get();
        }
    }

    public void prettyPrint(){
        for(int i = 0; i < 8; i++) {
            System.out.println(reg[i]);
        }
        System.out.println("-----------------------------------------");
    }
}
