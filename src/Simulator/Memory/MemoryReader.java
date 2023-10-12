package Simulator.Memory;

import Simulator.Hardware;
import Simulator.Wire;

public class MemoryReader implements Hardware {

    private final Memory memory;
    private final Wire w_address;
    private final Wire w_writeData;
    private final Wire w_readData = new Wire();
    private final Wire control_MemRead;
    private final Wire control_MemWrite;

    public MemoryReader(Memory memory, Wire w_address, Wire w_writeData, Wire control_MemWrite, Wire control_MemRead){
        this.memory = memory;
        this.w_address = w_address;
        this.w_writeData = w_writeData;
        this.control_MemRead = control_MemRead;
        this.control_MemWrite = control_MemWrite;
        execute();
        subWire();
    }

    public MemoryReader(Memory memory, Wire w_address){
        this(memory, w_address, new Wire(), new Wire(0b0), new Wire(0b1));
    }

    public Wire w_output_data() {
        return w_readData;
    }

    public void execute() {
        int address = w_address.get();
        int writeData = w_writeData.get();

        if(control_MemWrite.get() == 0b1){
            memory.setData(address, writeData);
        }
        if(control_MemRead.get() == 0b1) {
            w_readData.set(memory.getData(address));
        }
    }

    private void subWire(){
        this.w_address.subscribe(this);
        this.w_writeData.subscribe(this);
        this.control_MemRead.subscribe(this);
        this.control_MemWrite.subscribe(this);
    }
}
