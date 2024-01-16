import React, { useEffect, useState } from "react";
import { MyHeader } from "../components/Layout/Header/Header";
import { MyFooter } from "../components/Layout/Footer/Footer";
import { useNavigate, useParams } from "react-router-dom";
import CalendarComp from "../components/Calendar/CalendarComp";
import { checkRole } from "../functions/checkRole";
import UserListInRoom from "../components/Lists/UserList/UserListInRoom";
import { faChalkboardUser } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import AvviaMeeting from "../components/Buttons/GestioneMeetingButtons/AvviaMeeting";
import MyModifyForm from "../components/Forms/ModifyRoomForm/MyModifyForm";
import MeetingList from "../components/Calendar/CalendarViewer";

export const SingleRoom = () => {
    const navigate = useNavigate();
    const { id: id_stanza } = useParams();
    const [role, setRole] = useState("Partecipante"); // Default role value
    sessionStorage.setItem("idStanza", id_stanza);

    useEffect(() => {
        const fetchDataAndResize = async () => {
            try {
                console.log("fetching");
                const fetchedRole = await checkRole(id_stanza);
                console.log(fetchedRole);
                setRole(fetchedRole.nome);
            } catch (error) {
                console.error(error);
                // Handle error fetching role, set role state accordingly if needed
            }
        };

        fetchDataAndResize(); // Fetch the role when the component mounts

        window.addEventListener("resize", resizeSideNav);
        return () => {
            window.removeEventListener("resize", resizeSideNav);
        };
    }, [id_stanza]); // Run effect only when id_stanza changes

    useEffect(() => {
        resizeSideNav(); // Resize side nav when the role changes
    }, [role]); // Run effect only when role changes

    const resizeSideNav = () => {
        const mainSection = document.querySelector(".roomSec");
        const sideNav = document.querySelector(".side-nav");

        if (mainSection && sideNav) {
            const mainHeight = window.getComputedStyle(mainSection).height;
            sideNav.style.maxHeight = mainHeight;
        }
    };

    const handleGoToChangeScenario = () => {
        navigate(`/changescenario/${id_stanza}`);
    };
    const handleGoToAccessManagement = () => {
        navigate(`/accessManagement/${id_stanza}`);
    };

    const handleGoToBannedUserList = () => {
        navigate(`/bannedUserList/${id_stanza}`);
    };

    const isOrg = () => {
        return role === "Partecipante";
    };

    return (
        <>
            <header>
                <MyHeader />
            </header>
            <main>
                <section className={"roomSec"} id={"rSec"}>
                    <FontAwesomeIcon
                        icon={faChalkboardUser}
                        size="4x"
                        style={{ color: "#c70049" }}
                    />
                    <h1>Stanza {id_stanza}</h1>
                    <h2>Meetings programmati</h2>
                    <MeetingList />
                    {!isOrg() && (
                        <>
                            <div className={"masterDiv"}>
                                <div className={"childDiv"}>
                                    <h2>Schedula un nuovo meeting</h2>
                                    <CalendarComp />
                                </div>
                                <div className={"childDiv"}>
                                    <MyModifyForm />
                                    <button onClick={handleGoToChangeScenario}>
                                        Modifica lo scenario della stanza
                                    </button>
                                    <button onClick={handleGoToAccessManagement}>
                                        Gestione degli accessi
                                    </button>
                                    <button onClick={handleGoToBannedUserList}>
                                        Visualizza Lista Utenti Bannati
                                    </button>
                                </div>
                            </div>
                            <div className={"masterDiv"}>
                                <AvviaMeeting id_meeting={id_stanza} />
                            </div>
                        </>
                    )}
                </section>
                <aside className="side-nav">
                    <div className={"childDiv"}>
                        <UserListInRoom />
                    </div>
                </aside>
            </main>
            <footer>
                <MyFooter />
            </footer>
        </>
    );
};
