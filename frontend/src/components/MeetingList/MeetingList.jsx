import { useParams } from "react-router-dom";
import React, { useEffect, useState } from "react";
import '../UserList/UserList.css';
const MeetingListInRoom = () => {
    const [meetingList, setMeetingList] = useState([]);

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
            const response = await fetch(
                `http://localhost:8080/visualizzaMeeting/${id_stanza}`,
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
    return (
        <div>
            <h2>Meeting In Stanza:</h2>
            {meetingList && meetingList.map((meeting) => (
                <div key={meeting.id} className="user-card">
                    <span>Nome: {`${meeting.nome} `}</span><br/>
                    <span>Avviato: {`${meeting.is_avviato}`}</span>
                    <span>scenario: {`${meeting.id_scenario}`}</span>
                </div>
            ))}
        </div>
    );
};

export default MeetingListInRoom;