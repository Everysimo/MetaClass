import { useParams } from "react-router-dom";
import React, { useEffect, useState } from "react";
import './MeetingList.css';
import '../../Forms/PopUpStyles.css';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAlignCenter} from "@fortawesome/free-solid-svg-icons";
import dayjs from "dayjs";
import AvviaMeeting from "../../Buttons/GestioneMeetingButtons/AvviaMeeting";
import TerminaMeeting from "../../Buttons/GestioneMeetingButtons/TerminaMeeting";

const MeetingListRoom = () => {
    const [meetingList, setMeetingList] = useState([]);
    const [showButtonsMap, setShowButtonsMap] = useState({});
    const [showSuccessPopup, setShowSuccessPopup] = useState(false);
    const [message, setMessage] = useState('');

    const { id: id_stanza } = useParams();

    useEffect(() => { fetchMeetingList(); }, []);

    // Function to toggle button visibility for a specific user card
    const toggleButtons = (meetingId) => {
        setShowButtonsMap(prevState => ({
            ...prevState,
            [meetingId]: !prevState[meetingId] || false,
        }));
    };
    const requestOption = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("token")
        },
    };

    const fetchMeetingList = async () => {
        try {
            const response = await fetch(
                `http://localhost:8080/visualizzaSchedulingMeeting/${id_stanza}`,
                requestOption
            );
            if (!response.ok) {
                throw new Error('Errore nella richiesta');
            }
            const data = await response.json();
            console.log("Meeting schedulati:", data.value)

            setMeetingList(data.value);
        } catch (error) {
            console.error('Errore durante il recupero della lista:', error);
        }
    }

    const handleCloseSuccesPopUp = ()=> {
        setTimeout(() => {
            // Simuliamo il reindirizzamento dopo 2 secondi
            setShowSuccessPopup(false);
            // Aggiungi le azioni specifiche per il reindirizzamento
            window.location.replace(window.location.pathname);
        }, 1000);
    };


    return (
        <div>
            <h2>meeting In Stanza:</h2>
            {meetingList && meetingList.map((meeting) => (
                <div key={meeting.id} className="user-card">
                    <span>Nome: {`${meeting.nome} `}</span>
                    <span>Stato: {`${meeting.avviato ? 'Meeting Attivo' : 'Inattivo'} `}</span>
                    <span>Inizio: {`${dayjs(meeting.inizio).format('YYYY-DD-MM')} `}</span>
                    <button onClick={() => toggleButtons(meeting.id)}>
                        Opzioni <FontAwesomeIcon icon={faAlignCenter} style={{color: "#ffffff",}}/>
                    </button>
                    <div className={`options-container${showButtonsMap[meeting.id] ? ' open' : ''}`}>
                        {meeting.avviato ? (
                            // Se meeting.avviato è true, mostra il pulsante "Termina Meeting"
                            <TerminaMeeting id_meeting={meeting.id}/>
                        ) : (
                            // Se meeting.avviato è false, mostra il pulsante "Avvia Meeting"
                            <AvviaMeeting id_meeting={meeting.id}/>
                        )}
                    </div>
                </div>
            ))}
        </div>
    )
        ;
};

export default MeetingListRoom;