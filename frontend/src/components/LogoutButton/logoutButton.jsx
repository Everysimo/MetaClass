import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faRightFromBracket} from "@fortawesome/free-solid-svg-icons";
import React from "react";
import {useNavigate} from "react-router-dom";
import "./logoutButton.css";

function useHandleLogout() {
    const navigate = useNavigate();
    return () => {
        localStorage.setItem('isLoggedIn', 'false');
        navigate('/');
    };
}
export default function LogoutButton() {
    const handleLogout = useHandleLogout();
    return(<button onClick={handleLogout} className={"logoutButton"}>Logout <FontAwesomeIcon icon={faRightFromBracket} size="xl" style={{color: "#ffffff",}}/></button>);
}