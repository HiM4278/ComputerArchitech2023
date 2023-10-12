package Simulator.Register;

import Simulator.Wire;

public class MainRegister implements Register{
    private final int[] data = new int[8];
    private final Wire w_registerSelect;
    private final Wire w_writeData;
    private final Wire wc_regWrite;
    private final Wire w_readData1 = new Wire();
    private final Wire w_readData2 = new Wire();

    public MainRegister(Wire w_registerSelect, Wire w_writeData, Wire wc_regWrite){
        this.w_registerSelect = w_registerSelect;
        this.w_writeData = w_writeData;
        this.wc_regWrite = wc_regWrite;
        subWire();
        execute();
    }

    public Wire w_regA() {
        return w_readData1;
    }

    public Wire w_regB() {
        return w_readData2;
    }

    public void subWire(){
        this.w_registerSelect.subscribe(this);
    }

    @Override
    public void execute() {
        int regA = w_registerSelect.getRangeData(19,21);
        int regB = w_registerSelect.getRangeData(16,18);

        w_readData1.set(data[regA]);
        w_readData2.set(data[regB]);
    }

    public void write(){
        if(wc_regWrite.get() == 0b1){
            int regB = w_registerSelect.getRangeData(16,18);
            data[regB] = w_writeData.get();
        }
    }

    public void prettyPrint(){
        for(int i = 0; i < 8; i++) {
            System.out.println(data[i]);
        }
        System.out.println("-----------------------------------------");
    }
}
