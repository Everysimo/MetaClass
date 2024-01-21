import * as React from 'react';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DateCalendar } from '@mui/x-date-pickers/DateCalendar';
import dayjs from 'dayjs';
import './CalendarViewer.css';
import { MeetingListInRoom } from '../Lists/MeetingList/MeetingList'

export default function BasicDateCalendar() {
    const [selectedDate, setSelectedDate] = React.useState(null);

    const handleDateChange = (date) => {
        setSelectedDate(date);
    };

    const getDateRepresentation = (date) => {
        if (date && date.toDate instanceof Function) {
            const jsDate = date.toDate();
            return dayjs(jsDate).format('YYYY-DD-MM');
        } else if (typeof date === 'string') {
            return date;
        } else {
            return 'Invalid Date';
        }
    };

    return (
        <div className={"master whiteBg fitCont"}>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
                <DateCalendar onChange={handleDateChange} />
            </LocalizationProvider>
            {(
            <div className={"child list"}>
                <MeetingListInRoom formattedDate={getDateRepresentation(selectedDate)} />
            </div>
            )}
        </div>
    );
}


