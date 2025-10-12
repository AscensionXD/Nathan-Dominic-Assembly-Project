import java.io.*;
import java.util.*;

public class pepasm {

    public static void main(String[] args) throws IOException {
        if(args.length != 1) {
            System.out.println("Using java pepasm file.pep");
            return;
        }


        // Create a map for assembly to java conversion
        Map<String,String[]> codes = new HashMap<>();
        codes.put("STBA", new String[]{null,"F1"});
        codes.put("LDBA", new String[]{"D0","F0"});
        codes.put("STWA", new String[]{null,"F5"});
        codes.put("LDWA", new String[]{"D4","F4"});
        codes.put("ANDA", new String[]{"D8","F8"});
        codes.put("ASLA", new String[]{"0A",null});
        codes.put("ASRA", new String[]{"0B",null});
        codes.put("STOP", new String[]{"00",null});
        codes.put("CPBA", new String[]{"D7","F7"});
        codes.put("BRNE", new String[]{null,"39"});


        // Read the file
        Scanner sc = new Scanner(new File(args[0]));
        ArrayList<String> output = new ArrayList<>();

        // Loop through lines of file
        while(sc.hasNextLine()) {
            String line = sc.nextLine().trim();

            if(line.isEmpty() || line.startsWith(";") || line.equalsIgnoreCase(".END"))
                continue;

            // Split lines into parts
            String[] parts = line.split("[ ,]+");
            String instr = parts[0].toUpperCase();

            if(!codes.containsKey(instr)) {
                System.out.println("Unknown instruction: " + instr);
                continue;
            }

            // Determine address mode
            String mode = "";
            if(parts.length > 2) mode = parts[2].toLowerCase();
            String op;

            if(mode.equals("i")) op = codes.get(instr)[0];
            else if(mode.equals("d")) op = codes.get(instr)[1];
            else op = codes.get(instr)[0];

            output.add(op);

            // Convert to hex
            if(parts.length > 1 && parts[1].startsWith("0x")) {
                StringBuilder h = new StringBuilder(parts[1].substring(2).toUpperCase());
                while(h.length() < 4) h.insert(0, "0");
                output.add(h.substring(0,2));
                output.add(h.substring(2));
            }
        }

        sc.close();

        // Print output
        for(int i=0;i<output.size();i++){
            System.out.print(output.get(i));
            if(i<output.size()-1) System.out.print(" ");
        }

        System.out.println();
    }
}
