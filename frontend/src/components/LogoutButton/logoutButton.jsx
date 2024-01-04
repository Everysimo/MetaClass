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
            const userMetaID = localStorage.getItem('UserMetaID'); // Fetch the UserMetaID

            // Change the method to GET for logout
            const response = await axios.get('http://localhost:8080/logout', {
                params: { userMetaID } // Pass the UserMetaID as a query parameter
            });

            // Log the message from the backend if the logout was successful
            if (response && response.data && response.data.successo) {
                console.log('Logout message from backend:', response.data.messaggio);
            }

            // Remove authentication-related items from localStorage
            localStorage.removeItem('UserMetaID'); // Remove the UserMetaID
            localStorage.setItem('isLoggedIn', 'false');

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
