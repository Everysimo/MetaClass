import React, { Component } from "react";
import './FacebookLoginButton.css'
import FacebookLogin from "@greatsumini/react-facebook-login";

export default class Facebook extends Component {

    state = {
        isLoggedIn: localStorage.getItem('isLoggedIn') === 'true',
        nome: "",
        cognome: "",
        email: "",
        tokenAuth: "",
        metaId: ""
    };

    responseFacebook = (response) => {
        if (response && response.name) {
            const nameParts = response.name.split(' ');
            const nome = nameParts.slice(0, -1).join(' ');
            const cognome = nameParts.slice(-1).join(' ');

            this.setState(
                {
                    nome,
                    cognome,
                    email: response.email,
                    isLoggedIn: true // Set isLoggedIn to true after successful login
                },
                () => {
                    this.saveLoginStatusToLocalStorage();
                    this.componentDidMount(); // Save user data to backend server after login
                }
            );
        } else {
            // Handle cases where the response doesn't contain the expected properties
            console.error('Invalid response or missing name property');
            // You can add further error handling or logging here
        }
    };


    saveLoginStatusToLocalStorage = () => {
        // Save the isLoggedIn status to localStorage as a JSON string
        localStorage.setItem('isLoggedIn', JSON.stringify(true));
        // No need to reload here
    };

    handleLogout = () => {
        // Perform logout actions: clear user data, update localStorage, etc.
        localStorage.setItem('isLoggedIn', JSON.stringify(false));
        this.setState({
            isLoggedIn: false, // Update the state to reflect the logout
            nome: "",
            cognome: "",
            email: "",
            tokenAuth: "",
            metaId: ""
        });
    };
    async componentDidMount() {
        // Simple POST request with a JSON body using fetch
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'user-parameters' },
            body: JSON.stringify(this.state)
        };
        //da sostituire l'input con la chiamata API al server
        fetch('https://reqres.in/api/posts', requestOptions)
            .then(response => response.json());
    }
    responseLogin = response => {
        this.setState({
            tokenAuth: response.accessToken,
            metaId: response.userID
        })
    }

    render() {
        const { isLoggedIn, nome } = this.state;

        return (
            <div className={"loginForm"}>
                {isLoggedIn ? (
                    <>
                        <h2>Welcome, {nome}!</h2>
                        <button onClick={this.handleLogout}>Logout</button>
                    </>
                ) : (
                    <>
                        <h2>To use this system, you need to login via Facebook</h2>
                        <FacebookLogin
                            appId="3381145492205390"
                            onSuccess={this.responseFacebook}
                            onFail={(error) => {
                                console.log('Login Failed!', error);
                            }}
                            onProfileSuccess={this.responseFacebook}
                        >
                            Accedi con Facebook
                        </FacebookLogin>
                    </>
                )}
            </div>
        );
    }
}