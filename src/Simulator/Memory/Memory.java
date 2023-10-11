package Simulator.Memory;

import Simulator.Wire;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Memory{

    private int data[] = new int[65536];
    private Wire w_address;
    private Wire w_writeData;
    private Wire wc_memWrite;
    private Wire w_readData;
    private Wire wc_memRead;

    public Memory(Wire w_address, Wire w_writeData, Wire wc_memWrite,Wire wc_memRead, Wire w_readData){
        this.w_address = w_address;
        this.w_writeData = w_writeData;
        this.wc_memWrite = wc_memWrite;
        this.wc_memRead = wc_memRead;
        this.w_readData = w_readData;
        execute();
    }

    //Read file
    public void importData(String filename){
        Path file = Paths.get(filename);
        Charset charset = StandardCharsets.UTF_8;
        try (BufferedReader reader = Files.newBufferedReader(file, charset)){
            String line = null;
            int count = 0;
            while ((line = reader.readLine()) != null){
                int result = read(line);
                data[count] = result;
                count++;
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

    public void execute() {
        int memAddress = w_address.getData();

        if(wc_memWrite.getData() == 0b1){
            data[memAddress] = w_writeData.getData();
        }

        if(wc_memRead.getData() == 0b1){
            w_readData.setData(data[memAddress]);
        }
    }
}
