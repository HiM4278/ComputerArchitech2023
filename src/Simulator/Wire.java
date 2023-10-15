package Simulator;

import java.util.HashSet;
import java.util.Set;

public class Wire{
    private int data;
    private Wire wireRef = null;
    private int lsb_index = 0;
    private int msb_index = 0;
    private boolean need2com = false;
    private final Set<Hardware> h_inputs = new HashSet<>();

    /**
     * Constructor
     * @param wireRef reference wire from output instruction memory
     * @param lsb_index  a bit of data in wire ref. that target to less significant bit of this wire
     * @param msb_index a bit of data in wire ref. that target to most significant bit of this wire
     * @param need2com offsetField 16 bits is 2's complement number so need transform negative to be 32 bits negative integer
     */
    public Wire(Wire wireRef, int lsb_index, int msb_index, boolean need2com){
        this.data = 0;
        this.wireRef = wireRef;
        this.lsb_index = lsb_index;
        this.msb_index = msb_index;
        this.need2com = need2com;
    }

    /**
     * Constructor
     * @param wireRef reference wire from output instruction memory
     * @param lsb_index a bit of data in wire ref. that target to less significant bit of this wire
     * @param msb_index a bit of data in wire ref. that target to most significant bit of this wire
     */
    public Wire(Wire wireRef, int lsb_index, int msb_index){
        this(wireRef, lsb_index, msb_index, false);
    }

    /**
     * Constructor of Wire that assign data at first
     * @param data data that you want to assign
     */
    public Wire(int data){
        this.data = data;
    }

    /**
     * Constructor of default Wire (data in this wire is 0 at first)
     */
    public Wire(){
        this.data = 0;
    }

    public void subscribe(Hardware hardware) {
        h_inputs.add(hardware);
    }

    private void update(){
        for(Hardware hardware : h_inputs) hardware.execute();
    }

    /**
     * calculate a data of the wire that reference from other wire
     */
    private void calculate(){
        if(wireRef != null){
            data = wireRef.getRangeData(lsb_index, msb_index);
            if(need2com) data = signExtend(data);
        }
    }

    /**
     * Set value for a data
     * @param data
     */
    public void set(int data){
        this.data = data;
//        update();
    }

    /**
     * Get data from the wire
     * @return data
     */
    public int get(){
        this.calculate();
        return this.data;
    }

    /**
     * Get index of a data from the wire
     * @param index
     * @return data
     */
    public int get(int index){
        return this.get() >> index & 0b1;
    }

    /**
     * Get range of the data from the wire
     * @param lsb_index
     * @param msb_index
     * @return data
     */
    public int getRangeData(int lsb_index, int msb_index){
        return (this.get() >> lsb_index) & (-1 >>> (32 - (msb_index - lsb_index + 1)));
    }

    /**
     * Convert 16 bits to 32 bits with sign extend
     * @param num 16 bits 2's complement
     * @return 32 bits 2's complement
     */
    private int signExtend(int num) {
        if (((num & (1<<15)) >> 15) == 0b1) {
            num -= (1<<16);
        }
        return(num);
    }
}
