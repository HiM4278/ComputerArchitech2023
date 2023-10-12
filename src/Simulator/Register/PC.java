package Simulator.Register;
import Simulator.Wire;
public class PC implements Register{

    private int data;
    private final Wire wireInput;
    private final Wire wireOutput;
    private final Wire wireClock;


    public PC(Wire wireInput, Wire wireOutput, Wire wireClock){
        this.wireInput = wireInput;
        this.wireOutput = wireOutput;
        this.wireClock = wireClock;
        this.data = 0;
        subWire();
        execute();
    }

    public void subWire(){
        this.wireClock.subscribe(this);
    }
    @Override
    public void execute() {
        data = wireInput.get();
        wireOutput.set(data);
    }
}

