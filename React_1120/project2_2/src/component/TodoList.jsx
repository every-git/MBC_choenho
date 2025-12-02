import React from 'react'
import './TodoList.css'
import TodoItem from './TodoItem'
import { useState } from 'react';
import { useContext } from 'react';
import { TodoContext } from '../App';

  const TodoList = () => {

  const {todo} = useContext(TodoContext);

  const [search, setSearch] = useState('');

  const onChangeSearch = (e) => {
    setSearch(e.target.value);
  };

  const getSearchResult = () => {
    return search === "" ?
            todo :
            todo.filter((it) => it.content.toLowerCase().includes(search.toLowerCase()));
  };

  return (
    <div className='TodoList'>
      <h3>✅ 할 일 목록</h3>
      <input 
        className='searchbar'
        onChange={onChangeSearch}
        //onClick={(e) => setSearch(e.target.value)}
        placeholder='검색어를 입력해주세요.' 
      />
      <div className='list_wrapper'>
      {
        getSearchResult().map(
          (it) => (
            <TodoItem key={it.id} {...it} />
          )
        )
      }
      </div>
    </div>
  )
}

export default TodoList