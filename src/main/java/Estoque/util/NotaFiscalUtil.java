<<<<<<< HEAD
package Estoque.util;

import java.io.*;

public class NotaFiscalUtil {

    private static final String FILE_PATH = "nf_counter.txt";

    public static int getProximaNota(){
        try{
            File file = new File(FILE_PATH);
            if (!file.exists()){

                try(FileWriter writer = new FileWriter(file)){
                    writer.write("1");
                }
                return 1;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))){
                int atual = Integer.parseInt(reader.readLine());
                return atual;
                }
        }catch (IOException | NumberFormatException e){
            e.printStackTrace();
            return 1;
        }
    }

    public static void IncrementarNota(){
        int atual = getProximaNota();
        try (FileWriter writer = new FileWriter(FILE_PATH, false)){
            writer.write(String.valueOf(atual +1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
=======
package Estoque.util;

import java.io.*;

public class NotaFiscalUtil {

    private static final String FILE_PATH = "nf_counter.txt";

    public static int getProximaNota(){
        try{
            File file = new File(FILE_PATH);
            if (!file.exists()){

                try(FileWriter writer = new FileWriter(file)){
                    writer.write("1");
                }
                return 1;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))){
                int atual = Integer.parseInt(reader.readLine());
                return atual;
                }
        }catch (IOException | NumberFormatException e){
            e.printStackTrace();
            return 1;
        }
    }

    public static void IncrementarNota(){
        int atual = getProximaNota();
        try (FileWriter writer = new FileWriter(FILE_PATH, false)){
            writer.write(String.valueOf(atual +1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
>>>>>>> 8938a1b0403e078427565c1ae42d2318d6681d57
