import React, { Component } from "react";
import FacebookLogin from "@greatsumini/react-facebook-login";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFacebook } from "@fortawesome/free-brands-svg-icons";
import LogoutButton from "../LogoutButton/logoutButton";
import initializeFacebookSDK from "./fbSDK";
import NavigateToPageBtn from "../RelocateButton/HandleRelocateButton";
import { AuthContext } from "../../../Contexts/AuthContext/AuthContext";

export default class Facebook extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoggedIn: sessionStorage.getItem("isLoggedIn") === "true",
            nome: "",
            cognome: "",
            email: "",
            eta: "",
            sesso: "",
            metaId: "",
            isAdmin: "",
        };
    }

    componentDidMount() {
        initializeFacebookSDK(); // Call the initialization function here
    }

    handleUserID = (response) => {
        console.log(response);
        if (response && response.userID) {
            this.setState({
                metaId: response.userID,
            });
        } else {
            console.error("Invalid response or missing userID property");
        }
    };

    handleGender = (response) => {
        if (response.gender === "male") {
            return "M";
        } else if (response.gender === "female") {
            return "F";
        } else {
            return "O";
        }
    };

    responseFacebook = (response) => {
        if (response && response.name) {
            const nameParts = response.name.split(" ");
            const nome = nameParts.slice(0, -1).join(" ");
            const cognome = nameParts.slice(-1).join(" ");
            const gender = this.handleGender(response);
            this.setState(
                {
                    nome,
                    cognome,
                    email: response.email,
                    eta: response.birthday,
                    sesso: gender,
                    isLoggedIn: true,
                },
                () => {
                    this.sendDataToServer();
                    this.saveLoginStatusToLocalStorage();
                }
            );
        } else {
            console.error("Invalid response or missing name property");
        }
    };

    saveLoginStatusToLocalStorage = () => {
        sessionStorage.setItem("isLoggedIn", JSON.stringify(true));
        sessionStorage.setItem("UserMetaID", this.state.metaId);
        sessionStorage.setItem("nome", this.state.nome);
        sessionStorage.setItem("isAdmin", this.state.isAdmin);
    };

    sendDataToServer = async () => {
        const { nome, cognome, email, eta, sesso, metaId } = this.state;
        const dataToSend = {
            nome,
            cognome,
            email,
            eta,
            sesso,
            metaId,
        };
        const requestOptions = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(dataToSend),
        };
        try {
            const response = await fetch("http://localhost:8080/login", requestOptions);
            const responseData = await response.json();

            console.log("isAdmin from server:", responseData.isAdmin);

            const token = responseData.token;
            const isAdmin = responseData.isAdmin || false; // Set default value if not present

            console.log("isAdmin to set:", isAdmin);

            this.setState({
                isAdmin: isAdmin,
            });

            sessionStorage.setItem("token", token);
            sessionStorage.setItem("isAdmin", isAdmin);

        } catch (error) {
            console.error("Error:", error);
        }
    };

    render() {
        return (
            <AuthContext.Consumer>
                {(context) => {
                    const { login } = context;
                    const { isLoggedIn, nome } = this.state;

                    return (
                        <>
                            {isLoggedIn ? (
                                <>
                                    <h3>Ciao, {nome}!</h3>
                                    <NavigateToPageBtn />
                                    <LogoutButton />
                                </>
                            ) : (
                                <>
                                    <h2>Accedi/registrati con Facebook</h2>
                                    <FacebookLogin
                                        scope={"public_profile,user_gender,user_birthday"}
                                        fields={"id,name,birthday,gender,email"}
                                        appId="3381145492205390"
                                        onSuccess={this.handleUserID}
                                        onFail={(error) => {
                                            console.log("Login Failed!", error);
                                        }}
                                        onProfileSuccess={(response) => {
                                            this.responseFacebook(response);
                                            login({
                                                nome: this.state.nome,
                                                isAdmin: this.state.isAdmin,
                                            });
                                        }}
                                        className={"minWidth200"}
                                    >
                                        Login <FontAwesomeIcon icon={faFacebook} size={"xl"} style={{ color: "#ffffff" }} />
                                    </FacebookLogin>
                                </>
                            )}
                        </>
                    );
                }}
            </AuthContext.Consumer>
        );
    }
}
