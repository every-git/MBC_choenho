let tv = new Object();
tv.color = "white";
tv.price = 300000;
tv.info = function() {
    document.write("tv 색상: " + this.color + ", <br>");
    document.write("tv 가격: " + this.price + "<br>")
}

document.write("<h1>tv 객체 메서드 호출</h1>");
document.write(tv.color + "<br>");
document.write(tv.price + "<br>");
tv.info();

tv.tire = 4;
document.write("tv 타이어 개수: " + tv.tire + "<br>");
document.write("--------------------------------<br>");

let car = {
    color: "black",
    price: 5000000,
    info: function() {
        document.write("car 색상: " + this.color + ", <br>");
        document.write("car 가격: " + this.price + "<br>");
    }
}

document.write("<h1>car 객체 메서드 호출</h1>");
document.write(car.color + "<br>");
document.write(car.price + "<br>");
car.info();

car.tire = 4;
document.write("car 타이어 개수: " + car.tire + "<br>");
document.write("--------------------------------<br>");

car = {
    color: "red",
    price: 10000000,
    info: function() {
        document.write("car 색상: " + this.color + ", <br>");
        document.write("car 가격: " + this.price + "<br>");
    }
}

document.write("<h1>새로운 car 객체 메서드 호출</h1>");
document.write(car.color + "<br>");
document.write(car.price + "<br>");
car.info();

car.tire = 4;
document.write("car 타이어 개수: " + car.tire + "<br>");
document.write("--------------------------------<br>");

let today = new Date();
document.write("<h1>오늘 날짜 정보</h1>");
document.write("오늘 날짜: " + today.getFullYear() + "년 " + (today.getMonth() + 1) + "월 " + today.getDate() + "일" + "<br>");
document.write("지금 시간: " + today.getHours() + "시 " + today.getMinutes() + "분 " + today.getSeconds() + "초" + "<br>");

let dayNames = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
document.write("오늘 요일: " + dayNames[today.getDay()] + "<br>");
document.write("--------------------------------<br>");