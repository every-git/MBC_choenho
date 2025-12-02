import React from 'react'
import { useReducer } from 'react';

function reducer(state, action) {

    switch(action.type) {
        case 'INCREASE':
            return state + action.data;
        case 'DECREASE':
            return state + action.data;
        default:
            return state;
    }

}

const TestComp = () => {

    const [count, dispatch] = useReducer(reducer, 0);

    const ins = () => dispatch({
        type: 'INCREASE',
        data: 1
    });
    const dec = () => dispatch({
        type: 'DECREASE',
        data: -1
    });

  return (
    <div>
        <h3>TestComp</h3>
        <div>
            <strong>{count}</strong>
        </div>
        <div>
            <button onClick={ ins }>+</button>
            <button onClick={ dec }>-</button>
        </div>
    </div>
  )
}

export default TestComp