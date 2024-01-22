import { useParams } from "react-router-dom";
import React, { useEffect, useState } from "react";
import './MeetingList.css';
import '../../Forms/PopUpStyles.css';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAlignCenter} from "@fortawesome/free-solid-svg-icons";

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
            const idStanza = id_stanza;
            const response = await fetch(
                `http://localhost:8080/visualizzaSchedulingMeeting/${idStanza}`,
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
                    <button onClick={() => toggleButtons(meeting.id)}>
                        Options <FontAwesomeIcon icon={faAlignCenter} style={{color: "#ffffff",}}/>
                    </button>
                    <div className={`options-container${showButtonsMap[meeting.id] ? ' open' : ''}`}>
                        <button>Avvia Meeting</button>
                        { /*<button onClick={() => handleAvviaMeeting(user.id)}>Avvia Meeting</button>
                        <button onClick={() => handleTerminaMeeting(user.id)}>Termina Meeting</button>*/}
                    </div>
                </div>
            ))}
            {showSuccessPopup && (
                <div className={"modal"}>
                    <div className={"modal-content"}>
                        <span
                            className={"close"}
                            onClick={handleCloseSuccesPopUp}
                        >&times;</span>
                        <h3>{message}</h3>
                    </div>
                </div>
            )}
        </div>
    );
};

export default MeetingListRoom;