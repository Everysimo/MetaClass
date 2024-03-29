import React, { useState, useEffect } from 'react';
import dayjs from 'dayjs';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { MultiInputDateTimeRangeField } from '@mui/x-date-pickers-pro/MultiInputDateTimeRangeField';
import '../PopUpStyles.css'
import axios from 'axios';
import {faCheck, faRobot} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import InfoPopUpSchedule from "../infoPopUp/InfoPopUpSchedule";

const CalendarComp = ({ stima }) => {
    const infoPopup = InfoPopUpSchedule();
    const [showModal, setShowModal] = useState(false);
    const [name, setName] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [selectedDateTimeRange, setSelectedDateTimeRange] = useState([]);

    useEffect(() => {
        // Initialize the date of start and end, taking into account the offset
        const initialStartDate = new Date();
        const initialEndDate = new Date(initialStartDate.getTime() + stima * 60000);

        // Set the initial value for selectedDateTimeRange
        setSelectedDateTimeRange([dayjs(initialStartDate), dayjs(initialEndDate)]);
    }, [stima]);

    const handleDateTimeRangeChange = (newDateTimeRange) => {
        setSelectedDateTimeRange(newDateTimeRange);
    };
    const checkInput=(input)=>{
        if(input === ''){
            setErrorMessage("Il campo nome non può essere vuoto");
        }
        else if(!(input.charAt(0) === name.charAt(0).toUpperCase())) {
            setErrorMessage('Il nome del meeting deve cominciare con una lettera maiuscola');
        }
        else if (/[^[A-Z][A-Za-z0-9\s]*$]/.test(input)) {
            setErrorMessage('Il nome del meeting non può contenere caratteri speciali');
        }
        else setErrorMessage('');
    }
    const handleSubmit = async () => {
        if (name !== ''){
            try {
                console.log(selectedDateTimeRange)
                const [startDate, endDate] = selectedDateTimeRange.map((date) =>
                        date.format('YYYY-MM-DD HH:mm')
                );

                const token = sessionStorage.getItem('token');

                const meetingData = {
                    nome: name,
                    inizio: startDate,
                    fine: endDate,
                    id_stanza: sessionStorage.getItem('idStanza'),
                };
                const meetingDataJSON = JSON.stringify(meetingData);

                const response = await axios.post(
                    'http://localhost:8080/schedulingMeeting',
                    meetingDataJSON,
                    {
                        headers: {
                            'Content-Type': 'application/json',
                            Authorization: `Bearer ${token}`,
                        },
                    });
                setSuccessMessage(response.data.message);
            } catch (error) {
                console.error('Error:', error.response.data.message);
                setErrorMessage(error.response.data.message);
            }
        }
        else {
            setErrorMessage('Non lasciare campi vuoti');
        }
    };

    const handleShow = () =>{
        setShowModal(true);
    }
    const handleClose = () =>{
        setShowModal(false);
        if(successMessage){
            setSuccessMessage('');
            window.location.reload();
        }
        setErrorMessage('');
        setName('');
    }
    return (
        <>
        <button onClick={handleShow}>
            Schedula meeting
        </button>
            {showModal &&
                <div className={"modal"}>
                <div className={"modal-content"} style={{maxWidth: "400px", padding: "20px"}}>
                    <span className={"close"} onClick={handleClose}>
                        &times;
                    </span>
                    {successMessage ? (
                        <p>{successMessage} <FontAwesomeIcon icon={faCheck} size="2xl" style={{color: "#63E6BE",}} /></p>
                    ) : (
                        <LocalizationProvider
                            dateAdapter={AdapterDayjs}
                            dateFormats={{
                                // Set the default input format for the date
                                input: 'DD/MM/YYYY',
                            }}
                        >
                            <div className="dateTimePickerContainer">
                                <input
                                    type="text"
                                    value={name}
                                    onChange={(e) => {
                                        setName(e.target.value)
                                        checkInput(e.target.value)
                                    }}
                                    placeholder="Nome meeting"
                                    className="inputField"
                                    style={{maxWidth: "100%"}}
                                />
                                <div
                                    style={{
                                        height: "auto",
                                        width: "20px",
                                        cursor: "pointer",
                                        marginInline: "auto"
                                    }}
                                    onMouseOver={infoPopup.handleInfoMouseOver}
                                    onMouseLeave={infoPopup.handleInfoMouseLeave}
                                >
                                <FontAwesomeIcon
                                    className={"infoIcon"}
                                    icon={faRobot}
                                    size="2xl"
                                    style={{
                                        color: "#c70049",
                                        marginTop: "10px"
                                    }}/>
                                </div>
                                {infoPopup.popup}
                                <p style={{fontSize: "14px", textAlign: "center"}}>
                                    Stima fatta con il modulo di IA: {stima} minuti
                                </p>
                                <MultiInputDateTimeRangeField
                                    value={selectedDateTimeRange}
                                    onChange={handleDateTimeRangeChange}
                                    ampm={false}
                                    className="dateTimePicker"
                                />
                            </div>
                            <button onClick={handleSubmit} id="submitBtn">
                                Invio
                            </button>
                            {errorMessage && (
                                <p className={"errorMsg"}>{errorMessage}</p>
                            )}
                        </LocalizationProvider>
                        )}
                    </div>
                </div>
                }
        </>
    );
};

export default CalendarComp;
