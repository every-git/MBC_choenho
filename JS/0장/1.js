let max = Math.max(10, 5, 8, 30);

console.log(max);

let min = Math.min(10, 5, 8, 30);

console.log(min);

console.log("random number: " + Math.random());

console.log("sqrt : " + Math.sqrt(16));

let greenArr = ["교대", "방배", "강남"];
let yellowArr = ["미금", "정자", "수서"];

greenArr.splice(2, 1, "서초", "역삼"); // 2번째 인덱스에서 1개 삭제하고, 서초, 역삼 추가
console.log(greenArr);

var data1 = yellowArr.pop(); // 마지막 요소 삭제
console.log(data1); // 삭제된 요소 출력. 수서
console.log(yellowArr); // 삭제 후 배열 출력 [미금, 정자]

var data2 = yellowArr.shift(); // 첫 번째 요소 삭제
console.log(data2); // 삭제된 요소 출력 미금
console.log(yellowArr); // 삭제 후 배열 출력 [정자]

yellowArr.push(data2); // 마지막에 삭제된 요소 추가
console.log(yellowArr); // 추가 후 배열 출력 [정자, 미금]

yellowArr.unshift(data1); // 첫 번째에 삭제된 요소 추가
console.log(yellowArr); // 추가 후 배열 출력 [수서, 정자, 미금]

let str = "hello javascript";

let str2 = str.substring(3, 7);
console.log(str2); // lo j

let str3 = str.substring(3,-3);
console.log(str3); // hel

let str4 = str.substring(-3, 3);
console.log(str4); // 

let arr1 = [1, 2, 3, 4, 5];
let arr2 = [6, 7, 8, 9, 10];

let arr3 = [...arr1, ...arr2];
console.log(arr3); // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]