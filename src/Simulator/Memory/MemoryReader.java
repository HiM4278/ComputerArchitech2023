package Simulator.Memory;

import Simulator.Hardware;
import Simulator.Wire;

/**
 * The "MemoryReader" class represents a component that reads and writes data to a memory unit.
 */
public class MemoryReader implements Hardware {

    private final Memory memory;
    private final Wire w_address;
    private final Wire w_writeData;
    private final Wire w_readData;
    private final Wire control_MemRead;
    private final Wire control_MemWrite;

    /**
     * Constructs a Memory Reader component for reading and writing data to memory.
     *
     * @param memory           The memory unit to read from and write to.
     * @param w_address        [input] Wire representing the memory address.
     * @param w_writeData      [input] Wire representing data to write to memory.
     * @param control_MemWrite [control] Wire for controlling memory writes.
     * @param control_MemRead  [control] Wire for controlling memory reads.
     * @param w_readData       [output] Wire for reading data from memory.
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
     * Constructs a Memory Reader component for reading data from memory.
     * @param memory     The memory unit to read from.
     * @param w_address  [input] Wire representing the memory address.
     * @param w_readData [output] Wire for reading data from memory.
     */
    public MemoryReader(Memory memory, Wire w_address, Wire w_readData){
        this(memory, w_address, new Wire(), new Wire(0b0), new Wire(0b1), w_readData);
    }

    /**
     * Execute the read and write operations based on control signals and input data.
     */
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
