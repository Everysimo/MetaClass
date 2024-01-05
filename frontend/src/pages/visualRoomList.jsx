
import React from "react"
import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import RoomList from "../components/RoomList/RoomList";
import { useParams } from 'react-router-dom';   //imoortante import per passaggio dei parametri
export const VisualRoomList = () => {

    //const {value} = useParams();        //per passare il parametro

    return (

    <body>
    <header>
        <MyHeader/>
    </header>

    <section>
        <div>
            <RoomList/>
            {/*<p>Valore: {value}</p>      ottengo il parametro          */}
        </div>
    </section>

    <footer>
        <MyFooter/>
    </footer>
    </body>
    );
}