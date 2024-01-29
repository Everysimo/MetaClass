import React, { useEffect, useState } from "react";
import { MyHeader } from "../components/Layout/Header/Header";
import { MyFooter } from "../components/Layout/Footer/Footer";
import { useParams } from "react-router-dom";
import { checkRole } from "../functions/checkRole";
import UserListInRoom from "../components/Lists/UserList/UserListInRoom";
import { faChalkboardUser } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
    MyModifyForm,
    RequestSection,
    SelectScenario,
    CalendarComp
} from "../components/Forms";
import MeetingList from "../components/Calendar/CalendarViewer";
import BannedUserList from "../components/Lists/UserList/BannedUserList";
import MeetinginRoon from "../components/Lists/MeetingList/MeetinginRoon";
import "../css/MyApp.css";
import ALT from "../img/imm_mare.png";
import FetchAI from "../components/Forms/ScheduleMeetingForm/fetchAI"; // Replace with your actual CSS file path

export const SingleRoom = () => {
    const { id: id_stanza } = useParams();
    const [stima, setStima] = useState(null); // Initialize stima as null

    useEffect(() => {
        const fetchStima = async () => {
            try {
                const result = await FetchAI(id_stanza);
                setStima(result);
            } catch (error) {
                console.error('Error fetching stima:', error);
            }
        };

        fetchStima(); // Call the fetchStima function

        // Other useEffect dependencies
    }, [id_stanza]);
    const [role, setRole] = useState("Partecipante");
    const [stanzaSingola, setStanzaSingola] = useState("");
    const [isOrganizer, setIsOrganizer] = useState(false);

    sessionStorage.setItem("idStanza", id_stanza);

    useEffect(() => {
        fetchSingleRoom();
        // eslint-disable-next-line
    }, [id_stanza]);

    const requestOption = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("token")
        },
    };

    const fetchSingleRoom = async () => {
        try {
            const response = await fetch(`http://localhost:8080/visualizzaStanza/${id_stanza}`, requestOption);

            if (!response.ok) {
                throw new Error('Errore nel recupero degli scenari.');
            }

            const data = await response.json();

            setStanzaSingola(data.value);
            console.log("Stanza singola;", data.value);
        } catch (error) {
            console.error('Errore durante il recupero degli scenari:', error.message);
        }
    };

    useEffect(() => {
        const fetchDataAndResize = async () => {
            try {
                const fetchedRole = await checkRole(id_stanza);
                setRole(fetchedRole);
                setIsOrganizer(fetchedRole === "Organizzatore" || fetchedRole === "Organizzatore_Master");
            } catch (error) {
                console.error(error);
            }
        };

        fetchDataAndResize();

        window.addEventListener("resize", resizeSideNav);
        return () => {
            window.removeEventListener("resize", resizeSideNav);
        };
    }, [id_stanza, role, isOrganizer]);

    const resizeSideNav = () => {
        // No need to manually resize the side-nav, it will dynamically adjust based on the flex properties in the CSS.
    };

    const isOrg = () => {
        return isOrganizer;
    };

    const importImage = (nome) =>{
        return require(`../img/${nome}`);
    }

    return (
        <>
            <header>
                <MyHeader />
            </header>
            <main className="main-container">
                <section className="roomSec" id="rSec">
                    <FontAwesomeIcon
                        icon={faChalkboardUser}
                        size="4x"
                        style={{ color: "#c70049" }}
                    />
                    <h1>{stanzaSingola.nome}</h1>
                    <div className="room-info">
                        <h2>CODICE STANZA: {stanzaSingola.codice}</h2>
                        <h4>SCENARIO SELEZIONATO: {
                            stanzaSingola &&
                            stanzaSingola.scenario &&
                            stanzaSingola.scenario.nome
                        }</h4>
                        {
                            stanzaSingola &&
                            stanzaSingola.scenario &&
                            stanzaSingola.scenario.image.nome &&
                            <img
                                style={{
                                    borderRadius: "25px",
                                    maxWidth: "300px",
                                    height: "auto"
                                }}
                                src={importImage(stanzaSingola.scenario.image.nome)}
                                alt={ALT}/>
                        }
                    </div>
                    <div className="masterDiv">
                        <MeetingList />
                        {isOrg() && (
                            <div className="childDiv">
                                <h2>Funzioni organizzatore:</h2>
                                <CalendarComp stima={stima}/>
                                <MyModifyForm />
                                <SelectScenario Id_stanza={id_stanza} />
                                <RequestSection id_stanza={id_stanza} />
                                <BannedUserList id_stanza={id_stanza} />
                            </div>
                        )}
                    </div>
                </section>
                <aside className="side-nav">
                    <div className="childDiv">
                        <UserListInRoom />
                        <MeetinginRoon />
                    </div>
                </aside>
            </main>
            <footer>
                <MyFooter />
            </footer>
        </>
    );
};