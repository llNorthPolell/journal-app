import React from 'react';
import {Link, useParams} from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowLeft, faDesktop, faBookOpen, faCrosshairs } from '@fortawesome/free-solid-svg-icons';

function Toolbar(props) {
    const {journalId} = useParams();

    return (
            <div className="fixed-bottom container bg-dark toolbar">
                <ul className="nav nav-pills justify-content-center">
                    <li className="nav-item">
                        <Link to="/" className="nav-link text-center text-light">
                            <span className="fs-3"><FontAwesomeIcon icon={faArrowLeft} /></span> <br/>
                            Journals
                        </Link>
                    </li>
                    <li className="nav-item">
                        <Link to={"/"+journalId} className="nav-link text-center text-light">
                            <span className="fs-3"><FontAwesomeIcon icon={faDesktop} /></span> <br/>
                            Dashboard
                        </Link>
                    </li>
                    <li className="nav-item">
                        <Link to={"/"+journalId+"/goals"} className="nav-link text-center text-light">
                            <span className="fs-3"><FontAwesomeIcon icon={faCrosshairs} /></span> <br/>
                            Goals
                        </Link>
                    </li>
                    <li className="nav-item">
                        <Link to={"/"+journalId+"/entries"} className="nav-link text-center text-light">
                            <span className="fs-3"><FontAwesomeIcon icon={faBookOpen} /></span> <br/>
                            Journal Entries
                        </Link>
                    </li>
                </ul>
            </div>

    );

}
export default Toolbar;