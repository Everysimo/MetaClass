import React, { Component } from "react";
import "./FacebookLoginButton.css";
import FacebookLogin from "@greatsumini/react-facebook-login";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFacebook } from "@fortawesome/free-brands-svg-icons";
import LogoutButton from "../LogoutButton/logoutButton";
import initializeFacebookSDK from "./fbSDK";

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
        initializeFacebookSDK(); // Call the initialization function here
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
    handleGender = (response) =>{
        if(response.gender === "male"){
            return "M";
        }
        else if (response.gender === "female"){
            return "F";
        }
        else{
            return "O";
        }
    }

    responseFacebook = (response) => {
        if (response && response.name) {
            const nameParts = response.name.split(' ');
            const nome = nameParts.slice(0, -1).join(' ');
            const cognome = nameParts.slice(-1).join(' ');

            const gender = this.handleGender(response); // Call handleGender with response

            this.setState(
                {
                    nome,
                    cognome,
                    email: response.email,
                    eta: response.birthday,
                    sesso: gender, // Set the gender obtained from handleGender
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
