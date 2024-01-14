import React, { useState, useEffect } from 'react';
import LogoutButton from '../components/LogoutButton/logoutButton';
import { fetchUserDetails } from '../functions/fetchUserDetails';
import { MyHeader } from "../components/Header/Header";
import { MyFooter } from "../components/Footer/Footer";
import EditUserDetails from '../components/Forms/ModifyUsersForm/EditUserDetails';
import {faUser} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

export const Account = () => {
    const [userDetails, setUserDetails] = useState();

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
    return (
        <>
            <header>
                <MyHeader />
            </header>
            <main className={"bg"}>
            <section className={"roomSec"}>
                <h1>Dettagli account</h1>
                    <div className={"user-details transWhiteBg"}>
                        <FontAwesomeIcon icon={faUser} size="2xl" style={{color: "#c70049"}}/>
                        {userDetails && (
                            <>
                                {/* Display current user details */}
                                <p><strong>Nome: </strong> {userDetails.nome}</p>
                                <p><strong>Cognome: </strong> {userDetails.cognome}</p>

                                <p><strong>Email: </strong> {userDetails.email}</p>
                                <p><strong>Data di nascita: </strong> {userDetails.dataDiNascita}</p>
                            </>
                        )}
                        <div className={"masterDiv"}>
                            <EditUserDetails userDetails={userDetails} setUserDetails={setUserDetails} />
                            <div className={"childDiv"}>
                                <LogoutButton />
                            </div>
                        </div>
                    </div>
            </section>
            </main>
            <footer>
                <MyFooter />
            </footer>
        </>
    );
};

export default Account;
