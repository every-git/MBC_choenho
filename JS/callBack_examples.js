// 콜백 함수 사용 사례 모음

console.log('=== 1. 비동기 작업 (setTimeout) ===');
setTimeout(function() {
    console.log('3초 후 실행됩니다!');
}, 3000);
console.log('이건 바로 실행됩니다!');

console.log('\n=== 2. 배열 메서드 ===');
const numbers = [1, 2, 3, 4, 5];

// forEach - 각 요소마다 실행
numbers.forEach(function(num) {
    console.log('숫자:', num);
});

// map - 변환
const doubled = numbers.map(function(num) {
    return num * 2;
});
console.log('2배한 배열:', doubled);

// filter - 필터링
const evens = numbers.filter(function(num) {
    return num % 2 === 0;
});
console.log('짝수만:', evens);

console.log('\n=== 3. 재사용 가능한 함수 (현재 코드 스타일) ===');
function repeat(count, callBack) {
    for(let idx = 0; idx < count; idx++) {
        callBack(idx + 1);
    }
}

function origin(count) {
    console.log('원본:', count);
}

function double(count) {
    console.log('2배:', count * 2);
}

function square(count) {
    console.log('제곱:', count * count);
}

repeat(3, origin);   // 원본: 1, 원본: 2, 원본: 3
repeat(3, double);   // 2배: 2, 2배: 4, 2배: 6
repeat(3, square);   // 제곱: 1, 제곱: 4, 제곱: 9

console.log('\n=== 4. 이벤트 처리 (브라우저 환경에서 실행) ===');
// document.addEventListener('click', function(event) {
//     console.log('클릭됨!', event);
// });

console.log('\n=== 5. 데이터 처리 파이프라인 ===');
function processData(data, processor) {
    const result = [];
    for(let i = 0; i < data.length; i++) {
        result.push(processor(data[i]));
    }
    return result;
}

const prices = [1000, 2000, 3000];

// 세일 가격 계산 (20% 할인)
const salePrices = processData(prices, function(price) {
    return price * 0.8;
});
console.log('세일 가격:', salePrices);

// 세금 포함 가격 계산 (10% 세금)
const taxPrices = processData(prices, function(price) {
    return price * 1.1;
});
console.log('세금 포함 가격:', taxPrices);

