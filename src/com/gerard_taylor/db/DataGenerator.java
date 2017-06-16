package com.gerard_taylor.db;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Gerard on 6/11/2017.
 */
public class DataGenerator {

    String firstName = "Gerard";
    String lastName = "Taylor";
    String email = "";
    String address = "";
    String id = "";
    String pw = "";

    public void writeToFile() throws IOException {
        File file = new File("A:\\Graduate School\\Enterprise Computing with Java\\Module 2\\users.txt");
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        for(int i = 0; i < 8; i++){
            String[] info = new String[7];
            for(int j = 0; j < info.length; j++){
                switch(j) {
                    case 0:{
                        info[j] = firstName + "_" + Integer.toString(i);
                        break;
                    }
                    case 1: {
                        info[j] = lastName + "_" + Integer.toString(i);
                        break;
                    }
                    case 2: {
                        info[j] = "";
                        for(int k = 0; k < 9; k++){
                            if(k == 3 || k == 5){
                                info[j] += "-" + i;
                            }else{
                                info[j] += Integer.toString(i);
                            }
                        }
                        System.out.println(info[j]);
                        break;
                    }
                    case 3: {
                        info[j] = info[0] + "_" + info[1] +"@domain.com";
                        break;
                    }
                    case 4: {
                        info[j] = "address_" + i;
                        break;
                    }
                    case 5: {
                        info[j] = Character.toString(info[0].charAt(0)) + info[1].substring(0,2) + "_" + i;
                        break;
                    }
                    case 6: {
                        info[j] = "password" + i;
                        break;
                    }
                }
            }
            for(String s : info){
                writer.print(s + " ");
            }
            writer.println();
        }
        writer.close();
    }

}
