import React, {useState} from 'react';
import { Link } from 'react-router-dom';

function JournalCard(props) {
    return (
        <div className="journal-card-div col">
            <div className="card journal-card text-center">
                {
                    <img
                        className="card-img-top"
                        src={props.journal.img}
                        alt={props.journal.name}
                    />
                }
                <div className="card-body">
                    <h2><Link to={"/" + props.journal.key} className="card-title journal-link stretched-link">{props.journal.name}</Link></h2>
                </div>
            </div>
        </div>
    );
}
export default JournalCard;