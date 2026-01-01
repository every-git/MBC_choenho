<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원가입 - 대철이제철 게시판</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h2>회원가입</h2>
        
        <c:if test="${not empty errorMsg}">
            <div class="alert alert-danger">
                ${errorMsg}
            </div>
        </c:if>
        
        <!-- onsubmit returns false to prevent default submission, we handle it via JS function -->
        <form action="${pageContext.request.contextPath}/member/join" method="post" id="joinForm" onsubmit="return false;">
            <div class="form-group">
                <label>아이디:</label>
                <div style="display: flex; align-items: center; gap: 8px;">
                    <input type="text" name="id" id="id" required style="flex: 1;">
                    <button type="button" onclick="checkIdDuplicate()" style="flex-shrink: 0; padding: 12px 20px; background-color: var(--secondary); color: var(--text-white); border: none; border-radius: 4px; font-size: 14px; font-weight: 500; cursor: pointer; white-space: nowrap;">중복확인</button>
                </div>
                <span id="id_check_status" style="display: block; margin-top: 8px; font-size: 0.9rem;"></span>
            </div>
            
            <div class="form-group">
                <label>비밀번호:</label>
                <input type="password" name="password" id="password" required>
            </div>
            
            <div class="form-group">
                <label>비밀번호 확인:</label>
                <input type="password" id="password_confirm" required>
                <span id="password_match" style="display:none; color: var(--success); margin-left: 10px;">비밀번호가 일치합니다</span>
                <span id="password_mismatch" style="display:none; color: var(--danger); margin-left: 10px;">비밀번호가 일치하지 않습니다</span>
            </div>
            
            <div class="form-group">
                <label>이름:</label>
                <input type="text" name="name" required>
            </div>
            
            <div class="form-group">
                <label>이메일:</label>
                <input type="email" name="email" required>
            </div>
            
            <div class="form-group">
                <label>전화번호:</label>
                <input type="text" name="phone">
            </div>
            
            <div class="form-group">
                <label>회원 유형:</label>
                <input type="radio" name="role" value="MEMBER" id="role_member" checked onchange="toggleAdminPassword()"> 일반회원
                <input type="radio" name="role" value="ADMIN" id="role_admin" onchange="toggleAdminPassword()"> 관리자
            </div>

            <div class="form-group" id="admin_password_group" style="display:none;">
                <label>관리자 비밀번호:</label>
                <input type="password" name="admin_password" id="admin_password" placeholder="관리자 가입을 위한 비밀번호를 입력하세요">
                <small style="color: #999; display: block; margin-top: 5px;">관리자 가입을 위해서는 특별한 비밀번호가 필요합니다.</small>
            </div>
            
            <div class="form-group">
                <button type="button" onclick="submitJoinForm()">가입하기</button>
            </div>
        </form>
        
        <div class="links">
            <a href="${pageContext.request.contextPath}/member/login">로그인</a>
            <a href="${pageContext.request.contextPath}/">메인으로</a>
        </div>
    </div>
    
    <script>
        <%
            String message = (String)request.getAttribute("message");
            if (message != null) {
        %>
                alert("<%= message %>");
        <%
            }
        %>
        var isIdChecked = false;
        
        // Admin Password Toggle
        function toggleAdminPassword() {
            var adminRadio = document.getElementById('role_admin');
            var adminPasswordGroup = document.getElementById('admin_password_group');
            var adminPassword = document.getElementById('admin_password');

            if (adminRadio && adminRadio.checked) {
                adminPasswordGroup.style.display = 'block';
                adminPassword.setAttribute('required', 'required');
            } else {
                adminPasswordGroup.style.display = 'none';
                adminPassword.removeAttribute('required');
                adminPassword.value = '';
            }
        }

        // ID 중복 확인
        function checkIdDuplicate() {
            var idInput = document.getElementById('id');
            var id = idInput.value.trim();
            
            if (!id) {
                alert('아이디를 입력해주세요.');
                if(idInput) idInput.focus();
                return;
            }
            
            var idPattern = /^[a-zA-Z0-9]{4,20}$/;
            if (!idPattern.test(id)) {
                alert('아이디는 영문과 숫자만 사용 가능하며, 4-20자 사이여야 합니다.');
                if(idInput) idInput.focus();
                return;
            }
            
            fetch('${pageContext.request.contextPath}/member/idCheck?id=' + encodeURIComponent(id))
                .then(response => response.text())
                .then(result => {
                    var statusSpan = document.getElementById('id_check_status');
                    var idInput = document.getElementById('id');
                    if (result === 'available') {
                        statusSpan.textContent = '사용 가능한 아이디입니다.';
                        statusSpan.style.color = 'var(--success)';
                        isIdChecked = true;
                        idInput.setAttribute('data-checked', 'true');
                    } else {
                        statusSpan.textContent = '이미 사용 중인 아이디입니다.';
                        statusSpan.style.color = 'var(--danger)';
                        isIdChecked = false;
                        idInput.removeAttribute('data-checked');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('아이디 중복 확인 중 오류가 발생했습니다.');
                });
        }
        
        // 비밀번호 확인 실시간 체크
        var passwordConfirmInput = document.getElementById('password_confirm');
        if(passwordConfirmInput) {
            passwordConfirmInput.addEventListener('input', function() {
                var password = document.getElementById('password').value;
                var passwordConfirm = this.value;
                var matchSpan = document.getElementById('password_match');
                var mismatchSpan = document.getElementById('password_mismatch');
                
                if (passwordConfirm.length > 0) {
                    if (password === passwordConfirm) {
                        matchSpan.style.display = 'inline';
                        mismatchSpan.style.display = 'none';
                    } else {
                        matchSpan.style.display = 'none';
                        mismatchSpan.style.display = 'inline';
                    }
                } else {
                    matchSpan.style.display = 'none';
                    mismatchSpan.style.display = 'none';
                }
            });
        }
        
        // 아이디 변경 시 중복확인 초기화
        var idInput = document.getElementById('id');
        if(idInput) {
            idInput.addEventListener('input', function() {
                isIdChecked = false;
                this.removeAttribute('data-checked');
                var statusSpan = document.getElementById('id_check_status');
                if(statusSpan) statusSpan.textContent = '';
            });
        }

        // 명시적 제출 함수
        function submitJoinForm() {
            if (validateJoinForm()) {
                document.getElementById('joinForm').submit();
            }
        }
        
        // 폼 유효성 검사
        function validateJoinForm() {
            var idInput = document.getElementById('id');
            var password = document.getElementById('password').value;
            var passwordConfirm = document.getElementById('password_confirm').value;
            
            // role 체크
            var role = "MEMBER";
            var adminRadio = document.getElementById('role_admin');
            if (adminRadio && adminRadio.checked) {
                role = "ADMIN";
            }
            
            var adminPassword = document.getElementById('admin_password').value;
            
            // 1. 아이디 중복확인 체크
            if (!idInput || !idInput.getAttribute('data-checked')) {
                alert('아이디 중복확인을 해주세요.');
                if(idInput) idInput.focus();
                // return false (not needed in explicit call but good for consistency)
                return false;
            }
            
            // 2. 비밀번호 일치 확인
            if (password !== passwordConfirm) {
                alert('비밀번호가 일치하지 않습니다.');
                if(passwordConfirmInput) passwordConfirmInput.focus();
                return false;
            }

            // 3. 관리자 가입 시 비밀번호 확인 (9876)
            if (role === 'ADMIN') {
                if (!adminPassword || adminPassword.trim() === '') {
                    alert('관리자 가입을 위한 비밀번호를 입력해주세요.');
                    var apInput = document.getElementById('admin_password');
                    if(apInput) apInput.focus();
                    return false;
                }
                if (adminPassword !== '9876') {
                    alert('관리자 비밀번호가 올바르지 않습니다.');
                    var apInput = document.getElementById('admin_password');
                    if(apInput) apInput.focus();
                    return false;
                }
            }
            
            return true;
        }
    </script>
</body>
</html>
