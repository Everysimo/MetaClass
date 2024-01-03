import React from 'react';

const GenerateRows = ({ userDetails }) => {
    if (!userDetails) {
        return null;
    }

    const cells = Object.entries(userDetails).map(([key, value], index) => (
        <div className={"table-cell"} key={index}>
            <p><strong>{key}: </strong>{value}</p>
        </div>
    ));

    return <div className={"table-row"}>{cells}</div>;
};

export default GenerateRows;
