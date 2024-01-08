import axios from "axios";

export const fetchUserDetails = async () => {
    try {
        const token = sessionStorage.getItem('token');
        if (!token) {
            throw new Error('Token not found');
        }

        const response = await axios.get('http://localhost:8080/userDetails', {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });

        console.log('Response data:', response.data); // Log the received data

        if (response.status === 200) {
            const { value, message } = response.data;

            if (value) {
                return value; // Return the user details from the 'value' field
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

