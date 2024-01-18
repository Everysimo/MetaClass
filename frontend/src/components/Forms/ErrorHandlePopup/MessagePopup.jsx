import React, { useState } from "react";
import '../PopUpStyles.css';

export const MessagePopup = ({ message }) => {
    const [isVisible, setIsVisible] = useState(true);

    const handleClose = () => {
        setIsVisible(false);
    };

    return (
        <>
            {isVisible && (
                <div className={"modal"}>
                    <div className={"modal-content"}>
                        <span className={"close"} onClick={handleClose}>&times;</span>
                        <p>{message}</p>
                    </div>
                </div>
            )}
        </>
    );
}