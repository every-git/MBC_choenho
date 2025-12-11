import React from "react";
import { Nav } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const ComVeggie = (props) => {
    const { id, imgUrl, title, content, price } = props.veggie;
    const navigate = useNavigate();
    
    return (
    <div className="col-md-4" style={{marginBottom:"50px"}}>
        <Nav.Link className="c1" onClick={() => navigate(`/detail/veggie/${id}`)}>
            <img src={imgUrl} width="80%" alt={title} />
            <h5 style={{marginTop:"10px"}}>{title}</h5>
            <span>{content}</span>
            <p>{price}</p>
        </Nav.Link>
    </div>
    );
};

export default ComVeggie;
