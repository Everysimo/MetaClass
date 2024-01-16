// CommonParent.js
import React, { useState } from 'react';
import BurgerButton from '../Header/BurgerButton/menu-button';
import Popup from "../loginFormPopup/loginForm";
import MyMenu from "../Header/MyMenu/MyMenu";

const CommonParent = () => {
    const [isPopupOpen, setIsPopupOpen] = useState(false);

    const openPopup = () => {
        setIsPopupOpen(true);
    };

    const closePopup = () => {
        setIsPopupOpen(false);
    };

    return (
        <div>
            <BurgerButton openPopup={openPopup} />
            <MyMenu openPopup={openPopup} />
            <Popup isOpen={isPopupOpen} onClose={closePopup} />
        </div>
    );
};

export default CommonParent;
