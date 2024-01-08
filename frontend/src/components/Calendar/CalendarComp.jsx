import { useState } from 'react';
import { DemoContainer } from '@mui/x-date-pickers/internals/demo';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import dayjs from 'dayjs';

export default function CalendarComp() {
    const [value, setValue] = useState(dayjs()); // Initial state with today's date

    return (
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DemoContainer components={['DateTimePicker']}>
                <DateTimePicker
                    label="Uncontrolled picker"
                    defaultValue={dayjs('2022-04-17T15:30')}
                />
                <DateTimePicker
                    label="Controlled picker"
                    value={value}
                    onChange={(newValue) => setValue(newValue)}
                />
            </DemoContainer>
        </LocalizationProvider>
    );
}
