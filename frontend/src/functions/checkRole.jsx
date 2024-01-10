import axios from "axios";

export const checkRole = async (id_stanza) => {
    try {
        const token = sessionStorage.getItem('token');
        if (!token) {
            throw new Error('Token not found');
        }

        const response = await axios.get(`http://localhost:8080/getRuolo/${id_stanza}`, {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });

        console.log('Response data:', response.data); // Log the received data

        if (response.status === 200) {
            const role = response.data;

            if (role) {
                return role; // Return the user details from the 'value' field
            } else {
                throw new Error('No user details found');
            }
        } else {
            throw new Error('Failed to fetch user details');
        }
    } catch (error) {
        console.error(error);
        throw new Error('Failed to fetch user details');
    }
};

