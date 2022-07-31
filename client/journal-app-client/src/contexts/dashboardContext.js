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

    useEffect(()=>{
        async function callLoadDashboardWidgets(){
            if (journalDoc!=null){
                loadDashboardWidgets(journalDoc.key);
            }
        }
        callLoadDashboardWidgets();
    },[journalDoc,dashboardConfigs,journalEntriesList])

    async function loadDashboard(journalId) {
        if (userId == null){
            listUtil(journalEntriesList, setJournalEntriesList, { type: "TRUNCATE"});
            return;
        }
        console.log("Journal ID is : "+ journalId);
        const retreivedJournalDoc = getJournalDoc(journalId);
        setJournalDoc(retreivedJournalDoc);

        if (retreivedJournalDoc==null) return;
        
        let journalEntryDocs = await getJournalEntries(retreivedJournalDoc.key);
        let dashboardConfigDocs = await getDashboardConfig(retreivedJournalDoc.key);
        journalEntryDocs=journalEntryDocs.sort((a,b)=>{return a.dateOfEntry>b.dateOfEntry});
        journalEntryDocs=journalEntryDocs.sort((a,b)=>{return a.position<b.position});
        console.log ("Got journals: " + journalEntryDocs + " in " + retreivedJournalDoc.key);
        listUtil(journalEntriesList, setJournalEntriesList, { type: "SET", payload:journalEntryDocs});
        listUtil(dashboardConfigs,setDashboardConfigs, {type: "SET", payload: dashboardConfigDocs});
        
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
                            searchResults.push({...journalBodyItem, key: journalEntry.key,dateOfEntry:journalEntry.dateOfEntry});
                        
            })}  
        );
        return searchResults;
    }

    function getJournalEntry(key){
        return journalEntriesList.find(journalEntry=> journalEntry.key===key);
    }

    const values = {
        journalEntriesList, filterJournalEntries, getJournalEntry, loadDashboard, journalDoc,dashboardWidgetContents
    }

    
    return (
        <DashboardContext.Provider value={values}>
            {children}
        </DashboardContext.Provider>
    )

}

