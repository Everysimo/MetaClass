import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faRightFromBracket } from "@fortawesome/free-solid-svg-icons";
import React from "react";
import { useNavigate } from "react-router-dom";
import "./logoutButton.css";
import axios from 'axios';

function useHandleLogout() {
    const navigate = useNavigate();

    return async () => {
        try {
            const userMetaID = sessionStorage.getItem('UserMetaID'); // Fetch the UserMetaID
            const requestOptions = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + sessionStorage.getItem("token")
                },
            };
            // Change the method to GET for logout
            const response = await fetch('http://localhost:8080/Manuallogout', requestOptions);
            const responseData = await response.json();
            console.log('Server response:', responseData);
            // Log the message from the backend if the logout was successful
            if (response && response.data && response.data.successo) {
                console.log('Logout message from backend:', response.data.messaggio);
            }

            // Remove authentication-related items from localStorage
            sessionStorage.removeItem('UserMetaID'); // Remove the UserMetaID
            sessionStorage.setItem('isLoggedIn', 'false');

            navigate('/');
        } catch (error) {
            console.error('Error while logging out:', error);
        }
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
