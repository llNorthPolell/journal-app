import React from 'react';
import PicCarouselWidget from './widgets/pic-carousel/pic-carousel';
import LastEntryWidget from './widgets/last-entry/last-entry';
import LineGraphWidget from './widgets/chart/line-graph';
// Todo: Demo; Delete after server is connected
import myImg1 from './assets/40000000010005871.bmp';
import myImg2 from './assets/logo192.png';
import myImg3 from './assets/bg1.png';


function DashboardPage(){
    // Todo: Demo; Delete after server is connected
    const myPics= [
        {
            src:myImg1,
            label:"First slide label",
            caption:"Nulla vitae elit libero, a pharetra augue mollis interdum.",
            alt:"First slide"
        },
        {
            src:myImg2,
            label:"Second slide label",
            caption:"Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            alt:"Second slide"
        },
        {
            src:myImg3,
            label:"Third slide label",
            caption:"Lorem ipsum dolor sit amet, a pharetra augue mollis interdum.",
            alt:"Third slide"
        }
    ];

    const lastEntryDescription = "At vero eos et accusamus et iusto odio"
    +"dignissimos ducimus, qui blanditiis praesentium voluptatum deleniti"
    +"atque corrupti, quosdolores et quas molestias excepturi sint, obcaecati"
    +"cupiditate non provident, similique sunt in culpa, qui officia deserunt"
    +"mollitiaanimi, id est laborum et dolorum fuga. Et harum quidem rerum"
    +"facilis est et expedita distinctio. Nam libero tempore, cum solutanobis"
    +"est eligendi optio, cumque nihil impedit, quo minus id, quod maxime"
    +"placeat, facere possimus, omnis voluptas assumendaest, omnis dolor"
    +"repellendus.";

    return (
        <div id="dashboardDiv" className="container">
            <div className="input-group mb-3">
                <input id="searchbar" className="form-control" type="text" placeholder="search"/>
                <button id="searchBtn" className="btn btn-outline-secondary">Search</button>
            </div>
           
            <br/><br/>

            <h2>My Journal</h2>
            <br/>
            <div id="dashboardJournalContentDiv" className="container">
                <div className="row">
                    <LastEntryWidget description={lastEntryDescription}></LastEntryWidget>
                    <PicCarouselWidget picList={myPics}></PicCarouselWidget>
                </div>
                <div className="row">
                    <LineGraphWidget></LineGraphWidget>
                </div>
            </div>
            
        </div>
    );

}
export default DashboardPage;