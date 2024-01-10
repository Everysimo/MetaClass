import React, {useEffect, useState} from "react";
import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import {useNavigate, useParams} from "react-router-dom";
import CalendarComp from "../components/Calendar/CalendarComp";
import {checkRole} from "../functions/checkRole";

export const SingleRoom = () =>{
    const navigate = useNavigate();
    const { id: id_stanza } = useParams();
    const [role, setRole] = useState(false); // Default role value
    sessionStorage.setItem('idStanza', id_stanza);
    useEffect(() => {
        const fetchRole = async () => {
            try {
                const fetchedRole = await checkRole(id_stanza);
                setRole(fetchedRole === 'Organizzatore' || 'Organizzatore_Master'); // Update role state
            } catch (error) {
                console.error(error);
                // Handle error fetching role, set role state accordingly if needed
            }
        };

        fetchRole(); // Fetch the role when the component mounts
    }, [id_stanza]); // Run effect whenever id_stanza changes


    const handleGoToUserList= () => {
        // Naviga alla pagina di destinazione con il valore 42
        navigate(`/userListRoom/ ${id_stanza}`);
    };

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
                <MyHeader />
            </header>
            <section className={"contentSec"}>
                <h1>Stanza {id_stanza}</h1>
                <div className={"masterDiv"}>
                    {role &&
                        <div className={"childDiv"}>
                            <h2>Schedula un nuovo meeting</h2>
                            <CalendarComp/>
                        </div>
                    }
                    <div className={"childDiv"}>
                        <button onClick={handleGoToUserList}>Visualizza Lista Utenti in Stanza</button>
                        <button onClick={handleGoToModifyDataRoom}>Modifica la stanza</button>
                        <button onClick={handleGoToChangeScenario}>Modifica lo scenario della stanza</button>
                        <button onClick={handleGoToAccessManagement}>Gestione degli accessi</button>
                        <button onClick={handleGoToBannedUserList}>Visualizza Lista Utenti Bannati</button>
                    </div>
                </div>
            </section>
            <footer>
                <MyFooter/>
            </footer>
        </>
    );
}