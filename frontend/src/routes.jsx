import React from "react";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import {Login} from "./pages/login";
import {LoggedIn} from "./pages/loggedIn"
import UnloggedHome from "./pages/UnloggedHome";
import {CreateRoom} from "./pages/createroom";
import {UserProfile} from "./pages/userprofile";
import {ModifyRoom} from "./pages/modifyroom";



export const Rts = () =>{
    return(
        <Router>
            <Routes>
                <Route exact path="/" element={<UnloggedHome />}/>
                <Route path="/login" element={<Login />}/>
                <Route path="/createroom" element={<CreateRoom/>}/>
                <Route path="/modifyroom" element={<ModifyRoom/>}/>
                <Route path="/loggedIn" element={<LoggedIn />}/>
                <Route path="/userprofile" element={<UserProfile/>}/>
            </Routes>
        </Router>
    )
}