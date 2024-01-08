// LoggedIn.js
import React from 'react';
import '../css/MyApp.css';
import '../css/index.css';
import '../css/LoggedHome.css';
import { MyHeader } from '../components/Header/Header';
import { MyFooter } from "../components/Footer/Footer";
import LogoutButton from "../components/LogoutButton/logoutButton";
import { useNavigate } from 'react-router-dom';
export const LoggedInHome = () => {
    const nome = localStorage.getItem('nome');

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

                        <h5> IN QUANTO ADMIN...</h5>
                        <button onClick={handleGoToBannedUserList}>Visualizza Lista Utenti Bannati</button>

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
