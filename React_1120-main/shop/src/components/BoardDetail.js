import { useParams, useNavigate, Link } from "react-router-dom";
import { Container, Button, Card, Badge } from "react-bootstrap";
import { useEffect, useRef } from "react";

function BoardDetail({ posts, onDelete, onView }) {
  const { id } = useParams();
  const navigate = useNavigate();
  const post = posts.find((p) => p.id === Number(id));

  // 조회수 증가 (세션당 한 번만 실행)
  useEffect(() => {
    const postId = Number(id);
    
    if (!post || !onView) return;
    
    // sessionStorage에서 조회 여부 확인
    const viewedKey = `viewed_${postId}`;
    const hasViewed = sessionStorage.getItem(viewedKey);
    
    if (!hasViewed) {
      // 조회 표시 및 조회수 증가
      sessionStorage.setItem(viewedKey, 'true');
      onView(postId);
    }
  }, [id]); // id만 의존성으로 설정

  if (!post) {
    return (
      <Container style={{ marginTop: "30px" }}>
        <Card>
          <Card.Body style={{ textAlign: "center", padding: "50px" }}>
            <div style={{ fontSize: "18px", color: "#6c757d" }}>
              게시글을 찾을 수 없습니다.
            </div>
          </Card.Body>
        </Card>
      </Container>
    );
  }

  return (
    <Container style={{ marginTop: "30px", marginBottom: "50px" }}>
      <div style={{ marginBottom: "20px" }}>
        <Button 
          variant="outline-secondary" 
          onClick={() => navigate("/board")}
          style={{ fontWeight: "bold" }}
        >
          ← 목록으로
        </Button>
      </div>
      <Card style={{ border: "none", boxShadow: "0 2px 8px rgba(0,0,0,0.1)" }}>
        <Card.Header style={{ 
          padding: "25px",
          borderBottom: "1px solid #dee2e6"
        }}>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
            <div style={{ flex: 1 }}>
              <h3 style={{ margin: 0, marginBottom: "15px", fontWeight: "bold" }}>
                {post.title}
              </h3>
              <div style={{ fontSize: "14px", color: "#6c757d", display: "flex", alignItems: "center", gap: "20px" }}>
                <span>
                  <strong>조회수:</strong> {post.views || 0}
                </span>
              </div>
            </div>
            <div>
              <Button
                variant="primary"
                size="sm"
                onClick={() => navigate("/board")}
                style={{ marginRight: "10px", fontWeight: "bold" }}
              >
                게시글읽기
              </Button>
              <Button
                variant="success"
                size="sm"
                onClick={() => navigate(`/board/edit/${id}`)}
                style={{ marginRight: "10px", fontWeight: "bold" }}
              >
                수정
              </Button>
              <Button
                variant="danger"
                size="sm"
                onClick={() => {
                  if (window.confirm("정말 삭제하시겠습니까?")) {
                    onDelete(post.id);
                    navigate("/board");
                  }
                }}
                style={{ fontWeight: "bold" }}
              >
                삭제
              </Button>
            </div>
          </div>
        </Card.Header>
        <Card.Body style={{ padding: "30px" }}>
          <div
            style={{
              minHeight: "400px",
              padding: "30px",
              backgroundColor: "#f8f9fa",
              borderRadius: "8px",
              border: "1px solid #e9ecef",
              lineHeight: "1.8",
              fontSize: "16px",
              color: "#212529",
              whiteSpace: "pre-wrap"
            }}
          >
            {post.content.split("\n").map((line, i) => (
              <div key={i} style={{ marginBottom: line ? "10px" : "5px" }}>
                {line || "\u00A0"}
              </div>
            ))}
          </div>
        </Card.Body>
      </Card>
    </Container>
  );
}

export default BoardDetail;

