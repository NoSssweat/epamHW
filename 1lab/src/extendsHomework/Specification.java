package extendsHomework;

public class Specification {

    static abstract class Car {

	abstract void drive();

    }

    static class SportCar extends Car {
	public void drive() {
	    System.out.println("Wroom-Wroom");

	}

    }

}
