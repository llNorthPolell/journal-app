import React, {useState} from 'react';
import {Link} from "react-router-dom";

import PicCarouselWidget from './widgets/pic-carousel/pic-carousel';
import LastEntryWidget from './widgets/last-entry/last-entry';
import LineGraphWidget from './widgets/chart/line-graph';


function DashboardPage(props){
    const [contents] = useState(props.contents.widgets);


    return (
        <div id="dashboardDiv" className="container">
            <div className="row">
                <div className="input-group col mb-3">
                    <input id="searchbar" className="form-control" type="text" placeholder="search"/>
                    <button id="searchBtn" className="btn btn-outline-secondary">Search</button>
                </div>
                <div className="col mb-3">
                    <Link id="newEntryBtn" className="btn btn-primary" to={`${props.location.pathname}/new`}>+ New Entry </Link>
                </div>
            </div>

           
            <br/><br/>

            <h2>My Journal</h2>
            <div className="row row-cols-4"> 
            {
                contents.map(
                    widget=>(
                        (widget.type=="last-entry")?
                            <LastEntryWidget description={widget.payload}></LastEntryWidget>
                        :
                        (widget.type=="pic-carousel")?
                            <PicCarouselWidget picList={widget.payload}></PicCarouselWidget>
                        :
                        (widget.type=="line-graph")?
                            <LineGraphWidget title={widget.payload.title} labels={widget.payload.labels} data={widget.payload.data}></LineGraphWidget>
                        :
                        null
                    )
                ) 
            }
            </div>
            
        </div>
    );

}
export default DashboardPage;