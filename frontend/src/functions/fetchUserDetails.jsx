import axios from "axios";

export const fetchUserDetails = async () => {
    try {
        const token = sessionStorage.getItem('token');
        if (!token) {
            throw new Error('token not found');
        }

        const response = await axios.get('http://localhost:8080/userDetails', {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem("token")
            }
        });

        if (response.status === 200) {
            return response.data.data; // Assuming user details are in 'data' field
        } else {
            throw new Error('Failed to fetch user details');
        }
    } catch (error) {
        console.error(error);
        throw new Error('Failed to fetch user details');
    }
};

