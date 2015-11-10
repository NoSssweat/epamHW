package extendsHomework;

public class Construction {

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

    static class SportCar extends Cars {

	static void moveForwardFast() {
	    moveForeward();
	    System.out.println("so fast!");
	}

    }

}
