package Simulator.Memory;

import Simulator.Hardware;
import Simulator.Wire;

public class MemoryInstruction implements Hardware {

    private Wire w_pc;
    private Wire w_instruction;
    private Memory memory;

    public MemoryInstruction(Wire w_pc, Wire w_instruction, Memory memory){
        this.w_pc = w_pc;
        this.w_instruction = w_instruction;
        this.memory = memory;
        subWire();
        execute();
    }

    public void execute() {
        int memAddress = w_pc.getData();
        memory.getData(memAddress);
    }

    public void subWire(){
        this.w_pc.subscribe(this);
    }
}
