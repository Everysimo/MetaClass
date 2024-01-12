import {MyHeader} from "../components/Header/Header";
import {MyFooter} from "../components/Footer/Footer";
import {ViewPreviousMeeting} from "../components/MeetingList/ViewPreviousMeeting";

export const PreviousMeeting = () => {

    return(
        <>
        <header>
            <MyHeader/>
        </header>
            <section>
                <div>
                    <ViewPreviousMeeting/>
                </div>
            </section>
        <footer>
            <MyFooter/>
        </footer>
        </>
    );
}