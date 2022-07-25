import React, {useContext, useEffect, useState} from 'react';
import {useData} from '../contexts/dataContext';
import listUtil from '../util/functions/list-util';
import { processDashboardWidgets } from '../dashboard/widgets/widget-refinery';

const DashboardContext = React.createContext();

export function useDashboard() {
    return useContext(DashboardContext);
}

export function DashboardProvider ({children}){
    const { getJournalEntries,getJournalDoc, userId, getDashboardConfig } = useData();
    const [journalEntriesList, setJournalEntriesList] = useState([]);
    const [journalDoc, setJournalDoc] = useState();
    const [dashboardWidgetContents, setDashboardWidgetContents]=useState([]);
    const [dashboardConfigs,setDashboardConfigs] = useState([]);

    useEffect(async ()=>{
        if (journalDoc!=null){
            let journalEntryDocs = await getJournalEntries(journalDoc.key);
            let dashboardConfigDocs = await getDashboardConfig(journalDoc.key);
            journalEntryDocs=journalEntryDocs.sort((a,b)=>{return a.dateOfEntry>b.dateOfEntry});
            console.log ("Got journals: " + journalEntryDocs + " in " + journalDoc.key);
            listUtil(journalEntriesList, setJournalEntriesList, { type: "SET", payload:journalEntryDocs});
            listUtil(dashboardConfigs,setDashboardConfigs, {type: "SET", payload: dashboardConfigDocs});
        }
    }, [journalDoc])

    useEffect(async ()=>{
        if (journalDoc!=null){
            loadDashboardWidgets(journalDoc.key);
        }
    },[journalDoc,dashboardConfigs,journalEntriesList])

    async function loadDashboard(journalId) {
        if (userId == null){
            listUtil(journalEntriesList, setJournalEntriesList, { type: "TRUNCATE"});
            return;
        }
        console.log("Journal ID is : "+ journalId);
        setJournalDoc(getJournalDoc(journalId));
    }

    function loadDashboardWidgets(){
        listUtil(dashboardWidgetContents, setDashboardWidgetContents, { type: "SET", payload:processDashboardWidgets(dashboardConfigs,journalEntriesList)});
    }


    function filterJournalEntries(searchInput){
        let searchResults = [];
        //if (!isNaN(Date.parse(searchInput))){

        //} 

        journalEntriesList.map(
            journalEntry => {
                journalEntry.journalBodyItems.map(
                    journalBodyItem=>{
                        if (journalBodyItem.topic.includes(searchInput) || 
                            journalBodyItem.description.includes(searchInput))
                            searchResults.push(journalBodyItem);
                        
            })}  
        );
        return searchResults;
    }


    const values = {
        journalEntriesList, filterJournalEntries, loadDashboard, journalDoc,dashboardWidgetContents
    }

    
    return (
        <DashboardContext.Provider value={values}>
            {children}
        </DashboardContext.Provider>
    )

}

