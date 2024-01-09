import React, { useState } from 'react';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider, StaticDateTimePicker } from '@mui/x-date-pickers';
import dayjs from 'dayjs';
import axios from 'axios';

const CalendarComp = () => {
    const [selectedDateTime, setSelectedDateTime] = useState(dayjs('2022-04-17T15:30'));

    const handleDateTimeChange = (newDateTime) => {
        setSelectedDateTime(newDateTime);
    };

    const handleSubmit = async () => {
        try {
            // Format selectedDateTime to match your API's expected format
            const formattedDateTime = selectedDateTime.format(); // Customize this according to your API's format

            // Make an Axios POST request to your API endpoint
            const response = await axios.post('YOUR_API_ENDPOINT', {
                dateTime: formattedDateTime, // Pass the formatted date/time to your API
                // Add other parameters as needed
            });

            // Handle response or any further logic based on API call
            console.log('API Response:', response.data);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    return (
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <StaticDateTimePicker
                value={selectedDateTime}
                onChange={handleDateTimeChange}
                renderInput={(params) => <input {...params} />}
                // Customize the component according to your preferences
            />
            <button onClick={handleSubmit}>Submit</button>
        </LocalizationProvider>
    );
};

export default CalendarComp;

