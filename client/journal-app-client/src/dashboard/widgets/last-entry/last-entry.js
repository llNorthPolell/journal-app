import React from 'react';
import {Link, useLocation} from 'react-router-dom';

function LastEntryWidget(props){
    const currentURL = useLocation().pathname;
    return(
        <div className="card dashboard-widget">
            <h3 className="card-header">Last Entry</h3>
            <div className="card-body">
                <p className="card-text card-dashboard-widget-text">
                    {props.lastEntry.overview}
                </p>
                <Link className="btn btn-primary" to={currentURL+"/"+props.lastEntry.key}>More</Link>
            </div>
        </div>
    );
}
export default LastEntryWidget;