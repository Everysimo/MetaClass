// LoggedIn.js
import React, { useState } from 'react';
import '../css/MyApp.css';
import '../css/index.css';
import '../css/LoggedHome.css';
import { MyHeader } from '../components/Header/Header';
import { MyFooter } from "../components/Footer/Footer";
import LogoutButton from "../components/LogoutButton/logoutButton";
import { useNavigate } from 'react-router-dom';
import AvviaMeeting from "../components/GestioneMeeting/AvviaMeeting";
import CreaCategoria from "../components/CreaCategoria/CreaCategoria";
import CreaScenario from "../components/Forms/CreaScenarioForm/CreaScenario";
import ModificaScenario from "../components/Forms/CreaScenarioForm/ModificaScenario";
import AccediStanza from "../components/enterRoomPopup/accediStanza";

export const LoggedInHome = () => {
    const nome = sessionStorage.getItem('nome');
    const isAdmin = sessionStorage.getItem('isAdmin') === 'true';
    const [isAvviaMeetingVisible, setIsAvviaMeetingVisible] = useState(false);
    const [isVisibleScen, setIsVisibleScen] = useState(false);
    const [isVisibleModScen, setIsVisibleModScen] = useState(false);
    const [isVisibleCat, setIsVisibleCat] = useState(false);
    const [isVisibleAcc, setIsVisibleAcc] = useState(false);

    //fatte per prova per vedere se mi porta alla pagina tramite button
    const navigate = useNavigate();

    //funzione per andare alla pagina tramite onClick
    const handleGoToProfile= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/Account`);
    };

    const handleGoToRoomList= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/visualRoomList`);
    };
    const handleGoToCreateRoom= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/createroom`);
    };
    const closeAllComponents = () => {
        setIsAvviaMeetingVisible(false);
        setIsVisibleScen(false);
        setIsVisibleModScen(false);
        setIsVisibleCat(false);
        setIsVisibleAcc(false);
    };

    return (
        <>
            <header>
                <MyHeader />
            </header>
            <section className={"contentSec"}>
                <h1>BENTORNATO, {nome}</h1>
                <div className={"masterDiv"}>
                    <div className={"childDiv"}>
                        <button onClick={handleGoToProfile}>vai ad Account</button>
                        <button onClick={handleGoToRoomList}>vai all'elenco delle stanze</button>
                        <button onClick={handleGoToCreateRoom}>Crea stanza</button>
                        <button onClick={() => {
                            closeAllComponents();
                            setIsVisibleAcc(prevVisibility => !prevVisibility)
                        }}
                        >
                            Accedi Stanza
                        </button>
                        {isVisibleAcc &&
                            <AccediStanza onClose={() => setIsVisibleAcc(false)}/>
                        }
                    </div>
                    <div className={"childDiv"}>
                        <h5> IN QUANTO ORGANIZZATORE...</h5>
                        <button onClick={() => {
                            closeAllComponents();
                            setIsAvviaMeetingVisible(prevVisibility => !prevVisibility)
                        }}
                        >
                            AvviaMeeting
                        </button>
                        {isAvviaMeetingVisible &&
                            <AvviaMeeting id_meeting={1} onClose={() => setIsAvviaMeetingVisible(false)}/>
                        }
                    </div>
                    {isAdmin &&
                        <div className={"childDiv"}>
                            <h5> IN QUANTO ADMIN...</h5>

                            <button onClick={() => {
                                closeAllComponents();
                                setIsVisibleCat(prevVisibility => !prevVisibility)
                            }}>Crea Categoria
                            </button>
                            {isVisibleCat && <CreaCategoria onClose={() => setIsVisibleCat(false)}/>}

                            <button onClick={() => {
                                closeAllComponents();
                                setIsVisibleScen(prevVisibility => !prevVisibility)
                            }}>Crea Scenario
                            </button>
                            {isVisibleScen && <CreaScenario onClose={() => setIsVisibleScen(false)}/>}

                            <button onClick={() => {
                                closeAllComponents();
                                setIsVisibleModScen(prevVisibility => !prevVisibility)
                            }}>Modifica Scenario
                            </button>
                            {isVisibleModScen && <ModificaScenario onClose={() => setIsVisibleModScen(false)}/>}

                            <LogoutButton/>
                        </div>
                    }
                </div>
            </section>
            <footer>
            <MyFooter/>
            </footer>

        </>
    );
};
