import React from "react";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import {Login} from "./pages/login";
import UnloggedHome from "./pages/UnloggedHome";
import {CreateRoom} from "./pages/createroom";
import {ModifyRoom} from "./pages/modifyroom";
import GestioneAccessi from "./pages/accessManagement";
import {SelezionaScenario} from "./pages/modifyscenario";
import {LoggedInHome} from "./pages/LoggedInHome";
import {BannedUserList} from "./pages/bannedUserList";
import {Account} from "./pages/Account";
import {VisualRoomList} from "./pages/visualRoomList";
import {SingleRoom} from "./pages/SingleRoom";



export const Rts = () =>{
    return(
        <Router>
            <Routes>
                <Route exact path="/" element={<UnloggedHome />}/>
                <Route path="/login" element={<Login />}/>
                <Route path="/LoggedInHome" element={<LoggedInHome />}/>
                <Route path="/Account" element={<Account/>} />
                <Route path="/visualRoomList" element={<VisualRoomList/>} />
                <Route path="/SingleRoom/:id" element={<SingleRoom/>} />
                <Route path="/modifyroom/:id" element={<ModifyRoom/>}/>
                <Route path="/createroom" element={<CreateRoom/>}/>
                <Route path="/accessManagement" element={<GestioneAccessi/>}/>
                <Route path="/modifyscenario" element={<SelezionaScenario/>}/>

                <Route path="/bannedUserList" element={<BannedUserList/>}/>
            </Routes>
        </Router>
    )
}