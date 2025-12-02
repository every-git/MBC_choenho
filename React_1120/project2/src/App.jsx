import './App.css'
import Header from './component/Header'
import TodoEditor from './component/TodoEditor'
import TodoList from './component/TodoList'
import TestComp from './component/TestComp'
import { useState, useRef, useEffect } from 'react';

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



function App() {

  const [todo, setTodo] = useState(mockTodo);
  const idRef = useRef(3);

  /* 데이터 추가하기*/
  const onCreate = (content) => {
    const newItem = {
      id: idRef.current,
      isDone: false,
      content,
      createDate: new Date().getTime(),
    };
    setTodo([...todo, newItem]);
    idRef.current++;
  };
  

  /*데이터 수정하기*/
  const OnUpdate = (targetId) => {
    setTodo(
      todo.map(
        (it) => it.id === targetId ? { ...it, isDone: !it.isDone } : it
      )
    );
  };

  /*데이터 삭제하기*/
  const OnDelete = (targetId) => {
    setTodo(
      todo.filter((it) => it.id !== targetId)
    );
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
