package eip.smart.server.benchmark;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import eip.smart.cscommons.model.modeling.Area;
import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.server.model.modeling.ModelingLogic;
import eip.smart.server.model.modeling.file.FileModelingSaver;
import eip.smart.server.model.modeling.file.JavaFileModelingSaver;
import eip.smart.server.util.exception.ModelingNotFoundException;
import eip.smart.server.util.exception.ModelingObsoleteException;

/**
 * Created by vincent on 2/28/15.
 */
public class SerializationBenchmark {
	public static final String[]	SIZE_UNITS	= { "o", "Ko", "Mo", "Go", "To" };

	/**
	 * This is the entry point of the benchmark module,
	 * it starts a certain number of tests and print their result in the java log
	 *
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) {
		SerializationBenchmark benchmark = new SerializationBenchmark();

		ModelingLogic smallModeling = new ModelingLogic(); // 1 500 pts
		ModelingLogic mediumModeling = new ModelingLogic(); // 30 000 pts
		ModelingLogic largeModeling = new ModelingLogic(); // 1 500 000 pts

		smallModeling.setName("small_modeling_test");
		mediumModeling.setName("medium_modeling_test");
		largeModeling.setName("large_modeling_test");

		JavaFileModelingSaver defaultManager = new JavaFileModelingSaver();
		// DefaultZippedFileModelingManager defaultZippedManager = new DefaultZippedFileModelingManager();
		// JacksonFileModelingManager jacksonManager = new JacksonFileModelingManager();
		// JacksonZippedFileModelingManager jacksonZippedManager = new JacksonZippedFileModelingManager();

		List<Area> smallAreas = new ArrayList<>();
		List<Area> mediumAreas = new ArrayList<>();
		List<Area> largeAreas = new ArrayList<>();

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

		// DEFAULT
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

		// DEFAULT ZIP
		// benchmark.setBenchmarkedManager(defaultZippedManager);
		benchmark.setModeling(smallModeling);
		benchmark.startBenchmark(System.out);
		benchmark.clean();

		benchmark.setModeling(mediumModeling);
		benchmark.startBenchmark(System.out);
		benchmark.clean();

		benchmark.setModeling(largeModeling);
		benchmark.startBenchmark(System.out);
		benchmark.clean();

		// JACKSON
		// benchmark.setBenchmarkedManager(jacksonManager);
		benchmark.setModeling(smallModeling);
		benchmark.startBenchmark(System.out);
		benchmark.clean();

		benchmark.setModeling(mediumModeling);
		benchmark.startBenchmark(System.out);
		benchmark.clean();

		benchmark.setModeling(largeModeling);
		benchmark.startBenchmark(System.out);
		benchmark.clean();

		// JACKSON ZIP
		// benchmark.setBenchmarkedManager(jacksonZippedManager);
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

	/**
	 * Print a string formatted size (eg : "12 Go")
	 *
	 * @param size
	 *            byte count size
	 * @return formatted size
	 */
	public static String smartsize(long size) {
		int i;
		for (i = 0; i < 5; i++) {
			if (size < 1024)
				return size + " " + SerializationBenchmark.SIZE_UNITS[i];
			size /= 1024;
		}
		return size + " " + SerializationBenchmark.SIZE_UNITS[i];
	}

	FileModelingSaver	benchmarkedManager;

	Modeling			modeling;

	Modeling			result;

	/**
	 * Deletes the current modeling from the benchmark manager
	 */
	public void clean() {
		this.result = null;
		try {
			this.benchmarkedManager.delete(this.modeling.getName());
		} catch (ModelingNotFoundException e) {
			e.printStackTrace();
		}
	}

	public FileModelingSaver getBenchmarkedManager() {
		return this.benchmarkedManager;
	}

	public Modeling getModeling() {
		return this.modeling;
	}

	public void setBenchmarkedManager(FileModelingSaver benchmarkedManager) {
		this.benchmarkedManager = benchmarkedManager;
	}

	public void setModeling(Modeling modeling) {
		this.modeling = modeling;
	}

	/**
	 * Starts a new benchmark of the different serialization methods
	 *
	 * @param s
	 *            Le flux sur lequel écrire les résultats.
	 */
	public void startBenchmark(PrintStream s) {
		long elapsed, elapsedDecimals, size;
		File file;

		s.println("\n\nNow testing " + this.benchmarkedManager.getClass().getSimpleName() + "\n");

		// Write test
		s.println("Now starting write benchmark...");
		elapsed = System.nanoTime(); // in nanoseconds
		this.startWriteBenchmark();
		elapsed = System.nanoTime() - elapsed;

		elapsedDecimals = (elapsed / 1000000) % 1000; // in milliseconds
		s.println("Elapsed time : " + elapsed / 1000000000 /* in seconds */+ " s " + elapsedDecimals);
		file = new File(FileModelingSaver.getDir(), this.modeling.getName() + FileModelingSaver.MODELING_EXTENSION);
		size = file.length();
		s.println("File size : " + size + " bytes (" + SerializationBenchmark.smartsize(size) + ")");

		// Read test
		s.println("Now starting read benchmark...");
		elapsed = System.nanoTime(); // in nanoseconds
		this.startReadBenchmark();

		elapsed = System.nanoTime() - elapsed;
		elapsedDecimals = (elapsed / 1000000) % 1000; // in milliseconds
		s.println("Elapsed time : " + elapsed / 1000000000 /* in seconds */+ " s " + elapsedDecimals);
		// TODO compare result

	}

	/**
	 * Starts the benchmarking of reading methods
	 */
	private void startReadBenchmark() {
		try {
			this.result = this.benchmarkedManager.load(this.modeling.getName());
		} catch (ModelingNotFoundException | ModelingObsoleteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starts the benchmarking of writing methods
	 */
	private void startWriteBenchmark() {
		this.benchmarkedManager.save(this.modeling);
	}
}
