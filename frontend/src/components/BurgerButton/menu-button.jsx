import React, { useState } from 'react';
import './menu-button.css';
import MCLogo from "../../img/MetaClassLogo.png";
import { Link } from "react-router-dom";
import isLoggedIn from "./loginCheck";

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
                    {isLoggedIn()? (
                        <li><Link to="/loggedIn">ACCOUNT</Link></li>
                    ) : (
                        <li><Link to="/login">LOGIN</Link></li>
                    )}
                    <li><Link to="/">HOME</Link></li>
                    <li><Link to="/">ABOUT</Link></li>
                    <li><Link to="/">CONTACTS</Link></li>
                </ul>
                <img src={MCLogo} className='App-logo' alt='no image' id={"menu2-image"} />
            </div>
        </div>
    );
};

export function MyMenu() {
    return (
        <div>
            {isLoggedIn() ? (
                <Link to="/loggedIn">ACCOUNT</Link>
            ) : (
                <Link to="/login">LOGIN</Link>
            )}
            <Link to="/">HOME</Link>
            <Link to="/">ABOUT</Link>
            <Link to="/">CONTACTS</Link>
        </div>
    );
}

export default BurgerButton;

