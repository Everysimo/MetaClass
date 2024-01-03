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
//per la stampa di una lista di sstringhe in maniera dinamica
export const StringList = ({ strings }) => {
    return (
        <div style={{ overflowY: 'auto', overflowX: 'auto', maxHeight: '200px', maxWidth: '200px',border: '1px solid #ccc', padding: '10px', boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)' }}>
            {strings.map((str, index) => (
                <div key={index} style={{ border: '1px solid #ddd', borderRadius: '4px', padding: '8px', marginBottom: '8px' }}>
                    {str}
                </div>
            ))}
        </div>
    );
};