// index.js
import React from 'react';
import ReactDOM from 'react-dom';
import {AuthProvider} from "./Contexts/AuthContext/AuthContext";
import {App} from "./components/App/App";

ReactDOM.render(
    <React.StrictMode>
        <AuthProvider>
            <App />
        </AuthProvider>
    </React.StrictMode>,
    document.getElementById('root')
);
