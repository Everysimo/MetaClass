import { useParams } from "react-router-dom";
import React, { useEffect, useState } from "react";
import dayjs from 'dayjs';  // Import dayjs
import './MeetingList.css';
import AvviaMeeting from "../../Buttons/GestioneMeetingButtons/AvviaMeeting";
import TerminaMeeting from "../../Buttons/GestioneMeetingButtons/TerminaMeeting";
import ModificaMeeting from "../../Forms/ScheduleMeetingForm/ModificaMeeting";
export const MeetingListInRoom = ({ formattedDate }) => {
    const [meetingList, setMeetingList] = useState([]);
    const [showModal, setShowModal] = useState(false);

    const { id: id_stanza } = useParams();

    useEffect(() => { fetchUserList(); }, []);


    const requestOption = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("token")
        },
    };

    const fetchUserList = async () => {
        try {
            const idStanza = id_stanza
            const response = await fetch(
                `http://localhost:8080/visualizzaSchedulingMeeting/${idStanza}`,
                requestOption
            );
            if (!response.ok) {
                throw new Error('Errore nella richiesta');
            }
            const data = await response.json();
            setMeetingList(data.value);
        } catch (error) {
            console.error('Errore durante il recupero della lista di meeting:', error);
        }
    }

    const handleOpen = ()=>{
        setShowModal(true);
    }
    const handleClose = () =>{
        setShowModal(false);
    }
    return (
        <>
            <h3>Meeting Schedulati:</h3>
            {meetingList && meetingList.map((meeting) => {
                // Conditionally render based on the formattedDate
                if (dayjs(meeting.inizio).format('YYYY-DD-MM') === formattedDate) {
                    return (
                        <>
                            <div
                                key={meeting.id}
                                className="meeting"
                                onClick={handleOpen}
                            >
                                <span>Nome: {`${meeting.nome} `}</span>
                                <span>Stato: {`${meeting.avviato ? 'Meeting Attivo' : 'Meeting Inattivo'} `}</span>
                                {/* Format meeting.inizio in 'YYYY-DD-MM' */}
                                <span>Inizio: {`${dayjs(meeting.inizio).format('YYYY-DD-MM')} `}</span>
                            </div>
                            {showModal &&
                                <div className={'modal'}>
                                <div className={'modal-content'}>
                                    <span className={'close'} onClick={handleClose}>
                                        &times;
                                    </span>
                                    <ModificaMeeting id_meeting={meeting.id} nome={meeting.nome} inizio={meeting.inizio} fine={meeting.fine} />
                                    {meeting.avviato ? (
                                        // Se meeting.avviato è true, mostra il pulsante "Termina Meeting"
                                        <TerminaMeeting id_meeting={meeting.id}/>
                                    ) : (
                                        // Se meeting.avviato è false, mostra il pulsante "Avvia Meeting"
                                        <AvviaMeeting id_meeting={meeting.id}/>
                                    )}
                                </div>
                            </div>
                        }
                        </>
                    );
                } else {
                    return null;  // Don't render if it doesn't match the formattedDate
                }
            })}
        </>
    );
};
