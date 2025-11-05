package ex05;

public interface Payment {

    void pay(int amount);
}

class CreditCard implements Payment{
    void fare(int amount){ //교통요금
        System.out.printf("CreditCard로 %d 결제했습니다.\n", amount);
        
    }
    @Override
    public void pay(int amount){
        System.out.printf("CreditCard로 %d 결제했습니다.\n", amount);
    }
}

class KakaoPay implements Payment{
    void fare(int amount){ //서비스요금
        System.out.printf("KakaoPay로 %d 결제했습니다.\n", amount);
    }
    @Override
    public void pay(int amount){
        System.out.printf("KakaoPay로 %d 결제했습니다.\n", amount);
    }
}

class SamsungPay implements Payment{
    void charge(int amount){ //일반요금
        System.out.printf("SamsungPay로 %d 결제했습니다.\n", amount);
    }
    @Override
    public void pay(int amount){
        System.out.printf("SamsungPay로 %d 결제했습니다.\n", amount);
    }
}