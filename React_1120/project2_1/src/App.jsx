import './App.css'
import Header from './component/Header'
import TodoEditor from './component/TodoEditor'
import TodoList from './component/TodoList'
import TestComp from './component/TestComp'
import { useReducer, useRef, useEffect } from 'react';

const mockTodo = [
  {
    id: 0,
    isDone: false,
    content: 'React 공부하기',
    createDate: new Date().getTime(),
  },
  {
    id: 1,
    isDone: false,
    content: '책 읽기',
    createDate: new Date().getTime(),
  },
  
  {
    id: 2,
    isDone: false,
    content: '운동하기',
    createDate: new Date().getTime(),
  },
];

function reducer(state, action) {
  
  switch(action.type) {
    case "CREATE":
      return [action.newItem, ...state];
    case "UPDATE":
      return state.map(it => it.id === action.id ? { ...it, isDone: !it.isDone } : it);
    case "DELETE":
      return state.filter(it => it.id !== action.id);
    default:
      return state;
  }
}


function App() {

  const [todo, dispatch] = useRef(reducer, mockTodo);
  const idRef = useRef(3);

  /* 데이터 추가하기*/
  const onCreate = (content) => {
    dispatch({
      type: "CREATE",
      newItem: {
        id: idRef.current,
        isDone: false,
        content,
        createDate: new Date().getTime()
      }
    });
    idRef.current++;
  };
  

  /*데이터 수정하기*/
  const OnUpdate = (targetId) => {
    dispatch({
      type: "UPDATE",
      id: targetId
    });
  };

  /*데이터 삭제하기*/
  const OnDelete = (targetId) => {
    dispatch({
      type: "DELETE",
      id: targetId
    });
  };

  return (
    <div className='App'>
      <TestComp />
      <Header />
      <TodoEditor onCreate={onCreate} />
      <TodoList todo={todo} OnUpdate={OnUpdate} OnDelete={OnDelete}/>
    </div>
  )
}

export default App
