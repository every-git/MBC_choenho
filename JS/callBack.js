function repeat(count, callBack) {
    for(let idx = 0; idx < count; idx++) {
        callBack(idx + 1);
    }
}

function origin(count) {
    console.log(count);
}

function double(count) {
    console.log(count * 2);
}

repeat(5, origin);
repeat(5, double);

let greeting = (name) => console.log(`Hello, ${name}!`);
// 정용준 이름을 콜백함수로 넣어서 출력
