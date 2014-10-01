package graph;

public class IntegerPair implements Comparable<IntegerPair> {

	public final int a;
	public final int b;

	public IntegerPair(int a, int b) { 
		this.a=a;
		this.b=b;
	}

	public final int hashCode() {
		return a*17+b*33;
	}

	public final int compareTo(IntegerPair i) {
		if (i.a<a) return 1;
		if (i.a>a) return -1;
		if (i.b<b) return 1;
		if (i.b>b) return -1;
		return 0;
	}

	public static final int compare(IntegerPair id1, IntegerPair id2) {
		if (id1 == null)
			if (id2 == null)
				return 0;
			else
				return -1;
		else if (id2 == null)
			return 1;
		else
			return id1.compareTo(id2);
	}

	public final boolean equals(Object o) {
		if (!(o instanceof IntegerPair)) return false;
		IntegerPair i=(IntegerPair)o;
		return ((i.a==a)&(i.b==b));
	}

	public String toString() {
		return "[\"" + a + "," + b + "\"]";
	}

}