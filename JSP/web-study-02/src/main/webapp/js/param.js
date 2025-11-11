function check() {
    const form = document.forms["frm"];
    if (!form) {
        return true;
    }

    if(form.id.value.trim() === "") {
        alert("아이디를 입력해주세요.");
        form.id.focus();
        return false;
    }
    if(form.age.value.trim() === "") {
        alert("나이를 입력해주세요.");
        form.age.focus();
        return false;
    }
    else if (isNaN(form.age.value)) {
        alert("나이는 숫자만 입력해주세요.");
        form.age.focus();
        return false;
    }
    return true;
}

