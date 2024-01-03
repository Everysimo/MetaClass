import React, { Component } from "react";
import "./FacebookLoginButton.css";
import FacebookLogin from "@greatsumini/react-facebook-login";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFacebook } from "@fortawesome/free-brands-svg-icons";
import LogoutButton from "../LogoutButton/logoutButton";

export default class Facebook extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoggedIn: localStorage.getItem('isLoggedIn') === 'true',
            nome: "",
            cognome: "",
            email: "",
            eta: "",
            sesso: "",
            metaId: ""
        };
    }

    componentDidMount() {
        // Initialize Facebook SDK here
        window.fbAsyncInit = function() {
            window.FB.init({
                appId: '3381145492205390',
                autoLogAppEvents: true,
                xfbml: true,
                version: 'v12.0'
            });
        };

        // Load the SDK asynchronously
        (function(d, s, id) {
            var js, fjs = d.getElementsByTagName(s)[0];
            if (d.getElementById(id)) return;
            js = d.createElement(s); js.id = id;
            js.src = "https://connect.facebook.net/en_US/sdk.js";
            fjs.parentNode.insertBefore(js, fjs);
        }(document, 'script', 'facebook-jssdk'));
    }

    handleUserID = (response) => {
        if (response && response.userID) {
            this.setState({
                metaId: response.userID
            });
        } else {
            console.error('Invalid response or missing userID property');
        }
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
                    eta: response.birthday,
                    sesso: response.gender,
                    isLoggedIn: true
                },
                () => {
                    this.saveLoginStatusToLocalStorage();
                    this.sendDataToServer();
                }
            );
        } else {
            console.error('Invalid response or missing name property');
        }
    };

    saveLoginStatusToLocalStorage = () => {
        localStorage.setItem('isLoggedIn', JSON.stringify(true));
        localStorage.setItem('nome', this.state.nome);
    };

    sendDataToServer = async () => {
        const { nome, cognome, email, eta, sesso, metaId } = this.state;
        const dataToSend = {
            nome,
            cognome,
            email,
            eta,
            sesso,
            metaId
        };
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dataToSend)
        };
        try {
            console.log(JSON.stringify(dataToSend));
            const response = await fetch('http://localhost:8080/login', requestOptions);
            const responseData = await response.json();
            console.log('Server response:', responseData);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    render() {
        const { isLoggedIn, nome } = this.state;
        return (
            <div className={"loginForm"}>
                {isLoggedIn ? (
                    <>
                        <h2>Welcome, {nome}!</h2>
                        <LogoutButton />
                    </>
                ) : (
                    <>
                        <h2>To use this system, you need to login via Facebook</h2>
                        <FacebookLogin
                            scope={"public_profile,user_gender,user_birthday"}
                            fields={"id,name,birthday,gender,email"}
                            appId="3381145492205390"
                            onSuccess={this.handleUserID}
                            onFail={(error) => {
                                console.log('Login Failed!', error);
                            }}
                            onProfileSuccess={this.responseFacebook}
                        >
                            Login with Facebook
                            <FontAwesomeIcon icon={faFacebook} size={"xl"} style={{ color: '#ffffff' }} />
                        </FacebookLogin>
                    </>
                )}
            </div>
        );
    }
}
