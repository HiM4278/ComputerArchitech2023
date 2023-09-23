package Simulator.Register;

import Simulator.Wire;

public class MainRegister implements Register{
    private int data[] = new int[8];
    private Wire wireOutput;
    private Wire wireInput;
    private Wire wireControl;

    public MainRegister(int[] data, Wire wireOutput,  Wire wireInput, Wire wireControl){
        this.data = data;
        this.wireOutput = wireOutput;
        this.wireInput = wireInput;
        this.wireControl = wireControl;
    }
    @Override
    public void ReadData() {

    }

    @Override
    public void WriteData() {

    }
}
