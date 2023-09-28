package Simulator.Register;
import Simulator.Wire;
public class PC implements Register{

    private int data = 0;
    private Wire wireInput;
    private Wire wireOutput;


    public PC(Wire wireInput, Wire wireOutput){
        this.wireInput = wireInput;
        this.wireOutput = wireOutput;
        subWire();
        execute();
    }

    public void subWire(){
        this.wireInput.subscribe(this);
    }
    @Override
    public void execute() {
        data = wireInput.getData();
        wireOutput.setData(data);
    }
}

