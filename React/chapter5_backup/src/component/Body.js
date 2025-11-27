import './Body.css';
const Body = () => {

    const number = 10;
    return (
        <div className="body">
            <h1 className="body-title">Body</h1>
            <h2 className="body-number">{number}</h2>
            <h2 className="body-number">{number + 1}</h2>
            <h2 className="body-number">{number + 2}</h2>
        </div>
    )
}

export default Body;