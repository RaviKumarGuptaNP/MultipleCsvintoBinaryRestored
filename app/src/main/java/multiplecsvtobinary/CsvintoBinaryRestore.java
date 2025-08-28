import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CsvintoBinaryRestore {

    public static void main(String[] args) {

        String[] csvPaths = {
                "E:/New folder/New/one.csv",
                "E:/New folder/New/two.csv",
                "E:/New folder/New/three.csv"
        };

        String outputFolder = "E:/New folder/Restored"; // used for restored data in specific folder

        Map<String, byte[]> csvBytesByPath = new LinkedHashMap<>(); // map used for store binary data in memory

        for (String pathStr : csvPaths) {
            Path csvPath = Paths.get(pathStr);

            if (Files.exists(csvPath) && csvPath.toString().toLowerCase().endsWith(".csv")) {
                try {
                    byte[] data = Files.readAllBytes(csvPath); // Convert into binary
                    csvBytesByPath.put(csvPath.getFileName().toString(), data);
                    System.out.printf("Loaded: %s (%,d bytes)%n", csvPath.getFileName(), data.length);
                } catch (IOException e) {
                    System.err.printf("Failed to read %s: %s%n", csvPath, e.getMessage());
                }
            } else {
                System.err.println("Invalid path or not a CSV: " + pathStr);
            }
        }

        System.out.println("\n=== In-Memory Store Summary ===");
        long totalBytes = 0;
        for (Map.Entry<String, byte[]> entry : csvBytesByPath.entrySet()) {
            totalBytes += entry.getValue().length;
            System.out.printf("- %s -> %,d bytes%n", entry.getKey(), entry.getValue().length);
        }
        System.out.printf("Total files: %d, Total bytes: %,d%n", csvBytesByPath.size(), totalBytes);

        System.out.println("\n===========Restore Csvfile=======");
        Path outDir = Paths.get(outputFolder);

        for (Map.Entry<String, byte[]> entry : csvBytesByPath.entrySet()) {
            String fileName = entry.getKey();
            byte[] data = entry.getValue();
            Path outFile = outDir.resolve(fileName);

            try {
                Files.write(outFile, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                System.out.printf("Restored: %s (%d bytes)%n", outFile.toAbsolutePath(), data.length);
            } catch (IOException e) {
                System.err.printf("Failed to restore %s: %s%n", fileName, e.getMessage());
            }
        }

    }
}
