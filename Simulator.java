
import java.util.Scanner;

/**
 *
 * @author Stefan Larsen
 */
public class Simulator {

	private City[] currentPath;
	private double currentPathCost;
	private double T = 0.0;
	private int Nstop = 0;
	private int Ndec = 0;
	private Boolean verbose;

	public static void main(String[] args) {
		//init program and collect parameters from user inputs
		Simulator sim = new Simulator();
		System.out.println("Initial path: " + sim.toString());

		//run simulation
		sim.runAnnealingSimulation();

		// print the result of the simulation
		System.out.println("Best Path: " + sim.toString());
	}

	public Simulator() {
		init();
	}

	/**
	 * generate initial path and collect parameters from user inputs
	 */
	private void init() {
		// generate starting path and calculate the cost
		currentPath = CityGenerator.generate();
		currentPathCost = cost(currentPath);

		// Ask user for input
		try (Scanner scanner = new Scanner(System.in)) { //avoiding resource leak

			System.out.print("Initial temperature: ");
			while (!((T = ((scanner.hasNextDouble()) ? scanner.nextDouble() : 0.0)) > 0)) {
				System.out.print("That's not a positive number! \n"
						+ "Initial temperature: ");
				scanner.nextLine();
			}

			System.out.print("Iterations until termination: ");
			while (!((Nstop = ((scanner.hasNextInt()) ? scanner.nextInt() : 0)) > 0)) {
				System.out.print("That's not a positive number! \n"
						+ "Iterations until termination: ");
				scanner.nextLine();
			}

			System.out.print("Iterations until temperature decreases: ");
			while (!((Ndec = ((scanner.hasNextInt()) ? scanner.nextInt() : 0)) > 0)) {
				System.out.print("That's not a positive number! \n"
						+ "Iterations until temperature decreases: ");
				scanner.nextLine();
			}

			do {
				System.out.print("Verbose mode (yes/no)? ");
				switch (scanner.next().toLowerCase()) {
					case "y":
					case "yes": {
						verbose = true;
						continue;
					}
					case "n":
					case "no": {
						verbose = false;
						continue;
					}
					default: {
						System.out.println("Please enter yes or no.");
					}
				}
			} while (verbose == null);

		} catch (Exception e) {
			System.out.println("fatal error: " + e.toString());
			System.exit(1);
		}

		System.out.println(""); //print empty line
	}

	/**
	 * run sim
	 */
	public void runAnnealingSimulation() {
		int i_stop = 0;			// iteration stop counter		
		int i_dec = 0;			// iteration temp decrease counter		

		City[] newPath;			// new path returned by one of the mutation methods
		double newPathCost;		// cost of the new path
		double d;				// the difference between the current and new path

		// run simulated annealing
		while (i_stop < Nstop) {
			// increase stop counter
			i_stop++;

			//flip coin to decide mutation type
			if (RandomUtils.coinFlip()) {
				// run reversion mution
				newPath = reversion(currentPath);
			} else {
				// run transport mutation
				newPath = transport(currentPath);
			}

			// decide if path should be updated
			newPathCost = cost(newPath);
			d = newPathCost - currentPathCost;
			if (d < 0 || Math.exp((-d) / T) > RandomUtils.getRandom()) {
				currentPath = newPath;
				currentPathCost = newPathCost;
				i_stop = 0;		// reset stop counter
				i_dec++;		// increase decrease temp counter
				if (verbose) {
					System.out.println("New path: " + path2string(currentPath));
				}
			}

			// decide if temperature should be updated
			if (i_dec >= Ndec) {
				T = T * 0.9;
				i_dec = 0;
				if (verbose) {
					System.out.println("Temperature adjusted to: " + T);
				}
			}
		}
	}

	/**
	 * The segment between two randomly chosen cities in the current path is reversed.
	 *
	 * @param path
	 * @return City[]
	 */
	public static City[] reversion(City[] path) {
		City[] mutation = path.clone();
		int len = path.length;
		//pick two cities at random, which can't be the same or result in an equivalent path
		int city1 = 0, city2 = 0;
		while (city1 == city2 || (Math.floorMod((city2 - city1), len) >= (len - 2))) {
			city1 = RandomUtils.getRandomValue(len);
			city2 = RandomUtils.getRandomValue(len);
		}

		// reverse segment between city1 and city2
		for (int i = 0; i < (Math.floorMod((city2 - city1), len) + 1); i++) {
			mutation[(city1 + i) % len] = path[Math.floorMod((city2 - i), len)];
		}

		return mutation;
	}

	/**
	 * A segment between two randomly chosen cities in the current path is moved
	 * to another place in the path.
	 *
	 * @param path
	 * @return City[]
	 */
	public static City[] transport(City[] path) {
		int len = path.length;
		City[] mutation = new City[len];

		//pick segment of cities to transport and their new position
		int segmentStart = RandomUtils.getRandomValue(len);
		int segmentLen = RandomUtils.getRandomValue(len - 2) + 1;
		int newPosition = RandomUtils.getRandomValue(len - segmentLen - 1) + 1;

		// move the segment of cities to the new location in the path
		for (int i = 0; i < len; i++) {
			if (i < segmentLen) {
				mutation[i] = path[(segmentStart + i) % len];
			} else if (i < ((segmentLen + (len - segmentLen - newPosition)))) {
				mutation[i] = path[(segmentStart + newPosition + i) % len];
			} else {
				mutation[i] = path[(segmentStart + segmentLen + newPosition + i) % len];
			}
		}

		return mutation;
	}

	/**
	 * Calculate the cost of a path
	 *
	 * @param cities
	 * @return double
	 */
	public static double cost(City[] cities) {
		double cost = cities[cities.length - 1].distanceTo(cities[0]);
		for (int i = 0; i < cities.length - 1; i++) {
			cost = cost + cities[i].distanceTo(cities[i + 1]);
		}
		return cost;
	}

	/**
	 * Create String with the path and the cost of the path.
	 *
	 * @param cities
	 * @return String
	 */
	public final String path2string(City[] cities) {
		String s = "";
		for (City citie : cities) {
			s += citie.name() + "; ";
		}
		s += "cost: " + cost(cities);
		return s;
	}

	public City[] getCurrentPath() {
		return currentPath;
	}

	@Override
	public String toString() {
		return path2string(currentPath);
	}

}
