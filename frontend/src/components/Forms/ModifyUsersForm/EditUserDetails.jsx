import React, { useState } from 'react';
import axios from 'axios';
import './EditUserDetails.css';

const EditUserDetails = ({ userDetails, setUserDetails }) => {
    const [showModal, setShowModal] = useState(false);
    const [formData, setFormData] = useState({}); // Store edited fields here

    const handleInputChange = (e) => {
        // Update only the edited field in formData
        setFormData({ ...formData, [e.target.name]: e.target.value });
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

            // Assuming the API response contains updated user details directly
            if (response.status === 200) {
                setUserDetails(response.data); // Update userDetails in Account component
                setShowModal(false); // Hide the modal after updating
            }
        } catch (error) {
            console.error('Update error:', error);
            // Handle error scenarios
        }
    };

    return (
        <div>
            <button onClick={() => setShowModal(true)}>Modify Details</button>

            {showModal && (
                <div className="modal">
                    <div className="modal-content">
                        <span className="close" onClick={() => setShowModal(false)}>&times;</span>
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
                    </div>
                </div>
            )}
        </div>
    );
};

export default EditUserDetails;

