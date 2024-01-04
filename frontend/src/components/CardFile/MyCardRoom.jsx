
// CardComponent.js
import React from 'react';
import "./MyCardRoom.css"
const CardComponent = ({ name, id, description }) => {
    return (
        
        <div className="card">
            <h6>{name}</h6>
            <p>ID: {id}</p>
            <p>{description}</p>
        </div>
    );
};

export default CardComponent;
