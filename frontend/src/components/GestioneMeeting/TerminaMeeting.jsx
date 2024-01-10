import React, { useState } from 'react';
import "./MyModifyForm.css";
import {useParams} from "react-router-dom";

const TerminaMeeting = () => {
    const { id_meeting: id_meeting } = useParams();
    //si usa useParams per farsi passare il parametro
    const terminaMeeting = () => {
        console.log("sono nel button Invia");
        handleAvviaMeting();
    };

    const handleAvviaMeting = async () => {
        const requestOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            },
        };

        try {
            const response = await fetch(`http://localhost:8080/terminaMeeting/${id_meeting}`, requestOption);
            const responseData = await response.json();
            console.log("Risposta del server:", responseData);
        } catch (error) {
            console.error('Errore:', error);
        }
    };

    return (
        <>
            <div className="card-content">
                <div className="button-container">
                    <button type="button" onClick={terminaMeeting}> TerminaMeeting </button>
                </div>
            </div>
        </>
    );
};
export default TerminaMeeting;