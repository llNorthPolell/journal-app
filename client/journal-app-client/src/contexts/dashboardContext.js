import React, {useContext, useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import {useData} from '../contexts/dataContext';
import listUtil from '../util/functions/list-util';
import { processDashboardWidgets } from '../dashboard/widgets/widget-refinery';

const DashboardContext = React.createContext();

export function useDashboard() {
    return useContext(DashboardContext);
}

export function DashboardProvider ({children}){
    const {journalId} = useParams();

    const { getJournalEntries,getJournalDoc, userId, getDashboardConfig,loadDashboardData, dashboardLoaded } = useData();

    const [dashboardWidgetContents, setDashboardWidgetContents]=useState([]);

    useEffect(()=>{
        if (!dashboardLoaded) callLoadDashboard();
    },[journalId]);

    useEffect(()=>{
        if (!dashboardLoaded) callLoadDashboard();
        loadDashboardWidgets(journalId);
    }, [dashboardLoaded])

    async function callLoadDashboard(){
        await loadDashboardData(journalId);
    }

    async function loadDashboardWidgets(){
        const journalEntriesList = await getJournalEntries(journalId);
        const dashboardConfigs = await getDashboardConfig(journalId);
        listUtil(dashboardWidgetContents, setDashboardWidgetContents, { type: "SET", payload:processDashboardWidgets(dashboardConfigs,journalEntriesList)});
    }

    async function filterJournalEntries(searchInput){
        let searchResults = [];
        //if (!isNaN(Date.parse(searchInput))){

        //} 
        const journalEntriesList = await getJournalEntries(journalId);

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

    async function getJournalEntry(key){
        const journalEntriesList = await getJournalEntries(journalId);
        return journalEntriesList.find(journalEntry=> journalEntry.key===key);
    }

    const values = {
        getJournalEntries, filterJournalEntries, getJournalEntry, getJournalDoc,dashboardWidgetContents
    }

    
    return (
        <DashboardContext.Provider value={values}>
            {children}
        </DashboardContext.Provider>
    )

}

