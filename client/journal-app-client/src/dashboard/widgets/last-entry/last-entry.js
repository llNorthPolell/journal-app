import React from 'react';

function LastEntryWidget(props){
    return(
        <div className="card dashboard-widget">
            <h3 className="card-header">Last Entry</h3>
            <div className="card-body">
                <p className="card-text">
                    {props.description}
                </p>
                <a href="#" className="btn btn-primary">More</a>
            </div>
        </div>
    );
}
export default LastEntryWidget;