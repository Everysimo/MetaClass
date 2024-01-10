import React from 'react'
import {ComponentPreview, Previews} from '@react-buddy/ide-toolbox'
import {PaletteTree} from './palette'
import {SingleRoom} from "../pages/SingleRoom";

const ComponentPreviews = () => {
    return (
        <Previews palette={<PaletteTree/>}>
            <ComponentPreview path="/SingleRoom">
                <SingleRoom/>
            </ComponentPreview>
        </Previews>
    )
}

export default ComponentPreviews