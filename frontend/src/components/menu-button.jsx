import React, { useState } from 'react';
import '../css/MenuStyles.css';
import MCLogo from "../img/MetaClassLogo.png";

const BurgerButton = () => {
    const [isOpen, setIsOpen] = useState(false);

    const handleClick = () => {
        setIsOpen(!isOpen);
    };

    const buttonClass = `hamburger-menu ${isOpen ? 'open' : ''}`;
    const menuClass = `menu2 ${isOpen ? 'menu2Open' : ''}`;

    return (
        <div className="responsiveMenu">
            <div className={buttonClass} onClick={handleClick}>
                <div className="line"></div>
                <div className="line"></div>
                <div className="line"></div>
            </div>
            <div className={menuClass}>
                <ul>
                    <li><a href="#" className="table-cell">HOME</a></li>
                    <li><a href="#" id="goToCorsi-resp" className="table-cell">CORSI</a></li>
                    <li><a href="#" id="goToFooter-resp" className="table-cell">CONTATTI</a></li>
                    <li><a href="#" className="table-cell">ABOUT US</a></li>
                </ul>
                <img src={MCLogo} className='App-logo' alt='no image' id={"menu2-image"}></img>;
            </div>
        </div>
    );
};

export function MyMenu(){
    return(
        <div>
            <a href="/login">LOGIN</a>
            <a href="/">HOME</a>
            <a href="/">ABOUT</a>
            <a href="/">CONTACTS</a>
        </div>
    );
}
export default BurgerButton;
