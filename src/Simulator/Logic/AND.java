package Simulator.Logic;

import Simulator.Hardware;
import Simulator.Wire;

/**
 * AND is a class representing a logical AND gate in a simulated hardware component.
 * It takes two input wires and produces a result wire, which represents the logical AND of the inputs.
 */
public class AND implements Hardware {

    // Input wires representing the two operands.
    private final Wire w_operand1, w_operand2;

    // Output wire representing the result of the AND operation.
    private final Wire w_result;

    /**
     * Constructs an AND gate with the specified input and result wires.
     *
     * @param w_operand1 [input] Wire representing the first operand.
     * @param w_operand2 [input] Wire representing the second operand.
     * @param w_result [output] Wire representing the result of the AND operation.
     */
    public AND(Wire w_operand1, Wire w_operand2, Wire w_result) {
        this.w_operand1 = w_operand1;
        this.w_operand2 = w_operand2;
        this.w_result = w_result;
    }

    /**
     * Perform the logical AND operation on the input wires and set into the result wire.
     */
    @Override
    public void execute() {
        w_result.set(w_operand1.get() & w_operand2.get());
    }
}
