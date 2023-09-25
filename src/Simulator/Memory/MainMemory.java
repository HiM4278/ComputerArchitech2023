package Simulator.Memory;

import Simulator.Wire;


public class MainMemory implements Memory{

    private int data[] = new int[65536];
    private Wire w_address;
    private Wire w_writeData;
    private Wire wc_memWrite;
    private Wire w_readData;
    private Wire wc_memRead;

    public MainMemory(Wire w_address, Wire w_writeData, Wire wc_memWrite,Wire wc_memRead, Wire w_readData){
        this.w_address = w_address;
        this.w_writeData = w_writeData;
        this.wc_memWrite = wc_memWrite;
        this.wc_memRead = wc_memRead;
        this.w_readData = w_readData;
        subWire();
        execute();
    }

    public void subWire(){
        this.w_address.subscribe(this);
        this.w_writeData.subscribe(this);
        this.wc_memWrite.subscribe(this);
        this.wc_memRead.subscribe(this);
    }

    @Override
    public void execute() {
        int memAddress = w_address.getData();

        if(wc_memWrite.getData() == 0b1){
            data[memAddress] = w_writeData.getData();
        }

        if(wc_memRead.getData() == 0b1){
            w_readData.setData(data[memAddress]);
        }
    }
}
