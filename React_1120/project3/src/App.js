import './App.css';
import { Routes, Route } from 'react-router-dom';
import Home from './page/Home';
import Diary from './page/Diary';
import New from './page/New';
import Edit from './page/Edit';

import { Link } from 'react-router-dom';

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/diary" element={<Diary />} />
        <Route path="/new" element={<New />} />
        <Route path="/edit" element={<Edit />} />
      </Routes>
      <div>
        <Link to="/">Home</Link>
        <Link to="/diary">Diary</Link>
        <Link to="/new">New</Link>
        <Link to="/edit">Edit</Link>
      </div>
    </div>
  );
}

export default App;
