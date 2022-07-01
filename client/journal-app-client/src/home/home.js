import React, {useState, useEffect} from 'react';
import listUtil from '../util/functions/list-util';
import NewJournalForm from '../new-journal/new-journal';
import {journalRef, getImageURL} from '../firebase';
import {useAuth} from '../contexts/authContext';
import {getDocs, query, where} from 'firebase/firestore'
import { Link } from 'react-router-dom';

function HomePage(){
    const [journalList, setJournalList] = useState([]);
    const { user } = useAuth();

    useEffect(() => {
        if (user != null) {
            let createdJournalsQuery = query(journalRef, where("author", "==", user.uid));
            getDocs(createdJournalsQuery).then((snapshot) => {
                snapshot.docs.forEach((doc) => {
                    listUtil(journalList, setJournalList, { type: "INSERT", payload:{ ...doc.data(),key: doc.id }});
                });

            }).catch((err) => {
                console.log(err.message);
            });
        }
    }, [user]);


    return (
        <div id="homeDiv">
            <h1>Home Page</h1>
            <br/>
            <div id="JournalListDiv">
                <NewJournalForm></NewJournalForm>
                <br/>
                <ul className="row row-cols-4">
                    {
                        journalList.map(
                            journal =>(
                                <div className="journal-card-div col">
                                    <div className="card bg-dark text-white journal-card text-center">
                                        <img
                                            className="card-img-top"
                                            src= "https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/testImg.png?alt=media&token=f196b183-09ba-4b43-b603-e9bb36c209ec"
                                            alt={journal.name}
                                        />
                                        <div className="card-body">
                                            <h2><Link to={"/journal-app/"+journal.key} className="card-title journal-link stretched-link">{journal.name}</Link></h2>
                                        </div>
                                    </div>
                                </div>
                            )
                        )
                    }
                </ul>

            </div>
        </div>
    );
}
export default HomePage;
        