import React from 'react';
import {Link, useParams} from 'react-router-dom';


function LastEntryWidget(props){
    const {journalId} = useParams();
    return(
        <div className="card dashboard-widget widget-size-1-1">
            <h3 className="card-header">Last Entry</h3>
            <div className="card-body">
                <p className="card-text card-dashboard-widget-text">
                    {props.lastEntry.overview}
                </p>
                <Link className="btn btn-primary" to={"/"+journalId+"/"+((props.lastEntry.key)?props.lastEntry.key:"new")}>More</Link>
            </div>
        </div>
    );
}
export default LastEntryWidget;