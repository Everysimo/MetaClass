import React, { useState, useEffect } from 'react';
import LogoutButton from '../components/LogoutButton/logoutButton';
import { fetchUserDetails } from '../functions/fetchUserDetails';
import { MyHeader } from "../components/Header/Header";
import { MyFooter } from "../components/Footer/Footer";
import EditUserDetails from '../components/Forms/ModifyUsersForm/EditUserDetails';
import {faUser} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {useNavigate} from "react-router-dom";

export const Account = () => {
    const [userDetails, setUserDetails] = useState();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await fetchUserDetails();
                setUserDetails(data);
            } catch (error) {
                console.error(error);
            }
        };

        fetchData();
    }, []);

    useEffect(() => {
        console.log('Updated User Details:', userDetails);
    }, [userDetails]);

    const handlePreviousMeetingButton = () => {
        navigate('/previousMeeting');
    }

    return (
        <>
            <header>
                <MyHeader />
            </header>
            <section className={"sec"} id={"sec1"}>
                <h1 style={{color: "#ffffff"}}>Dettagli account</h1>
                    <div className={"user-details"}>
                        <FontAwesomeIcon icon={faUser} size="2xl" style={{color: "#002f53"}}/>
                        {userDetails && (
                            <>
                                {/* Display current user details */}
                                <p><strong>Name:</strong> {userDetails.nome}</p>
                                <p><strong>Surname:</strong> {userDetails.cognome}</p>
                                <p><strong>Email:</strong> {userDetails.email}</p>
                                <p><strong>Birthday:</strong> {userDetails.dataDiNascita}</p>
                            </>
                        )}
                        <div className={"masterDiv"}>
                            <EditUserDetails userDetails={userDetails} setUserDetails={setUserDetails} />
                            <button onClick={() => handlePreviousMeetingButton()}>Vai ai Meeting Precedenti</button>
                            <div className={"childDiv"}>
                                <LogoutButton />
                            </div>
                        </div>
                    </div>
            </section>
            <footer>
                <MyFooter />
            </footer>
        </>
    );
};

export default Account;
