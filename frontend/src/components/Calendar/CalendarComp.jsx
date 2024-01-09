import React, { useState } from 'react';
import dayjs from 'dayjs';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { MultiInputDateTimeRangeField } from '@mui/x-date-pickers-pro/MultiInputDateTimeRangeField';
import axios from "axios";

const CalendarComp = () => {
    const [selectedDateTimeRange, setSelectedDateTimeRange] = useState([
        dayjs('2022-04-17T15:30'),
        dayjs('2022-04-21T18:30'),
    ]);
    const [name, setName] = useState(''); // State for the 'name' input

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
                // Other properties of the Meeting object if needed
            };

            // Convert the object to a JSON string using JSON.stringify()
            const meetingDataJSON = JSON.stringify(meetingData);

            console.log(meetingDataJSON);
            // Make an Axios POST request to your API endpoint
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
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Enter name"
            />
            <MultiInputDateTimeRangeField
                value={selectedDateTimeRange}
                onChange={handleDateTimeRangeChange}
            />
            <button onClick={handleSubmit}>Submit</button>
        </LocalizationProvider>
    );
};

export default CalendarComp;


