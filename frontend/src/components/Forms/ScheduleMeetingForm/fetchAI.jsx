const FetchAI = async (idStanza) => {
    const requestOption = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("token")
        },
    };

    try {
        console.log('sono qui ', idStanza)
        const response = await fetch(`http://localhost:8080/stimaMeeting/${idStanza}`, requestOption);

        if (response.status === 200 && response.ok){
            const responseData = await response.json();
            console.log('dati di risposta: ', responseData)
            const durataDouble = responseData.value; // Assuming the double value is in the "data" field, adjust accordingly

            // Parse the double to an integer
            const durataInt = parseInt(durataDouble, 10); // Adjust the radix (second parameter) as needed

            console.log('Parsed Durata as Integer:', durataInt);
            return durataInt;
        }
        else {
            throw new Error('Errore nel recupero della stima');
        }

    } catch (error) {
        console.error('Errore nel recupero della stima:', error);
        throw error; // Rethrow the error to handle it at the calling site if needed
    }
};

export default FetchAI;

