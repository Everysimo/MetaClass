import React, {Component} from "react";
import './FacebookLoginButton.css'
import FacebookLogin from "@greatsumini/react-facebook-login";
export default class Facebook extends Component {

    state = {
        nome: "",
        cognome: "",
        email: "",
        tokenAuth: "",
        metaId: ""
    };

    responseFacebook = response => {
        this.setState({
            nome: response.name.split(' ').slice(0, -1).join(' '),
            cognome: response.name.split(' ').slice(-1).join(' '),
            email: response.email
        },
            this.componentDidMount
        );
    };

    componentDidMount() {
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
