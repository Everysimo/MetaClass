import '../css/MyApp.css';
import {SectionGenerator, DivGenerator} from '../components/functions';
import React from "react";
import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import '../data'
import {par1, par2, par3} from "../data";
import AccediStanza from "../components/enterRoomPopup/accediStanza";
import CreaScenario from "../components/CreaScenarioForm/CreaScenario";

export default function UnloggedHome(){
    const div1 = <DivGenerator content={par1}/>
    const div2 = <DivGenerator content={par2}/>
    const div3 = <DivGenerator content={par3}/>
    const sectionClass = "sec";
    return(
        <>
            <header>
                <MyHeader />
            </header>
            <SectionGenerator class={sectionClass} ID={"sec1"}/>
            <SectionGenerator content={div1}/>
            <SectionGenerator class={sectionClass} ID={"sec2"}/>
            <SectionGenerator content={div2}/>
            <SectionGenerator class={sectionClass} ID={"sec3"}/>
            <SectionGenerator class={"noShadow"} content={div3}/>
            <footer>
                <MyFooter />
            </footer>
        </>
    );
}