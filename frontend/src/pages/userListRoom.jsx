import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import React from "react";
import UserListInRoom from "../components/UserList/UserListInRoom";

export const UserListRoom = () => {
    return(
        <>
            <header>
                <MyHeader/>
            </header>
            <section>
                <div className={'total-body'}>
                    <div>
                        <UserListInRoom/>
                    </div>
                </div>
            </section>
            <footer>
                <MyFooter/>
            </footer>
        </>
    );
}
