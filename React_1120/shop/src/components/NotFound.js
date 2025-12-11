import { Container, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import "./NotFound.css";

const NotFound = () => {
  const navigate = useNavigate();

  return (
    <Container className="not-found-container">
      <div className="not-found-content">
        <div className="error-code">404</div>
        <h1 className="error-title">페이지를 찾을 수 없습니다</h1>
        <p className="error-message">
          요청하신 페이지가 존재하지 않거나 이동되었을 수 있습니다.
        </p>
        <div className="error-actions">
          <Button 
            variant="primary" 
            size="lg"
            onClick={() => navigate("/")}
            className="home-button"
          >
            홈으로 돌아가기
          </Button>
        </div>
      </div>
    </Container>
  );
};

export default NotFound;