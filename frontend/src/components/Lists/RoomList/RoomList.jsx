import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { checkOrg } from '../../../functions/checkRole';
import { faCrown } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const RoomList = () => {
    const [array, setArray] = useState([]);
    const [orgStatus, setOrgStatus] = useState({});

    useEffect(() => {
        const fetchElencoStanze = async () => {
            const requestOption = {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: 'Bearer ' + sessionStorage.getItem('token'),
                },
            };

            try {
                const response = await fetch(
                    'http://localhost:8080/visualizzaStanze',
                    requestOption
                );

                if (!response.ok) {
                    throw new Error('Errore nel recupero degli scenari.');
                }

                const data = await response.json();
                console.log("eccoli i dati delle stanze:", data);

                setArray(data.value);
                console.log("nell'array:", array);

                const orgStatusData = {};
                data.value.forEach((param) => {
                    const idStanza = param.id;
                    orgStatusData[idStanza] = false; // Default value, you might want to set it to true or false based on your logic
                });

                setOrgStatus(orgStatusData);
            } catch (error) {
                console.error(
                    'Errore durante il recupero degli scenari:',
                    error.message
                );
            }
        };

        fetchElencoStanze();
    }, []); // Empty dependency array means this effect runs once after the first render

    useEffect(() => {
        const fetchOrgStatus = async () => {
            const orgStatusData = {};

            for (const room of array) {
                try {
                    const role = await checkOrg(room.id);
                    orgStatusData[room.id] = role === true;
                } catch (error) {
                    console.error(error);
                    orgStatusData[room.id] = false; // or handle error based on your logic
                }
            }

            setOrgStatus(orgStatusData);
        };

        fetchOrgStatus();
    }, [array]); // Only run when the array state is updated

    return (
        <>
            {array.map((room, index) => (
                <div key={index} className={'user-card'}>
                    <h4>
                        Stanza: {room.nome}{' '}
                        {orgStatus[room.id] && (
                            <FontAwesomeIcon
                                icon={faCrown}
                                size="lg"
                                style={{ color: '#FFD43B' }}
                            />
                        )}
                    </h4>
                    {/* inserisci gli altri dati di cui hai bisogno */}
                    <button>
                        <Link
                            to={`/SingleRoom/${room.id}`}
                            style={{ textDecoration: 'none', color: 'inherit' }}
                        >
                            Vai alla pagina della stanza
                        </Link>
                    </button>
                </div>
            ))}
        </>
    );
};

export default RoomList;
