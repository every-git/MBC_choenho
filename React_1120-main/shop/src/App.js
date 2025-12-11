import { useState } from "react";
import "./App.css";
import { Navbar, Container, Nav, Button } from "react-bootstrap";
import data from "./db/fruit";
import Products from "./components/Products";
import { Routes, Route, useNavigate } from "react-router-dom";
import Detail from "./components/Detail";
import NotFound from "./components/NotFound";
import About from "./components/About";
import Member from "./components/Member";
import Location from "./components/Location";
import Title from "./components/Title";
import Title2 from "./components/Title2";
import data2 from "./db/veggie";
import ComVeggie from "./components/ComVeggie";
import Footer from "./components/Footer";
import axios from "axios";
import Cart from "./components/Cart";
import BoardList from "./components/BoardList";
import BoardWrite from "./components/BoardWrite";
import BoardDetail from "./components/BoardDetail";
import BoardEdit from "./components/BoardEdit";

function App() {
  const [fruit, setFruit] = useState(data);
  let [veggie, setVeggie] = useState(data2);
  let [count, setCount] = useState(1);
  let [input, setInput] = useState("");
  const [posts, setPosts] = useState([
    {
      id: 1,
      title: "사과는 언제 배송이 되나요?",
      content: "어제부터 기다렸는데 아직 배송이 안됐어요.",
      author: "김과일",
      date: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).getTime(),
      views: 1,
    },
    {
      id: 2,
      title: "수박크기가 작아요",
      content: "수박이 맛있고 달았습니다. 하지만 수박크기는 많이 작았어요.",
      author: "이채소",
      date: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000).getTime(),
      views: 2,
    },
    {
      id: 3,
      title: "오렌지 당도가 낮아요",
      content: "당도가 11birx이상은 아닌것같아요.",
      author: "박사과",
      date: new Date(Date.now() - 5 * 24 * 60 * 60 * 1000).getTime(),
      views: 1,
    },
    {
      id: 4,
      title: "딸기향이 이상해요",
      content: "딸기에서 흙냄새가 납니다.",
      author: "최옥수",
      date: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).getTime(),
      views: 1,
    },
  ]);
  const [postId, setPostId] = useState(6);
  const navigate = useNavigate();

  const sortByName = () => {
    let sortedFruit = [...fruit].sort((a, b) => (a.title > b.title ? 1 : -1));
    setFruit(sortedFruit);
  };

  const sortByPriceLowToHigh = () => {
    let sortedFruit = [...fruit].sort((a, b) => a.price - b.price);
    setFruit(sortedFruit);
  };

  const sortByPriceHighToLow = () => {
    let sortedFruit = [...fruit].sort((a, b) => b.price - a.price);
    setFruit(sortedFruit);
  };

  // 채소 정렬 함수들
  const sortVeggieByName = () => {
    let sortedVeggie = [...veggie].sort((a, b) => (a.title > b.title ? 1 : -1));
    setVeggie(sortedVeggie);
  };

  const sortVeggieByPriceLowToHigh = () => {
    let sortedVeggie = [...veggie].sort((a, b) => a.price - b.price);
    setVeggie(sortedVeggie);
  };

  const sortVeggieByPriceHighToLow = () => {
    let sortedVeggie = [...veggie].sort((a, b) => b.price - a.price);
    setVeggie(sortedVeggie);
  };

  // 게시판 함수들
  const handleAddPost = (newPost) => {
    const post = {
      id: postId,
      ...newPost,
      date: new Date().getTime(),
      views: 0,
    };
    setPosts([post, ...posts]);
    setPostId(postId + 1);
  };

  const handleViewPost = (id) => {
    setPosts(
      posts.map((post) =>
        post.id === id ? { ...post, views: (post.views || 0) + 1 } : post
      )
    );
  };

  const handleUpdatePost = (id, updatedPost) => {
    setPosts(
      posts.map((post) =>
        post.id === id
          ? { ...post, ...updatedPost, date: new Date().getTime() }
          : post
      )
    );
  };

  const handleDeletePost = (id) => {
    setPosts(posts.filter((post) => post.id !== id));
  };

  return (
    <div className="App">
      <Navbar bg="dark" variant="dark">
        <Container>
          <Navbar.Brand
            onClick={() => {
              navigate("/");
            }}
          >
            과일농장
          </Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link
              onClick={() => {
                navigate("/");
              }}
            >
              홈으로
            </Nav.Link>
            <Nav.Link
              onClick={() => {
                navigate("/detail/1");
              }}
            >
              상세페이지
            </Nav.Link>
            <Nav.Link
              onClick={() => {
                navigate("/cart");
              }}
            >
              장바구니
            </Nav.Link>
            <Nav.Link
              onClick={() => {
                navigate("/about");
              }}
            >
              회사소개
            </Nav.Link>
            <Nav.Link
              onClick={() => {
                navigate("/board");
              }}
            >
              게시판
            </Nav.Link>
          </Nav>
        </Container>
      </Navbar>
    
      <Routes>
        <Route
          path="/"
          element={
            <div>
            <div className="slider"></div>
              <Title />

              <div className="container">
                <div className="row">
                  <div className="col-md-6" style={{ textAlign: "left" }}>
                    {/* 검색 추가 */}
                    <input
                      placeholder="상품명을 입력하세요"
                      onChange={(e) => setInput(e.target.value)}
                      value={input}
                      style={{
                        padding: "10px",
                        marginLeft: "10px",
                        borderRadius: "4px",
                        border: "1px solid #ccc",
                        width: "250px",
                        marginRight: "10px"
                      }}
                    />
                  </div>
                  <div className="col-md-6" style={{ textAlign: "right" }}>
                    {/* select 추가 */}
                    <select
                      onChange={(e) => {
                        if (e.target.value === "low") sortByPriceLowToHigh();
                        if (e.target.value === "high") sortByPriceHighToLow();
                        if (e.target.value === "name") sortByName();
                      }}
                      style={{
                        padding: "10px",
                        marginLeft: "10px",
                        borderRadius: "4px",
                        border: "1px solid #ccc",
                        width: "150px"
                      }}
                    >
                      <option value="">정렬 선택</option>
                      <option value="low">낮은 가격순</option>
                      <option value="high">높은 가격순</option>
                      <option value="name">이름순</option>
                    </select>
                  </div>
                </div>
              </div>

              <div className="container" style={{ marginTop: "30px" }}>
                <div className="row">                    
                  {fruit
                    .filter((item) => {
                      return item.title.toLowerCase().includes(input.toLowerCase());
                    })
                    .map((fruit) => (
                      <Products {...fruit} key={fruit.id} />
                    ))}
                </div>
              </div>

              <div className="container">
                <div className="row">
                  <div style={{ textAlign: "center" }}>
                    <Title2 />
                  </div>
                </div>
              </div>

              <div className="container">
                <div className="row">
                  <div className="col-md-6" style={{ textAlign: "left" }}>
                    {/* 채소 정렬 select */}
                    <select
                      onChange={(e) => {
                        if (e.target.value === "low") sortVeggieByPriceLowToHigh();
                        if (e.target.value === "high") sortVeggieByPriceHighToLow();
                        if (e.target.value === "name") sortVeggieByName();
                      }}
                      style={{
                        padding: "10px",
                        marginLeft: "10px",
                        borderRadius: "4px",
                        border: "1px solid #ccc",
                        width: "150px"
                      }}
                    >
                      <option value="">정렬 선택</option>
                      <option value="low">낮은 가격순</option>
                      <option value="high">높은 가격순</option>
                      <option value="name">이름순</option>
                    </select>
                  </div>
                </div>
              </div>
                    
              <div className="container mt-3">
                <div className="row">
                  {veggie
                    .filter((item) => {
                      return item.title.toLowerCase().includes(input.toLowerCase());
                    })
                    .map((ele, i) => {
                      return <ComVeggie veggie={veggie[i]} key={veggie[i].id} />;
                    })}
                </div>
              </div>

              <div className="container">
                <div className="row">
                  <div style={{ textAlign: "center", marginTop: "30px" }}>
                    <Button
                      variant="outline-success"
                      count={count}
                      onClick={() => {
                        if (count === 1) {
                          axios
                            .get(
                              "https://sinaboro.github.io/react_data/veggie2.json"
                            )
                            .then((result) => {
                              let copy6 = [...veggie, ...result.data];
                              setVeggie(copy6);
                              setCount(count + 1);
                            });
                        } else if (count === 2) {
                          axios
                            .get(
                              "https://sinaboro.github.io/react_data/veggie3.json"
                            )
                            .then((result) => {
                              let copy11 = [...veggie, ...result.data];
                              setVeggie(copy11);
                              setCount(count + 1);
                            });
                        }
                        if (count === 3) {
                          alert("더이상 상품이 없습니다.");
                        }
                      }}
                    >
                      {" "}
                      + 3개 상품 더 보기{" "}
                    </Button>
                  </div>
                </div>
              </div>

              <Footer />
                    </div>
          }
        />

        <Route path="/detail/veggie/:paramId" element={<Detail fruit={fruit} veggie={veggie} />} />
        <Route path="/detail/:paramId" element={<Detail fruit={fruit} veggie={veggie} />} />
        <Route path="/cart" element={<Cart/>}/>
        <Route path="/about" element={<About />}>
            <Route path="member" element={<Member />} />
            <Route path="location" element={<Location />} />
          </Route>
        <Route path="/board" element={<BoardList posts={posts} onDelete={handleDeletePost} onView={handleViewPost} />} />
        <Route path="/board/write" element={<BoardWrite onAdd={handleAddPost} />} />
        <Route path="/board/:id" element={<BoardDetail posts={posts} onDelete={handleDeletePost} onView={handleViewPost} />} />
        <Route path="/board/edit/:id" element={<BoardEdit posts={posts} onUpdate={handleUpdatePost} />} />
        <Route path="/*" element={<NotFound />} />
        </Routes>
    </div>
);
}

export default App;
