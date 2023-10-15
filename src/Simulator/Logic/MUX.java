package Simulator.Logic;

import Simulator.Hardware;
import Simulator.Wire;

/**
 * MUX (Multiplexer) is a digital logic component that selects one of multiple input wires
 * and routes it to the output wire based on a control signal.
 */
public class MUX implements Hardware {

    // Input Wire representing the control signal for selecting the input.
    private final Wire w_select;

    // Array of input wire.
    private final Wire[] w_inputs;

    // Output wire
    private final Wire w_output;

    /**
     * Constructs a MUX with the specified control signal, input wires, and output wire.
     *
     * @param w_select [input] Wire representing the control signal for selecting an input.
     * @param w_inputs [input] Array of Wire objects representing the inputs to choose from.
     * @param w_output [output] Wire where the selected input is routed.
     */
    public MUX(Wire w_select, Wire[] w_inputs, Wire w_output) {
        this.w_select = w_select;
        this.w_inputs = w_inputs;
        this.w_output = w_output;
    }

    /**
     * Execute the MUX operation based on the control signal and input wires,
     * and route the selected input to the output wire.
     */
    @Override
    public void execute() {
        int select = w_select.get();
        w_output.set(w_inputs[select].get());
    }
}
