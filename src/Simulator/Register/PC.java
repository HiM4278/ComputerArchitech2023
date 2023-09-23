package Simulator.Register;
import Simulator.Wire;
public class PC implements Register{

    private int data[] = new int[1];
    private Wire wireInput;
    private Wire wireOutput;
    private Wire wireControl;


    public PC(int[] data, Wire wireInput, Wire wireOutput, Wire wireControl){
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

    public void increase(){

    }

    public void Jump(int addr){

    }
}

