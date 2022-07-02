import React, {useState, useEffect} from 'react';
import NewJournalForm from './new-journal/new-journal';
import { Link } from 'react-router-dom';
import { useData } from '../contexts/dataContext';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBook } from '@fortawesome/free-solid-svg-icons';

function HomePage(){
    const { journalList } = useData();

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

                                        {
                                            (journal.img != null)?
                                                <img
                                                    className="card-img-top"
                                                    src= "https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/testImg.png?alt=media&token=f196b183-09ba-4b43-b603-e9bb36c209ec"
                                                    alt={journal.name}
                                                />
                                            :
                                                <FontAwesomeIcon icon={faBook} />
}
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
        