import React, { Component } from "react";
import './FacebookLoginButton.css'
import FacebookLogin from "@greatsumini/react-facebook-login";

export default class Facebook extends Component {
    state = {
        isLoggedIn: false,
        userID: "",
        name: "",
        email: "",
        picture: ""
    };

    responseFacebook = response => {
        this.setState({
            isLoggedIn: true,
            userID: response.userID,
            name: response.name,
            email: response.email,
            picture: response.picture.data.url
        });
    };

    logoutFunct = () =>{
        this.setState({
            isLoggedIn: false,
            userID: "",
            name: "",
            email: "",
            picture: ""
        })
    }
    render() {
        let fbContent;

        if (this.state.isLoggedIn) {
            fbContent = (
                <div className={"loginForm"}>
                    <div className={"table-row"}>
                        <span className={"table-cell"}>
                            <h2>Welcome {this.state.name}</h2>
                        </span>
                    </div>
                    <div className={"table-row"}>
                        <span className={"table-cell"}>
                            <h3>Email: {this.state.email}</h3>
                        </span>
                    </div>
                    <div className={"table-row"}>
                        <span className={"table-cell"}>
                            <button onClick={this.logoutFunct} className={"logoutButton"}>Logout</button>
                            <button>Account</button>
                        </span>
                    </div>
                </div>
        );
        } else {
            fbContent = (
                <div className={"loginForm"}>
                    <h2>To use this system, you need to login via Facebook</h2>
                    <FacebookLogin
                        appId="3381145492205390"
                        onSuccess={(response) => {
                            console.log('Login Success!', response);
                        }}
                        onFail={(error) => {
                            console.log('Login Failed!', error);
                        }}
                        onProfileSuccess={(response) => {
                            console.log('Get Profile Success!', response);
                            this.responseFacebook(response);
                        }} >
                        Accedi con Facebook
                    </FacebookLogin>
                </div>
            );
        }
        return <div>{fbContent}</div>;
    }
}
