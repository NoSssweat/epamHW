package extendsHomework;

public class Variation {

    static class InertionReel {
	int capacity;
	String name;

	public int Reel(int lenghtOfLine) {
	    int numberOfRotation = lenghtOfLine / 10;
	    return numberOfRotation;
	}

    }

    static class MultiplicatorReel extends InertionReel {

	int gearRatio;

	public int MultipliReel(int lenghtOfLine, int gearRatio) {
	    int numberOfRotation = lenghtOfLine / 10 * gearRatio;
	    return numberOfRotation;

	}

    }
}
