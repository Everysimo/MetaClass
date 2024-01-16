import React, { useState } from 'react';
import './menu-button.css';
import MCLogo from "../../../img/commigo.png";
import { Link } from "react-router-dom";
import isLoggedIn from "./loginCheck";
import {UseSlidingAccount} from "../../SlidingAccount/slidingAccount";

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
                <nav>

                <ul>
                    {isLoggedIn()? (
                        <>
                            <li><Link to="/Account">ACCOUNT</Link></li>
                            <li><Link to="/LoggedInHome">HOME</Link></li>
                        </>
                    ) : (
                        <>
                            <li><Link to="/login">LOGIN</Link></li>
                            <li><Link to="/">HOME</Link></li>
                        </>
                    )}
                    <li><Link to="/">ABOUT</Link></li>
                    <li><Link to="/">CONTACTS</Link></li>
                </ul>
                <img src={MCLogo} className='App-logo' alt='nessuna immagine' id={"menu2-image"} />
                </nav>
            </div>
        </div>
    );
};

export function MyMenu() {
    return (
        <nav>
            {isLoggedIn()? (
                <Link to="/LoggedInHome">HOME</Link>
            ) : (
                <Link to="/">HOME</Link>
            )}
            <UseSlidingAccount />
            <Link to="/">ABOUT</Link>
            <Link to="/">CONTACTS</Link>
        </nav>
    );
}

export default BurgerButton;

