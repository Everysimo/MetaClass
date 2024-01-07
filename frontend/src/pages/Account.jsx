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
            <section className={"sec"}>
                <div className={"table-container"}>
                    {userDetails && (
                        <div className={"user-details"}>
                            <p><strong>Name:</strong> {userDetails.name}</p>
                            <p><strong>Email:</strong> {userDetails.email}</p>
                            {/* Include other details as needed */}
                        </div>
                    )}
                    <LogoutButton/>
                </div>
                </section>
            <footer>
                <MyFooter />
            </footer>
        </>
    );
};
