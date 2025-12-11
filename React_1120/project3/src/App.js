import './App.css';
import { Link, Routes, Route } from 'react-router-dom';
import Home from './page/Home';
import Diary from './page/Diary';
import New from './page/New';
import Edit from './page/Edit';
import React, { useReducer, useRef, useEffect, useState } from 'react';

function reducer(state, action) {
  switch (action.type) {
    case 'INIT':
      return action.data;
    case 'CREATE':
      return [action.data, ...state];
    case 'UPDATE':
      return state.map(it => it.id === action.data.id ? action.data : it);
    case 'DELETE':
      return state.filter(it => it.id !== action.data.id);
    default:
      return state;
  }
}

const mockData = [
  {
    id: "mock1",
    date: new Date().getTime() - 1,
    content: "mock content 1",
    emotionId: 1,
  },
  {
    id: "mock2",
    date: new Date().getTime() - 2,
    content: "mock content 2",
    emotionId: 2,
  },
  {
    id: "mock3",
    date: new Date().getTime() - 3,
    content: "mock content 3",
    emotionId: 3,
  },
];

export const DiaryStateContext = React.createContext();
export const DiaryDispatchContext = React.createContext();

function App() {
  const [data, dispatch] = useReducer(reducer, []);
  const [isDataLoaded, setIsDataLoaded] = useState(false);
  const idRef = useRef(0);

  useEffect(() => {
    dispatch({
      type: 'INIT',
      data: mockData,
    });
    setIsDataLoaded(true);
  }, []);

  const onCreate = (date, content, emotionId) => {
    //추가부분
    const now = new Date();
    const selectedDate = new Date(date);
    selectedDate.setHours(now.getHours());
    selectedDate.setMinutes(now.getMinutes());
    selectedDate.setSeconds(now.getSeconds());
    
    dispatch({
      type: 'CREATE',
      data: {
        id: idRef.current,
        //수정부분(시간))
        date: selectedDate.getTime(),
        content,
        emotionId,
      }
    });
    idRef.current++;
  };

  const onUpdate = (id, date, content, emotionId) => {
    
    dispatch({
      type: 'UPDATE',
      data: {
        id: id,
        date: new Date(date).getTime(),
        content,
        emotionId,
      }
    });
    idRef.current++;
  };

  const onDelete = (targetId) => {
    dispatch({
      type: 'DELETE',
      data: {
        id: targetId,
      }
    });
  };

  if (!isDataLoaded) {
    return <div>Loading...</div>;
  } else {
    return (
      <DiaryStateContext.Provider value={ data }>
        <DiaryDispatchContext.Provider value={ { onCreate, onUpdate, onDelete } }>
        <div className="App">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/diary/:id" element={<Diary />} />
          <Route path="/new" element={<New />} />
          <Route path="/edit/:id" element={<Edit />} />
        </Routes>
        <div>
          <Link to="/">Home</Link>
          <Link to="/diary">Diary</Link>
          <Link to="/new">New</Link>
          <Link to="/edit">Edit</Link>
        </div>
        </div>
        </DiaryDispatchContext.Provider>
      </DiaryStateContext.Provider> 
    );
  }
}

export default App;
