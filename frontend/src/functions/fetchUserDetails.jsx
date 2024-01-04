import axios from 'axios';

export const fetchUserDetails = async (Id) => {
    try {
        const response = await axios.post('http://localhost:8080/modifyUserData/${Id}');
        if (response.status === 200) {
            return response.data;
        } else {
            throw new Error('Failed to fetch user details');
        }
    } catch (error) {
        console.error(error);
        throw new Error('Failed to fetch user details');
    }
};
