import React, { useState, useEffect } from 'react';
import '../css/MyApp.css';
import '../css/index.css';
import '../css/LoggedHome.css';
import { MyHeader } from "../components/Header/Header"
import { MyFooter } from '../components/Footer/Footer';
import LogoutButton from '../components/LogoutButton/logoutButton';
import GenerateRows from '../components/GenerateRows';
import { fetchUserDetails } from '../functions/fetchUserDetails';

export const Account = () => {
    const [userDetails, setUserDetails] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await fetchUserDetails();
                setUserDetails(data);
            } catch (error) {
                // Handle error scenarios here
                console.error(error);
            }
        };

        fetchData();
    }, []);
    return (
        <>
            <header>
                <MyHeader />
            </header>
            <section className={"sec"} id={"sec1"}>
                <div className={"logoutForm"}>
                    <div className={"table-container"}>
                        {userDetails && (
                            <>
                                <GenerateRows userDetails={userDetails} />
                            </>
                        )}
                        <LogoutButton />
                    </div>
                </div>
            </section>
            <footer>
                <MyFooter />
            </footer>
        </>
    );
};
