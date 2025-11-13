function loginCheck() {
    if(document.frm.userid.value == "") { //아이디 입력 체크
        alert("아이디를 입력해주세요.");
        document.frm.userid.focus();
        return false;
    }
    if(document.frm.pwd.value == "") { //비밀번호 입력 체크
        alert("비밀번호를 입력해주세요.");
        document.frm.pwd.focus();
        return false;
    }
    return true;
}