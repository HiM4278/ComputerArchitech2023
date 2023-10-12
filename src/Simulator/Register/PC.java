package Simulator.Register;
import Simulator.Wire;
public class PC implements Register{

    public int data;
    private final Wire wireInput;
    private final Wire wireOutput;
    private final Wire wireClock;
    private final Wire wc_end;


    public PC(
            Wire wireClock,
            Wire wireInput,
            Wire wc_end, Wire wireOutput
    ){
        this.wireClock = wireClock;
        this.wireInput = wireInput;
        this.wc_end = wc_end;
        this.wireOutput = wireOutput;
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

    public boolean isEnd(){
        return wc_end.get() == 0b1;
    }
}

