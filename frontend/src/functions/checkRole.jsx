import axios from "axios";

export const checkRole = async (id_stanza) => {
    try {
        const token = sessionStorage.getItem('token');
        if (!token) {
            throw new Error('Token not found');
        }

        const response = await axios.post(`http://localhost:8080/getRuolo/${id_stanza}`, null, {
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            }
        });

        console.log('Response data:', response.data); // Log the received data

        if (response.status === 200) {
            const role = response.data.value.nome; // Assuming the role is in 'value' field
            if (role) {
                return role;
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

export const checkOrg = async (id_stanza) => {
    const fetchedRole = await checkRole(id_stanza);
    return fetchedRole === "Organizzatore_Master" || fetchedRole === "Organizzatore";
}