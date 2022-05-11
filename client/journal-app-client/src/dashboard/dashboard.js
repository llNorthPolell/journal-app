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

    const demoLineGraph1Labels = {
        y: 'BPM',
        x: 'Date' 
    };

    const demoLineGraph1Data = {
        x: ['March 1, 2022', 'March 2, 2022', 'March 3, 2022', 'March 4, 2022', 'March 5, 2022', 'March 8, 2022','March 10, 2022','March 11, 2022'],
        y:[
            {
                label: 'Dataset 1',
                data:[120,120,124,124,124,128,128,135],
                borderColor:'#88234d',
                backgroundColor: '#88234d'
            }
        ]
    };


    const demoLineGraph2Labels = {
        y: 'Time (mins)',
        x: 'Date' 
    };

    const demoLineGraph2Data = {
        x: ['March 1, 2022', 'March 2, 2022', 'March 3, 2022', 'March 4, 2022', 'March 5, 2022', 'March 8, 2022','March 10, 2022','March 11, 2022'],
        y:[
            {
                label: 'Target',
                data:[35,47,124,20,90,60,128,56],
                borderColor:'#88234d',
                backgroundColor: '#88234d'
            },
            {
                label: 'Actual',
                data:[60,60,60,60,65,65,65,70],
                borderColor:'#232388',
                backgroundColor: '#232388'
            }
        ]
    };


    return (
        <div id="dashboardDiv" className="container">
            <div className="row">
                <div className="input-group col mb-3">
                    <input id="searchbar" className="form-control" type="text" placeholder="search"/>
                    <button id="searchBtn" className="btn btn-outline-secondary">Search</button>
                </div>
                <div className="col mb-3">
                    <a id="newEntryBtn" className="btn btn-primary">+ New Entry </a>
                </div>
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
                    <LineGraphWidget title="Scale Speed" labels={demoLineGraph1Labels} data={demoLineGraph1Data}></LineGraphWidget>
                    <LineGraphWidget title="Practice Time" labels={demoLineGraph2Labels} data={demoLineGraph2Data}></LineGraphWidget>
                </div>
            </div>
            
        </div>
    );

}
export default DashboardPage;