import React from 'react';
import { useNavigate } from 'react-router-dom';

const NavigateToPageBtn = () => {
    const navigate = useNavigate();

    const navigateToSpecificPage = () => {
        // Replace '/specific-page' with the path of the page you want to navigate to
        navigate('/Account');
    };

    return (
        <div>
            <button onClick={navigateToSpecificPage}>Account</button>
        </div>
    );
};

export default NavigateToPageBtn;
