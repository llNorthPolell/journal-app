import {processDashboardWidgets} from '../widget-refinery';

const mockJournalEntries = [
    {"summary":"My Second Post","dateOfEntry":"2022-07-30","journal":"TX9nbmwt7WRXaEcZQbMG","lastUpdated":"2022-10-27T13:42:16.287Z","overview":"Day 2 of testing through emulator. So far so good!","journalBodyItems":[{"recordList":[{"recValue":"2","recKey":"PostCount","id":"c1a57ba8-9b6d-42fa-94aa-978b14c61de4"},{"recValue":"4","recKey":"Target","id":"90e9c4bf-1692-4dc6-9319-a086ce59457f"}],"topic":"Emulator Post Test","description":"Testing if able to navigate to journal via search results.","key":"6f9d6e4c-460d-4617-948b-fe4ce56e875a"}],"key":"LkBikDBQKL5M4osdmytR"},
    {"summary":"My First Entry","dateOfEntry":"2022-07-29","journal":"TX9nbmwt7WRXaEcZQbMG","lastUpdated":"2022-10-27T13:42:05.686Z","overview":"This is my first time posting on an emulator!!","journalBodyItems":[{"recordList":[{"recValue":"1","recKey":"PostCount","id":"478e4a97-1265-4ad1-9519-1493e61bc342"},{"recValue":"2","recKey":"Target","id":"2793fdb1-3a4d-4d7e-8945-96e5b3e5558d"}],"topic":"Emulator Post Test","description":"Wooot! Emulator works! First post!","key":"0124df4b-41c2-4a90-9b40-6bf52615e7ed"}],"key":"GnnBTJqN4MwvyqKG7d8t"}
]

const mockConfig = [{"journal":"TX9nbmwt7WRXaEcZQbMG","type":"line-graph","position":2,"title":"Test","labels":{"x":"Date","y":"PC"},"data":{"xValue":"dateOfEntry","yValues":[{"id":"f0a071ff-6731-45e1-9076-f8f0f26a43c7","backgroundColor":"#0236e8","borderColor":"#0236e8","label":"MyPC","yTopic":"Emulator Post Test","yRecord":"PostCount"},{"id":"b8178c3b-bec0-48f6-823f-9e1bb6d9e775","backgroundColor":"#f40000","borderColor":"#f40000","label":"Target","yTopic":"Emulator Post Test","yRecord":"Target"}]},"key":"nAykFPGHMpTOFGbtJVb1"}]

it ('should return line-graph data', ()=>{
    const output = processDashboardWidgets(mockConfig,mockJournalEntries);

    const lineGraphData = output[0].payload.data;
    const xData = lineGraphData.x;
    const yData = lineGraphData.y;

    const actualData = yData.find(y=> y.yTopic==="Emulator Post Test" && y.yRecord==="PostCount");
    const targetData = yData.find(y=> y.yTopic==="Emulator Post Test" && y.yRecord==="Target");


    console.log('x: ' + JSON.stringify(xData));
    console.log('actual y: ' + JSON.stringify(actualData));
    console.log('target y: ' + JSON.stringify(targetData));

    expect(actualData.data).toEqual(["2","1"])
    expect(targetData.data).toEqual(["4","2"])

})