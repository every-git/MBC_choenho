import React, { useContext } from 'react'
import './TodoItem.css'
import { TodoContext } from '../App'

const TodoItem = ({id, content, isDone, createDate }) => {

    const {OnUpdate, OnDelete} = useContext(TodoContext);
    
  return (
    <div className='TodoItem'>
        <div className='checkbox'>
            <input type="checkbox" 
            onClick={() => OnUpdate(id)}
            checked={isDone} />
        </div>
        <div className='title_col'>
            {content}
        </div>
        <div className='date_col'>
            {new Date(createDate).toDateString()}
        </div>
        <div className='btn_col'>
            <button onClick={() => OnDelete(id)}>삭제</button>
        </div>
    </div>
  )
}

export default TodoItem