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
import CreaScenario from "../components/CreaScenarioForm/CreaScenario";
import ModificaScenario from "../components/CreaScenarioForm/ModificaScenario";

export const LoggedInHome = () => {
    const nome = localStorage.getItem('nome');
    const [isAvviaMeetingVisible, setIsAvviaMeetingVisible] = useState(false);
    const [isVisibleScen, setIsVisibleScen] = useState(false);
    const [isVisibleModScen, setIsVisibleModScen] = useState(false);
    const [isVisibleCat, setIsVisibleCat] = useState(false);

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

    const handleGoToAccessManagement= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/accessManagement`);
    };

    const handleGoToBannedUserList= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/bannedUserList`);
    };





    return (
        <>
        <header>
            <MyHeader />
        </header>
        <section className={"sec"}>
            <div className={"table-container"}>
                <div className={"table-row"}>
                    <div className={"table-cell"}>

                        <button onClick={handleGoToProfile}>vai ad Account</button>
                        <button onClick={handleGoToRoomList}>vai all'elenco delle stanze</button>
                        <button onClick={handleGoToCreateRoom}>Crea stanza</button>

                        <h5> IN QUANTO ORGANIZZATORE...</h5>
                        <button onClick={handleGoToAccessManagement}>Gestione degli accessi</button>
                        <button onClick={() => setIsAvviaMeetingVisible(prevVisibility => !prevVisibility)}>AvviaMeeting </button>
                        {isAvviaMeetingVisible && <AvviaMeeting id_meeting={1} onClose={() => setIsAvviaMeetingVisible(false)} />}
                        <h5> IN QUANTO ADMIN...</h5>
                        <button onClick={handleGoToBannedUserList}>Visualizza Lista Utenti Bannati</button>

                        <button onClick={() => setIsVisibleCat(prevVisibility => !prevVisibility)}>Crea Categoria</button>
                        {isVisibleCat && <CreaCategoria onClose={() => setIsVisibleCat(false)} />}

                        <button onClick={() => setIsVisibleScen(prevVisibility => !prevVisibility)}>Crea Scenario</button>
                        {isVisibleScen && <CreaScenario onClose={() => setIsVisibleScen(false)} />}

                        <button onClick={() => setIsVisibleModScen(prevVisibility => !prevVisibility)}>Modifica Scenario</button>
                        {isVisibleModScen && <ModificaScenario onClose={() => setIsVisibleModScen(false)} />}

                        <LogoutButton/>
                    </div>
                </div>
            </div>
        </section>
            <footer>
                <MyFooter/>
            </footer>

        </>
    );
};
