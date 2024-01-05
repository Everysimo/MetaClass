// UserCard.jsx
import React from 'react';

const UserCard = ({ user }) => {
    return (
        <div style={{ border: '1px solid #ccc', padding: '10px', margin: '10px', borderRadius: '8px' }}>
            <h3>User ID: {user.id}</h3>
            <p>Name: {user.name}</p>
            <p>Lunghezza: {user.lunghezzapene}</p>
        </div>
    );
};

export default UserCard;
