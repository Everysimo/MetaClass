import React, { useState } from 'react';
import './menu-button.css';
import MCLogo from '../../../../img/commigo.png';
import { Link } from 'react-router-dom';
import { UseSlidingAccount } from '../SlidingAccount/slidingAccount';

const BurgerButton = ({ isLoggedIn }) => {
    const [isOpen, setIsOpen] = useState(false);
    const handleClick = () => {
        setIsOpen(!isOpen);
    };

    const buttonClass = `hamburger-menu ${isOpen ? 'open' : ''}`;
    const menuClass = `menu2 ${isOpen ? 'menu2Open' : ''}`;

    return (
        <div className='responsiveMenu'>
            <div className={buttonClass} onClick={handleClick}>
                <div className='line'></div>
                <div className='line'></div>
                <div className='line'></div>
            </div>
            <div className={menuClass}>
                <nav>
                    <ul>
                        {isLoggedIn ? (
                            <>
                                <li>
                                    <Link to='/Account'>ACCOUNT</Link>
                                </li>
                                <li>
                                    <Link to='/LoggedInHome'>DASHBOARD</Link>
                                </li>
                            </>
                        ) : (
                            <>
                                <li>
                                    <Link to='/login'>LOGIN</Link>
                                </li>
                                <li>
                                    <Link to='/'>HOME</Link>
                                </li>
                            </>
                        )}
                        <li>
                            <a href={'#footer'}>CONTATTI</a>
                        </li>
                    </ul>
                    <div className={"imgContainer"}>
                        <img src={MCLogo} className='App-logo' alt='nessuna immagine' id={'menu2-image'} />
                    </div>
                </nav>
            </div>
        </div>
    );
};

export function MyMenu() {
    return (
        <nav>
            <UseSlidingAccount />
            <a href={'#footer'}>CONTATTI</a>
        </nav>
    );
}

export default BurgerButton;
