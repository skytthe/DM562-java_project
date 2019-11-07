
import java.util.Random;

// 
// Decompiled by Procyon v0.5.36
// 
public class RandomUtils {

	private static Random generator;

	public static double getRandom() {
		return RandomUtils.generator.nextDouble();
	}

	public static boolean coinFlip() {
		return RandomUtils.generator.nextDouble() < 0.5;
	}

	public static int getRandomValue(final int bound) {
		return RandomUtils.generator.nextInt(bound);
	}

	static {
		RandomUtils.generator = new Random();
	}
}
