import React from "react";
import { NavLink, useNavigate } from "react-router-dom";

const Products = ({id, title, price, imgUrl, content }) => {
  const navigate = useNavigate();

  return (
    <div className="col-md-4" style={{ marginBottom: "50px" }}>
      <NavLink 
        className="c1" 
        to={`/detail/${id}`}
        onClick={(e) => {
          e.preventDefault();
          navigate(`/detail/${id}`);
        }}
      >
        <img src={`/${imgUrl}`} width="80%" alt={title} />
        <h5 style={{ marginTop: "10px" }}>{title}</h5>
        <p>{content}</p>
        <span>{price.toLocaleString()}원</span>
      </NavLink>
    </div>
  );
};

export default Products;
