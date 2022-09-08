import {useEffect} from 'react';
import {useParams} from 'react-router-dom';
import {useSelector, useDispatch} from 'react-redux';
import {useAuth} from '../../contexts/authContext';

import {setUserId, setCurrentJournal, setCurrentJournalEntry, reset} from '../../redux/session';
import useJournalList from './useJournalList';
import useJournalEntryList from './useJournalEntryList';

const useSession = (requiredOps) => {
    const {journalId, entryId} = useParams();
    const dispatch = useDispatch();

    const { auth } = useAuth();

    const [getJournal,journalListLoadStatus]=useJournalList(["getById","loadStatus"]);
    const [getJournalEntry,journalEntryListLoadStatus]=useJournalEntryList(["getById","loadStatus"]);

    const {userId, currentJournal, currentJournalEntry} = useSelector((state)=> state.session);

    useEffect(()=>{
        const unsubscribe = auth.onAuthStateChanged(authUser => {
            if (authUser)
                dispatch(setUserId(authUser.uid));    
        });
        return unsubscribe;
    },[]);

    useEffect(()=>{
        if (journalListLoadStatus==="loaded")
            dispatch(setCurrentJournal(getJournal(journalId)));  
    },[journalListLoadStatus])

    useEffect(()=>{
        if (journalEntryListLoadStatus==="loaded")
            dispatch(setCurrentJournalEntry(getJournalEntry(entryId)));  
    },[journalEntryListLoadStatus])

    let output = [];
    requiredOps.forEach(requiredOp=>{
        switch(requiredOp){
            case "user":
                output = [...output, userId];
                break;
            case "currentJournal":
                output = [...output, currentJournal];
                break;
            case "currentJournalEntry":
                output = [...output, currentJournalEntry];
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
export default useSession;