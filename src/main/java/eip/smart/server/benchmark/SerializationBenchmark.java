package eip.smart.server.benchmark;

import eip.smart.model.Area;
import eip.smart.model.Modeling;
import eip.smart.server.modeling.*;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Created by ct on 2/28/15.
 */
public class SerializationBenchmark {
    FileModelingManager benchmarkedManager;
    Modeling            modeling;
    Modeling            result;

    public void startBenchmark(PrintStream s) {
        long elapsed, elapsedDecimals, size;
        File file;

        s.println("\n\nNow testing " + benchmarkedManager.getClass().getSimpleName() + "\n");


        //Write test
        s.println("Now starting write benchmark...");
        elapsed = System.nanoTime(); // in nanoseconds
        startWriteBenchmark();
        elapsed = System.nanoTime() - elapsed;

        elapsedDecimals = (elapsed / 1000000) % 1000; // in milliseconds
        s.println("Elapsed time : " +
                elapsed / 1000000000 /* in seconds */ + " s " + elapsedDecimals);
        file = new File(FileModelingManager.DEFAULT_DIR, modeling.getName() + FileModelingManager.EXTENSION);
        size = file.length();
        s.println("File size : " + size + " bytes (" + smartsize(size) + ")");

        //Read test
        s.println("Now starting read benchmark...");
        elapsed = System.nanoTime(); // in nanoseconds
        startReadBenchmark();

        elapsed = System.nanoTime() - elapsed;
        elapsedDecimals = (elapsed / 1000000) % 1000; // in milliseconds
        s.println("Elapsed time : " +
                elapsed / 1000000000 /* in seconds */ + " s " + elapsedDecimals);
        //TODO compare result


    }

    public static final String[] SIZE_UNITS = {
            "o", "Ko", "Mo", "Go", "To"
    };

    public static String smartsize(long size) {
        int i;
        for (i = 0; i < 5; i++)
            if (size < 1024)
                return size + " " + SIZE_UNITS[i];
            else
                size /= 1024;
        return size + " " + SIZE_UNITS[i];
    }

    public void clean() {
        result = null;
        benchmarkedManager.delete(modeling.getName());
    }

    private void startWriteBenchmark() {
        benchmarkedManager.save(modeling);
    }

    private void startReadBenchmark() {
        result = benchmarkedManager.load(modeling.getName());
    }

    public static void startBenchmark() {

    }

    public static void main(String [] args) {
        SerializationBenchmark benchmark = new SerializationBenchmark();

        Modeling smallModeling = new Modeling();    // 1 500 pts
        Modeling mediumModeling = new Modeling();   // 30 000 pts
        Modeling largeModeling = new Modeling();     // 1 500 000 pts

        smallModeling.setName("small_modeling_test");
        mediumModeling.setName("medium_modeling_test");
        largeModeling.setName("large_modeling_test");

        DefaultFileModelingManager defaultManager = new DefaultFileModelingManager();
        DefaultZippedFileModelingManager defaultZippedManager = new DefaultZippedFileModelingManager();
        JacksonFileModelingManager jacksonManager = new JacksonFileModelingManager();
        JacksonZippedFileModelingManager jacksonZippedManager = new JacksonZippedFileModelingManager();

        ArrayList<Area> smallAreas = new ArrayList<Area>();
        ArrayList<Area> mediumAreas = new ArrayList<Area>();
        ArrayList<Area> largeAreas = new ArrayList<Area>();

        for (int i = 0; i < 3; ++i) {
            smallAreas.add(new Area());
            mediumAreas.add(new Area());
            largeAreas.add(new Area());
        }

        System.out.println("Generating points...");
        for (int i = 0; i < 3; ++i) {
            System.out.print("Pass " + (i + 1) + "/3.");
            smallAreas.get(i).generateTestPoints(500);
            System.out.print(".");
            mediumAreas.get(i).generateTestPoints(10000);
            System.out.print(".");
            largeAreas.get(i).generateTestPoints(500000);
            System.out.println(" DONE");
        }
        smallModeling.setAreas(smallAreas);
        mediumModeling.setAreas(mediumAreas);
        largeModeling.setAreas(largeAreas);

        //DEFAULT
        benchmark.setBenchmarkedManager(defaultManager);
        benchmark.setModeling(smallModeling);
        benchmark.startBenchmark(System.out);
        benchmark.clean();

        benchmark.setModeling(mediumModeling);
        benchmark.startBenchmark(System.out);
        benchmark.clean();

        benchmark.setModeling(largeModeling);
        benchmark.startBenchmark(System.out);
        benchmark.clean();

        //DEFAULT ZIP
        benchmark.setBenchmarkedManager(defaultZippedManager);
        benchmark.setModeling(smallModeling);
        benchmark.startBenchmark(System.out);
        benchmark.clean();

        benchmark.setModeling(mediumModeling);
        benchmark.startBenchmark(System.out);
        benchmark.clean();

        benchmark.setModeling(largeModeling);
        benchmark.startBenchmark(System.out);
        benchmark.clean();

        //JACKSON
        benchmark.setBenchmarkedManager(jacksonManager);
        benchmark.setModeling(smallModeling);
        benchmark.startBenchmark(System.out);
        benchmark.clean();

        benchmark.setModeling(mediumModeling);
        benchmark.startBenchmark(System.out);
        benchmark.clean();

        benchmark.setModeling(largeModeling);
        benchmark.startBenchmark(System.out);
        benchmark.clean();

        //JACKSON ZIP
        benchmark.setBenchmarkedManager(jacksonZippedManager);
        benchmark.setModeling(smallModeling);
        benchmark.startBenchmark(System.out);
        benchmark.clean();

        benchmark.setModeling(mediumModeling);
        benchmark.startBenchmark(System.out);
        benchmark.clean();

        benchmark.setModeling(largeModeling);
        benchmark.startBenchmark(System.out);
        benchmark.clean();

    }

    public FileModelingManager getBenchmarkedManager() {
        return benchmarkedManager;
    }

    public void setBenchmarkedManager(FileModelingManager benchmarkedManager) {
        this.benchmarkedManager = benchmarkedManager;
    }

    public Modeling getModeling() {
        return modeling;
    }

    public void setModeling(Modeling modeling) {
        this.modeling = modeling;
    }
}
