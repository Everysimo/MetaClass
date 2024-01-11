import React from 'react';
import { useNavigate } from 'react-router-dom';
import {faUser} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

const NavigateToPageBtn = () => {
    const navigate = useNavigate();

    const navigateToSpecificPage = () => {
        // Replace '/specific-page' with the path of the page you want to navigate to
        navigate('/Account');
    };

    return ( <button onClick={navigateToSpecificPage}>Account <FontAwesomeIcon icon={faUser} size="xl" style={{color: "#ffffff"}}/></button> );
};

export default NavigateToPageBtn;
