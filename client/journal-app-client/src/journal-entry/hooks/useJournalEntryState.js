import {useState} from 'react';
import useSimpleState from '../../util/hooks/useSimpleState';

const useJournalEntryState = initJournalEntry => {
    // for defaulting date field to today
    const today = new Date();
    const day = ((today.getDate()>9)?'':'0') + today.getDate();
    const month = ((today.getMonth()>9)?'':'0') + (today.getMonth() +1);
    const year = today.getFullYear();

    const DEFAULT = {
        journalCollection: "",
        summary: "",
        dateOfEntry: year+"-"+month+"-"+day,
        overview: "",
        journalBodyItems:[]
    };

    // Simple States
    const [journalCollection,setJournalCollection,handleChangeJournalCollection] = 
        useSimpleState(initJournalEntry? initJournalEntry.journalCollection : DEFAULT.journalCollection);
    const [summary,setSummary,handleChangeSummary] = 
        useSimpleState(initJournalEntry? initJournalEntry.summary : DEFAULT.summary);
    const [dateOfEntry,setDateOfEntry,handleChangeDateOfEntry] = 
        useSimpleState(initJournalEntry? initJournalEntry.dateOfEntry : DEFAULT.dateOfEntry);
    const [overview,setOverview,handleChangeOverview] = 
        useSimpleState(initJournalEntry? initJournalEntry.overview : DEFAULT.overview);

    // List States
    const [journalBodyItems,setJournalBodyItems]=useState(initJournalEntry? initJournalEntry.journalBodyItems : DEFAULT.journalBodyItems);

    return {
        get: {
            journalCollection: journalCollection,
            summary: summary,
            dateOfEntry: dateOfEntry,
            overview: overview,
            journalBodyItems: journalBodyItems
        },
        default: DEFAULT,
        set: {
            journalCollection: setJournalCollection,
            summary: setSummary,
            dateOfEntry: setDateOfEntry,
            overview: setOverview,
            journalBodyItems: setJournalBodyItems
        },
        handle:{
            handleChangeJournalCollection: handleChangeJournalCollection,
            handleChangeSummary: handleChangeSummary,
            handleChangeDateOfEntry: handleChangeDateOfEntry,
            handleChangeOverview: handleChangeOverview
        },
        setJournalBodyItems: setJournalBodyItems
    }


}
export default useJournalEntryState;