package Simulator.Logic;

import Simulator.Hardware;
import Simulator.Wire;

public class MUX implements Hardware {
    private final Wire w_select;
    private final Wire[] w_inputs;
    private final Wire w_output;

    public MUX(Wire w_select, Wire[] w_inputs, Wire w_output) {
        this.w_select = w_select;
        this.w_inputs = w_inputs;
        this.w_output = w_output;
        subWire();
        execute();
    }

    @Override
    public void execute() {
        int select = w_select.get();
        w_output.set(w_inputs[select].get());
    }

    private void subWire(){
        for (Wire wire : this.w_inputs) {
            wire.subscribe(this);
        }
        this.w_select.subscribe(this);
    }
}
