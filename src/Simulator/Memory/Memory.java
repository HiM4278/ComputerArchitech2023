package Simulator.Memory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Memory{

    private final int[] mem = new int[65536];
    public int numMemory = 0;

    public Memory(String filename){
        importData(filename);
    }

    /**
     * Import file
     * @param filename
     */
    private void importData(String filename){
        Path file = Paths.get(filename);
        Charset charset = StandardCharsets.UTF_8;
        try (BufferedReader reader = Files.newBufferedReader(file, charset)){
            String line;
            numMemory = 0;
            while ((line = reader.readLine()) != null){
                int result = read(line);
                mem[numMemory] = result;
                numMemory++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read data and parse string to int
     * @param exp each line of a file
     * @return int
     */
    private int read(String exp) {
        if(!exp.matches("^-?\\d+$")){
            throw new ArithmeticException(exp + " : Invalid expression");
        }else{
            return Integer.parseInt(exp);
        }
    }

    /**
     *
     * @return memory
     */
    public int[] toArrayList(){
        return mem;
    }

    /**
     * Get data from memory
     * @param address
     * @return data
     */
    public int getData(int address){
        return mem[address];
    }

    /**
     * Set data
     * @param address the address that want to set in memory
     * @param data the data that need to set in address
     */
    public void setData(int address, int data) {
        this.mem[address] = data;
    }

    public void prettyPrint(){
        for(int i = 0; i < numMemory; i++) {
            System.out.println("memory[" + i + "]=" + mem[i]);
        }
        System.out.println();
    }
}
