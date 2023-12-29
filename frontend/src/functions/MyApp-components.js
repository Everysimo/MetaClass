import '../css/MyApp.css';
import MCLogo from '../img/MetaClassLogo.png';
import BurgerButton, {MyMenu} from "./menu-button";
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

function GenerateRows({ Nomi }) {
    if (!Array.isArray(Nomi)) {
        return null; // Or handle this case accordingly
    }

    const cells = Nomi.map((item, index) => {
        if (item !== null) {
            return (
                <div className={"table-cell"} key={index}>
                    <p>{item}</p>
                </div>
            );
        }
        return null;
    });

    return <div className={"table-row"}>{cells}</div>;
}

export function MyFooter() {
    const stringsArray = ["Gatto Francesco", "Pesce Michele", "Cavaliere Domenico"];

    return (
        <div className={"table-container"}>
            <div className={"table-row"}>
                <div className={"table-cell"}>
                    <img src={MCLogo} className='App-logo' alt='no image'></img>
                    <GenerateRows Nomi={stringsArray} />
                </div>
            </div>
        </div>
    );
}