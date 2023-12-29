import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import {Login} from "./pages/login";
import MyApp from "./pages/MyApp";
import React from "react";
export const Rts = () =>{
    return(
        <Router>
            <Routes>
                <Route exact path="/" element={<MyApp />}/>
                <Route path="/login" element={<Login />}/>
            </Routes>
        </Router>
    )
}