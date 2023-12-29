import React from "react";

export function MyButton(){
    return(
        <button>click me!</button>
    );
}

export function SectionGenerator(props) {
    if(props.class != null) {
        return (
            <section className={props.class} id={props.ID}>
                <div className={"bg"}>
                    {props.content}
                </div>
            </section>
        );
    }
    else {
        return (
            <section>
                {props.content}
            </section>
        );
    }
}

export function DivGenerator(page){
    return (
        <div>
            {page.content}
            <MyButton />
        </div>
    );
}