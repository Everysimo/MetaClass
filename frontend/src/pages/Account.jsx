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
    const Id = localStorage.getItem('ID'); // Replace with the actual user ID

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await fetchUserDetails(Id);
                setUserDetails(data);
            } catch (error) {
                // Handle error scenarios here
                console.error(error);
            }
        };

        fetchData();
    }, [Id]);
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
