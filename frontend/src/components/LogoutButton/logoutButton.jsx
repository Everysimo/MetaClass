import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faRightFromBracket } from "@fortawesome/free-solid-svg-icons";
import React from "react";
import { useNavigate } from "react-router-dom";
import "./logoutButton.css";
import axios from 'axios';

async function callLogoutAPI() {
    try {
        const response = await axios.get('http://localhost:8080/logout');

        if (response.status === 200) {
            const { successo, messaggio } = response.data;

            if (successo) {
                console.log('Logged out successfully from the server');
            } else {
                console.error(`Failed to logout from the server: ${messaggio}`);
            }
        } else {
            console.error('Failed to logout from the server');
        }
    } catch (error) {
        console.error('Error while logging out:', error);
    }
}


function useHandleLogout() {
    const navigate = useNavigate();
    return async () => {
        // Call the logout API function
        await callLogoutAPI();

        // Update local storage and navigate
        localStorage.setItem('isLoggedIn', 'false');
        navigate('/');
    };
}

export default function LogoutButton() {
    const handleLogout = useHandleLogout();
    return (
        <button onClick={handleLogout} className={"logoutButton"}>
            Logout <FontAwesomeIcon icon={faRightFromBracket} size="xl" style={{ color: "#ffffff" }} />
        </button>
    );
}
