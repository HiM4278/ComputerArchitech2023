package Simulator.Register;

import Simulator.Wire;

public class MainRegister implements Register{
    private final int[] data = new int[8];
    private final Wire w_registerSelect;
    private final Wire w_writeData;
    private final Wire wc_regWrite;
    private final Wire w_readData1;
    private final Wire w_readData2;

    public MainRegister(Wire w_registerSelect, Wire w_writeData, Wire wc_regWrite ,Wire w_readData1, Wire w_readData2){
        this.w_registerSelect = w_registerSelect;
        this.w_writeData = w_writeData;
        this.wc_regWrite = wc_regWrite;
        this.w_readData1 = w_readData1;
        this.w_readData2 = w_readData2;
        subWire();
        execute();
    }

    public void subWire(){
        this.w_registerSelect.subscribe(this);
        this.w_writeData.subscribe(this);
        this.wc_regWrite.subscribe(this);
    }

    @Override
    public void execute() {
        int regA = w_registerSelect.getRangeData(19,21);
        int regB = w_registerSelect.getRangeData(16,18);

        if(wc_regWrite.get() == 0b1){
            data[regB] = w_writeData.get();
        }

        w_readData1.set(data[regA]);
        w_readData2.set(data[regB]);
    }
}
