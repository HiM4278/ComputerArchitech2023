package Simulator;

import java.util.HashSet;
import java.util.Set;

public class Wire {
    private int data;
    private final Set<Hardware> h_inputs = new HashSet<>();

    public Wire(){
        this.data = 0;
    }

    public Wire(int data){
        this.data = data;
    }

    public void subscribe(Hardware hardware) {
        h_inputs.add(hardware);
    }

    private void update(){
        for(Hardware hardware : h_inputs) hardware.execute();
    }

    public void setData(int data){
        this.data = data;
        update();
    }

    public int getData(){
        return this.data;
    }

    public int getData(int index){
        return this.data >> index & 0b1;
    }

    public int getRangeData(int lsb_index, int msb_index){
        return (this.data >> lsb_index) & (~(0b1 << msb_index - lsb_index + 1));
    }
}
