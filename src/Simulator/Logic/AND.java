package Simulator.Logic;

import Simulator.Hardware;
import Simulator.Wire;

public class AND implements Hardware {
    private final Wire w_operand1, w_operand2;
    private final Wire w_result;

    public AND(Wire w_operand1, Wire w_operand2, Wire w_result) {
        this.w_operand1 = w_operand1;
        this.w_operand2 = w_operand2;
        this.w_result = w_result;
        this.subWire();
        this.execute();
    }

    private void subWire(){
        this.w_operand1.subscribe(this);
        this.w_operand2.subscribe(this);
    }

    @Override
    public void execute() {
        w_result.set(w_operand1.get() & w_operand2.get());
    }
}
