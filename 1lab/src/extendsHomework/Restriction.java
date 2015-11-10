package extendsHomework;


public class Restriction {

    static class RacingCar {
	static void moveRight() {
	    System.out.println("turn right");
	}

	static void moveLeft() {
	    System.out.println("turn left");
	}

	static void moveForeward() {
	    System.out.println("move forvard");
	}

	static void moveBackward() {
	    System.out.println("move backward");
	}
	
	static void race() {
	    System.out.println("Be careful on road");
	}
    }

    static class OurdatedRacingCar extends RacingCar {

	static void race() {
	    
	    throw new UnsupportedOperationException();

	}

    }
    
}
