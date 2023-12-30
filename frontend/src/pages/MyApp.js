import '../css/MyApp.css';
import {SectionGenerator, DivGenerator, MyButton} from '../functions/functions';
import React from "react";
import {MyFooter, MyHeader} from "../functions/MyApp-components";

export default function MyApp(){
    const par1 =
        <div>
            <h1>
                section 1
            </h1>

            <p>
                Welcome to our cutting-edge VR educational system!<br/>
                At our company, we've revolutionized the concept of interactive learning
                by introducing a state-of-the-art Virtual Reality (VR) platform
                designed specifically for immersive and engaging classroom experiences.<br/>
            </p>
        </div>;
    const par2 =
        <div>
            <h1>
                section 2
            </h1>
            <p>
                Our VR system offers a seamless blend of technology and education,
                enabling students and instructors to transcend physical limitations
                and dive into a world of limitless possibilities.<br/>
                Imagine stepping into a virtual environment where the laws of physics,
                history, or biology come to life right before your eyes.
            </p>
        </div>;
    const par3 =
        <div>
            <h1>
                section 3
            </h1>
            <p>
                Through our meticulously crafted VR modules,
                students can interact with complex concepts,
                explore historical events firsthand, dissect virtual organisms,
                or even embark on interactive field trips to remote locations
                – all from the comfort of their classroom.<br/>
                The system's intuitive interface ensures ease of use for both educators and learners,
                empowering teachers to create customized lessons tailored to their curriculum.
            </p>
        </div>;

    const div1 = <DivGenerator content={par1}/>
    const div2 = <DivGenerator content={par2}/>
    const div3 = <DivGenerator content={par3}/>
    const sectionClass = "sec";
    return(
        <div className='wrapper'>
            <header>
                <MyHeader />
            </header>

            <body>


                <SectionGenerator class={sectionClass} ID={"sec1"}/>
                <SectionGenerator content={div1}/>
                <SectionGenerator class={sectionClass} ID={"sec2"}/>
                <SectionGenerator content={div2}/>
                <SectionGenerator class={sectionClass} ID={"sec3"}/>
                <SectionGenerator content={div3}/>

            </body>
            <footer>
                <MyFooter />
            </footer>
        </div>
    );
}