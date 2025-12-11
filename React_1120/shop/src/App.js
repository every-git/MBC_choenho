import "./App.css";
import { Navbar, Container, Nav, Button } from "react-bootstrap";
import data from "./db/fruit";
import veggieData from "./db/veggie";
import Products from "./components/Products";
import { Routes, Route, useNavigate } from "react-router-dom";
import Detail from "./components/Detail";
import { useState } from "react";
import NotFound from "./components/NotFound";
import About from "./components/About";
import Member from "./components/Member";
import Location from "./components/Location";
import Title from "./components/Title";
import Title2 from "./components/Title2";

function App() {
  const [fruit, setFruit] = useState(data);
  const [veggie] = useState(veggieData);
  const navigate = useNavigate();

  const sortByName = () => {
    let sortedFruit = [...fruit].sort((a, b) => (a.title > b.title ? 1 : -1));
    setFruit(sortedFruit);
    console.log(sortedFruit);
  };
  const sortByPriceLowToHigh = () => {
    let sortedFruit = [...fruit].sort((a, b) => a.price - b.price);
    setFruit(sortedFruit);
    console.log(sortedFruit);
  };
  const sortByPriceHighToLow = () => {
    let sortedFruit = [...fruit].sort((a, b) => b.price - a.price);
    setFruit(sortedFruit);
    console.log(sortedFruit);
  };

  return (
    <div className="App">
      <Navbar bg="dark" variant="dark">
        <Container>
          <Navbar.Brand href="#home">과일농장</Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link onClick={() => navigate("/")}>홈으로</Nav.Link>
            <Nav.Link onClick={() => navigate("/detail")}>상세페이지</Nav.Link>
            <Nav.Link onClick={() => navigate("/cart")}>장바구니</Nav.Link>
            <Nav.Link onClick={() => navigate("/about")}>회사소개</Nav.Link>
          </Nav>
        </Container>
      </Navbar>
      <Routes>
        <Route
          path="/"
          element={
            <>
              <div className="slider" />
              <Title />
              <div className="container sort-buttons-container">
                <Button
                  variant="outline-primary"
                  onClick={sortByName}
                  className="sort-btn"
                >
                  이름순 정렬
                </Button>
                <Button
                  variant="outline-secondary"
                  onClick={sortByPriceLowToHigh}
                  className="sort-btn"
                >
                  낮은가격순 정렬
                </Button>
                <Button
                  variant="outline-success"
                  onClick={sortByPriceHighToLow}
                  className="sort-btn"
                >
                  높은가격순 정렬
                </Button>
              </div>
              <div className="container" style={{ textAlign: "center" }}>
                <div className="row">
                  {fruit.map((fruit) => (
                    <Products
                      key={fruit.id}
                      id={fruit.id}
                      title={fruit.title}
                      price={fruit.price}
                      imgUrl={fruit.imgUrl}
                      content={fruit.content}
                    />
                  ))}
                </div>
              </div>

              <div className="container" style={{ marginTop: "50px" }}>
                <div className="row">
                  <div style={{ textAlign: "center", width: "100%" }}>
                    <Title2 />
                    <Button variant="outline-success" style={{ marginTop: "20px" }}>
                      + 3개 상품 더 보기
                    </Button>
                  </div>
                </div>
              </div>

              <div className="container" style={{ marginTop: "30px", textAlign: "center" }}>
                <div className="row">
                  {veggie.map((item) => (
                    <Products
                      key={item.id}
                      id={item.id}
                      title={item.title}
                      price={item.price}
                      imgUrl={item.imgUrl}
                      content={item.content}
                    />
                  ))}
                </div>
              </div>
            </>
          }
        />

        <Route path="/detail/:paramId" element={<Detail fruit={fruit} />} />
        <Route path="/about" element={<About />}>
          <Route
            index
            element={
              <div>
                <h4>회사소개 페이지입니다</h4>
              </div>
            }
          />
          <Route path="member" element={<Member />} />
          <Route path="location" element={<Location />} />
        </Route>
        <Route path="/*" element={<NotFound />} />
      </Routes>
    </div>
  );
}

export default App;
