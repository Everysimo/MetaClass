import React, { useRef, useState } from "react";
import './slidingAccount.css';
import { Link } from "react-router-dom";
import LogoutButton from "../LogoutButton/logoutButton";
import { useNavigate } from "react-router-dom";
import Facebook from "../FacebookLogin/FacebookLoginButton";
import isLoggedIn from "../Header/BurgerButton/loginCheck";
import NavigateToPageBtn from "../RelocateButton/HandleRelocateButton";

export const UseSlidingAccount = () => {
    const [showModal, setShowModal] = useState(false);
    const name = sessionStorage.getItem('nome');
    const navigate = useNavigate();
    const timeoutRef = useRef(null);
    const handleMouseOver = () => {
        setShowModal(true);
        clearTimeout(timeoutRef.current);
    };

    const handleMouseLeave = () => {
        timeoutRef.current = setTimeout(() => {
            setShowModal(false);
        }, 200);
    };

    const handlePopupMouseOver = () => {
        clearTimeout(timeoutRef.current);
    };

    const handlePopupMouseLeave = () => {
        setShowModal(false);
    };

    return (
        <>
        {isLoggedIn()? (
            <>
            <Link
                to={"/Account"}
                onMouseOver={handleMouseOver}
                onMouseLeave={handleMouseLeave}
            >
                ACCOUNT
            </Link>
            <div
                className={`accountPopup ${showModal ? 'show' : ''}`}
                onMouseOver={handlePopupMouseOver}
                onMouseLeave={handlePopupMouseLeave}
            >
                <h3>Ciao, {name}</h3>
                <NavigateToPageBtn />
                <LogoutButton />
            </div>
            </>
            ) : (
            <>
                <Link
                    to={"/login"}
                    onMouseOver={handleMouseOver}
                    onMouseLeave={handleMouseLeave}
                >
                    LOGIN
                </Link>
                <div
                    className={`accountPopup ${showModal ? 'show' : ''}`}
                    onMouseOver={handlePopupMouseOver}
                    onMouseLeave={handlePopupMouseLeave}
                >
                    <Facebook />
                </div>
            </>
            )}
        </>
    );
};

