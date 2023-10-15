package Simulator;

import java.util.HashSet;
import java.util.Set;

/**
 * The "Wire" class represents a wire in a digital circuit simulation.
 * A wire can hold data and reference other wires.
 */
public class Wire{
    private int data;
    private Wire wireRef = null;
    private int lsb_index = 0;
    private int msb_index = 0;
    private boolean need2com = false;
    private final Set<Hardware> h_inputs = new HashSet<>();

    /**
     * Constructs a Wire that references another wire,
     * specifying data range and the need for 2's complement conversion.
     * @param wireRef The reference wire from output instruction memory.
     * @param lsb_index The least significant bit index in the reference wire's data.
     * @param msb_index The most significant bit index in the reference wire's data.
     * @param need2com Indicates whether the data needs to be 2's complement converted.
     */
    public Wire(Wire wireRef, int lsb_index, int msb_index, boolean need2com){
        this.data = 0;
        this.wireRef = wireRef;
        this.lsb_index = lsb_index;
        this.msb_index = msb_index;
        this.need2com = need2com;
    }

    /**
     * Constructs a Wire that references another wire, specifying data range without 2's complement conversion.
     * @param wireRef The reference wire from output instruction memory.
     * @param lsb_index The least significant bit index in the reference wire's data.
     * @param msb_index The most significant bit index in the reference wire's data.
     */
    public Wire(Wire wireRef, int lsb_index, int msb_index){
        this(wireRef, lsb_index, msb_index, false);
    }

    /**
     * Constructs a Wire with an initial data value.
     * @param data The data to assign to the wire.
     */
    public Wire(int data){
        this.data = data;
    }

    /**
     * Constructs a default Wire with an initial data value of 0.
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
     * Calculate the data of the wire by referencing other wires.
     */
    private void calculate(){
        if(wireRef != null){
            data = wireRef.getRangeData(lsb_index, msb_index);
            if(need2com) data = signExtend(data);
        }
    }

    /**
     * Set the value for the data stored in the wire.
     * @param data The new data value to set.
     */
    public void set(int data){
        this.data = data;
    }

    /**
     * Get the data from the wire, calculating it if necessary.
     * @return The data stored in the wire.
     */
    public int get(){
        this.calculate();
        return this.data;
    }

    /**
     * Get a specific index of the data stored in the wire.
     * @param index The index of the data bit to retrieve.
     * @return The value of the data bit at the specified index.
     */
    public int get(int index){
        return this.get() >> index & 0b1;
    }

    /**
     * Get a range of data bits from the wire.
     * @param lsb_index The least significant bit index.
     * @param msb_index The most significant bit index.
     * @return The data within the specified range.
     */
    public int getRangeData(int lsb_index, int msb_index){
        return (this.get() >> lsb_index) & (-1 >>> (32 - (msb_index - lsb_index + 1)));
    }

    /**
     * Convert a 16-bit 2's complement number to a 32-bit 2's complement number with sign extension.
     * @param num The 16-bit 2's complement number to be extended.
     * @return The resulting 32-bit 2's complement number.
     */
    private int signExtend(int num) {
        if (((num & (1<<15)) >> 15) == 0b1) {
            num -= (1<<16);
        }
        return(num);
    }
}
