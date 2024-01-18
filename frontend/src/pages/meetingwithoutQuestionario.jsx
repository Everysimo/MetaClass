import {MyHeader} from "../components/Layout/Header/Header";
import {MyFooter} from "../components/Layout/Footer/Footer";
import {useEffect, useState} from "react";

export const MeetingXQuestionario = () => {

    const {array, setArray} = useState([]);


    useEffect(() => {
        fetchMeeting();
    }, []);

    const requestOption = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("token")
        },
    };

    const fetchMeeting = async () => {
        try {
            const response = await fetch('http://localhost:8080/visualizzaQuestionari', requestOption);

            if (!response.ok) {
                throw new Error('Errore nel recupero dei meeting senza questionari.');
            }

            const data = await response.json();
            console.log("array dei meeting", data);


        } catch (error) {
            console.error('Errore durante il recupero deim:', error.message);
        }
    };

    return(
        <>
            <header>
                <MyHeader/>
            </header>

            form

            <footer>
                <MyFooter/>
            </footer>
        </>
    );

}
