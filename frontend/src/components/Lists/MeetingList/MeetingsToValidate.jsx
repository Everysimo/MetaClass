import React, { useEffect, useState } from "react";

export const MeetingsToValidate = () => {
    const [arrayMeeting, setArray] = useState([]);

    useEffect(() => {
        const fetchMeeting = async () => {
            const requestOption = {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + sessionStorage.getItem("token")
                },
            };

            try {
                const response = await fetch('http://localhost:8080/visualizzaQuestionari', requestOption);

                if (!response.ok) {
                    throw new Error('Errore nel recupero dei meeting senza questionari.');
                }

                const data = await response.json();
                setArray(data.value || []);
            } catch (error) {
                console.error('Errore durante il recupero dei meeting:', error.message);
                setArray([]);
            }
        };

        fetchMeeting();
    }, []);

    // Return an object with the array
    return { arrayMeeting };
};
