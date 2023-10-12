package Simulator;

import java.util.HashSet;
import java.util.Set;

public class Wire implements Hardware{
    private int data;
    private Wire wireRef = null;
    private int lsb_index = 0;
    private int msb_index = 0;
    private boolean need2com = false;
    private final Set<Hardware> h_inputs = new HashSet<>();

    public Wire(Wire wireRef, int lsb_index, int msb_index, boolean need2com){
        this.data = 0;
        this.wireRef = wireRef;
        this.lsb_index = lsb_index;
        this.msb_index = msb_index;
        this.need2com = need2com;
        subWire();
        execute();
    }

    public Wire(Wire wireRef, int lsb_index, int msb_index){
        this(wireRef, lsb_index, msb_index, false);
    }

    public Wire(int data){
        this.data = data;
    }

    public Wire(){
        this.data = 0;
    }

    public void subscribe(Hardware hardware) {
        h_inputs.add(hardware);
    }

    private void update(){
        for(Hardware hardware : h_inputs) hardware.execute();
    }

    private void calculate(){
        if(wireRef != null){
            data = wireRef.getRangeData(lsb_index, msb_index);
            if(need2com) data = convertNum(data);
        }
    }

    public void set(int data){
        this.data = data;
//        update();
    }

    public int get(){
        return this.data;
    }

    public int get(int index){
        return this.data >> index & 0b1;
    }

    public int getRangeData(int lsb_index, int msb_index){
        return (this.data >> lsb_index) & (-1 >>> (32 - (msb_index - lsb_index + 1)));
    }

    private int convertNum(int num) {
        if (((num & (1<<15)) >> 15) == 0b1) {
            num -= (1<<16);
        }
        return(num);
    }

    private void subWire(){
        this.wireRef.subscribe(this);
    }

    @Override
    public void execute() {
        this.calculate();
    }
}
