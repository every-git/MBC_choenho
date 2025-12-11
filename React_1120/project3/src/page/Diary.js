import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import useDiary from '../hooks/useDiary';
import { getFormattedDate } from '../util';
import Header from '../component/Header';
import Button from '../component/Button';
import Viewer from '../component/Viewer';

const Diary = () => {
    const {id} = useParams();
    const data = useDiary(id);    
    
    const navigate = useNavigate();

    const goBack = () => {
        navigate(-1);
    };

    const goEdit = () => {
        navigate(`/edit/${id}`);
    };

    if(!data) {
        return <div>일기 데이터 Loading...</div>;
    } else {
        const {date, content, emotionId} = data;
        const title = `${getFormattedDate(new Date(date))} 기록`;

        return (
            <div>
                <Header title={title} 
                leftChild={<Button text={"뒤로가기"} onClick={goBack} />} 
                rightChild={<Button text={"수정하기"} onClick={goEdit} />} />
                <div>{id}번 일기</div>
                <div>다이어리 페이지 입니다.</div>
                <Viewer content={content} emotionId={emotionId} />
            </div>
        );
    }
};

export default Diary;