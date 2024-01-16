import React from 'react';
import './Header.css';
import { useAuth } from '../../../Contexts/AuthContext/AuthContext';
import Comm from '../../../img/commigo.png';
import BurgerButton, { MyMenu } from './Menu/menu-button';

export function MyHeader() {
    const { isLoggedIn } = useAuth();

    return (
        <div className={'table-row'}>
            <div className={'table-cell'} id={'header-left'}>
                <img src={Comm} className='App-logo' alt='nessuna immagine'></img>
            </div>
            <div className={'table-cell'} id={'header'}>
                <div id={'menu'}>
                    <MyMenu />
                </div>
            </div>
            <div className={'table-cell'} id={'header-resp'}>
                <BurgerButton isLoggedIn={isLoggedIn} />
            </div>
        </div>
    );
}
