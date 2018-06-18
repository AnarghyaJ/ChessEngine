/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sillychessengine;
import java.io.BufferedReader;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anarghya
 */
public class ImportPGN {
    
    static File inputFile;
    HashMap<Integer,String> map;
    public void readFile(String file){
    String temp="";
    inputFile=new File(file);
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
            
        }
    
    }
    
}
