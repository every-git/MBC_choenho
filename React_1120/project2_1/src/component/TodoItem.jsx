import React from 'react'
import './TodoItem.css'

const TodoItem = ({id, content, isDone, createDate, OnUpdate, OnDelete}) => {

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