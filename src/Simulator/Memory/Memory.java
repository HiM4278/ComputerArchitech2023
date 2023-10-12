package Simulator.Memory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Memory{

    private final int[] data = new int[65536];
    private int numMemory = 0;

    public Memory(String filename){
        importData(filename);
    }

    //Read file
    private void importData(String filename){
        Path file = Paths.get(filename);
        Charset charset = StandardCharsets.UTF_8;
        try (BufferedReader reader = Files.newBufferedReader(file, charset)){
            String line;
            numMemory = 0;
            while ((line = reader.readLine()) != null){
                int result = read(line);
                data[numMemory] = result;
                numMemory++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int read(String exp) {
        if(!exp.matches("^-?\\d+$")){
            throw new ArithmeticException(exp + " : Invalid expression");
        }else{
            return Integer.parseInt(exp);
        }
    }

    public int getData(int address){
        return data[address];
    }

    public void setData(int address, int data) {
        this.data[address] = data;
    }
}
