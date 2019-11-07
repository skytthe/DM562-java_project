
import java.util.InputMismatchException;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

// 
// Decompiled by Procyon v0.5.36
// 
public class CityGenerator {

	private static City[] fixedList() {
		return new City[]{
			new City("A", 16.8215D, 16.6572D),
			new City("B", 18.3529D, 12.1304D),
			new City("C", 14.9995D, 13.9122D),
			new City("D", 10.2841D, 12.9196D),
			new City("E", 4.2275D, 11.9579D),
			new City("F", 0.836446D, 13.4498D),
			new City("G", 10.4551D, 0.885736D),
			new City("H", 6.53566D, 16.7396D),
			new City("I", 16.6793D, 17.9234D),
			new City("J", 15.7796D, 14.0669D),
			new City("K", 3.90809D, 7.93377D),
			new City("L", 9.94376D, 9.35403D),
			new City("M", 7.08658D, 11.2766D),
			new City("N", 11.5908D, 17.2236D),
			new City("O", 12.0871D, 17.3643D)};
	}

	private static City[] customList(final Scanner scanner) {
		try {
			final ArrayList<City> list = new ArrayList<City>();
			System.out.println("Please enter the file name: ");
			final Scanner scanner2 = new Scanner(new FileReader(scanner.next()));
			while (scanner2.hasNextLine()) {
				final String[] split = scanner2.nextLine().split("[ .]");
				list.add(new City(split[0],
						Integer.parseInt(split[1]) + Double.parseDouble(split[2])
						/ Math.pow(10.0, split[2].length()),
						Integer.parseInt(split[3]) + Double.parseDouble(split[4])
						/ Math.pow(10.0, split[4].length())));
			}
			scanner2.close();
			return list.toArray(new City[0]);
		} catch (IOException ex) {
			System.out.println("Problem reading the file. Aborting.");
			System.exit(1);
		} catch (NumberFormatException ex2) {
			System.out.println("Improperly formed file. Aborting.");
			System.exit(1);
		}
		return null;
	}

	public static City[] generate() {
		final Scanner scanner = new Scanner(System.in);
		City[] array = null;
		do {
			System.out.println("Enter 1 to use the default list of cities,"
					+ " or 2 to read a custom list from a file.");
			try {
				switch (scanner.nextInt()) {
					case 1: {
						array = fixedList();
						continue;
					}
					case 2: {
						array = customList(scanner);
						continue;
					}
					default: {
						System.out.println("Please enter 1 or 2.");
						continue;
					}
				}
			} catch (InputMismatchException ex) {
				System.out.println("Please enter a number.");
				scanner.nextLine();
			}
		} while (array == null);
		return array;
	}

	/*
	* 
	* Not part of the original CityGenerator.class
	*
	 */
	public static City[] generateFixedList() {
		return generateFixedList(false);
	}

	public static City[] generateFixedList(boolean shuffle) {
		City[] cities = fixedList();

		if (shuffle) {
			Collections.shuffle(Arrays.asList(cities));
		}

		return cities;
	}

	public static City[] generateNumberCityList(int n, boolean shuffle) {
		City[] cities = new City[n];

		for (int i = 0; i < n; i++) {
			cities[i] = new City("" + (i),
					1 + Math.cos(2 * Math.PI * ((double) i / n)),
					1 + Math.sin(2 * Math.PI * ((double) i / n)));
		}

		if (shuffle) {
			Collections.shuffle(Arrays.asList(cities));
		}

		return cities;
	}

}
