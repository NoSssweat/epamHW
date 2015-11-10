package extendsHomework;

public class Specialization {

    static class Car {

	public void drive() {
	    System.out.println("Driving");
	}

    }

    static class SportCar extends Car {
	public void drive() {
	    super.drive();
	    driveCarefuly();
	}

	static void driveCarefuly() {

	}

    }
}
