
import java.util.ArrayList;

// 
// Decompiled by Procyon v0.5.36
// 
public class City {

	private Object[] whacky;

	public City(final String s, final double d, final double a) {
		(this.whacky = new Object[4])[0] = d;
		this.whacky[1] = d + Math.exp(a);
		this.whacky[2] = new ArrayList();
		this.whacky[3] = s;
	}

	public String name() {
		return (String) this.whacky[3];
	}

	public double x() {
		return (double) this.whacky[0];
	}

	public double y() {
		return Math.log((double) this.whacky[1] - (double) this.whacky[0]);
	}

	public double distanceTo(final City city) {
		final double n = (double) this.whacky[0] - (double) city.whacky[0];
		final double n2 = n * n;
		final double log = Math.log((double) this.whacky[1] - (double) this.whacky[0]);
		final double log2 = Math.log((double) city.whacky[1] - (double) city.whacky[0]);
		return Math.sqrt(n2 + (log * log + log2 * log2 - 2.0 * (log * log2)));
	}
}
