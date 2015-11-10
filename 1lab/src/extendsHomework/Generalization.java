package extendsHomework;


public class Generalization {
    
    static class FishingReel {
	int capacity;
	String name;
	// standart gear ratio - 1:1

    }

    static class MultiplicatorReel extends FishingReel {

	String gearRatio;

    }

}
