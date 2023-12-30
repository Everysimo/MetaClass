import React from 'react';
import '../css/MyApp.css';
import '../css/index.css';
import {MyHeader, MyFooter} from '../components/MyApp-components.jsx';
import {Input} from "@chakra-ui/react";
export const Login = () =>{

    return(
        <body>
            <header>
                <MyHeader />
            </header>
            <section className={"sec"}>
                <div className={"table-container"}>
                    <div className={"table-row"}>
                        <div className={"table-cell"}>
                            ciao
                        </div>
                    </div>
                    <div className={"table-row"}>
                        <div className={"table-cell"}>
                            ciao
                        </div>
                    </div>
                    <div className={"table-row"}>
                        <div className={"table-cell"}>
                            ciao
                        </div>
                    </div>
                    <div className={"table-row"}>
                        <div className={"table-cell"}>
                            ciao
                        </div>
                    </div>
                    <div className={"table-row"}>
                        <div className={"table-cell"}>
                            ciao
                        </div>
                    </div>
                    <Input />
                </div>
            </section>
            <footer>
                <MyFooter />
            </footer>
        </body>
    );
}