import React, {useContext, useEffect, useState} from 'react';
import {useData} from '../contexts/dataContext';
import listUtil from '../util/functions/list-util';


const DashboardContext = React.createContext();

export function useDashboard() {
    return useContext(DashboardContext);
}

export function DashboardProvider ({children}){
    const { getJournalEntries,getJournalDoc, userId } = useData();
    const [journalEntriesList, setJournalEntriesList] = useState([]);
    const [journalDoc, setJournalDoc] = useState();


    async function loadDashboard(journalId) {
        if (userId == null){
            listUtil(journalEntriesList, setJournalEntriesList, { type: "TRUNCATE"});
            return;
        }
        console.log("Journal ID is : "+ journalId);
        const journalEntryDocs = await getJournalEntries(journalId);
        console.log ("Got journals: " + journalEntryDocs + " in " + journalId);
        listUtil(journalEntriesList, setJournalEntriesList, { type: "SET", payload:journalEntryDocs});
        setJournalDoc(getJournalDoc(journalId));
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
        journalEntriesList, filterJournalEntries, loadDashboard, journalDoc
    }

    
    return (
        <DashboardContext.Provider value={values}>
            {children}
        </DashboardContext.Provider>
    )

}

