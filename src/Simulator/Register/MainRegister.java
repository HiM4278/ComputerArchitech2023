package Simulator.Register;

import Simulator.Wire;

public class MainRegister implements Register{
    private final int[] data = new int[8];
    private final Wire w_clock;
    private final Wire w_registerA;
    private final Wire w_registerB;
    private final Wire w_writeReg;
    private final Wire w_writeData;
    private final Wire wc_regWrite;
    private final Wire w_readDataA;
    private final Wire w_readDataB;

    public MainRegister(Wire w_clock, Wire w_registerA, Wire w_registerB, Wire w_writeReg, Wire w_writeData, Wire wc_regWrite, Wire w_readDataA, Wire w_readDataB){
        this.w_clock = w_clock;
        this.w_registerA = w_registerA;
        this.w_registerB = w_registerB;
        this.w_writeReg = w_writeReg;
        this.w_writeData = w_writeData;
        this.wc_regWrite = wc_regWrite;
        this.w_readDataA = w_readDataA;
        this.w_readDataB = w_readDataB;
        subWire();
        execute();
    }

    public int[] toArrayList(){
        return data;
    }

    public void subWire(){
        this.w_registerA.subscribe(this);
        this.w_registerB.subscribe(this);
        this.w_writeReg.subscribe(this);
    }

    @Override
    public void execute() {
        w_readDataA.set(this.data[w_registerA.get()]);
        w_readDataB.set(this.data[w_registerB.get()]);
    }

    public void write(){
        if(wc_regWrite.get() == 0b1){
            if(w_writeReg.get() != 0) data[w_writeReg.get()] = w_writeData.get();
        }
    }

    public void prettyPrint(){
        for(int i = 0; i < 8; i++) {
            System.out.println(data[i]);
        }
        System.out.println("-----------------------------------------");
    }
}
