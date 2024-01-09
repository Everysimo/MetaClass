
import React from 'react'
import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import UserList from "../components/UserList/UserList";

export const BannedUserList = ({List}) => {


    return(
        <>
            <header>
                <MyHeader/>
            </header>
            <section>
                <div className={'total-body'}>
                    <div>
                        <UserList userList={List} />
                    </div>
                </div>
            </section>
            <footer>
                <MyFooter/>
            </footer>
        </>
    );
}