import React from "react";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import {Login} from "./pages/login";
import UnloggedHome from "./pages/UnloggedHome";
import {CreateRoom} from "./pages/createroom";
import {UserProfile} from "./pages/userprofile";
import {ModifyRoom} from "./pages/modifyroom";
import {LoggedInHome} from "./pages/LoggedInHome";
import {AStest} from "./pages/about";



export const Rts = () =>{
    return(
        <Router>
            <Routes>
                <Route exact path="/" element={<UnloggedHome />}/>
                <Route path="/login" element={<Login />}/>
                <Route path="/LoggedInHome" element={<LoggedInHome />}/>
                <Route path="/createroom" element={<CreateRoom/>}/>
                <Route path="/modifyroom" element={<ModifyRoom/>}/>
                <Route path="/about" element={<AStest/>}/>
                {/*nella routes specifico che voglio inviare anche un valore*/}
                <Route path="/userprofile/:value" element={<UserProfile/>} />
                {/*<Route path="/userprofile" element={<UserProfile/>}/>*/}
            </Routes>
        </Router>
    )
}