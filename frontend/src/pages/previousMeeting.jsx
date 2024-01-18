import {MyHeader} from "../components/Layout/Header/Header";
import {MyFooter} from "../components/Layout/Footer/Footer";
import {ViewPreviousMeeting} from "../components/Lists/MeetingList/ViewPreviousMeeting";

export const PreviousMeeting = () => {

    return(
        <>
        <header>
            <MyHeader/>
        </header>
            <section>

            <ViewPreviousMeeting/>
            </section>
        <footer>
            <MyFooter/>
        </footer>
        </>
    );
}