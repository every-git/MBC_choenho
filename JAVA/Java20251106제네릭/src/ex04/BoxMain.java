package ex04;

public class BoxMain {
	public static void main(String[] args) {
		Box<Person> pBox = new Box<>();
		Box<Student> sBox = new Box<>();
		Box<Teacher> tBox = new Box<>();
		Box<AlbaStudent> aBox = new Box<>();
	}
}
class Car {}
class Toy {}
class Person {}
class Student extends Person {}
class AlbaStudent extends Student {}
class Teacher extends Person {}