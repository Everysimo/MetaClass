import React, { useState } from "react";
import './infoPopUp.css';

const InfoPopUp = () => {
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
                    Questo questionario serve per allenare il modulo di IA
                    e per fornire una stima più accurata della durata consigliata dei prossimi meetings
                </p>
            </div>
        ),
    };
};

export default InfoPopUp;
