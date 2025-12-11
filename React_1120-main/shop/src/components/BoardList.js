import { useState } from "react";
import { Table, Button, Card } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";

function BoardList({ posts, onDelete, onView }) {
  const navigate = useNavigate();
  const sortedPosts = [...posts].sort((a, b) => b.id - a.id);

  return (
    <div className="container" style={{ marginTop: "30px", marginBottom: "50px" }}>
      <div className="row">
        <div className="col-12">
          <h3 style={{ textAlign: "center", marginBottom: "30px", fontWeight: "bold" }}>
            과일농장 게시판
          </h3>
          <div style={{ textAlign: "right", marginBottom: "20px" }}>
            <Button
              variant="primary"
              onClick={() => navigate("/board/write")}
              style={{ fontWeight: "bold" }}
            >
              문의글 작성하기
            </Button>
          </div>
          <Table striped bordered hover style={{ borderCollapse: "collapse" }}>
            <thead>
              <tr style={{ backgroundColor: "#f8f9fa" }}>
                <th style={{ width: "8%", textAlign: "center", padding: "15px", borderBottom: "2px solid #dee2e6" }}>번호</th>
                <th style={{ width: "30%", padding: "15px", borderBottom: "2px solid #dee2e6" }}>제목</th>
                <th style={{ width: "35%", padding: "15px", borderBottom: "2px solid #dee2e6" }}>문의글</th>
                <th style={{ width: "10%", textAlign: "center", padding: "15px", borderBottom: "2px solid #dee2e6" }}>조회수</th>
                <th style={{ width: "17%", textAlign: "center", padding: "15px", borderBottom: "2px solid #dee2e6" }}>문의하기</th>
              </tr>
            </thead>
            <tbody>
              {sortedPosts.length === 0 ? (
                <tr>
                  <td colSpan="5" style={{ textAlign: "center", padding: "50px", color: "#6c757d" }}>
                    <div style={{ fontSize: "18px" }}>📝 게시글이 없습니다.</div>
                  </td>
                </tr>
              ) : (
                sortedPosts.map((post, index) => (
                  <tr key={post.id}>
                    <td style={{ textAlign: "center", padding: "15px", borderBottom: "1px solid #dee2e6" }}>
                      {post.id}
                    </td>
                    <td style={{ padding: "15px", borderBottom: "1px solid #dee2e6" }}>
                      {post.title}
                    </td>
                    <td style={{ padding: "15px", borderBottom: "1px solid #dee2e6" }}>
                      {post.content.length > 50 
                        ? post.content.substring(0, 50) + "..." 
                        : post.content}
                    </td>
                    <td style={{ textAlign: "center", padding: "15px", borderBottom: "1px solid #dee2e6" }}>
                      {post.views || 0}
                    </td>
                    <td style={{ textAlign: "center", padding: "15px", borderBottom: "1px solid #dee2e6" }}>
                      <Button
                        variant="primary"
                        size="sm"
                        onClick={() => {
                          navigate(`/board/${post.id}`);
                        }}
                        style={{ marginRight: "5px", fontSize: "12px", padding: "5px 10px" }}
                      >
                        게시글읽기
                      </Button>
                      <Button
                        variant="success"
                        size="sm"
                        onClick={() => navigate(`/board/edit/${post.id}`)}
                        style={{ marginRight: "5px", fontSize: "12px", padding: "5px 10px" }}
                      >
                        수정
                      </Button>
                      <Button
                        variant="danger"
                        size="sm"
                        onClick={(e) => {
                          e.stopPropagation();
                          if (window.confirm("정말 삭제하시겠습니까?")) {
                            onDelete(post.id);
                          }
                        }}
                        style={{ fontSize: "12px", padding: "5px 10px" }}
                      >
                        삭제
                      </Button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </Table>
        </div>
      </div>
    </div>
  );
}

export default BoardList;

