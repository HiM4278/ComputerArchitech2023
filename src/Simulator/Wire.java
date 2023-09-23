package Simulator;

public class Wire {
    private int data;

    public Wire(){
        this.data = 0;
    }

    public Wire(int data){
        this.data = data;
    }

    public void setData(int data){
        this.data = data;
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
