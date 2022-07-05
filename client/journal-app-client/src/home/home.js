import React, { useState, useEffect } from 'react';
import NewJournalForm from './new-journal/new-journal';
import { Link } from 'react-router-dom';
import { useData } from '../contexts/dataContext';

function HomePage() {
    const { journalList } = useData();

    return (
        <div id="homeDiv">
            <h1>Home Page</h1>
            <br />
            <div id="JournalListDiv">
                <NewJournalForm></NewJournalForm>
                <br />
                <ul className="row row-cols-4">
                    {
                        journalList.map(
                            journal => (
                                <div className="journal-card-div col">
                                    <div className="card bg-dark text-white journal-card text-center">
                                        {
                                            <img
                                                className="card-img-top"
                                                src={journal.img}
                                                alt={journal.name}
                                            />
                                        }
                                        <div className="card-body">
                                            <h2><Link to={"/journal-app/" + journal.key} className="card-title journal-link stretched-link">{journal.name}</Link></h2>
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
