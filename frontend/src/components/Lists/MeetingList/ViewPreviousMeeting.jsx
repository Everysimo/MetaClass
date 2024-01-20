import React, { useEffect, useState } from "react";
import { MeetingsToValidate } from "./MeetingsToValidate";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFileCircleCheck } from "@fortawesome/free-solid-svg-icons";
import Questionario from "../../Forms/CompilaQuestionarioForm/Questionario";

export const ViewPreviousMeeting = () => {
    const { arrayMeeting } = MeetingsToValidate();  // Access the array using anotherArray.arrayMeeting
    const [meetingArray, setMeetingArray] = useState([]);

    useEffect(() => {
        // Fetch meetingsArray from visualizzaMeetingPrecedenti
        fetchPreviousMeeting();
        // eslint-disable-next-line
    }, []);


    const fetchPreviousMeeting = async () => {
        try {
            const response = await fetch(`http://localhost:8080/visualizzaMeetingPrecedenti`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + sessionStorage.getItem("token")
                },
            });

            if (!response.ok) {
                throw new Error('Errore nella richiesta');
            }

            const data = await response.json();
            const meetings = data.value || [];
            setMeetingArray(meetings);
            console.log("arrayMeeting:", arrayMeeting);
            console.log("meetingArray:", meetingArray);

        } catch (error) {
            console.error('Errore durante il recupero della lista di meeting:', error);
        }
    };

    return (
        <>
            <h3>TUTTI I MIEI MEETING</h3>
            <div className={"table-container"}>
                <div className={"table-row"}>
                    <span className={"table-cell"}>Nome del Meeting:</span>
                    <span className={"table-cell"}>Inizio:</span>
                    <span className={"table-cell"}>Fine:</span>
                    <span className={"table-cell"}>ID della Stanza:</span>
                    <span className={"table-cell"}>Compila</span>
                </div>
                {meetingArray.map((meeting) => (
                    <div key={meeting.id} className={"table-row"}>
                        <span className={"table-cell"}>{meeting.nome}</span>
                        <span className={"table-cell"}>{meeting.inizio}</span>
                        <span className={"table-cell"}>{meeting.fine}</span>
                        <span className={"table-cell"}>{meeting.id_stanza.id}</span>
                        {/* Check if the current element is present in anotherArray.arrayMeeting */}
                        {(() => {
                            const isIdPresent = arrayMeeting.some(
                                anotherMeeting => anotherMeeting.id === meeting.id);
                            return (
                                <span className={"table-cell"}>
                                {isIdPresent ? (
                                    <Questionario id_meeting={meeting.id}/>
                                ) : (
                                    <FontAwesomeIcon icon={faFileCircleCheck} size="2xl" style={{ color: "#00f900" }} />
                                )}
                                </span>
                            );
                        })()}
                    </div>
                ))}
                </div>
        </>
    );
};
