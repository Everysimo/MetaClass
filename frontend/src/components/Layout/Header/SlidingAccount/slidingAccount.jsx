import React, { useRef, useState } from "react";
import './slidingAccount.css';
import { Link } from "react-router-dom";
import LogoutButton from "../../../Buttons/LogoutButton/logoutButton";
import Facebook from "../../../Buttons/FacebookLoginButton/FacebookLoginButton";
import isLoggedIn from "../Menu/loginCheck";
import NavigateToPageBtn from "../../../Buttons/RelocateButton/HandleRelocateButton";

export const UseSlidingAccount = () => {
    const [showModal, setShowModal] = useState(false);
    const name = sessionStorage.getItem('nome');
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
                <Link to="/LoggedInHome">DASHBOARD</Link>
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
                <Link to="/">HOME</Link>
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

