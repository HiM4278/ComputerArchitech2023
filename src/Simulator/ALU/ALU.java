package Simulator.ALU;

import Simulator.Hardware;
import Simulator.Wire;

public class ALU implements Hardware {
    private final Wire w_operand1, w_operand2;
    private final Wire w_result;
    private final Wire w_zero;
    private final Wire wc_ALUControl;

    /**
     * Constructs an ALU (Arithmetic Logic Unit).
     * @param w_operand1 [input] Operand1 data
     * @param w_operand2 [input] Operand2 data
     * @param wc_ALUControl [control] Determines the operation to perform on operand1 and operand2.
     * @param w_result [output] Result of ALU operation on operand1 and operand2.
     * @param w_zero [output] 1 if operand1 == operand2 and ALUControl == '0110' (subtraction).
     */
    public ALU(
            Wire w_operand1,
            Wire w_operand2,
            Wire wc_ALUControl,
            Wire w_result,
            Wire w_zero
    ){
        this.w_operand1 = w_operand1;
        this.w_operand2 = w_operand2;
        this.wc_ALUControl = wc_ALUControl;
        this.w_result = w_result;
        this.w_zero = w_zero;
    }

    private int nand(int a, int b){
        return ~(a & b);
    }

    private int add(int a, int b){
        return a + b;
    }

    /**
     * Executes the ALU (Arithmetic Logic Unit) operation based on the control signals and input operands.
     * Updates the result and zero flag wires based on the specified operation.
     */
    @Override
    public void execute() {
        int aInvert = wc_ALUControl.get(3);
        int bInvert = wc_ALUControl.get(2);
        int operation = wc_ALUControl.getRangeData(0,1);

        // Retrieve operand values from w_operand1 and w_operand2
        int a = w_operand1.get();
        int b = w_operand2.get();

        // Perform inversion of operands if indicated by control signals
        if(aInvert == 0b1) a = ~a + 1; // a = -a
        if(bInvert == 0b1) b = ~b + 1; // b = -b

        // Perform the specified ALU operation based on the operation code
        int result = 0;
        switch (operation) {
            case 0b00 -> result = nand(a, b);
            case 0b10 -> result = add(a, b);
        }

        // Update the zero wire (w_zero)
        if(result == 0) w_zero.set(1);
        else w_zero.set(0);

        // Update the result wire (w_result) with the computed result value
        w_result.set(result);
    }
}
