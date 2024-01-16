// Popup.js

import React, { useState } from 'react';
import './loginForm.css';

const Popup = ({ isOpen, onClose }) => {
    return (
        <div className={`popup-container ${isOpen ? 'open' : ''}`} onClick={onClose}>
            <div className="popup-content" onClick={(e) => e.stopPropagation()}>
                {/* Your popup content goes here */}
                <p>This is your popup content.</p>
            </div>
        </div>
    );
};

export default Popup;

