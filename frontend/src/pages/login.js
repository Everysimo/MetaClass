import React from 'react';
import '../css/MyApp.css';
import '../css/index.css';
import {MyHeader, MyFooter} from '../functions/MyApp-components.js';
import {Input} from "@chakra-ui/react";
export const Login = () =>{

    return(
        <div className='wrapper'>
            <header>
                <MyHeader />
            </header>
            <body>
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
            </body>
            <footer>
                <MyFooter />
            </footer>
        </div>
    );
}