import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../PopUpStyles.css';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {faCheck, faUserPen} from "@fortawesome/free-solid-svg-icons";

const EditUserDetails = ({ userDetails, setUserDetails }) => {
    const [showModal, setShowModal] = useState(false);
    const [formData, setFormData] = useState({});
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [formReset, setFormReset] = useState(false); // State for form reset

    const handleInputChange = (e) => {
        setErrorMessage(''); // Reset error message on input change

        if (e.target.name === 'dataDiNascita') {
            const selectedDate = new Date(e.target.value);
            const currentDate = new Date();

            if (selectedDate > currentDate) {
                setErrorMessage('La data di nascita non può essere nel futuro!');
                return;
            }

            const formattedDate = `${(selectedDate.getMonth() + 1)
                .toString()
                .padStart(2, '0')}/${selectedDate.getDate().toString().padStart(2, '0')}/${selectedDate.getFullYear()}`;

            setFormData({ ...formData, [e.target.name]: formattedDate });
        } else if (e.target.name === 'email') {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(e.target.value)) {
                setErrorMessage('Formato email non valido');
                return;
            }
            setFormData({ ...formData, [e.target.name]: e.target.value });
        } else {
            setFormData({ ...formData, [e.target.name]: e.target.value });
        }
    };


    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const token = sessionStorage.getItem('token');
            if (!token) {
                throw new Error('Token not found');
            }

            // Validate at least one field is filled
            if (!Object.values(formData).some(value => value !== undefined && value !== '')) {
                setErrorMessage('At least one field should be filled');
                return;
            }

            const response = await axios.post(
                'http://localhost:8080/modifyUserData',
                formData,
                {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + token
                    }
                }
            );

            console.log('Update response:', response.data);

            if (response.status === 200 && response.data.value) {
                setSuccessMessage(response.data.message);
                setShowModal(true);
                setUserDetails({ ...userDetails, ...formData }); // Update userDetails with the form data
                setErrorMessage(''); // Clear error message on success
            } else {
                setSuccessMessage(response.data.message || 'Changes were unsuccessful');
                setShowModal(true);
            }
        } catch (error) {
            setSuccessMessage(error.response.data.message);
            setShowModal(true);
        }
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setFormReset(true);// Set form reset to true when modal is closed

    };

    useEffect(() => {
        if (formReset) {
            setFormData({}); // Reset form data
            setSuccessMessage(''); // Reset success message
            setErrorMessage(''); // Reset error message
            setFormReset(false); // Reset formReset state
        }
    }, [formReset]);

    return (
        <div className={"childDiv"}>
            <button
                onClick={() => setShowModal(true)}
                className={"minWidth200"}
            >
                Modifica <FontAwesomeIcon icon={faUserPen} size="lg" style={{ color: "#ffffff" }} />
            </button>

            {showModal && (
                <div className="modal">
                    <div className="modal-content">
                        <span className="close" onClick={handleCloseModal}>&times;</span>
                        {successMessage ? (
                            <p className="success-message">{successMessage} <FontAwesomeIcon icon={faCheck} size="2xl" style={{color: "#63E6BE",}} /></p>
                        ) : (
                            <form onSubmit={handleSubmit}>
                                {/* Editable fields */}
                                <input
                                    type="text"
                                    name="nome"
                                    placeholder={userDetails ? userDetails.nome : 'Enter Name'}
                                    onChange={handleInputChange}
                                />
                                <input
                                    type="text"
                                    name="cognome"
                                    placeholder={userDetails ? userDetails.cognome : 'Enter Surname'}
                                    onChange={handleInputChange}
                                />
                                <input
                                    type="text"
                                    name="email"
                                    placeholder={userDetails ? userDetails.email : 'Enter e-mail'}
                                    onChange={handleInputChange}
                                />
                                <input
                                    type="date"
                                    name="dataDiNascita"
                                    placeholder={userDetails ? userDetails.dataDiNascita : 'Enter Date of Birth'}
                                    onChange={handleInputChange}
                                />
                                <button type="submit">Salva</button>
                            </form>
                        )}
                        {errorMessage && (
                            <p className="error-message">{errorMessage}</p>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
};

export default EditUserDetails;
