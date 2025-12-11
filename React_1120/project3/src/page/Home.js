import Editor from '../component/Editor';
import Header from '../component/Header';
import Button from '../component/Button';
import { useState, useContext, useEffect } from 'react';
import { DiaryStateContext } from '../App';
import { getMonthRangeByDate } from '../util';
import DiaryList from '../component/DiaryList';


const Home = () => {
    const data = useContext(DiaryStateContext);
    const [filteredData, setFilteredData] = useState([]);
    
    const [pivotDate, setPivotDate] = useState(new Date());

    const headerTitle = `${pivotDate.getFullYear()}년 ${pivotDate.getMonth() + 1}월`;
    
    const onIncreaseMonth = () => {
        setPivotDate(new Date(pivotDate.getFullYear(), pivotDate.getMonth() + 1, 1));
    };

    const onDecreaseMonth = () => {
        setPivotDate(new Date(pivotDate.getFullYear(), pivotDate.getMonth() - 1, 1));
    };

    useEffect(() => {
        const { beginTimeDate, endTimeDate } = getMonthRangeByDate(pivotDate);
        if(data.length > 0) {
            setFilteredData(data.filter(it => it.date >= beginTimeDate && it.date <= endTimeDate));
        } else {
            setFilteredData([]);
        }
    }, [data, pivotDate]);

    return (
        <div>
            <Header 
                title= {headerTitle} 
                leftChild={<Button text = {"<"} onClick={onDecreaseMonth} />} 
                rightChild={<Button text = {">"} onClick={onIncreaseMonth} />}
            />
            <DiaryList data = {filteredData} />
        </div>
    );
};

export default Home;