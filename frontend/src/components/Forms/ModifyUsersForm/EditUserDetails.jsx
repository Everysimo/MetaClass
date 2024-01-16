import React, {useEffect, useState} from 'react';
import axios from 'axios';
import '../PopUpStyles.css';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faUserPen} from "@fortawesome/free-solid-svg-icons";

const EditUserDetails = ({ userDetails, setUserDetails }) => {
    const [showModal, setShowModal] = useState(false);
    const [formData, setFormData] = useState({});
    const [successMessage, setSuccessMessage] = useState('');
    const [formReset, setFormReset] = useState(false); // State for form reset

    const handleInputChange = (e) => {
        if (e.target.name === 'dataDiNascita') {
            const date = new Date(e.target.value);
            const formattedDate = `${(date.getMonth() + 1)
                .toString()
                .padStart(2, '0')}/${date.getDate().toString().padStart(2, '0')}/${date.getFullYear()}`;
            setFormData({ ...formData, [e.target.name]: formattedDate });
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
            } else {
                setSuccessMessage(response.data.message || 'Changes were unsuccessful');
                setShowModal(true);
            }
        } catch (error) {
            console.error('Update error:', error);
            setSuccessMessage('Changes were unsuccessful');
            setShowModal(true);
        }
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setFormReset(true); // Set form reset to true when modal is closed
    };

    useEffect(() => {
        if (formReset) {
            setFormData({}); // Reset form data
            setSuccessMessage(''); // Reset success message
            setFormReset(false); // Reset formReset state
        }
    }, [formReset]);

    return (
        <div className={"childDiv"}>
            <button
                onClick={() => setShowModal(true)}
                className={"minWidth200"}
            >
                Modifica <FontAwesomeIcon icon={faUserPen} size="lg" style={{color: "#ffffff",}} />
            </button>

            {showModal && (
                <div className="modal">
                    <div className="modal-content">
                        <span className="close" onClick={handleCloseModal}>&times;</span>
                        {successMessage ? (
                            <p className="success-message">{successMessage}</p>
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
                                <button type="submit">Save Changes</button>
                            </form>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
};

export default EditUserDetails;

