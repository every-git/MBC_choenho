import { useState, useEffect } from "react";
import { Form, Button, Container, Card } from "react-bootstrap";
import { useParams, useNavigate } from "react-router-dom";

function BoardEdit({ posts, onUpdate }) {
  const { id } = useParams();
  const navigate = useNavigate();
  const post = posts.find((p) => p.id === Number(id));

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [author, setAuthor] = useState("");

  useEffect(() => {
    if (post) {
      setTitle(post.title);
      setContent(post.content);
      setAuthor(post.author);
    }
  }, [post]);

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

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!title || !content || !author) {
      alert("모든 항목을 입력해주세요.");
      return;
    }
    onUpdate(Number(id), { title, content, author });
    navigate(`/board/${id}`);
  };

  return (
    <Container style={{ marginTop: "30px", marginBottom: "50px" }}>
      <Card style={{ border: "none", boxShadow: "0 2px 8px rgba(0,0,0,0.1)" }}>
        <Card.Header style={{ 
          padding: "20px",
          borderBottom: "1px solid #dee2e6"
        }}>
          <h3 style={{ margin: 0, fontWeight: "bold" }}>✏️ 글수정</h3>
        </Card.Header>
        <Card.Body style={{ padding: "30px" }}>
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-4">
              <div style={{ display: "flex", alignItems: "center", marginBottom: "10px" }}>
                <Form.Label style={{ fontWeight: "bold", fontSize: "16px", margin: 0, marginRight: "10px", minWidth: "60px" }}>
                  작성자
                </Form.Label>
                <Form.Control
                  type="text"
                  placeholder="작성자를 입력하세요"
                  value={author}
                  onChange={(e) => setAuthor(e.target.value)}
                  style={{ padding: "12px", fontSize: "15px", width: "300px" }}
                />
              </div>
            </Form.Group>
            <Form.Group className="mb-4">
              <div style={{ display: "flex", alignItems: "center", marginBottom: "10px" }}>
                <Form.Label style={{ fontWeight: "bold", fontSize: "16px", margin: 0, marginRight: "10px", minWidth: "60px" }}>
                  제목
                </Form.Label>
                <Form.Control
                  type="text"
                  placeholder="제목을 입력하세요"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  style={{ padding: "12px", fontSize: "15px", width: "500px" }}
                />
              </div>
            </Form.Group>
            <Form.Group className="mb-4">
              <div style={{ display: "flex", alignItems: "flex-start", marginBottom: "10px" }}>
                <Form.Label style={{ fontWeight: "bold", fontSize: "16px", margin: 0, marginRight: "10px", minWidth: "60px", paddingTop: "12px" }}>
                  내용
                </Form.Label>
                <Form.Control
                  as="textarea"
                  rows={12}
                  placeholder="내용을 입력하세요"
                  value={content}
                  onChange={(e) => setContent(e.target.value)}
                  style={{ padding: "15px", fontSize: "15px", lineHeight: "1.6", textAlign: "left", width: "500px" }}
                />
              </div>
            </Form.Group>
            <div style={{ textAlign: "right", marginTop: "30px", paddingTop: "20px", borderTop: "1px solid #dee2e6" }}>
              <Button
                variant="outline-secondary"
                onClick={() => navigate(`/board/${id}`)}
                style={{ marginRight: "10px", padding: "10px 20px", fontWeight: "bold" }}
              >
                취소
              </Button>
              <Button 
                variant="primary" 
                type="submit"
                style={{ padding: "10px 30px", fontWeight: "bold" }}
              >
                수정하기
              </Button>
            </div>
          </Form>
        </Card.Body>
      </Card>
    </Container>
  );
}

export default BoardEdit;

