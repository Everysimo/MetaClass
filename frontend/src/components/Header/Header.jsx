import './header.css';
import '../../css/MyApp.css'
import MCLogo from '../../img/MetaClassLogo.png';
import BurgerButton, {MyMenu} from "../BurgerButton/menu-button";
import React from 'react';
export function MyHeader(){
    return(
        <div className={"table-row"}>
            <div className={"table-cell"} id={"header-left"}>
                <img src={MCLogo} className='App-logo' alt='no image'></img>
            </div>
            <div className={"table-cell"} id={"header"}>
                <div id={"menu"}>
                    <MyMenu />
                </div>
            </div>
            <div className={"table-cell"} id={"header-resp"}>
                <BurgerButton />
            </div>
        </div>
    );
}