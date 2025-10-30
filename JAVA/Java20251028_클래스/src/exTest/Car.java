package exTest;

public class Car {
    String color;
    String gearType;
    int door;

    public Car() {
        this.color = "white";
        this.gearType = "auto";
        this.door = 4;
    }

    public Car(String color) {
        this.color = color;
        this.gearType = "auto";
        this.door = 4;
    }

    public Car(String color, String gearType) {
        this.color = color;
        this.gearType = gearType;
        this.door = 4;
    }

    public Car(String color, String gearType, int door) {
        this.color = color;
        this.gearType = gearType;
        this.door = door;
    }
}


