import React from 'react'
import './DiaryList.css'
import Button from './Button';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import DiaryItem from './DiaryItem';

const sortOptionList = [
    { value: "latest", name: "최신순" },
    { value: "oldest", name: "오래된 순" },
];

const DiaryList = ({ data }) => {
    const [sortType, setSortType] = useState('latest');
    const [sortedData, setSortedData] = useState(data);

    // 정렬 조건
    useEffect(() => {
        const compare = (a, b) => {
            if(sortType === 'latest') {
                return b.date - a.date;
            } else {
                return a.date - b.date;
            }
        };
        /* 깊은 복사
        {name: 'John', age: 20} -> js객체
        {"name": "John", "age": 20} -> JSON 문자열
        JSON.parse(JSON.stringify(data)) 는 data를 깊은 복사하여 새로운 객체를 만듭니다. 원본 데이터를 보존하기 위해 사용합니다.
        const copyList = data (얕은 복사) -> 원본 데이터를 변경하면 원본 데이터도 변경됩니다.
        */
        const copyList = JSON.parse(JSON.stringify(data));
        copyList.sort(compare);
        setSortedData(copyList);
        
    }, [data, sortType]);

    const onChangeSortType = (e) => {
        setSortType(e.target.value);
    };

    const navigate = useNavigate();

    const onClickNew = () => {
        navigate('/new');
    }; // onClickNew

    return (
        <div className='DiaryList'>
            <div className='menu_wrapper'>
                <div className='left_col'>
                    <select value={sortType} onChange={onChangeSortType}>
                        {sortOptionList.map((it, idx) => (
                            <option key={idx} value={it.value}>
                                {it.name}
                            </option>
                        ))}
                    </select>
                </div>
                <div className='right_col'>
                    <Button type={"positive"} text={"새 일기 쓰기"} onClick={onClickNew}/>
                </div>
            </div> {/* menu_wrapper */}
            <div className='list_wrapper'>
                {sortedData.map((it) => (
                    <DiaryItem key={it.id} {...it} />
                ))}
            </div> {/* list_wrapper */}
        </div>
    )
}

export default DiaryList;