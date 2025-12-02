import './TodoEditor.css'
import { useState, useRef } from 'react';
import { useContext } from 'react';
import { TodoContext } from '../App';


const TodoEditor = () => {

  const {onCreate} = useContext(TodoContext);
  const [content, setContent] = useState('');
  const inputRef = useRef();

  const onChangeContent = (e) => {
    setContent(e.target.value);
  };

  const onSubmit = () => {
    if(!content) {
      inputRef.current.focus();
      return;
    }
    onCreate(content);
    setContent('');
  };

  const onKeyDown = (e) => {
    if(e.keyCode === 13) {
      onSubmit();
    }
  };

  return (
    <div className='TodoEditor'>
      <h3>✏️ 새로운 할 일 작성하기</h3>
      <div className='editor_wrapper'>
        <input 
          value={content}
          onChange={onChangeContent}
          ref={inputRef}
          onKeyDown={onKeyDown}
          placeholder='할 일을 입력해주세요.' 
        />
        <button onClick={onSubmit}>추가</button>
      </div>
    </div>
  )
}

export default TodoEditor