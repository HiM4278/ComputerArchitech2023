import Assember.ReadInstruction;
import Simulator.Simulator;

public class Main {

    private static void printBIN(int n){
        System.out.print(n);
        System.out.println(" : 0b" + String.format("%32s", Integer.toBinaryString(n)).replaceAll(" ", "0"));
    }

    public static void main(String[] args) {
        Simulator simulator = new Simulator();
        simulator.run("input.txt");
    }
}
