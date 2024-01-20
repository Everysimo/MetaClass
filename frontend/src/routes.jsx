import React from "react";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import {Login,
    UnloggedHome,
    LoggedInHome,
    Account,
    SingleRoom,
    PreviousMeeting
} from "./pages";

export const Rts = () =>{
    return(
        <Router>
            <Routes>
                <Route exact path="/" element={<UnloggedHome />}/>
                <Route path="/login" element={<Login />}/>
                <Route path="/LoggedInHome" element={<LoggedInHome />}/>
                <Route path="/Account" element={<Account/>} />
                <Route path="/SingleRoom/:id" element={<SingleRoom/>} />
                <Route path="/previousMeeting" element={<PreviousMeeting/>}/>
            </Routes>
        </Router>
    )
}