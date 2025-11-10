let result = confirm("정말로 탈퇴 하시겠습니까?");

if(result) {
    document.write("탈퇴 처리되었습니다.");
} else {
    document.body.innerHTML = "탈퇴 취소되었습니다.";
}