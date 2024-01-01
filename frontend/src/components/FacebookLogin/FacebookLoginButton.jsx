import React, { Component } from "react";
import './FacebookLoginButton.css'
import FacebookLogin from "@greatsumini/react-facebook-login";

let isLoggedIn = localStorage.getItem('isLoggedIn') === 'true'; // Read login status from localStorage

export default class Facebook extends Component {
    state = {
        nome: "",
        cognome: "",
        email: "",
        tokenAuth: "",
        metaId: ""
    };

    responseFacebook = (response) => {
        this.setState(
            {
                nome: response.name.split(' ').slice(0, -1).join(' '),
                cognome: response.name.split(' ').slice(-1).join(' '),
                email: response.email
            },
            this.saveLoginStatusToLocalStorage // Save login status to local storage
        );
    };

    saveLoginStatusToLocalStorage = () => {
        // Save the isLoggedIn status to localStorage as a JSON string
        localStorage.setItem('isLoggedIn', JSON.stringify(true));
        isLoggedIn = true; // Update isLoggedIn outside the state
    };
    handleLogout = () => {
        // Perform logout actions: clear user data, update localStorage, etc.
        localStorage.setItem('isLoggedIn', JSON.stringify(false));
        isLoggedIn = false; // Update isLoggedIn outside the state
        // Additional logic for clearing user data or performing other logout actions
        window.location.reload(); // Reload the page after logout
    };


    componentDidMount() {
        // Check localStorage for login status upon component mount
        if (isLoggedIn) {
            this.setState({ isLoggedIn });
        }
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
        let fbContent;
        if (isLoggedIn) {
            // Show logout button if the user is logged in
            fbContent = (
                <div className={"loginForm"}>
                    <h2>Welcome, {this.state.nome}!</h2>
                    <button onClick={this.handleLogout}>Logout</button>
                </div>
            );
        } else {
            fbContent = (
                <div className={"loginForm"}>
                    <h2>To use this system, you need to login via Facebook</h2>
                    <FacebookLogin
                        appId="3381145492205390"
                        onSuccess={(response) => {
                            this.responseLogin(response)
                        }}
                        onFail={(error) => {
                            console.log('Login Failed!', error);
                        }}
                        onProfileSuccess={(response) => {
                            this.responseFacebook(response)
                        }}
                    >
                        Accedi con Facebook
                    </FacebookLogin>
                </div>
            );
        }
        return <div>{fbContent}</div>;
    }
}
