package Simulator.ALU;

import Simulator.Hardware;
import Simulator.Wire;

public class ADD implements Hardware {
    private final Wire w_operand1, w_operand2;
    private final Wire w_result;

    /**
     * Constructs an Adder with the specified input and result wires.
     * @param w_operand1 [input] Wire representing the first operand.
     * @param w_operand2 [input] Wire representing the second operand.
     * @param w_result   [output] Wire representing the result of the operand1 add with operand2.
     */
    public ADD(Wire w_operand1, Wire w_operand2, Wire w_result) {
        this.w_operand1 = w_operand1;
        this.w_operand2 = w_operand2;
        this.w_result = w_result;
    }

    /**
     * Perform the ADD operation on the input wires and set into the result wire.
     */
    @Override
    public void execute() {
        w_result.set(w_operand1.get() + w_operand2.get());
    }
}