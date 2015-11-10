package extendsHomework;

public class Extension {

    static class Cars {
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
    }

    static class RacingCar extends Cars {

	static void race() {

	}

    }
}
