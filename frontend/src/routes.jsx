import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import {Login} from "./pages/login";
import {LoggedIn} from "./pages/loggedIn"
import UnloggedHome from "./pages/UnloggedHome";
import React from "react";
export const Rts = () =>{
    return(
        <Router>
            <Routes>
                <Route exact path="/" element={<UnloggedHome />}/>
                <Route path="/login" element={<Login />}/>
                <Route path="/loggedIn" element={<LoggedIn />}/>
            </Routes>
        </Router>
    )
}