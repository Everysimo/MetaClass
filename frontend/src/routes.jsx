import React from "react";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import {Login} from "./pages/login";
import {LoggedInHome} from "./pages/LoggedInHome"
import UnloggedHome from "./pages/UnloggedHome";
import {CreateRoom} from "./pages/createroom";
import {ModifyRoom} from "./pages/modifyroom";
import {Account} from "./pages/Account";



export const Rts = () =>{
    return(
        <Router>
            <Routes>
                <Route exact path="/" element={<UnloggedHome />}/>
                <Route path="/login" element={<Login />}/>
                <Route path="/createroom" element={<CreateRoom/>}/>
                <Route path="/modifyroom" element={<ModifyRoom/>}/>
                <Route path="/LoggedInHome" element={<LoggedInHome />}/>
                <Route path={"/Account"} element={<Account />}/>
            </Routes>
        </Router>
    )
}