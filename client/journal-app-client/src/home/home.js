import NewJournalForm from './new-journal/new-journal';
import JournalCard from './journal-card/journal-card';
import { useAuth } from '../contexts/authContext';
import {Link} from 'react-router-dom';

import useJournalList from '../facades/hooks/useJournalList';

function HomePage() {
    const { user } = useAuth();

    const [journalList] = useJournalList(["getAll"]);

    return (
        (user == null) ?
            <div id="landingDiv" className="container-fluid page-div">
                <section id="heroSection" className="sectionDiv">
                    <div className="row align-items-center">
                        <div className="col" >
                            <img
                                src="https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/Home%20Page%2FMountains.png?alt=media&token=071a97c5-9aec-4b30-aab8-df7ef57d3ab8"
                                alt="heroimg"
                                className="backgroundImg"
                            ></img>
                        </div>

                        <div className="col overlay">
                            <div className="container">
                                <h1 className="homeTextHeading">Keep track of your progress on the fly</h1>
                                <p className="homeTextBody"> Tired of using Excel to keep track of things?
                                Want a visually-appealing UI to help you track your progress?
                                You've come to the right place! </p>
                                <br/>
                                <Link className="btn btn-link btn-primary text-light" to="/login">Try for Free</Link>
                            </div>
                        </div>
                    </div>
                </section>
                <section className="sectionDiv sectionEven">
                    <div className="row">
                        <div className="col">
                            <div className="container">
                                <h1>Have control over things you write!</h1>
                                <p>
                                    No more clutter! Easily write, find and undo your progress!
                                </p>
                            </div>
                        </div>

                        <div className="col">
                            <img
                                src="https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/Home%20Page%2FBinders.png?alt=media&token=38bf0679-d9f4-4bfd-8aec-bd26d363f865"
                                alt=""
                                width="70%"
                                className="framedImg"
                            ></img>
                        </div>
                    </div>
                </section>  

                <section className="sectionDiv sectionOdd">
                    <div className="row">
                        <div className="col">
                            <img
                                src="https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/Home%20Page%2FCleanRoom.png?alt=media&token=9a04bf2e-4f76-4640-924b-66e31ce48d96"
                                alt=""
                                width="50%"
                                className="framedImg"
                            ></img>
                        </div>
                        <div className="col">
                            <h1>Manage your stuff with a friendly UI</h1>
                            <p>Journal App lets you record your progress in a user-friendly UI, and provides
                                some convenient tools to help you stay on top of things!</p>
                        </div>
                    </div>
                </section>

                <section className="sectionDiv sectionEven container-fluid">
                    <div className="row">
                        <div className="col-md-4 d-flex">
                            <div className="card landingCard">
                                <img
                                    src="https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/Home%20Page%2FUkeleleBooks.png?alt=media&token=fe9f791c-6462-460d-933d-a6b8ba2886b5"
                                    alt=""
                                    className="card-image-top"
                                ></img>
                                <div className="card-body">
                                    <h5 className="card-title">Great for Practice Logs!</h5>
                                    <p className="card-text">With graphs, goals and a scribbleboard, organize yourself to achieve perfection!</p>
                                </div>
                            </div>
                        </div>
                        <div className="col-md-4 d-flex">
                            <div className="card landingCard">
                                <img
                                    src="https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/Home%20Page%2FResearchPapers.png?alt=media&token=4f5c781a-be5e-4090-821e-226326145b99"
                                    alt=""
                                    className="card-image-top"
                                ></img>
                                <div className="card-body">
                                    <h5 className="card-title">Use as alternative to JIRA for Personal Projects! </h5>
                                    <p>With the activity board, counters and scrum board, keep track of your progress and plan at ease. </p>
                                </div>
                            </div>
                        </div>

                        <div className="col-md-4 d-flex">
                            <div className="card landingCard">
                                <img
                                    src="https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/Home%20Page%2FChemistry.png?alt=media&token=2bef70fb-6683-411f-b770-729c60200020"
                                    alt=""
                                    className="card-image-top"
                                ></img>
                                <div className="card-body">
                                    <h5 className="card-title">For science!</h5>
                                    <p>With graphs, counters and scribbleboard, store your research data in a presentable manner. </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>  
                <footer id="landingFooter" className="container-fluid footer">
                    <ul className="nav justify-content-end">
                        <li className="nav-item"><Link to="#" className="nav-link text-dark">Contact Us</Link></li>
                        <li className="nav-item"><Link to="#" className="nav-link text-dark">Feedback</Link></li>
                        <li className="nav-item"><p className="navbar-text">&copy;2022 llnorthpolell</p></li>
                    </ul>  
                </footer>            
            </div>
            :
            <div id="JournalListDiv" className="container page-div">
                <NewJournalForm></NewJournalForm>
                <br />
                <ul className="row row-cols-4">
                    {
                        journalList.map(
                            journal => (
                                <JournalCard journal={journal}></JournalCard>
                            )
                        )
                    }
                </ul>

            </div>


    );
}
export default HomePage;
