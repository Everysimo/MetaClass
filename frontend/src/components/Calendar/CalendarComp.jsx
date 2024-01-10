import React, { useState } from 'react';
import dayjs from 'dayjs';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { MultiInputDateTimeRangeField } from '@mui/x-date-pickers-pro/MultiInputDateTimeRangeField';
import './CalendarComp.css';
import axios from "axios";
const CalendarComp = () => {
    const [selectedDateTimeRange, setSelectedDateTimeRange] = useState([
        dayjs('2024-04-17T15:30'),
        dayjs('2024-04-21T18:30'),
    ]);
    const [name, setName] = useState('');
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
                id_stanza: sessionStorage.getItem('idStanza')
            };

            const meetingDataJSON = JSON.stringify(meetingData);

            const response = await axios.post('http://localhost:8080/schedulingMeeting',
                meetingDataJSON, {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    }
                });

            console.log('API Response:', response.data);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    return (
        <LocalizationProvider
            dateAdapter={AdapterDayjs}
            dateFormats={{
                // Set the default input format for the date
                input: 'DD/MM/YYYY',
            }}>
            <div className="dateTimePickerContainer">
                <input
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="Enter name"
                    className="inputField"
                />
                <MultiInputDateTimeRangeField
                    value={selectedDateTimeRange}
                    onChange={handleDateTimeRangeChange}
                    ampm={false}
                    className="dateTimePicker"
                />
            </div>
            <button onClick={handleSubmit} id={"submitBtn"}>Submit</button>
        </LocalizationProvider>
    );
};

export default CalendarComp;



