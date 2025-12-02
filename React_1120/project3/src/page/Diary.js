import React from 'react';
import { useParams } from 'react-router-dom';

const Diary = () => {
    const params = useParams();
    

    return (
        <div>
            <h1>Diary 페이지 입니다.</h1>
            {params}
        </div>
    );
};

export default Diary;