
import React from 'react'
import {MyHeader} from "../components/Layout/Header/Header";
import {MyFooter} from "../components/Layout/Footer/Footer";
import BannedUserList from "../components/Lists/UserList/BannedUserList";

export const BannedList = ({List}) => {


    return(
        <>
            <header>
                <MyHeader/>
            </header>
            <section>
                <div className={'total-body'}>
                    <div>
                        <BannedUserList userList={List} />
                    </div>
                </div>
            </section>
            <footer>
                <MyFooter/>
            </footer>
        </>
    );
}