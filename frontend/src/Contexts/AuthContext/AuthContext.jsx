// AuthContext.js
import React, { createContext, useContext, useState } from 'react';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [isLoggedIn, setIsLoggedIn] = useState(sessionStorage.getItem('isLoggedIn') === 'true');
    const [userInfo, setUserInfo] = useState({
        nome: sessionStorage.getItem('nome') || '',
        isAdmin: JSON.parse(sessionStorage.getItem('isAdmin')) || false,
    });

    const login = (userData) => {
        setIsLoggedIn(true);
        setUserInfo(userData);
    };

    const logout = () => {
        setIsLoggedIn(false);
        setUserInfo({
            nome: '',
            isAdmin: false,
        });
    };

    return (
        <AuthContext.Provider value={{ isLoggedIn, userInfo, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
