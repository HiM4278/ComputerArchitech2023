package Simulator.Memory;

import Simulator.Wire;


public class MainMemory implements Memory{

    private int data[] = new int[65536];
    private Wire wireInput;
    private Wire wireOutput;
    private Wire wireControl;

    public MainMemory(int[] data, Wire wireInput, Wire wireOutput, Wire wireControl){
        this.data = data;
        this.wireInput = wireInput;
        this.wireOutput = wireOutput;
        this.wireControl = wireControl;
    }
    @Override
    public void ReadData() {

    }

    @Override
    public void WriteData() {

    }
}
