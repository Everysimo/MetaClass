import React, { useState, useEffect } from 'react';
import dayjs from 'dayjs';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { MultiInputDateTimeRangeField } from '@mui/x-date-pickers-pro/MultiInputDateTimeRangeField';
import '../PopUpStyles.css';
import axios from 'axios';
import { faCheck } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const ModificaMeeting = ({ id_meeting, nome: initialNome, inizio: initialInizio, fine: initialFine }) => {
    const [showModal, setShowModal] = useState(false);
    const [name, setName] = useState(initialNome || '');
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [selectedDateTimeRange, setSelectedDateTimeRange] = useState([
        dayjs(initialInizio || '2024-01-12T15:30'),
        dayjs(initialFine || '2024-01-12T18:30'),
    ]);

    const handleDateTimeRangeChange = (newDateTimeRange) => {
        setSelectedDateTimeRange(newDateTimeRange);
    };

    const handleSubmit = async () => {
        try {
            const [startDate, endDate] = selectedDateTimeRange.map((date) => date.format('YYYY-MM-DD HH:mm'));

            const token = sessionStorage.getItem('token');

            const meetingData = {
                nome: name,
                inizio: startDate,
                fine: endDate,
               // id_stanza: sessionStorage.getItem('idStanza'),
            };

            const meetingDataJSON = JSON.stringify(meetingData);
            console.log("la stringa json:", meetingDataJSON);

            const response = await axios.post(
                `http://localhost:8080/modifyScheduling/${id_meeting}`,  // Utilizzo l'id_meeting da props
                meetingDataJSON,
                {
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${token}`,
                    },
                }
            );
            setSuccessMessage(response.data.message);
        } catch (error) {
            console.error('Error:', error.response.data.message);
            setErrorMessage(error.response.data.message || 'An error occurred');
        }
    };

    const handleShow = () => {
        setShowModal(true);
    };

    const handleClose = () => {
        setShowModal(false);
        if(successMessage){
            setSuccessMessage('');
            window.location.reload();
        }
        setErrorMessage('');
    };

    return (
        <>
            <button onClick={handleShow}>Modifica meeting</button>
            {showModal && (
                <div className={'modal'}>
                    <div className={'modal-content'} style={{ maxWidth: '400px', padding: '20px' }}>
                        <span className={'close'} onClick={handleClose}>
                          &times;
                        </span>
                        {successMessage ? (
                            <p>
                                {successMessage} <FontAwesomeIcon icon={faCheck} size="2xl" style={{ color: '#63E6BE' }} />
                            </p>
                        ) : (
                            <LocalizationProvider
                                dateAdapter={AdapterDayjs}
                                dateFormats={{
                                    input: 'DD/MM/YYYY',
                                }}
                            >
                                <div className="dateTimePickerContainer">
                                    <input
                                        type="text"
                                        value={name}
                                        onChange={(e) => setName(e.target.value)}
                                        placeholder="Enter name"
                                        className="inputField"
                                        style={{ maxWidth: '100%', marginBottom: '20px'}}
                                    />
                                    <MultiInputDateTimeRangeField
                                        value={selectedDateTimeRange}
                                        onChange={handleDateTimeRangeChange}
                                        ampm={false}
                                        className="dateTimePicker"
                                    />
                                </div>
                                <button onClick={handleSubmit} id="submitBtn">
                                    Salva
                                </button>
                                {errorMessage && <p>{errorMessage}</p>}
                            </LocalizationProvider>
                        )}
                    </div>
                </div>
            )}
        </>
    );
};

export default ModificaMeeting;
