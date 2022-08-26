import React, { useEffect } from 'react';
import NewJournalForm from './new-journal/new-journal';
import JournalCard from './journal-card/journal-card';
import { useData } from '../contexts/dataContext';
import { useAuth } from '../contexts/authContext';


function HomePage() {
    const { journalList, clearDashboardData } = useData();
    const { user } = useAuth();

    useEffect(()=>{
        clearDashboardData();
    },[]);

    return (
        (user == null) ?
            <div id="landingDiv" className="container-fluid page-div">
                <div className="landingContent">
                    <section className="sectionDiv">
                        <div className="row align-items-center">
                            <div className="col" >
                                <img 
                                    src="https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/Home%20Page%2FExcel.png?alt=media&token=7e66228f-120f-4711-9ef8-09e152a16930"
                                    alt=""
                                    className="backgroundImg"
                                ></img>
                            </div>
                            
                            <div className="col overlay">
                                <h1> Tired of using Excel to keep track of things?</h1>
                                <h1> Want a visually-appealing UI to help you track your progress? </h1> 
                                <h1> You've come to the right place! </h1>

                            </div>
                        </div>
                    </section>


                    <section className="sectionDiv sectionEven">
                        <div className="row">                       
                            <div className="col">
                                <h1>
                                    Want to save trees?
                                    Wish you can "ctrl+z" your writing on paper? Are you fogetting
                                    something and wish you could "ctrl+F" in real life?
                                </h1>
                            </div>

                            <div className="col">
                                <img 
                                    src="https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/Home%20Page%2FBinders.png?alt=media&token=38bf0679-d9f4-4bfd-8aec-bd26d363f865"
                                    alt=""
                                    width="70%"
                                ></img>
                            </div>
                        </div>
                    </section>


                    <section className="sectionDiv sectionOdd">
                        <h1>Journal App has you covered!</h1>

                    </section>


                    <section className="sectionDiv sectionEven">
                        <div className="row"> 
                            <div className="col">    
                                <img 
                                    src="https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/Home%20Page%2FCleanRoom.png?alt=media&token=9a04bf2e-4f76-4640-924b-66e31ce48d96"
                                    alt=""
                                    width="50%"
                                ></img>
                            </div>
                            <div className="col">    
                                <h1>Journal App lets you record your progress in a user-friendly UI, and provides
                                    some convenient tools to help you stay on top of things!</h1>
                            </div>
                        </div>
                    </section>


                    <section className="sectionDiv sectionOdd container-fluid">
                        <h1>Suitable for many journal-keeping tasks, including Practice Logs, Development Logs for Personal Projects, Research and more!</h1>
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
                </div>
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
