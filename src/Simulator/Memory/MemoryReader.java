package Simulator.Memory;

import Simulator.Hardware;
import Simulator.Wire;

public class MemoryReader implements Hardware {

    private final Memory memory;
    private final Wire w_address;
    private final Wire w_writeData;
    private final Wire w_readData;
    private final Wire control_MemRead;
    private final Wire control_MemWrite;

    /**
     * Constructor of MemoryReader
     * @param memory
     * @param w_address [input] Address of memory
     * @param w_writeData [input] Write data from Registers
     * @param control_MemWrite [control] control memory that write data on memory
     * @param control_MemRead [control] control memory that read data on memory
     * @param w_readData [output] Read data in address
     */
    public MemoryReader(
            Memory memory,
            Wire w_address,
            Wire w_writeData,
            Wire control_MemWrite,
            Wire control_MemRead,
            Wire w_readData
    ){
        this.memory = memory;
        this.w_address = w_address;
        this.w_writeData = w_writeData;
        this.control_MemRead = control_MemRead;
        this.control_MemWrite = control_MemWrite;
        this.w_readData = w_readData;
        execute();
    }

    /**
     *Constructor
     * @param memory
     * @param w_address wire address (input)
     * @param w_readData wire read data (output)
     */
    public MemoryReader(Memory memory, Wire w_address, Wire w_readData){
        this(memory, w_address, new Wire(), new Wire(0b0), new Wire(0b1), w_readData);
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
}
