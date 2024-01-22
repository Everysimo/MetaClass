import React, { useEffect, useState } from "react";
import { MyHeader } from "../components/Layout/Header/Header";
import { MyFooter } from "../components/Layout/Footer/Footer";
import { useNavigate, useParams } from "react-router-dom";
import CalendarComp from "../components/Forms/ScheduleMeetingForm/CalendarComp";
import { checkRole } from "../functions/checkRole";
import UserListInRoom from "../components/Lists/UserList/UserListInRoom";
import { faChalkboardUser } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import AvviaMeeting from "../components/Buttons/GestioneMeetingButtons/AvviaMeeting";
import MyModifyForm from "../components/Forms/ModifyRoomForm/MyModifyForm";
import MeetingList from "../components/Calendar/CalendarViewer";
import RequestSection from "../components/Forms/AccessRequest/RequestSection";
import SelectScenario from "../components/Forms/SelectNewScenario/SelectScenario";
import BannedUserList from "../components/Lists/UserList/BannedUserList";
import MeetingListRoom from "../components/Lists/MeetingList/MeetinginRoon";

export const SingleRoom = () => {
    const navigate = useNavigate();
    const { id: id_stanza } = useParams();
    const [role, setRole] = useState("Partecipante"); // Default role value
    const [stanzaSingola, setStanzaSingola] = useState("");

    sessionStorage.setItem("idStanza", id_stanza);

    useEffect(() => {
        fetchSingleRoom();
        // eslint-disable-next-line
    }, []);

    const requestOption = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("token")
        },
    };
    const fetchSingleRoom = async() => {
        try {
            const response = await fetch(`http://localhost:8080/visualizzaStanza/${id_stanza}`, requestOption);

            if (!response.ok) {
                throw new Error('Errore nel recupero degli scenari.');
            }

            const data = await response.json();

            setStanzaSingola(data.value);
            console.log("Stanza singola;", data.value)


        } catch (error) {
            console.error('Errore durante il recupero degli scenari:', error.message);
        }
    };

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
                        style={{color: "#c70049"}}
                    />
                    <h1>Stanza {id_stanza}</h1>
                    <div style={{ border: '2px solid #ccc', borderRadius: '8px', padding: '10px', boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)' }}>
                        <h2>CODICE STANZA: {stanzaSingola.codice}</h2>
                        <h4>
                            SCENARIO SELEZIONATO: {stanzaSingola && stanzaSingola.scenario && stanzaSingola.scenario.nome}
                        </h4>
                    </div>
                    <h2>Meetings programmati</h2>
                    <div className={"masterDiv"}>
                    <MeetingList/>
                    {!isOrg() && (
                        <>
                            <div className={"childDiv"}>
                                <h2>Funzioni organizzatore:</h2>
                                <CalendarComp/>
                                <MyModifyForm/>
                                <SelectScenario Id_stanza = {id_stanza}/>
                                <RequestSection id_stanza = {id_stanza} />
                                <BannedUserList id_stanza={id_stanza} />
                            </div>
                        </>
                    )}
                    </div>
                </section>
                <aside className="side-nav">
                    <div className={"childDiv"}>
                    <UserListInRoom />
                        <MeetingListRoom/>
                    </div>
                </aside>
            </main>
            <footer>
                <MyFooter />
            </footer>
        </>
    );
};
