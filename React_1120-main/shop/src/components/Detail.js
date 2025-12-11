import { useParams } from "react-router-dom";
import { Nav } from "react-bootstrap";
import { useState, useEffect } from "react";
import { addItem } from "../store.js";
import { useDispatch } from "react-redux";
import { Button } from "react-bootstrap";
import { Link } from "react-router-dom";

function Detail(props) {
  let [tap, setTap] = useState(0);
  let { paramId } = useParams();
  //console.log(paramId);
  //const { imgUrl, title, content, price } = props.fruit[paramId];

  // dispatch 정의 추가
  const dispatch = useDispatch();
  // 상품 유효성 체크 (이건 Hook 호출 이후에 실행되어야 함)
  // ID 범위로 채소인지 과일인지 확인 (10 이상이면 채소)
  const paramIdNum = Number(paramId);
  const isVeggie = paramIdNum >= 10;
  let selproduct = null;
  if (isVeggie) {
    selproduct = props.veggie ? props.veggie.find((x) => x.id === paramIdNum) : null;
  } else {
    selproduct = props.fruit ? props.fruit.find((x) => x.id === paramIdNum) : null;
  }
  // 훅은 조건문(if) 아래에서 호출하면 안 됨 (React가 Hook 순서 기억을 못함)
  if (!selproduct) {
    return <div>해당 상품이 존재하지 않습니다.</div>;
  }
  const { id, imgUrl, title, content, price } = selproduct;
  console.log("내가 선택한 상품은: " + id + " " + title);

  return (
    <div className="container">
      <div className="row">
        <div className="col-md-6">
          <img src={"/" + imgUrl} width="100%" alt={title} />
          {/* <img src={process.env.PUBLIC_URL + '/' + imgUrl} width="100%" alt={title} /> */}
        </div>
        <div className="col-md-6">
          <h5 className="pt-5">{title}</h5>
          <p>{content}</p>
          <p>{price}</p>
          <button
            className="btn btn-danger"
            onClick={() => {
              // dispatch(addItem( {id : 1, imgurl : 'fruit1.jpg', name : 'Grey Yordan', count : 1}))
              let imgurlValue = imgUrl.replace("img/", "");
              dispatch(
                addItem({
                  id: id,
                  imgurl: imgurlValue,
                  name: title,
                  count: 1,
                })
              );
            }}
            style={{ marginRight: "10px" }}
          >
            주문하기
          </button>
          <Link to="/cart">
            <Button variant="outline-success"> 주문상품 확인하기 </Button>
          </Link>
        </div>
      </div>
      <Nav
        variant="tabs"
        defaultActiveKey="link0"
        style={{ marginTop: "50px" }}
      >
        <Nav.Item>
          <Nav.Link onClick={() => setTap(0)} eventKey="link0">
            버튼0
          </Nav.Link>
        </Nav.Item>
        <Nav.Item>
          <Nav.Link onClick={() => setTap(1)} eventKey="link1">
            버튼1
          </Nav.Link>
        </Nav.Item>
        <Nav.Item>
          <Nav.Link onClick={() => setTap(2)} eventKey="link2">
            버튼2
          </Nav.Link>
        </Nav.Item>
      </Nav>
      <div>{tap === 0 ? "내용0" : tap === 1 ? "내용1" : "내용2"}</div>
    </div>
  );
}

function TabContent({ tap }) {
  let [fade, setFade] = useState("");

  useEffect(() => {
    setTimeout(() => {
      setFade("end");
    }, 10);
    return () => {
      setFade("");
    };
  }, [tap]);

  return (
    <div className={"start " + fade}>
      {[<div>내용0</div>, <div>내용1</div>, <div>내용2</div>][tap]}
    </div>
  );
}

export default Detail;
