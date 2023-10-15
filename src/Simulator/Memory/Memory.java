package Simulator.Memory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The "Memory" class represents a memory unit in a simulated computer architecture. It allows for the storage
 * and retrieval of 32 bits data at specific memory addresses.
 */
public class Memory{

    // An array to store memory contents.
    private final int[] mem = new int[65536];

    // The number of memory locations used.
    public int numMemory = 0;

    private void resetMem(){
        for(int i:mem) {i = 0;}
    }

    /**
     * Import data from a file to initialize memory contents.
     * @param filename The name of the file containing memory data.
     */
    public void importData(String filename){
        this.resetMem();
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
     * Read data from a string and parse it as an integer.
     * @param exp Each line from a file.
     * @return The integer representation of the data.
     */
    private int read(String exp) {
        if(!exp.matches("^-?\\d+$")){
            throw new ArithmeticException(exp + " : Invalid expression");
        }else{
            return Integer.parseInt(exp);
        }
    }

    /**
     * Retrieve the memory contents as an array.
     * @return The array representing memory contents.
     */
    public int[] toArrayList(){
        return mem;
    }

    /**
     * Get data from a specific memory address.
     * @param address The memory address from which to retrieve data.
     * @return The data stored at the specified address.
     */
    public int getData(int address){
        return mem[address];
    }

    /**
     * Set data at a specific memory address.
     * @param address The memory address where data will be set.
     * @param data    The data to store at the specified address.
     */
    public void setData(int address, int data) {
        this.mem[address] = data;
    }

    /**
     * Print the contents of memory.
     */
    public void prettyPrint(){
        for(int i = 0; i < numMemory; i++) {
            System.out.println("memory[" + i + "]=" + mem[i]);
        }
        System.out.println();
    }
}
