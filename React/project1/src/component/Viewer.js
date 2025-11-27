import React, { useState } from 'react';

const Viewer = (props) => {
    return (
        <div>
            <div>현재 카운터: </div>
            <h1>{props.count}</h1>
        </div>
    );
};

export default Viewer;