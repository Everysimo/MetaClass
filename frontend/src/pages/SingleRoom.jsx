import React, { useEffect, useState } from "react";
import { MyHeader } from "../components/Header/Header";
import { MyFooter } from "../components/Footer/Footer";
import { useNavigate, useParams } from "react-router-dom";
import CalendarComp from "../components/Calendar/CalendarComp";
import { checkRole } from "../functions/checkRole";
import UserListInRoom from "../components/UserList/UserListInRoom";
import {faChalkboardUser, faPlay} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import AvviaMeeting from "../components/GestioneMeeting/AvviaMeeting";
import MeetingListInRoom from "../components/MeetingList/MeetingList";

export const SingleRoom = () => {
    const navigate = useNavigate();
    const { id: id_stanza } = useParams();
    const [role, setRole] = useState("Partecipante"); // Default role value
    const [isAvviaMeetingVisible, setIsAvviaMeetingVisible] = useState(false);
    sessionStorage.setItem('idStanza', id_stanza);

    useEffect(() => {
        const fetchRole = async () => {
            try {
                console.log("fetching");
                const fetchedRole = await checkRole(id_stanza);
                setRole(
                    fetchedRole === "Organizzatore" || fetchedRole === "Organizzatore_Master" ?
                        fetchedRole : "Partecipante"
                );
                console.log(fetchedRole);
            } catch (error) {
                console.error(error);
                // Handle error fetching role, set role state accordingly if needed
            }
        };

        fetchRole(); // Fetch the role when the component mounts
    }, [id_stanza]); // Run effect whenever id_stanza changes

    useEffect(() => {
        const resizeSideNav = () => {
            const mainSection = document.querySelector('.roomSec');
            const sideNav = document.querySelector('.side-nav');

            if (mainSection && sideNav) {
                const mainHeight = window.getComputedStyle(mainSection).height;
                sideNav.style.maxHeight = mainHeight;
            }
        };

        window.addEventListener('resize', resizeSideNav);
        resizeSideNav();

        return () => {
            window.removeEventListener('resize', resizeSideNav);
        };
    }, []);

    const handleGoToModifyDataRoom= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/modifyroom/ ${id_stanza}`);
    };
    const handleGoToChangeScenario= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/changescenario/ ${id_stanza}`);
    };
    const handleGoToAccessManagement= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/accessManagement/ ${id_stanza}`);
    };

    const handleGoToBannedUserList= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/bannedUserList/ ${id_stanza}`);
    };

    return(
        <>
            <header>
                <MyHeader/>
            </header>
            <main>
                <section className={"roomSec"}>
                    <FontAwesomeIcon icon={faChalkboardUser} size="4x" style={{color: "#c70049",}} />
                    <h1>Stanza {id_stanza}</h1>
                    <div className={"masterDiv"}>
                        {role &&
                            <div className={"childDiv"}>
                                <h2>Schedula un
                                    nuovo meeting</h2>
                                <CalendarComp/>
                            </div>
                        }
                        <div className={"childDiv"}>
                            <button onClick={handleGoToModifyDataRoom}>Modifica la stanza</button>
                            <button onClick={handleGoToChangeScenario}>Modifica lo scenario della stanza</button>
                            <button onClick={handleGoToAccessManagement}>Gestione degli accessi</button>
                            <button onClick={handleGoToBannedUserList}>Visualizza Lista Utenti Bannati</button>
                        </div>
                    </div>
                    {role &&
                        <div className={"masterDiv"}>
                            <div className={"childDiv"}>
                                    <AvviaMeeting
                                        id_meeting={id_stanza}
                                    />

                            </div>
                        </div>
                    }
                </section>
                <aside className="side-nav">
                    <div className={"childDiv"}>
                        <UserListInRoom/>
                        <MeetingListInRoom/>
                    </div>
                </aside>
            </main>
            <footer>
                <MyFooter/>
            </footer>
        </>
    );
}