import React from "react";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import {Login} from "./pages/login";
import UnloggedHome from "./pages/UnloggedHome";
import {CreateRoom} from "./pages/createroom";
import {ModifyRoom} from "./pages/modifyroom";
import GestioneAccessi from "./pages/accessManagement";
import {LoggedInHome} from "./pages/LoggedInHome";
import {BannedList} from "./pages/bannedUserList";
import {Account} from "./pages/Account";
import {SingleRoom} from "./pages/SingleRoom";
import {VisualRoomList} from "./pages/visualRoomList";
import {SelezionaScenario} from "./pages/changescenario";
import {PreviousMeeting} from "./pages/previousMeeting";

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
                <Route path="/accessManagement/:id" element={<GestioneAccessi/>}/>
                <Route path="/changescenario/:id" element={<SelezionaScenario/>}/>
                <Route path="/bannedUserList/:id" element={<BannedList/>}/>
                <Route path="/previousMeeting" element={<PreviousMeeting/>}/>

            </Routes>
        </Router>
    )
}