/* ============================================ */
/* 네비게이션 바 스크롤 감지 기능 */
/* 스크롤 방향에 따라 네비게이션 바를 숨기거나 표시 */
/* ============================================ */
document.addEventListener('DOMContentLoaded', function() {
    let lastScrollTop = 0; // 이전 스크롤 위치 저장
    let scrollThreshold = 100; // 스크롤 임계값 (100px 이상 스크롤해야 숨김)
    const navbar = document.querySelector('.navbar');
    
    // 네비게이션 바가 없으면 함수 종료
    if (!navbar) return;
    
    /**
     * 스크롤 이벤트 핸들러
     * 스크롤 방향과 위치에 따라 네비게이션 바 표시/숨김 제어
     */
    function handleScroll() {
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop; // 현재 스크롤 위치
        const scrollDifference = scrollTop - lastScrollTop; // 스크롤 방향 계산 (양수: 아래, 음수: 위)
        
        // 아래로 스크롤하고 임계값을 넘었을 때만 숨기기
        if (scrollDifference > 0 && scrollTop > scrollThreshold) {
            // 아래로 스크롤 - 네비게이션 바 숨기기
            navbar.classList.add('navbar-hidden');
        } else if (scrollDifference < 0 || scrollTop <= scrollThreshold) {
            // 위로 스크롤 또는 상단 근처 - 네비게이션 바 보이기
            navbar.classList.remove('navbar-hidden');
        }
        
        // 현재 스크롤 위치 저장 (음수 방지)
        lastScrollTop = scrollTop <= 0 ? 0 : scrollTop;
    }
    
    // 스크롤 이벤트 리스너 추가 (throttle 적용으로 성능 최적화)
    let ticking = false; // 애니메이션 프레임 요청 중복 방지 플래그
    window.addEventListener('scroll', function() {
        if (!ticking) {
            // requestAnimationFrame을 사용하여 브라우저 렌더링 최적화
            window.requestAnimationFrame(function() {
                handleScroll();
                ticking = false; // 다음 이벤트 허용
            });
            ticking = true; // 현재 처리 중임을 표시
        }
    }, { passive: true }); // passive 옵션으로 스크롤 성능 향상
});

/* ============================================ */
/* Apple TV+ 슬라이드 갤러리 자동 재생 기능 */
/* 슬라이드를 자동으로 전환하고 사용자가 제어할 수 있도록 함 */
/* ============================================ */
document.addEventListener('DOMContentLoaded', function() {
    const slides = document.querySelectorAll('.tv-slide'); // 모든 슬라이드 요소
    const dots = document.querySelectorAll('.dot'); // 슬라이드 인디케이터 도트
    const playBtn = document.getElementById('tvPlayBtn'); // 재생/정지 버튼
    let currentSlide = 0; // 현재 활성 슬라이드 인덱스
    let autoPlayInterval = null; // 자동 재생 인터벌 ID
    let isPlaying = false; // 재생 상태 플래그
    const slideInterval = 3000; // 슬라이드 전환 간격 (3초)
    
    /**
     * 특정 인덱스의 슬라이드를 표시하는 함수
     * @param {number} index - 표시할 슬라이드 인덱스
     */
    function showSlide(index) {
        // 모든 슬라이드와 도트에서 active 클래스 제거
        slides.forEach(slide => slide.classList.remove('active'));
        dots.forEach(dot => dot.classList.remove('active'));
        
        // 현재 슬라이드와 도트에 active 클래스 추가
        if (slides[index]) {
            slides[index].classList.add('active');
        }
        if (dots[index]) {
            dots[index].classList.add('active');
        }
        
        currentSlide = index; // 현재 슬라이드 인덱스 업데이트
    }
    
    /**
     * 다음 슬라이드로 이동하는 함수
     * 마지막 슬라이드에서 첫 번째 슬라이드로 순환
     */
    function nextSlide() {
        const nextIndex = (currentSlide + 1) % slides.length; // 순환 인덱스 계산
        showSlide(nextIndex);
    }
    
    /**
     * 자동 재생을 시작하는 함수
     * 일정 간격으로 슬라이드를 자동으로 전환
     */
    function startAutoPlay() {
        // 이미 재생 중이면 함수 종료
        if (autoPlayInterval) return;
        
        isPlaying = true;
        // 재생 버튼에 playing 클래스 추가 (아이콘 변경)
        if (playBtn) {
            playBtn.classList.add('playing');
        }
        // setInterval로 자동 재생 시작
        autoPlayInterval = setInterval(nextSlide, slideInterval);
    }
    
    /**
     * 자동 재생을 중지하는 함수
     * 인터벌을 클리어하고 재생 상태를 업데이트
     */
    function stopAutoPlay() {
        if (autoPlayInterval) {
            clearInterval(autoPlayInterval); // 인터벌 클리어
            autoPlayInterval = null;
        }
        isPlaying = false;
        // 재생 버튼에서 playing 클래스 제거 (아이콘 변경)
        if (playBtn) {
            playBtn.classList.remove('playing');
        }
    }
    
    // 재생 버튼 클릭 이벤트: 재생/정지 토글
    if (playBtn) {
        playBtn.addEventListener('click', function() {
            if (isPlaying) {
                stopAutoPlay(); // 재생 중이면 정지
            } else {
                startAutoPlay(); // 정지 중이면 재생
            }
        });
    }
    
    // 도트 클릭 이벤트: 특정 슬라이드로 이동
    dots.forEach((dot, index) => {
        dot.addEventListener('click', function() {
            showSlide(index); // 클릭한 도트의 인덱스로 슬라이드 이동
            // 도트 클릭 시 자동 재생이 켜져있으면 재시작 (타이머 리셋)
            if (isPlaying) {
                stopAutoPlay();
                startAutoPlay();
            }
        });
    });
    
    // 마우스 호버 시 자동 재생 일시 정지
    const tvGallery = document.querySelector('.tv-gallery');
    if (tvGallery) {
        // 마우스가 갤러리 위에 올라갔을 때
        tvGallery.addEventListener('mouseenter', function() {
            if (isPlaying) {
                stopAutoPlay(); // 재생 중이면 일시 정지
            }
        });
        
        // 마우스가 갤러리에서 벗어났을 때
        tvGallery.addEventListener('mouseleave', function() {
            // 재생 버튼이 활성화되어 있으면 자동 재생 재개
            if (playBtn && playBtn.classList.contains('playing')) {
                startAutoPlay();
            }
        });
    }
    
    // 초기 슬라이드 표시 (첫 번째 슬라이드)
    if (slides.length > 0) {
        showSlide(0);
    }
    
    /* ============================================ */
    /* 준비중 팝업 기능 */
    /* 모든 버튼 클릭 시 "준비중입니다" 팝업 표시 */
    /* ============================================ */
    
    /**
     * 준비중 팝업을 표시하는 함수
     * 화면 중앙에 팝업을 생성하고 2초 후 자동으로 제거
     */
    function showComingSoonPopup() {
        // 기존 팝업이 있으면 제거 (중복 방지)
        const existingPopup = document.querySelector('.coming-soon-popup');
        if (existingPopup) {
            existingPopup.remove();
        }
        
        // 팝업 요소 생성
        const popup = document.createElement('div');
        popup.className = 'coming-soon-popup';
        popup.innerHTML = '<div class="popup-content"><p>준비중입니다.</p></div>';
        document.body.appendChild(popup);
        
        // 애니메이션을 위해 약간의 지연 후 표시 (CSS transition 작동을 위해)
        setTimeout(() => {
            popup.classList.add('show'); // show 클래스 추가로 페이드 인 효과
        }, 10);
        
        // 2초 후 자동으로 제거
        setTimeout(() => {
            popup.classList.remove('show'); // show 클래스 제거로 페이드 아웃 효과
            // CSS transition 완료 후 요소 제거
            setTimeout(() => {
                popup.remove();
            }, 300); // transition 시간과 동일하게 설정
        }, 2000);
    }
    
    // 모든 버튼에 클릭 이벤트 리스너 추가
    const allButtons = document.querySelectorAll('.btn, a.btn, button.btn');
    allButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 링크 동작 방지
            showComingSoonPopup(); // 팝업 표시
        });
    });
});
