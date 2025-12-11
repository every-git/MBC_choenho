import React, { useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import useDiary from '../hooks/useDiary';
import { getFormattedDate } from '../util';
import Header from '../component/Header';
import Button from '../component/Button';
import Viewer from '../component/Viewer';
import { DiaryDispatchContext } from '../App';
import Editor from '../component/Editor';

const Edit = () => {
    const {id} = useParams();
    const data = useDiary(id);
    const navigate = useNavigate();
    const {onDelete, onUpdate} = useContext(DiaryDispatchContext);

    const onClickDelete = () => {
        if(window.confirm("정말 삭제하시겠습니까?")) {
            onDelete(id);
            navigate("/", { replace: true });
        }
    };

    const onClickUpdate = (state) => {
        if(window.confirm("정말 수정하시겠습니까?")) {
            onUpdate(id, state.date, state.content, state.emotionId);
            navigate("/", { replace: true });
        }
    };

    const goBack = () => {
        navigate(-1);
    };

    if(!data) {
        return <div>일기 데이터 Loading...</div>;
    } else {
        const {date, content, emotionId} = data;
        const title = `${getFormattedDate(new Date(date))} 기록`;
    }
    return (
        <div>
            <Header title={"일기 수정하기"} 
                leftChild={<Button text={"뒤로가기"} onClick={goBack} />} 
                rightChild={<Button text={"삭제하기"} onClick={onClickDelete} type={"negative"} />} />
            <Editor initData={data} onSubmit={onClickUpdate} />
        </div>
    );
};

export default Edit;