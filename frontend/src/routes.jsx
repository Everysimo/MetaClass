import React from "react";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import {Login} from "./pages/login";
import UnloggedHome from "./pages/UnloggedHome";
import {CreateRoom} from "./pages/createroom";
import GestioneAccessi from "./pages/accessManagement";
import {LoggedInHome} from "./pages/LoggedInHome";
import {BannedList} from "./pages/bannedUserList";
import {Account} from "./pages/Account";
import {SingleRoom} from "./pages/SingleRoom";
import {SelezionaScenario} from "./pages/changescenario";
import {PreviousMeeting} from "./pages/previousMeeting";
import Questionario from "./components/Forms/CompilaQuestionarioForm/Questionario";
import {MeetingXQuestionario} from "./pages/meetingwithoutQuestionario";

export const Rts = () =>{
    return(
        <Router>
            <Routes>
                <Route exact path="/" element={<UnloggedHome />}/>
                <Route path="/login" element={<Login />}/>
                <Route path="/LoggedInHome" element={<LoggedInHome />}/>
                <Route path="/Account" element={<Account/>} />
                <Route path="/SingleRoom/:id" element={<SingleRoom/>} />
                <Route path="/createroom" element={<CreateRoom/>}/>
                <Route path="/accessManagement/:id" element={<GestioneAccessi/>}/>
                <Route path="/changescenario/:id" element={<SelezionaScenario/>}/>
                <Route path="/bannedUserList/:id" element={<BannedList/>}/>
                <Route path="/previousMeeting" element={<PreviousMeeting/>}/>
                <Route path="/meetingwithoutQuestionario" element={<MeetingXQuestionario/>}/>
                <Route path="/Questionario/:id" element={<Questionario/>}/>
            </Routes>
        </Router>
    )
}