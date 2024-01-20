import {MyHeader} from "../components/Layout/Header/Header";
import {MyFooter} from "../components/Layout/Footer/Footer";
import {ViewPreviousMeeting} from "../components/Lists/MeetingList/ViewPreviousMeeting";

export const PreviousMeeting = () => {

    return(
        <>
        <header>
            <MyHeader/>
        </header>
            <main className={"bg"} >
                <section
                    className={"roomSec"}
                    style={{
                        justifyContent: "center",
                        alignItems: "center"}}
                >
                    <div
                        className={"transWhiteBg"}
                        style={{
                            marginTop: "5%",
                            marginBottom:"5%",
                            padding:"1%",
                            borderRadius:"25px"}}
                    >
                        <ViewPreviousMeeting/>
                    </div>
                </section>
            </main>
        <footer>
            <MyFooter/>
        </footer>
        </>
    );
}