import React from 'react';
import { useSearchParams } from 'react-router-dom';

const Home = () => {
    const {searchParams, setSearchParams} = useSearchParams();
    
    return (
        <div>
            <h1>Home 페이지 입니다.</h1>
        </div>
    );
};

export default Home;