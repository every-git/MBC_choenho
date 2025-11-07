package ex09;

public class Friend extends Person {

    private String phoneNumber;
    private String email;

    public Friend() {}
    public Friend(String name, String phoneNumber, String email) {
        super(name);
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getInfo() {
        return super.getInfo() + "    " + phoneNumber + "    " + email;
    }
}
