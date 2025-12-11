import { useContext, useState, useEffect } from 'react';
import { DiaryStateContext } from '../App';
import { useNavigate } from 'react-router-dom';

const useDiary = (id) => {
    const data = useContext(DiaryStateContext);
    const [diary, setDiary] = useState();
    const navigate = useNavigate();

    useEffect(() => {
        const matchDiary = data.find(it => it.id === id);
        if (matchDiary) {
            setDiary(matchDiary);
        } else {
            alert('일기를 찾을 수 없습니다.');
            navigate('/', { replace: true });
        }
    }, [id, data, navigate]);

    return diary;
};

export default useDiary;