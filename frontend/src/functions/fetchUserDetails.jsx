import axios from 'axios';

export const fetchUserDetails = async () => {
    try {
        const UserMetaID = localStorage.getItem('UserMetaID');
        console.log('UserMetaID:', UserMetaID);

        if (!UserMetaID) {
            throw new Error('UserMetaID not found');
        }

        const response = await axios.get('http://localhost:8080/userDetails', {
            headers: {
                'UserMetaID': UserMetaID
            }
        });

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
