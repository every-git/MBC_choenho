const arr = {"a" : 10, "b" : 20, "c" : 30};

let {a , b , c} = arr;

console.log(a); // 10
console.log(b); // 20
console.log(c); // 30

const test = {"kor" : 100, "eng" : 90, "math" : 80};

let {kor, eng, math} = test;

console.log(kor); // 100
console.log(eng); // 90
console.log(math); // 80

let num = [1, 2, 3];

let [x, y, z] = num;
console.log(x + "," + y + "," + z); // 1,2,3