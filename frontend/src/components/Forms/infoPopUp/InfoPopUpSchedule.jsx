import React, { useState } from "react";
import './infoPopUp.css';

const InfoPopUpSchedule = () => {
    const [info, setInfo] = useState(false);

    const handleInfoMouseOver = () => {
        setInfo(true);
    };

    const handleInfoMouseLeave = () => {
        setInfo(false);
    };

    return {
        handleInfoMouseOver,
        handleInfoMouseLeave,
        popup: (
            <div className={`info ${info ? 'open' : ''}`}>
                <p>
                    Il range di minuti è dato dalla stima fatta dal modulo di intelligenza artificiale
                    per capire, in base alla valutazione degli utenti, qual è la durata ideale di un meeting
                </p>
            </div>
        ),
    };
};

export default InfoPopUpSchedule;