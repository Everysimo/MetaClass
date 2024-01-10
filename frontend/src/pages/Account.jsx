import React, { useState, useEffect } from 'react';
import LogoutButton from '../components/LogoutButton/logoutButton';
import { fetchUserDetails } from '../functions/fetchUserDetails';
import { MyHeader } from "../components/Header/Header";
import { MyFooter } from "../components/Footer/Footer";
import EditUserDetails from '../components/Forms/ModifyUsersForm/EditUserDetails';

export const Account = () => {
    const [userDetails, setUserDetails] = useState();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await fetchUserDetails();
                setUserDetails(data);
                console.log(userDetails);
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
            <section className={"sec"}>
                <div className="user-details">
                    {userDetails && (
                        <>
                            {/* Display current user details */}
                            <p><strong>Name:</strong> {userDetails.nome}</p>
                            <p><strong>Surname:</strong> {userDetails.cognome}</p>
                            <p><strong>Email:</strong> {userDetails.email}</p>
                            <p><strong>Birthday:</strong> {userDetails.dataDiNascita}</p>
                            {/* Include other details as needed */}
                        </>
                    )}
                </div>
                {/* Pass setUserDetails as prop to EditUserDetails */}
                <EditUserDetails userDetails={userDetails} setUserDetails={setUserDetails} />
                <LogoutButton />
            </section>
            <footer>
                <MyFooter />
            </footer>
        </>
    );
};

export default Account;
