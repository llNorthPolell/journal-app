import {useEffect} from 'react';
import {useParams} from 'react-router-dom';
import {useSelector, useDispatch} from 'react-redux';
import {loadJournalEntryList, insertJournalEntry, updateJournalEntry, setCurrentJournal, reset} from '../../redux/journal-entries';

const useJournalEntryList = (requiredOps) => {
    const {journalId} = useParams();
    const dispatch = useDispatch();

    const {currentJournal,journalEntries,status} = useSelector((state)=>state.journalEntries);

    useEffect(()=>{
        if (status ==="loading" || (currentJournal && journalId===currentJournal.key)) return;
        dispatch(setCurrentJournal(journalId));
        dispatch(loadJournalEntryList(journalId)); 
    },[])

    const createJournalEntry = async (newJournalEntry)=>{
        const returnJournalEntry = await dispatch(insertJournalEntry(newJournalEntry)).unwrap();
        return returnJournalEntry;
    }

    const getJournalEntry = (key)=>{
        return journalEntries.find(journalEntry=> journalEntry.key===key);
    }


    const filterJournalEntries = (searchInput) => {
        let searchResults = [];

        journalEntries.forEach(
            journalEntry => {
                journalEntry.journalBodyItems.forEach(
                    journalBodyItem=>{
                        if (journalBodyItem.topic.includes(searchInput) || 
                            journalBodyItem.description.includes(searchInput))
                            searchResults.push({...journalBodyItem, key: journalEntry.key,dateOfEntry:journalEntry.dateOfEntry});     
            })}  
        );

        return searchResults;
    }



    const editJournalEntry = async(journalEntryId, payload) => {
        const returnJournalEntry = await dispatch(updateJournalEntry({journalEntryId: journalEntryId,payload:payload})).unwrap();
        return returnJournalEntry;
    }

    let output = [];
    requiredOps.forEach(requiredOp=>{
        switch(requiredOp){
            case "getAll":
                output = [...output, journalEntries];
                break;
            case "getById":
                output = [...output, getJournalEntry];
                break;
            case "loadStatus":
                output = [...output, status];
                break;
            case "insert":
                output = [...output, createJournalEntry];
                break;
            case "filter":
                output = [...output, filterJournalEntries];
                break;
            case "update":
                output = [...output, editJournalEntry];
                break;
            case "delete":
                break;
            case "reset":
                output = [...output, reset];
                break;
            default:
                break;
        }
    });
    return output;



}
export default useJournalEntryList;