package Simulator.Register;
import Simulator.Wire;
public class PC implements Register{

    public int data;
    private final Wire wireInput;
    private final Wire wireOutput = new Wire();
    private final Wire wireClock;


    public PC(Wire wireInput, Wire wireClock){
        this.wireInput = wireInput;
        this.wireClock = wireClock;
        this.data = 0;
        subWire();
        execute();
    }

    public Wire w_output() {
        return wireOutput;
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

