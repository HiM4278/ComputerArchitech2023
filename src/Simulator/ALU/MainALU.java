package Simulator.ALU;

import Simulator.Wire;

public class MainALU implements ALU{
    private final Wire w_operand1, w_operand2;
    private final Wire w_result;
    private final Wire w_zero;
    private final Wire w_ALUControl;

    public MainALU(Wire w_operand1, Wire w_operand2, Wire w_result, Wire w_zero, Wire w_ALUControl){
        this.w_operand1 = w_operand1;
        this.w_operand2 = w_operand2;
        this.w_result = w_result;
        this.w_zero = w_zero;
        this.w_ALUControl = w_ALUControl;
        subWire();
        execute();
    }

    private void subWire(){
        this.w_operand1.subscribe(this);
        this.w_operand2.subscribe(this);
        this.w_ALUControl.subscribe(this);
    }

    private int and(int a, int b){
        return a & b;
    }

    private int or(int a, int b){
        return a | b;
    }

    private int add(int a, int b){
        return a + b;
    }

    @Override
    public void execute() {
        int aInvert = w_ALUControl.getData(3);
        int bInvert = w_ALUControl.getData(2);
        int operation = w_ALUControl.getRangeData(0,1);

        int a = w_operand1.getData();
        int b = w_operand2.getData();

        int result = 0;

        if(aInvert == 0b1) a = ~a + 1; // a = -a
        if(bInvert == 0b1) b = ~b + 1; // b = -b

        switch (operation) {
            case 0b00 -> result = and(a, b);
            case 0b01 -> result = or(a, b);
            case 0b10 -> result = add(a, b);
        }

        if(result == 0) w_zero.setData(1);
        else w_zero.setData(0);

        w_result.setData(result);
    }
}
