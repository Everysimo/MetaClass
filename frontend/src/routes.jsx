import React from "react";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import {Login} from "./pages/login";
import UnloggedHome from "./pages/UnloggedHome";
import {CreateRoom} from "./pages/createroom";
import {Account} from "./pages/Account";
import {ModifyRoom} from "./pages/modifyroom";
import GestioneAccessi from "./pages/accessManagement";
import {SelezionaScenario} from "./pages/modifyscenario";
import {LoggedInHome} from "./pages/LoggedInHome";
import {SingleRoom} from "./pages/SingleRoom";



export const Rts = () =>{
    return(
        <Router>
            <Routes>
                <Route exact path="/" element={<UnloggedHome />}/>
                <Route path="/login" element={<Login />}/>
                <Route path="/LoggedInHome" element={<LoggedInHome />}/>
                <Route path="/createroom" element={<CreateRoom/>}/>
                <Route path="/modifyroom" element={<ModifyRoom/>}/>
                <Route path="/SingleRoom" element={<SingleRoom />}/>
                {/*nella routes specifico che voglio inviare anche un valore*/}
                <Route path="/Account" element={<Account/>} />
                {/*<Route path="/userprofile" element={<UserProfile/>}/>*/}
                <Route path="/accessManagement" element={<GestioneAccessi/>}/>
                <Route path="/modifyscenario" element={<SelezionaScenario/>}/>
            </Routes>
        </Router>
    )
}