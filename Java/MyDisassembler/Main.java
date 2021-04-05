
import elf.*;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Input file name not found");
            return;
        }
        try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(args[0]));
             OutputStreamWriter output = (args.length > 1) ? new OutputStreamWriter(new FileOutputStream(args[1])) : new OutputStreamWriter(System.out)) {
            MyDisassembler disassembler = new MyDisassembler(new ElfFile(stream));
            disassembler.disassembleAll(output);
        } catch (FileNotFoundException e) {
            System.err.println("File is not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
