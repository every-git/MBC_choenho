import React, { useContext } from 'react'
import './DiaryItem.css'
import { useNavigate } from 'react-router-dom';
import { getEmotionImgById, getFormattedDate } from '../util';
import Button from './Button';
import { DiaryDispatchContext } from '../App';

const DiaryItem = ({id, content, emotionId, date}) => {
    const navigate = useNavigate();
    const {onDelete} = useContext(DiaryDispatchContext);
    
    const goDetail = () => {
        navigate(`/diary/${id}`);
    };

    const goEdit = () => {
        navigate(`/edit/${id}`);
    };

    const goDelete = () => {
        if(window.confirm("정말 삭제하시겠습니까?")) {
            onDelete(id);
        }
    };

    return (
        <div className='DiaryItem'>
            <div className={['img_section', `img_section_${emotionId}`].join(" ")} onClick={goDetail}>
                <img src={getEmotionImgById(emotionId)} alt={`emotion${emotionId}`} />
            </div>

            <div className='info_section' onClick={goDetail}>
                <div className='date_wrapper'>
                    {new Date(date).toLocaleString()}
                </div>
                <div className='content_wrapper'>
                    {content.slice(0, 25)}
                </div>
            </div>

            <div className='button_section'>
                <Button text={'수정하기'} onClick={goEdit} />
                <Button text={'삭제하기'} onClick={goDelete} type={'negative'} />
            </div>
        
        </div>
    );
};

export default DiaryItem;