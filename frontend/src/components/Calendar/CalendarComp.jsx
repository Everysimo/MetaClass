import React, { useState, useEffect } from 'react';
import dayjs from 'dayjs';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { MultiInputDateTimeRangeField } from '@mui/x-date-pickers-pro/MultiInputDateTimeRangeField';
import './CalendarComp.css';
import axios from 'axios';
import { MessagePopup } from '../Forms/ErrorHandlePopup/MessagePopup'; // Import the MessagePopup component

const CalendarComp = () => {
    const [showModal, setShowModal] = useState(false);
    const [name, setName] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [selectedDateTimeRange, setSelectedDateTimeRange] = useState([
        dayjs('2024-01-12T15:30'),
        dayjs('2024-01-12T18:30'),
    ]);

    const handleDateTimeRangeChange = (newDateTimeRange) => {
        setSelectedDateTimeRange(newDateTimeRange);
    };

    const handleSubmit = async () => {
        try {
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
                }
            );
            setSuccessMessage(response.data.message);
        } catch (error) {
            console.error('Error:', error.response.data.message);
            setErrorMessage(error.response.data.message || 'An error occurred');
        }
    };


    const handleShow = () =>{
        setShowModal(true);
    }
    const handleClose = () =>{
        setShowModal(false);
        setSuccessMessage('');
        setErrorMessage('');
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
                        <p>{successMessage}</p>
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
                                    onChange={(e) => setName(e.target.value)}
                                    placeholder="Enter name"
                                    className="inputField"
                                    style={{maxWidth: "430px", margin:"20px"}}
                                />
                                <MultiInputDateTimeRangeField
                                    value={selectedDateTimeRange}
                                    onChange={handleDateTimeRangeChange}
                                    ampm={false}
                                    className="dateTimePicker"
                                />
                            </div>
                            <button onClick={handleSubmit} id="submitBtn">
                                Submit
                            </button>
                            {errorMessage && (
                                <p>{errorMessage}</p>
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
