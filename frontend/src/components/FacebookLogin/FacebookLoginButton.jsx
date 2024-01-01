import React, {Component, useState} from "react";
import './FacebookLoginButton.css'
import FacebookLogin from "@greatsumini/react-facebook-login";

export default class Facebook extends Component {

    state = {
        metaId: "",
        nome: "",
        email: "",
        cognome: "",
        tokenAuth: ""
    };

    responseFacebook = response => {
        this.setState({
            nome: response.name.split(' ').slice(0, -1).join(' '),
            cognome: response.name.split(' ').slice(-1).join(' '),
            email: response.email
        }, ()=>{
            console.log(JSON.stringify(this.state))
        });
    };

    responseLogin = response => {
        this.setState({
            tokenAuth: response.accessToken,
            metaId: response.userID
        })
    }

    logoutFunct = () =>{
        this.setState({
            metaId: "",
            name: "",
            email: "",
            picture: ""
        })
    }
    render() {
        let fbContent;
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
        return <div>{fbContent}</div>;
    }
}
